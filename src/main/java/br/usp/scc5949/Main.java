package br.usp.scc5949;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang.ArrayUtils;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;

public class Main {

	public static void main(String[] args) {
		try {
			final ClassLoader loader = Thread.currentThread().getContextClassLoader();
			
			String path = loader.getResource("bc.csv").getFile();
			MappingIterator<String[]> it = Main.readCsvResource(path);
			Main.printCsvResource(it);
			
			System.out.println();
			
			path = loader.getResource("nhl.csv").getFile();
			it = Main.readCsvResource(path);
			Main.printCsvResource(it);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static MappingIterator<String[]> readCsvResource(final String path) throws IOException {
		final File csvFile = new File(path);
		final CsvMapper mapper = new CsvMapper();
		mapper.enable(CsvParser.Feature.WRAP_AS_ARRAY);
		final MappingIterator<String[]> it = mapper.readerFor(String[].class).readValues(csvFile);
		return it;
	}
	

	public static void printCsvResource(final MappingIterator<String[]> it) {
		while (it.hasNext()) {
			final String[] row = it.next();
			System.out.println(ArrayUtils.toString(row));
		}
	}

}
