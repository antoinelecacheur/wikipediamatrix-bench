package fr.univrennes1.istic.wikipediamatrix;

import java.util.ArrayList;

import org.jsoup.nodes.Element;

public class Headers {
	ArrayList<Element> titres;
	
	public Headers() {
		this.titres = new ArrayList<Element>();
	}
	
	public int getTitresCount() {
		return titres.size();
	}

	public ArrayList<Element> getTitres() {
		return titres;
	}

	public void setTitres(ArrayList<Element> titres) {
		this.titres = titres;
	}
}
