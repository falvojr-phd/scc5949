package br.usp.scc5949.util;

import java.util.Map;

public class MapUtil {

	private MapUtil() {
		super();
	}

	public static Object[][] parseToMatrix(Map<String, Double> probabilities) {
		Object[][] twoDarray = new Object[probabilities.size()][2];

		Object[] keys = probabilities.keySet().toArray();
		Object[] values = probabilities.values().toArray();

		for (int row = 0; row < twoDarray.length; row++) {
			twoDarray[row][0] = keys[row];
			twoDarray[row][1] = values[row];
		}
		return twoDarray;
	}
}
