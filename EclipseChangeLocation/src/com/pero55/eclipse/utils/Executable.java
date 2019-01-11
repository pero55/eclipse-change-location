package com.pero55.eclipse.utils;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The application change location for Eclipse projects
 * 
 * https://web.archive.org/web/20160421171614/http://www.joeflash.ca/blog/2008/11/moving-a-fb-workspace-update.html
 * sample: -f /input/ori.location -n /output/.location
 * 
 * @author Nicola Perini
 *
 */
public final class Executable {
	private Executable() {
		super();
	}

	final private static Logger LOG = Logger.getLogger(Executable.class.getName());

	public static final String PACKAGE = "";
	final private static String USAGE_INFO = "USAGE: java -jar -f [Path eclipse file '.location' file] -n [New Path 'Projects']";
	final private static String USAGE_INFO1 = "{workspace_dir}/.metadata/.plugins/org.eclipse.core.resources/.projects/{project_folder}";
	final protected static String ARG1 = "-f";
	final protected static String ARG2 = "-n";

	public static void main(String[] args) {
		try {
			ChangeLocation c = ChangeLocation.getInstance(args);
			if (c != null) {

				if (c.generate()) {
					if (LOG.isLoggable(Level.INFO)) {
						LOG.info(new StringBuilder().append("File converted :").append(c.getNewPathProject())
								.toString());
					}
				} else {
					if (LOG.isLoggable(Level.SEVERE)) {
						LOG.severe(new StringBuilder().append("File NOT converted :").append(c.getNewPathProject())
								.toString());
					}
				}

			} else {
				LOG.info(USAGE_INFO);
				LOG.info(USAGE_INFO1);
				return;
			}
		} catch (IOException e) {
			LOG.severe(e.getMessage());
			LOG.info(USAGE_INFO);
			LOG.info(USAGE_INFO1);
		}
	}
}