package com.padskiiz.engine;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;

public class Utils {

	@SuppressWarnings("resource")
	public static String loadResource(String fileName) throws Exception {
		String result = "";
        try (InputStream in = Utils.class.getClass().getResourceAsStream(fileName)) {
            result = new Scanner(in, "UTF-8").useDelimiter("\\A").next();
        }
        return result;
	}

	public static List<String> readAllLines(String fileName) throws Exception {
		List<String> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(Utils.class.getClass().getResourceAsStream(fileName)))) {
            String line;
            while ((line = br.readLine()) != null) {
                list.add(line);
            }
        }
        return list;
	}
}
