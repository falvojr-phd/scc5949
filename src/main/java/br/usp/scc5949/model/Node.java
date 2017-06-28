package br.usp.scc5949.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Node {
	
	private String id;
	private List<String> values;
	private Map<String, Double> probabilities;
	private List<Node> parents;

	public Node(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
		if (probabilities == null) {
			probabilities = new HashMap<>();
		}
		return probabilities;
	}

	public void setProbabilities(Map<String, Double> probabilities) {
		this.probabilities = probabilities;
	}

	public List<Node> getParents() {
		if (parents == null) {
			parents = new ArrayList<>();
		}
		return parents;
	}

	public void setParents(List<Node> parents) {
		this.parents = parents;
	}

	public void evalMarginalProbabilities() {
		this.probabilities = this.values.stream()
				.collect(Collectors.groupingBy(e -> e, Collectors.counting()))
				.entrySet().stream()
				.collect(Collectors.toMap(Map.Entry::getKey, e -> evalAverage(e.getValue())));
	}

	private double evalAverage(Long valueCount) {
		final double valuesCount = (double) this.values.size();
		return valueCount / valuesCount;
	}


}
