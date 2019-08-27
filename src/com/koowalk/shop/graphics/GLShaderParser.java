package com.koowalk.shop.graphics;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * A class to parse and load shaders. Adds functionality in the form of shader includes,
 * which can be enabled and disabled in the parser using a flag.
 *
 */
public class GLShaderParser {
	/**
	 * Flags that alter the way this parser does its parsing
	 *
	 */
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
	
	/**
	 * Parse the given shader file
	 * @param file A GLSL shader file
	 * @throws FileNotFoundException When the file does not exist
	 */
	public GLShaderParser(File file) throws FileNotFoundException {
		reader = new Scanner(file);
		this.file = file;
		flags = new boolean[Flag.TOTAL];
	}
	
	/**
	 * @see com.koowalk.shop.graphics.GLShaderParser#GLShaderParser(File)
	 * @param file A GLSL shader file
	 * @throws FileNotFoundException
	 */
	public GLShaderParser(String file) throws FileNotFoundException {
		this(new File(file));
	}
	
	/**
	 * Set the given boolean flag. Changes the state of the parser for when parse()
	 * is called
	 * @param flag The flag to be changed
	 * @param status It's new value
	 */
	public void setFlag(Flag flag, boolean status) {
		flags[flag.index] = status;
	}
	
	//TODO: Improve this
	/**
	 * Actually parse the given shader file. Once this method is called, the GLShaderParser 
	 * object becomes useless.
	 * @return The shader program as a string.
	 */
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
