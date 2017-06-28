package br.usp.scc5949;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;

import br.usp.scc5949.model.Node;

public class Main {

	public static void main(String[] args) {
		try {
			final ClassLoader loader = Thread.currentThread().getContextClassLoader();
			String path = loader.getResource("bc.csv").getFile();
			Main.readCsvResource(path);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static List<Node> readCsvResource(final String path) throws IOException {
		final File csvFile = new File(path);
		final CsvMapper mapper = new CsvMapper();
		mapper.enable(CsvParser.Feature.WRAP_AS_ARRAY);
		final MappingIterator<String[]> it = mapper.readerFor(String[].class).readValues(csvFile);
		final List<Node> nodes = new ArrayList<>();
		if (it.hasNext()) {
			final String[] headers = it.next();
			for (String header : headers) {
				nodes.add(new Node(header));
			}
			while (it.hasNext()) {
				final String[] values = it.next();
				for (int i = 0; i < values.length; i++) {
					String value = values[i];
					nodes.get(i).getValues().add(value);
				}
			}
			for (Node node : nodes) {
				node.evalMarginalProbabilities();
			}
		}
		return nodes;
	}
	

	public static void printCsvResource(final MappingIterator<String[]> it) {
		while (it.hasNext()) {
			final String[] row = it.next();
			System.out.println(ArrayUtils.toString(row));
		}
	}

}
