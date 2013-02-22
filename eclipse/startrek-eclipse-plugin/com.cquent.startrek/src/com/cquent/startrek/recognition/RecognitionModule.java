/*
 * Copyright 1999-2004 Carnegie Mellon University.
 * Portions Copyright 2004 Sun Microsystems, Inc.
 * Portions Copyright 2004 Mitsubishi Electric Research Laboratories.
 * All Rights Reserved.  Use is subject to license terms.
 *
 * See the file "license.terms" for information on usage and
 * redistribution of this file, and for a DISCLAIMER OF ALL
 * WARRANTIES.
 *
 */

package com.cquent.startrek.recognition;

import edu.cmu.sphinx.jsapi.JSGFGrammar;
import edu.cmu.sphinx.recognizer.Recognizer;
import edu.cmu.sphinx.frontend.util.Microphone;
import edu.cmu.sphinx.result.Result;
import edu.cmu.sphinx.util.props.ConfigurationManager;
import edu.cmu.sphinx.util.props.PropertyException;
import java.io.IOException;
import java.net.URL;
import javax.speech.recognition.GrammarException;
import javax.speech.recognition.Rule;
import javax.speech.recognition.RuleGrammar;
import javax.speech.recognition.RuleParse;

import org.eclipse.core.commands.ExecutionEvent;

import com.cquent.startrek.utils.WorkspaceUtils;


public class RecognitionModule {
    private Recognizer recognizer;
    private JSGFGrammar jsgfGrammarManager;
    private Microphone microphone;
    private ExecutionEvent event;

    public RecognitionModule(ExecutionEvent event) throws 
            IOException, PropertyException, InstantiationException {

        URL url = RecognitionModule.class.getResource("props/jsgf.config.xml");
        ConfigurationManager cm = new ConfigurationManager(url);
        this.event = event;
        // retrive the recognizer, jsgfGrammar and the microphone from
        // the configuration file.

        recognizer = (Recognizer) cm.lookup("recognizer");
        jsgfGrammarManager = (JSGFGrammar) cm.lookup("jsgfGrammar");
        microphone = (Microphone) cm.lookup("microphone");
    }


    public void execute() throws IOException, GrammarException  {
        System.out.println("JSGF Demo Version 1.0\n");

        System.out.print(" Loading recognizer ...");
        recognizer.allocate();
        System.out.println(" Ready");

        if (microphone.startRecording()) {
            loadAndRecognize("java");
        } else {
            System.out.println("Can't start the microphone");
        }

        System.out.print("\nDone. Cleaning up ...");
        recognizer.deallocate();

        System.out.println(" Goodbye.\n");
        System.exit(0);
    }


    private void loadAndRecognize(String grammarName) throws
            IOException, GrammarException  {
        jsgfGrammarManager.loadJSGF(grammarName);
        dumpSampleSentences(grammarName);
        recognizeAndReport();
    }

    /**
     * Performs recognition with the currently loaded grammar.
     * Recognition for potentially multiple utterances until an 'exit'
     * tag is returned.
     *
     * @htrows GrammarException if an error in the JSGF grammar is
     * encountered
     */
    private void recognizeAndReport() throws GrammarException {
        boolean done = false;


        while (!done)  {
            Result result = recognizer.recognize();
            String bestResult = result.getBestFinalResultNoFiller();
            handleResult(bestResult);
            RuleGrammar ruleGrammar = jsgfGrammarManager.getRuleGrammar();
            RuleParse ruleParse = ruleGrammar.parse(bestResult, null);
            if (ruleParse != null) {
                System.out.println("\n  " + bestResult + '\n');
                done = isExit(ruleParse);
            } 
        }
    }

    private void handleResult(String bestResult) {
		RecognitionType rType = RecognitionType.fromString(bestResult);
		switch (rType) {
			case CREATE_JAVA_PROJECT : WorkspaceUtils.createJavaProject(event);
			case CREATE_PACKAGE: WorkspaceUtils.createJavaPackage(event);
			case CREATE_INTERFACE: WorkspaceUtils.createJavaInterface(event);
			case ADD_SPRING_NATURE: WorkspaceUtils.addSpringNature(event);
			case CREATE_CLASS: WorkspaceUtils.createJavaClass(event);
		}
		
		
	}


	/**
     * Searches through the tags of the rule parse for an 'exit' tag.
     *
     * @return true if an 'exit' tag is found
     */
    private boolean isExit(RuleParse ruleParse) {
        String[] tags = ruleParse.getTags();

        for (int i = 0; tags != null && i < tags.length; i++) {
            if (tags[i].trim().equals("exit")) {
                return true;
            }
        }
        return  false;
    }






    /**
     * Dumps out a set of sample sentences for this grammar.  
     *  TODO: Note the current
     *  implementation just generates a large set of random utterances
     *  and tosses away any duplicates. There's no guarantee that this
     *  will generate all of the possible utterances. (yep, this is a hack)
     *
     */
    private void dumpSampleSentences(String title) {
        System.out.println(" ====== " + title + " ======");
        System.out.println("Speak one of: \n");
        jsgfGrammarManager.dumpRandomSentences(200);
        System.out.println(" ============================");
    }
    
    /**
     * Main method for running the jsgf demo.
     * @param args program arguments (none)
     */
    public static void main(String[] args) {

    }
}
