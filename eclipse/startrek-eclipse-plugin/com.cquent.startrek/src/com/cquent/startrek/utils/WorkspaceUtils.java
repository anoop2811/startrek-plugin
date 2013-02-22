package com.cquent.startrek.utils;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.handlers.HandlerUtil;

public class WorkspaceUtils {

	public static boolean createJavaClass(ExecutionEvent event){
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();
		// Get all projects in the workspace
		IProject[] projects = root.getProjects();
		// Loop over all projects
		for (IProject project : projects) {
			try {
				// Only work on open projects with the Java nature
				if (project.isOpen()
						&& project
								.isNatureEnabled("org.eclipse.jdt.core.javanature")) {
					IJavaProject javaProject = JavaCore.create(project);
					IFolder folder = project.getFolder("src");
					IPackageFragmentRoot srcFolder = javaProject
							.getPackageFragmentRoot(folder);
					IPackageFragment fragment = srcFolder
							.createPackageFragment(project.getName(), true,
									null);
					fragment.createCompilationUnit("Test.java", "", true, null);
				}
			} catch (CoreException e) {
				e.printStackTrace();
				return false;
			}
		}
		MessageDialog.openInformation(HandlerUtil.getActiveShell(event), "Info", "Created java class with name 'Test'");
		return true;

	}
	
	public static boolean createJavaProject(ExecutionEvent event){
		try {
			IWorkspace workspace = ResourcesPlugin.getWorkspace();
			IWorkspaceRoot root = workspace.getRoot();
			IProject project = root.getProject("Test");
			project.create(null);
			project.open(null);
			IProjectDescription description = project.getDescription();
			description.setNatureIds(new String[] { JavaCore.NATURE_ID });
			project.setDescription(description, null);
			IJavaProject javaProject = JavaCore.create(project);
			//IFolder binFolder = project.getFolder("bin");
			//binFolder.create(false, true, null);
			IFolder sourceFolder = project.getFolder("src");
			sourceFolder.create(false, true, null);
			IPackageFragmentRoot packageRoot = javaProject.getPackageFragmentRoot(sourceFolder);
			IClasspathEntry[] oldEntries = javaProject.getRawClasspath();
			IClasspathEntry[] newEntries = new IClasspathEntry[oldEntries.length + 1];
			System.arraycopy(oldEntries, 0, newEntries, 0, oldEntries.length);
			newEntries[oldEntries.length] = JavaCore.newSourceEntry(packageRoot.getPath());
			javaProject.setRawClasspath(newEntries, null);
			
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		
		MessageDialog.openInformation(HandlerUtil.getActiveShell(event), "Info", "Created java class with name 'Test'");
		return true;
		//return false;
	}
	
	public static boolean createJavaInterface(ExecutionEvent event){
		return false;
	}
	
	public static boolean createJavaPackage(ExecutionEvent event){
		return false;
	}
	
	public static boolean addSpringNature(ExecutionEvent event){
		return false;
	}

	public static boolean createWebProject(ExecutionEvent event){
		return false;
	}

	
}
