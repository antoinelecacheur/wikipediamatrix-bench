package fr.univrennes1.istic.wikipediamatrix;

import java.util.ArrayList;

import org.jsoup.nodes.Element;

public class Table {
	int nbTable;
	int nbColumns = 0;
	ArrayList<Element> headers;
	ArrayList<Row> rows;

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

	public int getNbColumns() {
		return nbColumns;
	}

	public void setNbColumns(int nbColumns) {
		this.nbColumns = nbColumns;
	}

	public int getRowsCount() {
		return rows.size();

	}

	public void addRow() {
		this.getRows().add(new Row());
	}

	public String[] fillHead() {
		String[] ligne = new String[this.getHeaders().size()];
		try {
			for (int indice = 0; indice < this.getHeaders().size(); indice++) {
				String content = this.getHeaders().get(indice).text();
				if (content.contains(",")) {
					ligne[indice] = "\"" + content + "\"";
				} else {
					ligne[indice] = content;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return ligne;
	}

	public String[] fillRow(int id) {
		String[] ligne = new String[this.getRows().get(id).getElementsCount()];
		try {
			int cpt=0;
			for (int indice = 0; indice < this.getHeaders().size(); indice++) {
				Row row = this.getRows().get(id);
				String content = row.getElements().get(indice).text();
				if (content.contains(",")) {
					ligne[indice] = "\"" + content + "\"";
				} else {
					ligne[indice] = content;
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return ligne;

	}
}
