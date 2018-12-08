package fr.univrennes1.istic.wikipediamatrix;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class MatrixExtractor {

	final static String baseWikiURL = "https://en.wikipedia.org/wiki/";
	ArrayList<Table> tables;
	String url;

	public MatrixExtractor(String url) throws IOException {
		this.tables = new ArrayList<Table>();
		this.url = url;
		this.readURL(url);
	}

	public void exportToCSV(String fileoutputName) throws IOException {
		for (Table tableau : this.tables) {
			String[] headers = tableau.fillHead();
			try {
				BufferedWriter writer = Files
						.newBufferedWriter(Paths.get(fileoutputName + mkCSVFileName(url, tableau.getId())));
				CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(headers));
				for (int id = 0; id < tableau.getRows().size(); id++) {
					String[] ligne = tableau.fillRow(id);
					csvPrinter.printRecord(ligne);
				}

				csvPrinter.flush();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	}

	public void readURL(String url) throws IOException {
		try {
			Document doc = Jsoup.connect(baseWikiURL + url).get();
			Elements tables = doc.getElementsByClass("wikitable sortable");
			for (Element table : tables) {
				Table tableau = new Table(tables.indexOf(table) + 1);

				Elements lignes = table.select("tr");
				Elements headers = lignes.get(0).select("th");
				tableau.setNbColumns(headers.size());
				tableau.setHeaders(headers);
				lignes.select("td");
				for (int indice = 1; indice < lignes.size(); indice++) {
					Element ligne = lignes.get(indice);
					if(ligne.select("th").size()==1) {
						tableau.addRow();
						ArrayList<Element> elements = new ArrayList<Element>();
						elements.add(ligne.select("th").get(0));
						Elements entries = ligne.select("td");
						elements.addAll(entries);
						tableau.getRows().get(tableau.getRowsCount()-1).setElements(elements);
					} else {
						Elements entries = ligne.select("td");
						if (!entries.isEmpty()) {
							tableau.addRow();
							tableau.getRows().get(tableau.getRowsCount() - 1).setElements(entries);
						}
					}
					

				}
				this.tables.add(tableau);
			}
		} catch (HttpStatusException e) {
			System.out.println("Unable to log to the URL");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public ArrayList<Table> getTables() {
		return tables;
	}

	public void setTables(ArrayList<Table> tables) {
		this.tables = tables;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String mkCSVFileName(String url, int n) {
		return url.trim() + "-" + n + ".csv";
	}
}
