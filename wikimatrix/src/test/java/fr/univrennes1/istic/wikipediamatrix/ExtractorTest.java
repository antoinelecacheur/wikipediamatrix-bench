package fr.univrennes1.istic.wikipediamatrix;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;

public class ExtractorTest {

	/**
	 * Test automatisé pour vérifier le nombre de Tableaux d'une URL, le nombre de
	 * colonnes et de lignes d'un tableau.
	 * 
	 * @param extractor L'extractor de l'URL qu'on souhaite tester.
	 * @throws IOException
	 */
	@Test
	public void testExtractor(MatrixExtractor extractor) throws IOException {
		// Chemin de lecture des fichiers.
		String fileoutputName = "output/html/";
		int nbTables = extractor.getTables().size();
		try {
			Document doc = Jsoup.connect(extractor.baseWikiURL + extractor.getUrl()).get();
			Elements tables = doc.getElementsByClass("wikitable sortable");
			// On vérifie que le nombre de tableaux enregistrés est bien égal au nombre de
			// tableaux de type 'wikitable sortable' dans le fichier HTML
			assertEquals(nbTables, tables.size());

		} catch (Exception e) {
		}

		// On teste pour chaque tableau
		for (int indice = 0; indice < nbTables; indice++) {
			try (Reader reader = Files.newBufferedReader(
					Paths.get(extractor.mkCSVFileName(fileoutputName + extractor.getUrl(), indice + 1)));
					CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader());) {
				// On s'assure de l'existence du fichier
				assertNotNull(csvParser);
				Table table = extractor.getTables().get(indice);
				// Le nombre de lignes (hors headers) correspond bien aux lignes stockées
				assertEquals(csvParser.getRecords().size(), table.getRowsCount());
				// Nombre de colonnes dans le headers correspond bien à celui stocké
				assertEquals(csvParser.getHeaderMap().size(), table.getHeaders().size());
			}
		}

	}
}
