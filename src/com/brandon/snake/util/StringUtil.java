package com.brandon.snake.util;

public class StringUtil {
	public static int countOccurrences(String haystack, char needle) {
		int count = 0;
		for (char ch : haystack.toCharArray()) {
			if (ch == needle) {
				count++;
			}
		}
		return count;
	}
}
