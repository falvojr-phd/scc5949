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

import br.usp.scc5949.util.MapUtils;
import dnl.utils.text.table.TextTable;

/**
 * Node abstraction for marginal and conditional probabilities.
 * 
 * @author falvojr
 */
public class Node {

	private Integer id;
	private String name;
	private List<Integer> influences;
	private List<String> values = new ArrayList<>();;
	private Map<String, Double> probabilities = new TreeMap<>();

	public Node(Integer id, String name, List<Integer> influences) {
		this.id = id;
		this.name = name;
		this.influences = influences;
		// Add node identity on last position.
		this.influences.add(id);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Integer> getInfluences() {
		return influences;
	}

	public void setInfluences(List<Integer> influences) {
		this.influences = influences;
	}

	public List<String> getValues() {
		return values;
	}

	public void setValues(List<String> values) {
		this.values = values;
	}

	public NodePrinter evalProbabilities() {
		// Verify if node is marginal (only yourself influence).
		if (this.influences.size() == 1) {
			final Double sum = (double) this.values.size();
			final Map<String, Double> mappedValues = this.values.stream()
					.collect(Collectors.groupingBy(e -> e, Collectors.counting())).entrySet().stream()
					.collect(Collectors.toMap(Map.Entry::getKey, e -> evalAverage(e.getValue(), sum)));
			this.probabilities.putAll(mappedValues);

		} else {
			String groupKey = "";
			final Map<String, Long> auxProbabilities = new TreeMap<>();

			final Set<Entry<String>> entrySet = TreeMultiset.create(this.values).entrySet();
			for (Entry<String> entry : entrySet) {
				final String[] values = entry.getElement().split("\\s+");
				// Ignores node identity (last position) in the key.
				final String key = Arrays.toString(Arrays.copyOf(values, values.length - 1));
				if (!groupKey.equals(key)) {
					groupKey = key;
					if (!groupKey.isEmpty()) {
						this.putAllProbabilities(auxProbabilities);
						auxProbabilities.clear();
					}
				}
				auxProbabilities.put(entry.getElement(), Long.valueOf(entry.getCount()));
			}
			// Save last grouped key.
			this.putAllProbabilities(auxProbabilities);
		}
		return new NodePrinter();
	}

	private void putAllProbabilities(final Map<String, Long> auxProbabilities) {
		final Double sum = auxProbabilities.values().stream().mapToDouble(Number::doubleValue).sum();
		final Map<String, Double> mappedValues = auxProbabilities.entrySet().stream()
				.collect(Collectors.toMap(Map.Entry::getKey, e -> this.evalAverage(e.getValue(), sum)));
		this.probabilities.putAll(mappedValues);
	}
	
	private double evalAverage(Long valueCount, Double sum) {
		return valueCount / sum;
	}
	
	public class NodePrinter {

		public void printProbabilities() {
			final String[] header = { name, "Probabilities" };
			final Object[][] data = MapUtils.parseToMatrix(probabilities);
			final TextTable textTable = new TextTable(header, data);
			textTable.printTable();
			System.out.println();
		}
		
	}
}