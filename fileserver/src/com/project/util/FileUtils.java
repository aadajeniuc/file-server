package com.project.util;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

public class FileUtils {

    public FileUtils() {
    }
	public static int deleteRecursive(File directory){
		int count = 0;
		File [] files = directory.listFiles(new FileFilter(){
					public boolean accept(File file){
						return !file.isDirectory();
					}
		});
		for (int i = 0; i < files.length; i++) {
			files[i].delete();
			count++;
		}
		File [] dirs = directory.listFiles(new FileFilter(){
					public boolean accept(File file){
						return file.isDirectory();
					}
		});
		for (int i = 0; i < dirs.length; i++) {
			count+=deleteRecursive(dirs[i]);
		}
		directory.delete();
		return count;
	}

	/**
	 * converts a path with /../ or /./ to a direct path
	 * handles absolute paths relative paths windoze or linux formats
	 * throws RuntimeExceptions in the case of invalid input eg just ..
	 * or \path
	 * @param strPath
	 * @return
	 */
	public static String toDirectPath(String strPath){
		Path path = toPathList(strPath);
		normalize(path);
		return path.toString();
	}
	
	private static Path toPathList(String strPath){
		Path path = new Path(strPath);
		
		StringTokenizer st = new StringTokenizer(path.relativeSegments, "/\\");
		while(st.hasMoreTokens()) path.segments.add(st.nextToken());
		
		return path;
	}
	
	private static void normalize(Path path){
		List normalized = new ArrayList();
		for(int i = 0; i < path.segments.size(); i++){
			String segment = (String)path.segments.get(i);
			if( segment.equals(".") ) {
				continue;
			}
			else if( segment.equals("..") ) {
				if(i < normalized.size() - 1) throw new StringIndexOutOfBoundsException("Realtive path invalid");
				normalized.remove(i - 1);
			}
			else{
				normalized.add(segment);
			}
		}
		path.segments = normalized;
	}
	
	private static class Path{
		
		boolean absolute;
		String root = null;
		String relativeSegments = null;
		List segments = new ArrayList();

		public Path(String strPath){
			if(strPath.startsWith("\\")) {
				throw new IllegalArgumentException("Paths should not start with \\");
			}
			if(strPath.startsWith("/")) {
				this.absolute = true;
				this.root = "/";
				this.relativeSegments = strPath.substring(1);
			}
			else {
				char[] chars = strPath.toCharArray();
				if (chars.length > 1  &&
					chars[1] == ':' &&
					Character.isLetter(chars[0]) ){
					this.absolute  = true;
					this.root = new String(chars,0,2) + "/";
					this.relativeSegments = strPath.substring(2);
				}
				else{
					this.absolute = false;
					this.relativeSegments = strPath;
				}
			}
			
		}
		public String toString(){
			Iterator iter = segments.iterator();
			StringBuffer sb = new StringBuffer();
			if(absolute){
				sb.append(root);
			}
			for(int i = 0; iter.hasNext(); i++){
				sb.append(iter.next().toString());
				if(i < segments.size() - 1)sb.append("/");
			}
			return sb.toString();
		}
	}
}