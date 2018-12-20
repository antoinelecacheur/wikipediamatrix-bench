package fr.univrennes1.istic.wikipediamatrix;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.math3.stat.Frequency;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.junit.Test;

/*
 * TEST
 */

public class BenchTest {

	/**
	 * Méthode à lancer pour créer les tableaux. Effectue une batterie de tests sur
	 * le nombre de tableaux, de colonnes et de lignes pour chaque URL.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testBenchExtractors() throws Exception {

		String BASE_WIKIPEDIA_URL = "https://en.wikipedia.org/wiki/";
		// directory where CSV files are exported (HTML extractor)
		String outputDirHtml = "output" + File.separator + "html" + File.separator;
		assertTrue(new File(outputDirHtml).isDirectory());
		// directory where CSV files are exported (Wikitext extractor)
		String outputDirWikitext = "output" + File.separator + "wikitext" + File.separator;
		assertTrue(new File(outputDirWikitext).isDirectory());

		File file = new File("inputdata" + File.separator + "wikiurls.txt");
		BufferedReader br = new BufferedReader(new FileReader(file));
		String url;
		int nurl = 0;
		// Statistiques descriptives du nombre de colonnes et de lignes
		DescriptiveStatistics nbColonnes = new DescriptiveStatistics();
		DescriptiveStatistics nbLignes = new DescriptiveStatistics();
			List<String> headersFreq = new ArrayList<String>();

		while ((url = br.readLine()) != null) {
			String wurl = BASE_WIKIPEDIA_URL + url;
			System.out.println("Wikipedia url: " + wurl);
			// On instancie l'extracteur pour chaque URL qui va faire tout le travail
			MatrixExtractor extractor = new MatrixExtractor(url);
			String fileoutputName = "output/html/";
			// Écriture des CSV
			extractor.exportToCSV(fileoutputName);
			// Tests automatiques
			ExtractorTest test = new ExtractorTest();
			test.testExtractor(extractor);

			// Élaboration des statistiques descriptives
			for (int indice = 0; indice < extractor.getTables().size(); indice++) {
				try (Reader reader = Files.newBufferedReader(
						Paths.get(extractor.mkCSVFileName(fileoutputName + extractor.getUrl(), indice + 1)));
						CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader());) {

					nbColonnes.addValue(csvParser.getHeaderMap().size());
					nbLignes.addValue(csvParser.getRecords().size());
					for (int indiceHead = 0; indiceHead < extractor.getTables().get(indice).getHeaders()
							.size(); indiceHead++) {
						headersFreq.add(extractor.getTables().get(indice).getHeaders().get(indiceHead).text());
					}
				}
			}

			nurl++;
		}

		br.close();
		assertEquals(nurl, 336);
		
		Map<String, Long> counts = headersFreq.stream().collect(Collectors.groupingBy(e -> e, Collectors.counting()));
		afficherStats(nbColonnes, nbLignes, counts);

	}

	/**
	 * Tests sur l'url 'Comparison_of_Canon_EOS_digital_cameras'
	 * 
	 * @throws IOException
	 */
	@Test
	public void testCanon() throws IOException {
		String url = "Comparison_of_Canon_EOS_digital_cameras";
		MatrixExtractor extractor = new MatrixExtractor(url);
		assertEquals(extractor.getTables().size(), 1);
		String fileoutputName = "output/html/";
		extractor.exportToCSV(fileoutputName);

		try (Reader reader = Files.newBufferedReader(Paths.get(extractor.mkCSVFileName(fileoutputName + url, 1)));
				CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader());) {
			assertNotNull(csvParser);
			// Nombre de lignes (hors headers)
			assertEquals(csvParser.getRecords().size(), 62);
			// Nombre de colonnes dans le headers
			assertEquals(csvParser.getHeaderMap().size(), 18);
			for (CSVRecord csvRecord : csvParser) {
				// Accessing Values by Column Index
				assertEquals(csvRecord.size(), 18);
			}
		}
	}

	/**
	 * Tests sur l'url 'Comparison_between_U.S._states_and_countries_by_GDP_(PPP)',
	 * par opposition à la précédente URL on n'avait pas de headers en fin de
	 * tableau
	 * 
	 * @throws IOException
	 */
	@Test
	public void testUS() throws IOException {
		String url = "Comparison_between_U.S._states_and_countries_by_GDP_(PPP)";
		MatrixExtractor extractor = new MatrixExtractor(url);
		assertEquals(extractor.getTables().size(), 1);
		String fileoutputName = "output/html/";
		extractor.exportToCSV(fileoutputName);

		try (Reader reader = Files.newBufferedReader(Paths.get(extractor.mkCSVFileName(fileoutputName + url, 1)));
				CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader());) {
			assertNotNull(csvParser);
			// Nombre de lignes (hors headers)
			assertEquals(csvParser.getRecords().size(), 51);
			// Nombre de colonnes dans le headers
			assertEquals(csvParser.getHeaderMap().size(), 4);
			for (CSVRecord csvRecord : csvParser) {
				// Accessing Values by Column Index
				assertEquals(csvRecord.size(), 4);
			}

		}
	}

	/**
	 * Affichage des statistiques descriptives pour le nombre de colonnes et de
	 * lignes.
	 * 
	 * @param nbColonnes
	 * @param nbLignes
	 */
	public void afficherStats(DescriptiveStatistics nbColonnes, DescriptiveStatistics nbLignes, Map<String,Long> counts) {
		System.out.println("\n" + "Nombre minimal de colonnes : " + nbColonnes.getMin() + "\n"
				+ "Nombre maximal de colonnes : " + nbColonnes.getMax() + "\n" + "Nombre moyen de colonnes : "
				+ nbColonnes.getMean() + "\n" + "Variance du nombre de colonnes : " + nbColonnes.getStandardDeviation()
				+ "\n" + "\n" + "Nombre minimal de lignes : " + nbLignes.getMin() + "\n" + "Nombre maximal de lignes : "
				+ nbLignes.getMax() + "\n" + "Nombre moyen de lignes : " + nbLignes.getMean() + "\n"
				+ "Variance du nombre de lignes : " + nbLignes.getStandardDeviation() + "\n" +
				counts.toString());
	}

}
