package com.koowalk.shop.graphics;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GLShaderParser {
	public static enum Flag {
		IGNORE_INCLUDES(0);
		
		public static final int TOTAL = 1; // A count of all available flags
		
		private int index;
		private Flag(int index) {
			this.index = index;
		}
	}
	
	private Scanner reader;
	private File file;
	boolean[] flags;
	private static Pattern includePattern = Pattern.compile("#include \"(.+)\"");
	
	public GLShaderParser(File file) throws FileNotFoundException {
		reader = new Scanner(file);
		this.file = file;
		flags = new boolean[Flag.TOTAL];
	}
	
	public GLShaderParser(String file) throws FileNotFoundException {
		this(new File(file));
	}
	
	public void setFlag(Flag flag, boolean status) {
		flags[flag.index] = status;
	}
	
	//TODO: Improve this
	public String parse() {
		StringBuilder program = new StringBuilder();
		
		while (reader.hasNextLine()) {
			String line = reader.nextLine();
			Matcher m = includePattern.matcher(line);
			if (m.matches() && !flags[Flag.IGNORE_INCLUDES.index]) {
				try {
					line = m.replaceAll(new GLShaderParser((file.getParent() != null? file.getParent() + "/": "") + m.group(1)).parse());
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
			
			program.append(line + "\n");
		}
		
		reader.close();
		return program.toString();
	}
}
