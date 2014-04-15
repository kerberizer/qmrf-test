package net.idea.rest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

/**
 * http://www.ietf.org/rfc/rfc4627.txt
 * @author nina
 *
 */
public class JSONUtils {
	public static String jsonEscape(String value) {
		if(value==null) return null;
		else return value.replace("\\", "\\\\")
        //.replace("/", "\\/")
        .replace("\b", "\\b")
        .replace("\f", "\\f")
        .replace("\n", "\\n")
        .replace("\r", "\\r")
        .replace("\t", "\\t")
        .replace("\"", "\\\"")
		.replace("\u0016", "")
		.replace("\u0019", "");
		
		
		/*
		 * 
		 %x22 /          ; "    quotation mark  U+0022
         %x5C /          ; \    reverse solidus U+005C
         %x2F /          ; /    solidus         U+002F
         %x62 /          ; b    backspace       U+0008
         %x66 /          ; f    form feed       U+000C
         %x6E /          ; n    line feed       U+000A
         %x72 /          ; r    carriage return U+000D
         %x74 /          ; t    tab             U+0009
         %x75 4HEXDIG )  ; uXXXX                U+XXXX
         */
	}
	/**
	 * Returns null, if the value is null, or a quoted string otherwise
	 * @param value
	 * @return
	 */
	public static String jsonQuote(String value) {
		if(value==null) return null;
		else return String.format("\"%s\"",value);
	}
	/**
	 * Used for JSONP callbacks
	 * TODO: full name validation
	 * @param functionName
	 * @return
	 */
	public static String jsonSanitizeCallback(String functionName) {
		if(functionName==null) return null;
		else return functionName.replace("(","").replace(")","");
	}
	
	/**
	 * 
	 * @param txt
	 * @return true if alphanumeric & special
	 */
	private static final Pattern regex = Pattern.compile("[$&+_~,:;=?@#|%(){}\\[\\]\\.\\^\\!\\-]");
	
	public static boolean acceptString(String txt) {
		Matcher matcher = regex.matcher(txt);
		//remove special and check if the rest is alphanumeric
		String nospecial = matcher.replaceAll("");
		return (StringUtils.isAlphanumeric(nospecial));

	}
}
