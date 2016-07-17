package com.padskiiz.engine;

import java.io.InputStream;
import java.util.Scanner;

public class Utils {

	@SuppressWarnings("resource")
	public static String loadResource(String fileName) throws Exception {
		String result = "";
        try (InputStream in = Utils.class.getClass().getResourceAsStream(fileName)) {
            result = new Scanner(in, "UTF-8").useDelimiter("\\A").next();
        }
        return result;
	}

}
