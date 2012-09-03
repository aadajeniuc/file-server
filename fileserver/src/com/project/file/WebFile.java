package com.project.file;

import java.io.File;
import java.io.Serializable;
import java.text.NumberFormat;

public class WebFile implements Serializable, Comparable {

	private String name;
	private String url;
	private String lastModified;
	private String mime;
	private long fileSize;
	private File file;

	public WebFile(String name, String url, String lastModified, String mime, long fileSize,File file) {
		this.name = name;
		this.url = url;
		this.lastModified = lastModified;
		this.mime = mime;
		this.fileSize = fileSize;
		this.file = file;
	}

	public void setName(String newName) {
		name = newName;
	}

	public String getName() {
		return name;
	}

	public void setUrl(String newUrl) {
		url = newUrl;
	}

	public String getUrl() {
		return url;
	}

	public void setLastModified(String newLastModified) {
		lastModified = newLastModified;
	}

	public String getLastModified() {
		return lastModified;
	}

	public void setMime(String newMime) {
		mime = newMime;
	}

	public String getMime() {
		return mime;
	}

	public int compareTo(Object other) {
		WebFile otherFile = (WebFile) other;
		return name.compareTo(otherFile.getName());
	}

	public String getFileSize() {
		return fileSizeText(fileSize);
	}
	public long getFileSizeL() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	private String fileSizeText(long number) {
		NumberFormat numFormat = NumberFormat.getNumberInstance();
		long remainder;
		if (fileSize > 1000000000) {
			remainder = (fileSize % 100000000);
			number = number - remainder;
			double gigabytes = (double) number / (1000000000);
			return numFormat.format(gigabytes) + " GB";
		}
		else if (fileSize > 1000000) {
			remainder = (fileSize % 100000);
			number = number - remainder;
			double megabytes = (double) number / (1000000);
			return numFormat.format(megabytes) + " MB";
		}
		else if (fileSize > 1000) {
			remainder = (fileSize % 100);
			number = number - remainder;
			double kilobytes = (double) number / (1000);
			return numFormat.format(kilobytes) + " KB";
		}
		else {
			return numFormat.format(number) + " bytes";
		}

	}
    public File getFile() {
    	return file;
    }
    public void setFile(File file) {
    	this.file = file;
    }
}
