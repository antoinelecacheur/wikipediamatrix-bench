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

	final String baseWikiURL = "https://en.wikipedia.org/wiki/";
	ArrayList<Table> tables;
	String url;

	/**
	 * Constructeur qui initialise les tables, stocke l'url et remplit les tables
	 * avec readURL.
	 */
	public MatrixExtractor(String url) throws IOException {
		this.tables = new ArrayList<Table>();
		this.url = url;
		this.readURL(url);
	}

	/**
	 * Méthode d'export vers des fichiers CSV de tous les tableaux de l'URL de
	 * l'extractor. Le chemin de destination fileoutputName est output/html/, on le
	 * laisse en argument au cas où il faudrait changer.
	 */
	public void exportToCSV(String fileoutputName) throws IOException {
		// On va réitérer le procédé pour chaque table
		for (Table tableau : this.tables) {
			// On stocke le contenu des headers dans un tableau de String pour les écrire
			// dans le CSV
			String[] headers = tableau.fillHead();
			try {
				BufferedWriter writer = Files
						.newBufferedWriter(Paths.get(fileoutputName + mkCSVFileName(url, tableau.getId())));
				CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(headers));

				// Écriture pour chaque ligne
				for (int id = 0; id < tableau.getRows().size(); id++) {
					String[] ligne = tableau.fillRow(id);
					csvPrinter.printRecord(ligne);
				}

				csvPrinter.flush();

			} catch (Exception e) {
			}

		}
	}

	/**
	 * Lit l'URL url (en rajoutant l'url de wikipedia de base), parcourt le fichier
	 * HTML source de la page en question. Stocke le contenu des headers et des
	 * lignes dans une Table pour chaque tableau de type 'wikitable sortable'.
	 */
	public void readURL(String url) throws IOException {
		try {
			Document doc = Jsoup.connect(baseWikiURL + url).get();
			Elements tables = doc.getElementsByClass("wikitable sortable");
			// On parcourt les tableaux du type 'wikitable sortable' qui seront les uniques
			// types de tableaux traités
			for (Element table : tables) {
				// On instancie un nouveau tableau, on stocke son numéro pour créer le CSV
				Table tableau = new Table(tables.indexOf(table) + 1);

				Elements lignes = table.select("tr");
				// Les headers sont les balises titres de la première ligne
				Elements headers = lignes.get(0).select("th");
				tableau.setHeaders(headers);

				// On parcourt les lignes (autres que les headers)
				for (int indice = 1; indice < lignes.size(); indice++) {
					Element ligne = lignes.get(indice);
					// On récupère les balise <th> qui sont en première colonne (certains tableaux à
					// 'double entrée')
					if (ligne.select("th").size() == 1) {
						tableau.addRow();
						ArrayList<Element> elements = new ArrayList<Element>();
						elements.add(ligne.select("th").get(0));
						Elements entries = ligne.select("td");
						elements.addAll(entries);
						tableau.getRows().get(tableau.getRowsCount() - 1).setElements(elements);
					} else {
						// Pour les tableaux classiques on récupère tous les élements de la ligne
						Elements entries = ligne.select("td");
						if (!entries.isEmpty()) {
							tableau.addRow();
							tableau.getRows().get(tableau.getRowsCount() - 1).setElements(entries);
						}
					}
				}
				// On remplit l'extractor avec le contenu des tableaux lus
				this.tables.add(tableau);
			}
		} catch (HttpStatusException e) {
			// Certaines URL renvoient un code HTTP 404, ou encore 200, etc.
			System.out.println("Impossible de se connecter à l'URL.");
		} catch (Exception e) {
			// Erreurs liées à la lecture et stockage en java du HTML.
			System.out.println("Impossible de lire le tableau.");
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
