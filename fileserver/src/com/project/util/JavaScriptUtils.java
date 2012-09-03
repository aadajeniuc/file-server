package com.project.util;

public class JavaScriptUtils {

	public JavaScriptUtils() {
	}
	/**
	 * URL encode string
	 */
	public static String escape(final String in){
		StringBuffer sb = new StringBuffer(in.length()+3);
		char c;
		for (int i = 0; i < in.length(); i++) {
			c = in.charAt(i);
			if(c<16){  // 42
				encode0(c,sb);
			} else if (c < 42 ){
				encode(c,sb);
			} else if (c < 44){
				sb.append(c);
			} else if (c == 44){
				encode(c,sb);
			} else if (c < 58){
				sb.append(c);
			} else if (c < 64){
				encode(c,sb);
			} else if (c < 91){
				sb.append(c);
			} else if (c < 95){
				encode(c,sb);
			} else if (c == 95){
				sb.append(c);
			} else if (c < 97){
				encode(c,sb);
			} else if (c < 123){
				sb.append(c);
			} else {
				encode(c,sb);
			}
		}
		return sb.toString();
	}


	/**
	 * UN-URL encode string
	 */
	public static String unescape(final String s){
		StringBuffer sb = new StringBuffer(s.length());

		for(int i = 0; i < s.length(); i++){
			char c = s.charAt(i);
			switch(c){
				case '%':{
					try{
						sb.append((char)Integer.parseInt(s.substring(i + 1, i + 3), 16));
						i += 2;
						break;
					} catch(NumberFormatException nfe){
						throw new IllegalArgumentException();
					} catch(StringIndexOutOfBoundsException siob){
						String end = s.substring(i);
						sb.append(end);
						if(end.length() == 2)i++;
					}
					break;
				}
				default:{
					sb.append(c);
					break;
				}
			}
		}
		return sb.toString();
	}

	private static final void encode0(final char c, final StringBuffer sb){
		sb.append("%0").append(Integer.toHexString(c));
	}
	private static final void encode(final char c, final StringBuffer sb){
		if(c>15)sb.append('%').append(Integer.toHexString(c));
	}


}