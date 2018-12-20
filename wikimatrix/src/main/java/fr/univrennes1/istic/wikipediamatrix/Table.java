package fr.univrennes1.istic.wikipediamatrix;

import java.util.ArrayList;

import org.jsoup.nodes.Element;

public class Table {
	int nbTable;
	ArrayList<Element> headers;
	ArrayList<Row> rows;

	/**
	 * Le num�ro de tableau est stock� pour g�rer les noms de fichiers CSV pour les
	 * diff�rents tableaux d'une URL. On instancie les headers et les rows.
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
	 * M�thode n�cessaire pour formater les titres pour la m�thode withHeaders().
	 * 
	 * @return un tableau de String contenant les titres du tableau.
	 */
	public String[] fillHead() {
		// Gestion des titres en double dans un m�me tableau (sinon le tableau n'est pas
		// cr��)
		this.duplicate();
		String[] ligne = new String[this.getHeaders().size()];
		try {
			// On parcourt les headers
			for (int indice = 0; indice < this.getHeaders().size(); indice++) {
				String content = this.getHeaders().get(indice).text();
				// On g�re le cas o� le texte contient d�j� une virgule
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
	 * M�thode n�cessaire pour formater les lignes pour la m�thode printRecord().
	 * 
	 * @param id num�ro de la ligne � remplir
	 * @return un tableau de String contenant le texte de la ligne
	 */
	public String[] fillRow(int id) {
		String[] ligne = new String[this.getRows().get(id).getElementsCount()];
		try {
			// On parcourt les �lements de la ligne
			for (int indice = 0; indice < this.getHeaders().size(); indice++) {
				Row row = this.getRows().get(id);
				String content = row.getElements().get(indice).text();
				// Gestion du texte contenant d�j� une virgule
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
	 * incr�mentant le nom. Ex : S'il y a deux fois le titre "Titre" dans le
	 * tableau, la deuxi�me occurrence est remplac�e par "Titre1". M�thode sensible
	 * � la casse.
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
