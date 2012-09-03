package com.project.util;


import java.io.*;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 *  Used to create ZIP or JAR files from a whole directory.
 *  <br>
 *
 */

public class ZipCompressor implements Serializable{
	private File sourceDir;
	private File archiveFile;

	private static int DEFAULT_BUFFER_SIZE = 1024;
	private int BUFFER_SIZE = DEFAULT_BUFFER_SIZE;

	public ZipCompressor() {
	}

	public ZipCompressor(int newBufferSize) {
	  BUFFER_SIZE = newBufferSize;
	}

	public ZipCompressor(File sourceDir, File archiveFile) throws IOException {
		this.sourceDir = sourceDir;
		this.archiveFile = archiveFile;
		if(!archiveFile.getParentFile().exists()){
		  archiveFile.getParentFile().mkdirs();
		  archiveFile.createNewFile();
		}
	}

	public void setSourceDir(File newSourceDir) {
		sourceDir = newSourceDir;
	}

	public void setArchiveFile(File newArchiveFile) {
		archiveFile = newArchiveFile;
	}

	public File getSourceDir() {
		return sourceDir;
	}

	public File getArchiveFile() {
		return archiveFile;
	}

	/**
	 *  Opens up the zip and extracts the files to the temp dir.
	 *
	 */
	public void compress(boolean vebose) throws FileNotFoundException, IOException {
	  ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(archiveFile)));
	  ZipEntry entry = null;

	  File [] files = sourceDir.listFiles();
	  for (int i = 0; i < files.length; i++) {
		if(files[i].isDirectory())compress(zos,files[i],vebose);
		else {
		  if(vebose)System.out.println(files[i]);
		  entry = new ZipEntry(getPath(files[i]));
		  zos.putNextEntry(entry);
		  compress(zos,files[i]);
		}
	  }
	  zos.finish();
	  zos.close();

	}
	/** recursable */
	private void compress(ZipOutputStream zos,File directory,boolean vebose) throws FileNotFoundException, IOException {
	  ZipEntry entry = null;
	  File [] files = directory.listFiles();
	  for (int i = 0; i < files.length; i++) {
		if(files[i].isDirectory())compress(zos,files[i],vebose);
		else {
		  if(vebose)System.out.println(files[i]);
		  entry = new ZipEntry(getPath(files[i]));
		  zos.putNextEntry(entry);
		  compress(zos,files[i]);
		}
	  }
	}
	/**  do one file */
	private void compress(ZipOutputStream zos,File file) throws FileNotFoundException,IOException{
	  BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
	  byte[] bytes = new byte[BUFFER_SIZE];
	  int len = 0;
	  while ((len = bis.read(bytes)) >= 0) {
		  zos.write(bytes,0,len);
	  }
	  bis.close();
	  zos.closeEntry();
	}
    /** subtract the root from the path */
    private String getPath(File file){
	  int len = (int)sourceDir.toString().length();
	  return file.toString().substring(len+1);
    }

}
