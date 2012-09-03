package com.project.util;


import java.io.*;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 *  Used to extract files from JARs or ZIPs. It requires a directory to extract into
 *  and a valid file to extract.<br>
 *  <br>
 *
 */

public class ZipExtractor implements Serializable{
	private File extractDir;
	private File archiveFile;
	private boolean overwrite = true;

	private static int DEFAULT_BUFFER_SIZE = 1024;
	private int BUFFER_SIZE = DEFAULT_BUFFER_SIZE;

	public ZipExtractor() {
	}

	public ZipExtractor(int newBufferSize) {
	  BUFFER_SIZE = newBufferSize;
	}

	public ZipExtractor(File extractDir, File archiveFile) {
		this.extractDir = extractDir;
		this.archiveFile = archiveFile;
		if(!extractDir.exists()){
		  System.out.println("making dir:"+extractDir);
		  extractDir.mkdirs();
		}
	}

	public void setExtractDir(File newExtractDir) {
		extractDir = newExtractDir;
	}

	public void setArchiveFile(File newArchiveFile) {
		archiveFile = newArchiveFile;
	}

	public File getExtractDir() {
		return extractDir;
	}

	public boolean isOverwrite() {
		return overwrite;
	}

	public void setOverwrite(boolean overwrite) {
		this.overwrite=overwrite;
	}

	public File getArchiveFile() {
		return archiveFile;
	}


	/**
	 *  Opens up the zip and extracts the files to the temp dir.
	 *
	 *@param  vebose  if true Prints out a list of the zips contents on to System.out
	 *@return an ArrayList of java.io.File objects that
	 *      will be as per the path in the zip with the root being the temp dir
	 *@exception  java.io.FileNotFoundException
	 *@exception  java.io.IOException
	 */
	public ArrayList extract(boolean vebose) throws FileNotFoundException, IOException {
		ZipInputStream zis = new ZipInputStream(new FileInputStream(archiveFile));
		ZipEntry entry = null;
		ArrayList result = new ArrayList();
		while ((entry = zis.getNextEntry()) != null) {
			if (vebose) {
				System.out.println("Extracting:" + entry.getName());
			}
			result.add(extract(zis, entry));
		}
		zis.close();
		return result;
	}


	/**
	 *  Extract a single file from the stream. N.B. the stream must be in the correct
	 *  position for this to work
	 *
	 *@param  zis                        ZipInputStream open and ready
	 *@param  entry                      A valid entry read from the stream
	 *@return                            The inflated file generated in the temp dir
	 *@exception  java.io.FileNotFoundException
	 *@exception  java.io.IOException
	 */
	private File extract(ZipInputStream zis, ZipEntry entry) throws FileNotFoundException, IOException {
		createPath(entry.getName());
		File fileToUse = new File(extractDir, entry.getName());
		if (fileToUse.exists()) {
			if(!overwrite)return fileToUse;
		} else {
			fileToUse.createNewFile();
		}
		if(fileToUse.isDirectory())return fileToUse;


		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(fileToUse),BUFFER_SIZE);
		byte[] bytes = new byte[BUFFER_SIZE];
		int len = 0;
		while ((len = zis.read(bytes)) >= 0) {
			bos.write(bytes, 0, len);
		}
		bos.close();
		zis.closeEntry();
		return fileToUse;
	}


	/**
	 *  This add all the necessary directories in the root of the zip path to the
	 *  temp dir.
	 *
	 *@param  entryName        The string name in the Zip file (virtual path)
	 *@exception  java.io.IOException  if the directories can not be made
	 */
	private void createPath(String entryName) throws IOException {
		int slashIdx = entryName.lastIndexOf('/');
		if (slashIdx >= 0) {
			// there is path info
			String firstPath = entryName.substring(0, slashIdx);
			File dir = new File(extractDir, firstPath);
			if (!dir.exists()) {
				dir.mkdirs();
			}
		}
	}
}
