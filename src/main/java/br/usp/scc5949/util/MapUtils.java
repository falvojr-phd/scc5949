package br.usp.scc5949.util;

import java.util.Map;

/**
 * Useful class for {@link Map}'s.
 * 
 * @author falvojr
 */
public final class MapUtils {

	private MapUtils() {
		super();
	}

	public static <T, S> Object[][] parseToMatrix(Map<T, S> map) {
		final Object[][] matrix = new Object[map.size()][2];

		final Object[] keys = map.keySet().toArray();
		final Object[] values = map.values().toArray();

		for (int row = 0; row < matrix.length; row++) {
			matrix[row][0] = keys[row];
			matrix[row][1] = values[row];
		}
		return matrix;
	}
}
