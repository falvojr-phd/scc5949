package br.usp.scc5949;

import java.io.File;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import com.google.common.collect.Lists;

import br.usp.scc5949.model.BayesNode;

/**
 * Main class for file (CSV) read and {@link BayesNode} configurations.
 * 
 * @author falvojr
 */
public class Main {

	public static void main(String[] args) {
		final int dirIndex = ArrayUtils.indexOf(args, "-f");
		final int pathIndex = dirIndex + 1;
		if (dirIndex > -1) {
		    try {
		        final String path = Paths.get(args[pathIndex]).getFileName().toString();
				final int bcIndex = ArrayUtils.indexOf(args, "-bc");
				final int nhlIndex = ArrayUtils.indexOf(args, "-nhl");
		        if (bcIndex != -1) {
		        	solveBc(path);
				} else if (nhlIndex != -1) {
					solveNhl(path);
				} else {
					System.err.println("O argumento -bc ou -nhl e obrigatorio. Exemplo de sintaxe: -f [path] -bc");
				}
		    } catch (InvalidPathException | IndexOutOfBoundsException | IOException exception) {
		        System.err.println("O path especificado para o argumento -f nao e valido");
		    }
		} else {
		    System.err.println("O argumento -f e obrigatorio, bem como seu respectivo path. Exemplo de sintaxe: -d [path]");
		}
	}

	public static void solveBc(final String path) throws IOException {
		final Map<Integer, List<Integer>> bcInfluences = new HashMap<>();
		bcInfluences.put(0, Lists.newArrayList());
		bcInfluences.put(1, Lists.newArrayList());
		bcInfluences.put(2, Lists.newArrayList());
		bcInfluences.put(3, Lists.newArrayList(1, 2));
		bcInfluences.put(4, Lists.newArrayList(0, 3));
		bcInfluences.put(5, Lists.newArrayList(3));
		bcInfluences.put(6, Lists.newArrayList(3));
		bcInfluences.put(7, Lists.newArrayList(3));
		bcInfluences.put(8, Lists.newArrayList(4));
		bcInfluences.put(9, Lists.newArrayList(4));
		bcInfluences.put(10, Lists.newArrayList(5));
		bcInfluences.put(11, Lists.newArrayList(6));
		bcInfluences.put(12, Lists.newArrayList(3, 10));
		bcInfluences.put(13, Lists.newArrayList(3, 10));
		bcInfluences.put(14, Lists.newArrayList(10));
		bcInfluences.put(15, Lists.newArrayList(4, 14));

		Main.whritePossibilitiesTables(path, bcInfluences);
	}

	public static void solveNhl(final String path) throws IOException {
		final Map<Integer, List<Integer>> nhlInfluences = new HashMap<>();
		nhlInfluences.put(0, Lists.newArrayList());
		nhlInfluences.put(1, Lists.newArrayList(0));
		nhlInfluences.put(2, Lists.newArrayList(0, 3, 4));
		nhlInfluences.put(3, Lists.newArrayList(0));
		nhlInfluences.put(4, Lists.newArrayList(0));
		nhlInfluences.put(5, Lists.newArrayList());
		nhlInfluences.put(6, Lists.newArrayList());
		nhlInfluences.put(7, Lists.newArrayList());
		nhlInfluences.put(8, Lists.newArrayList(0, 6));
		nhlInfluences.put(9, Lists.newArrayList(6));
		nhlInfluences.put(10, Lists.newArrayList(6));
		nhlInfluences.put(11, Lists.newArrayList(2, 3, 4, 6, 7, 12));
		nhlInfluences.put(12, Lists.newArrayList(8, 9, 10));
		nhlInfluences.put(13, Lists.newArrayList(0, 2, 3, 4, 11));

		Main.whritePossibilitiesTables(path, nhlInfluences);
	}

	public static List<BayesNode> whritePossibilitiesTables(final String path, Map<Integer, List<Integer>> influences)
			throws IOException {
		final CsvMapper mapper = new CsvMapper();
		mapper.enable(CsvParser.Feature.WRAP_AS_ARRAY);
		final MappingIterator<String[]> it = mapper.readerFor(String[].class).readValues(new File(path));

		final List<BayesNode> nodes = new ArrayList<>();

		if (it.hasNext()) {
			final String[] headers = it.next();
			for (int index = 0; index < headers.length; index++) {
				final String name = headers[index];
				nodes.add(new BayesNode(index, name, influences.get(index)));
			}
		}

		while (it.hasNext()) {
			final String[] values = it.next();
			for (int index = 0; index < values.length; index++) {
				final BayesNode node = nodes.get(index);
				String preparedValue = "";
				for (final Integer influence : node.getInfluences()) {
					preparedValue += String.format("%-12s", values[influence]);
				}
				node.getValues().add(preparedValue);
			}
		}

		for (final BayesNode node : nodes) {
			node.evalProbabilities().printProbabilities();
		}

		return nodes;
	}

}