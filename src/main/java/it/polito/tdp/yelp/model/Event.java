package it.polito.tdp.yelp.model;

public class Event implements Comparable<Event>{
	
	public enum EventType{
		DA_INTERVISTARE,
		FERIE
	}

	private int giorno;
	private User intervistato;
	private Giornalista giornalista;
	private EventType tipo;
	public Event(int giorno, User intervistato, Giornalista giornalista, EventType type) {
		super();
		this.giorno = giorno;
		this.intervistato = intervistato;
		this.giornalista = giornalista;
		this.tipo = type;
	}
	public int getGiorno() {
		return giorno;
	}
	public User getIntervistato() {
		return intervistato;
	}
	public Giornalista getGiornalista() {
		return giornalista;
	}
	public EventType getTipo() {
		return this.tipo;
	}
	@Override
	public int compareTo(Event o) {
		// devo ordinare in base alla data
		return this.giorno - o.giorno;
	}
	
	
}
