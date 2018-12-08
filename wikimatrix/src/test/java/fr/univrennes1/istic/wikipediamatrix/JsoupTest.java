package fr.univrennes1.istic.wikipediamatrix;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

public class JsoupTest {
	@Test
	public void testUrl() throws IOException {
		Document doc = Jsoup.connect("http://en.wikipedia.org/").get();
		System.out.println(doc.title());
		Elements newsHeadlines = doc.select("#mp-itn b a");
		for (Element headline : newsHeadlines) {
		  System.out.println(("%s\n\t%s" + headline.attr("title") + headline.absUrl("href")));
		}
	}
	
	@Test
	public void testUrlCanon() throws IOException {
		String url = "https://en.wikipedia.org/wiki/Comparison_of_Canon_EOS_digital_cameras";
		Document doc = Jsoup.connect(url).get();
		//System.out.println(doc.title());
		Elements tables = doc.getElementsByClass("wikitable sortable");
		
		ArrayList<ArrayList<String>> contenuTable = new ArrayList<ArrayList<String>>();
		for (Element table : tables) {
			
			Table tableau = new Table(tables.indexOf(table)+1);
			
			Elements lignes = table.select("tr");
			for (Element ligne : lignes) {
				
				if(lignes.indexOf(ligne)==0) {
					Elements headers = ligne.select("th");
					if (headers.isEmpty()) {
						headers = ligne.select("td");
					}
					tableau.setHeaders(headers);

				} else if (lignes.indexOf(ligne)<lignes.size()){
					tableau.addRow();
					Elements entries = ligne.select("td");
					for (Element entry : entries) {
						tableau.getRows().get(lignes.indexOf(ligne)-1).setElements(entries);//add(entry);
						
					}
				}
				
				
			}
			System.out.println(tableau.getHeaders());
			createCsv(tableau);
		}
		     
	}
	
	public void createCsv(Table tableau) throws IOException {
		//List<String> h = new ArrayList<String>();
		
		String[] headers = tableau.fillRow(0);
		
		
		
		BufferedWriter writer = Files.newBufferedWriter(Paths.get("./output/html/test-" + tableau.getId() + ".csv"));
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                .withHeader(headers));
        for(int id = 1; id<tableau.getRows().size();id++) {
        	String[] ligne = tableau.fillRow(id);
        	csvPrinter.printRecord(ligne);
        }
        
        csvPrinter.flush();
	}
	
	
	
}
