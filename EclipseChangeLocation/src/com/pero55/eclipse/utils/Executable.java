package com.pero55.eclipse.utils;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * The application change location for Eclipse projects
 * 
 * https://web.archive.org/web/20160421171614/http://www.joeflash.ca/blog/2008/11/moving-a-fb-workspace-update.html
 * 
 * @author Nicola Perini
 *
 */
public final class Executable {
	final private static Logger LOG = Logger.getLogger(Executable.class.getName());

	public static final String PACKAGE = "";
	final private static String USAGE_INFO = "USAGE: java -jar -f [Path eclipse file '.location' file] -n [New Path 'Projects']";
	final private static String USAGE_INFO1 = "{workspace_dir}/.metadata/.plugins/org.eclipse.core.resources/.projects/{project_folder}";
	final private static String ARG1 = "-f";
	final private static String ARG2 = "-n";

	public static void main(String[] args) {
		String locationPathFile = null;
		String newLocationEclipseProject = null;
		boolean isExecutable = false;
		if ((args != null) && (args.length > 0) && args.length == 4) {
			for (int i = 0; i < args.length; i++) {
				if ((args[i].equalsIgnoreCase(ARG1)) && (args[(i + 1)] != null)) {
					locationPathFile = args[(i + 1)];
				} else if ((args[i].equalsIgnoreCase(ARG2)) && (args[(i + 1)] != null)) {
					newLocationEclipseProject = args[(i + 1)];
				}
			}
		} else {
			LOG.info(USAGE_INFO);
			LOG.info(USAGE_INFO1);
			return;
		}
		if ((newLocationEclipseProject != null) && (locationPathFile != null)) {
			File f = new File(locationPathFile);
			if ((f.exists()) && (!f.isDirectory())) {
				isExecutable = true;
			} else {
				LOG.info("File specified do not exist!");
				return;
			}
			if (isExecutable) {

				ChangeLocation c;
				try {
					c = new ChangeLocation(locationPathFile, newLocationEclipseProject);
					if (c.generate()) {
						LOG.info("File converted :" + newLocationEclipseProject);
					} else {
						LOG.severe("File NOT converted :" + newLocationEclipseProject);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			} else {
				LOG.info(USAGE_INFO);
				LOG.info(USAGE_INFO1);
			}
		} else {
			LOG.info("No file found!");
		}
	}
}
