package fr.univrennes1.istic.wikipediamatrix;

import java.util.ArrayList;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Row {
	ArrayList<Element> elements;
	
	public Row() {
		this.elements = new ArrayList<Element>();
	}

	public ArrayList<Element> getElements() {
		return elements;
	}

	public void setElements(ArrayList<Element> elements) {
		this.elements = elements;
	}
	
	public int getElementsCount() {
		return elements.size();
	}

	public void add(Element entry) {
		this.getElements().add(entry);
		
	}
}
