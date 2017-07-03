package br.usp.scc5949.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import com.google.common.collect.Multiset.Entry;
import com.google.common.collect.TreeMultiset;

import dnl.utils.text.table.TextTable;

public class Node {

	private String name;
	private List<Integer> influences;
	private List<String> values;
	private Map<String, Double> probabilities;

	public Node(String name, List<Integer> influences) {
		this.name = name;
		this.influences = influences;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Integer> getInfluences() {
		if (influences == null) {
			influences = new ArrayList<>();
		}
		return influences;
	}

	public void setInfluences(List<Integer> influences) {
		this.influences = influences;
	}

	public List<String> getValues() {
		if (values == null) {
			values = new ArrayList<>();
		}
		return values;
	}

	public void setValues(List<String> values) {
		this.values = values;
	}

	public Map<String, Double> getProbabilities() {
		return probabilities;
	}

	public void setProbabilities(Map<String, Double> probabilities) {
		this.probabilities = probabilities;
	}

	public void evalProbabilities() {
		if (this.influences.size() == 1) {
			final Double sum = (double) this.values.size();
			final Map<String, Double> mappedValues = this.values.stream()
					.collect(Collectors.groupingBy(e -> e, Collectors.counting())).entrySet().stream()
					.collect(Collectors.toMap(Map.Entry::getKey, e -> evalAverage(e.getValue(), sum)));
			this.probabilities = new TreeMap<>(mappedValues);

		} else {
			this.probabilities = new TreeMap<>();

			String groupKey = "";
			final Map<String, Long> auxProbabilities = new TreeMap<>();

			final TreeMultiset<String> ordenedValues = TreeMultiset.create(this.values);
			final Set<Entry<String>> entrySet = ordenedValues.entrySet();
			for (Entry<String> entry : entrySet) {
				final String[] values = entry.getElement().split("\\s+");
				final String key = Arrays.toString(Arrays.copyOf(values, values.length - 1));
				if (!groupKey.equals(key)) {
					groupKey = key;
					if (!groupKey.isEmpty()) {
						final Double sum = auxProbabilities.values().stream().mapToDouble(Number::doubleValue).sum();
						final Map<String, Double> mappedValues = auxProbabilities.entrySet().stream()
								.collect(Collectors.toMap(Map.Entry::getKey, e -> evalAverage(e.getValue(), sum)));
						this.probabilities.putAll(mappedValues);
						auxProbabilities.clear();
					}
				}
				auxProbabilities.put(entry.getElement(), Long.valueOf(entry.getCount()));
			}
		}
	}

	public void print() {
		final String[] header = { this.name, "Probabilities" };
		final Object[][] data = getMatrixProbabilities();
		final TextTable textTable = new TextTable(header, data);
		textTable.printTable();
		System.out.println();
	}

	private double evalAverage(Long valueCount, Double sum) {
		return valueCount / sum;
	}

	private Object[][] getMatrixProbabilities() {
		Object[][] twoDarray = new Object[this.probabilities.size()][2];

		Object[] keys = this.probabilities.keySet().toArray();
		Object[] values = this.probabilities.values().toArray();

		for (int row = 0; row < twoDarray.length; row++) {
			twoDarray[row][0] = keys[row];
			twoDarray[row][1] = values[row];
		}
		return twoDarray;
	}
}