package it.polito.tdp.yelp.model;

public class Giornalista {
	
	private int id;
	private int nIntervistati;
	public Giornalista(int id) {
		super();
		this.id = id;
		this.nIntervistati = 0;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getnIntervistati() {
		return nIntervistati;
	}
	public void incrementaNIntervistati() {
		this.nIntervistati++;
	}
	@Override
	public String toString() {
		return "Giornalista: " + id + " ha intervistato " + nIntervistati + " persone.";
	}
	

}
