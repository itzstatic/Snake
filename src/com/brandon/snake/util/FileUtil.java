package com.brandon.snake.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileUtil {
	
	private FileUtil(){}
	
	public static String read(String path) {
		StringBuilder str = new StringBuilder();
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(path));
			String next = "";
			
			while ((next = reader.readLine()) != null) {
				str.append(next + "\n");
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return str.toString();
	}
}
