package com.pero55.eclipse.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.logging.Logger;

/***
 * The class collects details in order to generate a new .location file
 * Based
 * 
 * @author Pero55
 *
 */

public class ChangeLocation {
	final private static Logger LOG = Logger.getLogger(ChangeLocation.class.getName());

	final static String PRE_PATH = "URI//file:/";
	final static int MAX_LENGTH = 255;
	final static String OS = "os.name";
	final static String WIN = "WIN";

	private String sourceLocation; // Current location .location file
	private String newEclipsePathProject; // New Path for Eclipse projects
	private String fileToWrite; // New Path for Eclipse projects
	private int oldPathLenght; // Old length of location
	private byte[] oldTail;
	private int fileOldContentSize;
	private int fileNewContentSize;

	public ChangeLocation(String sourceLocation, String newPathProject) throws IOException {
		this.setSourceLocation(sourceLocation);
		this.setFileToWrite(newPathProject);
		this.setNewPathProject(PRE_PATH + newPathProject);
		setProperties();
	}

	public String getSourceLocation() {
		return sourceLocation;
	}

	public void setSourceLocation(String sourceLocation) {
		this.sourceLocation = sourceLocation;
	}

	public String getNewPathProject() {
		return newEclipsePathProject;
	}

	public void setNewPathProject(String newPathProject) {
		this.newEclipsePathProject = newPathProject;
	}

	/**
	 * Set the properties For change location Object
	 * 
	 * @return
	 * @throws IOException
	 */
	private void setProperties() throws IOException {
		File fileInput = new File(sourceLocation); // Read

		byte[] fileOldContent = Files.readAllBytes(fileInput.toPath());
		this.fileOldContentSize = fileOldContent.length;

		// Copy
		for (int i = 0, j = 0, k = 0; i < fileOldContent.length; i++, j++) {

			// Check if I'm on URI I'm on the byte of the length
			if ((i + 1) < fileOldContent.length && fileOldContent[i + 1] == 85 && fileOldContent[i + 2] == 82
					&& fileOldContent[i + 3] == 73) {
				oldPathLenght = fileOldContent[i] & 0xff;

				i = i + oldPathLenght; // offset to the end of file
				this.oldTail = new byte[fileOldContent.length - i]; // to be
																	// added at
																	// the end
			}

			// Copy tail of the original file file location
			if (oldTail != null && i < fileOldContent.length - 1) {
				oldTail[k++] = fileOldContent[i + 1];
			}

		}
		int diff = oldPathLenght - newEclipsePathProject.length();
		if (diff >= 0) {
			fileNewContentSize = fileOldContentSize - diff + 1;
		} else {
			fileNewContentSize = fileOldContentSize + diff;
		}
		return;
	}

	/**
	 * Change eclipse Location
	 * 
	 * @return
	 * @throws Exception
	 */

	public boolean generate() {

		boolean Ret = false;

		// Check OS
		if (!System.getProperty(OS).toUpperCase().contains(WIN)) {
			LOG.severe("Sorry, Only for win system!");
			return false;
		}

		try {

			//URI uri = new URI(getNewPathProject());
			File fileInput = new File(getSourceLocation()); // Read
			byte[] fileOriContent = Files.readAllBytes(fileInput.toPath());
			byte[] fileNewContent = new byte[fileNewContentSize];

			for (int i = 0, j = 0; i < fileOldContentSize; i++, j++) {

				// Check if I'm on URI I'm on the byte of the length
				if ((i + 1) < fileOriContent.length && fileOriContent[i + 1] == 85 && fileOriContent[i + 2] == 82
						&& fileOriContent[i + 3] == 73) {

					// Write lenght before URI
					fileNewContent[j++] = getNewEclipseProjectLenght();

					// Copy new URI
					for (int z = 0; z < getNewPathProject().length(); z++, j++) {
						fileNewContent[j] = (byte) getNewPathProject().charAt(z);
					}

					// Copy the part of the file
					for (int l = 0; l < this.oldTail.length; l++, j++) {
						fileNewContent[j] = oldTail[l];
					}
					break;

				} else {
					fileNewContent[j] = fileOriContent[i];
				}
			}
			Ret = write(fileNewContent, getFileToWrite());
		} catch (IOException e) {
			LOG.severe(e.getMessage());
		}
		return Ret;
	}


	/***
	 * byte Write method
	 * 
	 * @param input
	 * @param outputFileName
	 * @throws Exception
	 */
	private static boolean write(byte[] input, String outputFileName) {
		boolean ret = false;
		if (outputFileName != null) {
			OutputStream output = null;
			FileOutputStream fileOutPutStream = null;
			try {
				fileOutPutStream = new FileOutputStream(outputFileName);
				output = new BufferedOutputStream(fileOutPutStream);
				output.write(input);
				ret = true;
			} catch (Exception e) {
				LOG.severe("Write error:" + e.getMessage());
			} finally {

				if (output != null) {
					try {
						output.close();
					} catch (IOException e) {
						LOG.severe("File output stream error" + e.getMessage());
					}
				}
				if (fileOutPutStream != null) {
					try {
						fileOutPutStream.close();
					} catch (IOException e) {
						LOG.severe("File output stream error" + e.getMessage());
					}
				}
			}
		} else {
			LOG.severe("Write error file name is null");
		}
		return ret;
	}

	public Integer getOldPathLenght() {
		return oldPathLenght;
	}

	public void setOldPathLenght(Integer oldPathLenght) {
		this.oldPathLenght = oldPathLenght;
	}

	private byte getNewEclipseProjectLenght() {
		return Byte.parseByte(String.valueOf(getNewPathProject().length()));
	}

	public String getFileToWrite() {
		return fileToWrite;
	}

	public void setFileToWrite(String fileToWrite) {
		this.fileToWrite = fileToWrite;
	}
}
