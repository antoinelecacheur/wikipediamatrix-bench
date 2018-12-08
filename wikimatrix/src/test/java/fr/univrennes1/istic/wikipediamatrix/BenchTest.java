package fr.univrennes1.istic.wikipediamatrix;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;

/*
 * TEST
 */

public class BenchTest {
	
	/*
	*  the challenge is to extract as many relevant tables as possible
	*  and save them into CSV files  
	*  from the 300+ Wikipedia URLs given
	*  see below for more details
	**/
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
	    while ((url = br.readLine()) != null) {
	       String wurl = BASE_WIKIPEDIA_URL + url; 
	       System.out.println("Wikipedia url: " + wurl);
	       // TODO: do something with the Wikipedia URL 
	       MatrixExtractor extractor = new MatrixExtractor(url);
	       String fileoutputName = "output/html/";
	       extractor.exportToCSV(fileoutputName);
	       
	       ExtractorTest test = new ExtractorTest();
	       test.testExtractor(extractor);
		    
	       
	       // for exporting to CSV files, we will use mkCSVFileName 
	       // example: for https://en.wikipedia.org/wiki/Comparison_of_operating_system_kernels
	       // the *first* extracted table will be exported to a CSV file called 
	       // "Comparison_of_operating_system_kernels-1.csv"

	       // the *second* (if any) will be exported to a CSV file called
	       // "Comparison_of_operating_system_kernels-2.csv"
	       
	       // TODO: the HTML extractor should save CSV files into output/HTML
	       // see outputDirHtml 
	       
	       // TODO: the Wikitext extractor should save CSV files into output/wikitext
	       // see outputDirWikitext      
	       
	       nurl++;	       
	    }
	    
	    br.close();	    
	    assertEquals(nurl, 336);
	    



	}
	
	@Test
	public void testCanon() throws IOException {
		String url  = "Comparison_of_Canon_EOS_digital_cameras";
		MatrixExtractor extractor = new MatrixExtractor(url);
		assertEquals(extractor.getTables().size(),1);
		 String fileoutputName = "output/html/";
		 extractor.exportToCSV(fileoutputName);
		 
		 try (
		            Reader reader = Files.newBufferedReader(Paths.get(mkCSVFileName(fileoutputName +url ,1)));
		            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader());
		        ) {
			 assertNotNull(csvParser);
			 // Nombre de lignes (hors headers)
			 assertEquals(csvParser.getRecords().size(),62);
			 // Nombre de colonnes dans le headers
			 assertEquals(csvParser.getHeaderMap().size(),18);
		            for (CSVRecord csvRecord : csvParser) {
		                // Accessing Values by Column Index
		            	assertEquals(csvRecord.size(),18);
		            }   
		        }		
	}
	
	@Test
	public void testUS() throws IOException {
		String url  = "Comparison_between_U.S._states_and_countries_by_GDP_(PPP)";
		MatrixExtractor extractor = new MatrixExtractor(url);
		assertEquals(extractor.getTables().size(),1);
		 String fileoutputName = "output/html/";
		 extractor.exportToCSV(fileoutputName);
		 
		 try (
		            Reader reader = Files.newBufferedReader(Paths.get(mkCSVFileName(fileoutputName +url ,1)));
		            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader());
		        ) {
			 assertNotNull(csvParser);
			 // Nombre de lignes (hors headers)
			 assertEquals(csvParser.getRecords().size(),51);
			 // Nombre de colonnes dans le headers
			 assertEquals(csvParser.getHeaderMap().size(),4);
		            for (CSVRecord csvRecord : csvParser) {
		                // Accessing Values by Column Index
		            	assertEquals(csvRecord.size(),4);
		            }
		            
		        }		
	}

	private String mkCSVFileName(String url, int n) {
		return url.trim() + "-" + n + ".csv";
	}

}
