package br.usp.scc5949;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import com.google.common.collect.Lists;

import br.usp.scc5949.model.Node;

public class Main {
	
	private static final ClassLoader CLASS_LOADER = Thread.currentThread().getContextClassLoader();

	public static void main(String[] args) {
		try {
			Main.solveBc();
			Main.solveNhl();
		} catch (IOException e) {
			System.err.println(e);
		}
	}

	public static void solveBc() throws IOException {
		final Map<Integer, List<Integer>> bcInfluences = new HashMap<>();
		bcInfluences.put(0, Lists.newArrayList(0));
		bcInfluences.put(1, Lists.newArrayList(1));
		bcInfluences.put(2, Lists.newArrayList(2));
		bcInfluences.put(3, Lists.newArrayList(1, 2, 3));
		bcInfluences.put(4, Lists.newArrayList(0, 3, 4));
		bcInfluences.put(5, Lists.newArrayList(3, 5));
		bcInfluences.put(6, Lists.newArrayList(3, 6));
		bcInfluences.put(7, Lists.newArrayList(3, 7));
		bcInfluences.put(8, Lists.newArrayList(4, 8));
		bcInfluences.put(9, Lists.newArrayList(4, 9));
		bcInfluences.put(10, Lists.newArrayList(5, 10));
		bcInfluences.put(11, Lists.newArrayList(6, 11));
		bcInfluences.put(12, Lists.newArrayList(3, 10, 12));
		bcInfluences.put(13, Lists.newArrayList(3, 10, 13));
		bcInfluences.put(14, Lists.newArrayList(10, 14));
		bcInfluences.put(15, Lists.newArrayList(4, 14, 15));

		final String path = CLASS_LOADER.getResource("bc.csv").getFile();
		Main.whritePossibilitiesTables(path, bcInfluences);
	}

	public static void solveNhl() throws IOException {
		final Map<Integer, List<Integer>> nhlInfluences = new HashMap<>();
		nhlInfluences.put(0, Lists.newArrayList(0));
		nhlInfluences.put(1, Lists.newArrayList(0, 1));
		nhlInfluences.put(2, Lists.newArrayList(0, 3, 4, 2));
		nhlInfluences.put(3, Lists.newArrayList(0, 3));
		nhlInfluences.put(4, Lists.newArrayList(0, 4));
		nhlInfluences.put(5, Lists.newArrayList(5));
		nhlInfluences.put(6, Lists.newArrayList(6));
		nhlInfluences.put(7, Lists.newArrayList(7));
		nhlInfluences.put(8, Lists.newArrayList(0, 6, 8));
		nhlInfluences.put(9, Lists.newArrayList(6, 9));
		nhlInfluences.put(10, Lists.newArrayList(6, 10));
		nhlInfluences.put(11, Lists.newArrayList(2, 3, 4, 6, 7, 12, 11));
		nhlInfluences.put(12, Lists.newArrayList(8, 9, 10, 12));
		nhlInfluences.put(13, Lists.newArrayList(0, 2, 3, 4, 11, 13));

		final String path = CLASS_LOADER.getResource("nhl.csv").getFile();
		Main.whritePossibilitiesTables(path, nhlInfluences);
	}

	public static List<Node> whritePossibilitiesTables(final String path, Map<Integer, List<Integer>> influences)
			throws IOException {
		final CsvMapper mapper = new CsvMapper();
		mapper.enable(CsvParser.Feature.WRAP_AS_ARRAY);
		final MappingIterator<String[]> it = mapper.readerFor(String[].class).readValues(new File(path));

		final List<Node> nodes = new ArrayList<>();

		if (it.hasNext()) {
			final String[] headers = it.next();
			for (int i = 0; i < headers.length; i++) {
				final String name = headers[i];
				nodes.add(new Node(name, influences.get(i)));
			}
		}
		
		while (it.hasNext()) {
			final String[] values = it.next();
			for (int i = 0; i < values.length; i++) {
				final Node node = nodes.get(i);
				
				String preparedValue = "";
				for (Integer influence : node.getInfluences()) {
					preparedValue += String.format("%-13s", values[influence]);
				}
				node.getValues().add(preparedValue);
			}
		}

		for (Node node : nodes) {
			node.evalProbabilities();
			node.print();
		}
		
		return nodes;
	}

}