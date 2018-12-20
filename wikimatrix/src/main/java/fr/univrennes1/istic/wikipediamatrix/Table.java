package fr.univrennes1.istic.wikipediamatrix;

import java.util.ArrayList;

import org.jsoup.nodes.Element;

public class Table {
	int nbTable;
	ArrayList<Element> headers;
	ArrayList<Row> rows;

	/**
	 * Le numéro de tableau est stocké pour gérer les noms de fichiers CSV pour les
	 * différents tableaux d'une URL. On instancie les headers et les rows.
	 */
	public Table(int nbTable) {
		this.nbTable = nbTable;
		this.headers = new ArrayList<Element>();
		this.rows = new ArrayList<Row>();
	}

	public ArrayList<Element> getHeaders() {
		return headers;
	}

	public void setHeaders(ArrayList<Element> headers) {
		this.headers = headers;
	}

	public ArrayList<Row> getRows() {
		return rows;
	}

	public void setRows(ArrayList<Row> rows) {
		this.rows = rows;
	}

	public int getId() {
		return nbTable;
	}

	public void setId(int id) {
		this.nbTable = id;
	}

	public int getRowsCount() {
		return rows.size();

	}

	/**
	 * Ajout d'une nouvelle ligne vide
	 */
	public void addRow() {
		this.getRows().add(new Row());
	}

	/**
	 * Méthode nécessaire pour formater les titres pour la méthode withHeaders().
	 * 
	 * @return un tableau de String contenant les titres du tableau.
	 */
	public String[] fillHead() {
		// Gestion des titres en double dans un même tableau (sinon le tableau n'est pas
		// créé)
		this.duplicate();
		String[] ligne = new String[this.getHeaders().size()];
		try {
			// On parcourt les headers
			for (int indice = 0; indice < this.getHeaders().size(); indice++) {
				String content = this.getHeaders().get(indice).text();
				// On gère le cas où le texte contient déjà une virgule
				if (content.contains(",")) {
					ligne[indice] = "\"" + content + "\"";
				} else {
					ligne[indice] = content;
				}
			}
		} catch (Exception e) {
		}

		return ligne;
	}

	/**
	 * Méthode nécessaire pour formater les lignes pour la méthode printRecord().
	 * 
	 * @param id numéro de la ligne à remplir
	 * @return un tableau de String contenant le texte de la ligne
	 */
	public String[] fillRow(int id) {
		String[] ligne = new String[this.getRows().get(id).getElementsCount()];
		try {
			// On parcourt les élements de la ligne
			for (int indice = 0; indice < this.getHeaders().size(); indice++) {
				Row row = this.getRows().get(id);
				String content = row.getElements().get(indice).text();
				// Gestion du texte contenant déjà une virgule
				if (content.contains(",")) {
					ligne[indice] = "\"" + content + "\"";
				} else {
					ligne[indice] = content;
				}

			}
		} catch (Exception e) {
		}

		return ligne;

	}

	/**
	 * Parcourt la liste des headers du tableau et remplace les doublons en
	 * incrémentant le nom. Ex : S'il y a deux fois le titre "Titre" dans le
	 * tableau, la deuxième occurrence est remplacée par "Titre1". Méthode sensible
	 * à la casse.
	 */
	public void duplicate() {
		int nbHeaders = this.getHeaders().size();
		int cpt = 0;
		for (int nbHead = 0; nbHead < nbHeaders - 1; nbHead++) {
			String header = this.getHeaders().get(nbHead).text();
			for (int indice = nbHead + 1; indice < nbHeaders; indice++) {
				if (header.equals(this.getHeaders().get(indice).text())) {
					cpt++;
					this.getHeaders().get(indice).text(this.getHeaders().get(indice).text() + cpt);
				}
			}
		}
	}

}
