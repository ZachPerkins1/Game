package com.koowalk.shop.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

public class Logger {
	public static final int LOG_HISTORY_COUNT = 10;
	public static final int BUFFER_CAPACITY = 200;
	public static final String LOG_FOLDER = "logs";
	
	private static PrintWriter writer;
	private static StringBuilder buffer;	
	private static int messageCount;
	
	private static DateTimeFormatter formatter;
		
	public static void info(Object o) {
		print("[INFO] " + o.toString());
	}
	
	public static void error(Object o) {
		print("[ERRO] " + o.toString());
	}
	
	public static void warn(Object o) {
		print("[WARN] " + o.toString());
	}
	
	public static void print(String item) {
		String formattedItem = formatter.format(LocalDateTime.now()) + " " + item;
		filePrint(formattedItem);
		System.out.println(formattedItem);
	}
	
	private static void filePrint(String item) {
		messageCount++;
		buffer.append(item + System.lineSeparator());
		if (messageCount >= BUFFER_CAPACITY) {
			writeBuffer();
		}
	}
	
	public static void exception(Exception e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		filePrint(sw.toString());
		e.printStackTrace();
	}
	
	public static void init() {
		File logs = getLogFile();
		File[] files = logs.listFiles();
		
		while (files.length >= LOG_HISTORY_COUNT) {
			deleteOldestFile(files);
			files = logs.listFiles();
		}
		
		File latest = findFileByName(files, "latest.log");
		if (latest != null) {
			try {
				Files.move(latest.toPath(), logs.toPath().resolve(formatFileTimestamp(latest)));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try {
			writer = new PrintWriter(logs.toPath().resolve("latest.log").toString());
			buffer = new StringBuilder();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		createFormatter();
	}
	
	public static void finish() {
		writeBuffer();
		writer.close();
	}
	
	private static File getLogFile() {
		File logs = new File(LOG_FOLDER);
		if (!logs.exists()) {
			logs.mkdirs();
		}
		
		return logs;
	}
	
	private static void deleteOldestFile(File[] files) {
		FileTime min = FileTime.fromMillis(System.currentTimeMillis());
		int minIndex = 0;
		
		for (int i = 0; i < files.length; i++) {
			File f = files[i];
			if (f.isFile()) {
				try {
					FileTime time = Files.getLastModifiedTime(f.toPath());
					if (time.compareTo(min) == -1) {
						min = time;
						minIndex = i;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		files[minIndex].delete();
	}
	
	private static File findFileByName(File[] files, String name) {
		for (File f : files) {
			if (f.getName().equals(name))
				return f;
		} 
		
		return null;
	}
	
	private static String formatFileTimestamp(File f) {
		try {
			FileTime time = Files.getLastModifiedTime(f.toPath());
			return time.toString().replace(':', '_') + ".log";
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	private static void createFormatter() {
		DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
		builder.appendPattern("HH:mm:ss");
		formatter = builder.toFormatter();
	}
	
	private static void writeBuffer() {
		writer.write(buffer.toString());
		buffer = new StringBuilder();
	}
}
