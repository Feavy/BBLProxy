package utils;

import java.util.List;

public class Debug {

	public static void debug(List<Integer> bytes) {

		for (int c : bytes.toArray(new Integer[bytes.size()])) {

			System.out.print(c + ",");

		}

		System.out.println("");

	}
	
}
