package com.cquent.startrek.handlers;

import java.io.IOException;

import javax.speech.recognition.GrammarException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.handlers.HandlerUtil;

import com.cquent.startrek.recognition.RecognitionModule;

import edu.cmu.sphinx.util.props.PropertyException;

public class VoiceCommandHandler extends AbstractHandler {

	
	public Object execute(ExecutionEvent event) throws ExecutionException {
        try {
            RecognitionModule recognitionModule = new RecognitionModule(event);
            recognitionModule.execute();
        } catch (IOException ioe) {
            System.out.println("I/O Error " + ioe);
        } catch (PropertyException e) {
            System.out.println("Problem configuring recognizer" + e);
        } catch (InstantiationException  e) {
            System.out.println("Problem creating components " + e);
        } catch (GrammarException  e) {
            System.out.println("Problem with Grammar " + e);
        }
        return null;
	}

}
