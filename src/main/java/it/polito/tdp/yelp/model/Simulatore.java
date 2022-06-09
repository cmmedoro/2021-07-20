package it.polito.tdp.yelp.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.yelp.model.Event.EventType;

public class Simulatore {
	
	//dati in ingresso 
	private int x1;
	private int x2;
	
	//dati in uscita
	//i giornalisti sono rappresentati da un numero compreso da 0 a x1-1
	private List<Giornalista> giornalisti;
	private int numGiorniSimulati;
	
	//modello del mondo
	private Set<User> intervistati;
	private Graph<User, DefaultWeightedEdge> grafo;
	
	//coda degli eventi + tipo eventi
	private PriorityQueue<Event> queue;
	
	
	public Simulatore(Graph<User, DefaultWeightedEdge> grafo) {
		this.grafo = grafo;
	}
	
	public void init(int x1, int x2) {
		this.x1 = x1;
		this.x2 = x2;
		this.intervistati = new HashSet<User>();
		this.numGiorniSimulati = 0;
		this.giornalisti = new ArrayList<>();
		for(int id = 0; id < x1; id++) {
			this.giornalisti.add(new Giornalista(id));
		}
		//Devo caricare la coda con le interviste del primo giorno
		for(Giornalista g : this.giornalisti) {
			//scelgo intervistato fra tutti i vertici del grafo
			User intervistato = this.selezionaIntervistato(this.grafo.vertexSet());
			this.intervistati.add(intervistato);
			g.incrementaNIntervistati();
			this.queue.add(new Event(1, intervistato, g, EventType.DA_INTERVISTARE));
		}
	}
	
	public void run() {
		while(!this.queue.isEmpty() && this.intervistati.size() < x2) {
			Event e = this.queue.poll();
			this.numGiorniSimulati = e.getGiorno();
			//elaboro evento
			processEvent(e);
		}
	}

	private void processEvent(Event e) {
		switch(e.getTipo()) {
		case DA_INTERVISTARE:
			double caso = Math.random();
			if(caso < 0.6) {
				//caso 1
				User vicino = this.selezionaAdiacente(e.getIntervistato());
				if(vicino == null) {
					vicino = this.selezionaIntervistato(this.grafo.vertexSet());
				}
				this.queue.add(new Event(e.getGiorno()+1, vicino, e.getGiornalista(), EventType.DA_INTERVISTARE));
				this.intervistati.add(vicino);
				e.getGiornalista().incrementaNIntervistati();
			}else if (caso < 0.8) {
				//caso 2: rimando a domani la scelta
				this.queue.add(new Event(e.getGiorno()+1, e.getIntervistato(), e.getGiornalista(), EventType.FERIE));
				//non scelgo ancora chi dovrò intervistare perchè nel frattempo altre persone sono scelte da altri giornalisti
			}
			else {
				//caso 3: domani continuo con lo stesso
				this.queue.add(new Event(e.getGiorno()+1, e.getIntervistato(), e.getGiornalista(), EventType.DA_INTERVISTARE));
			}
			break;
		case FERIE:
			User vicino = selezionaAdiacente(e.getIntervistato());
			if(vicino == null) {
				vicino = selezionaIntervistato(this.grafo.vertexSet());
			}
			this.queue.add( new Event(e.getGiorno()+1,vicino, e.getGiornalista(),EventType.DA_INTERVISTARE)) ;
			
			this.intervistati.add(vicino);
			e.getGiornalista().incrementaNIntervistati();
			break;
		}
		
	}

	public int getX1() {
		return x1;
	}

	public void setX1(int x1) {
		this.x1 = x1;
	}

	public int getX2() {
		return x2;
	}

	public void setX2(int x2) {
		this.x2 = x2;
	}

	public List<Giornalista> getGiornalisti() {
		return giornalisti;
	}

	public int getNumGiorniSimulati() {
		return numGiorniSimulati;
	}
	/**
	 * Seleziona un intervistato dalla lista specificata evitando di selezionare coloro che sono in this.intervistati
	 * @param lista
	 * @return
	 */
	private User selezionaIntervistato(Collection<User> lista) {
		Set<User> candidati = new HashSet<User>(lista);
		candidati.removeAll(this.intervistati); 
		//insieme dei potenziali candidati
		//devo estrarre un numero casuale fra 0 e gli elementi del set
		int scelto = (int)(Math.random()*candidati.size());
		return (new ArrayList<>(candidati).get(scelto));
	}
	
	private User selezionaAdiacente(User u) {
		//prendo i vicini, toglo i visitati e vedo se ne restano
		List<User> vicini = Graphs.neighborListOf(this.grafo, u);
		vicini.removeAll(this.intervistati); 
		if(vicini.size() == 0) { 
			//se è vertice isolato o tutti gli adiacenti sono gi stati intervistati
			return null;
		}
		double max = 0;
		for(User v : vicini) {
			double peso = this.grafo.getEdgeWeight(this.grafo.getEdge(u, v));
			if(peso > max) {
				max = peso;
			}
		}
		List<User> migliori = new ArrayList<>();
		for(User vv : vicini) {
			double peso = this.grafo.getEdgeWeight(this.grafo.getEdge(u, vv));
			if(peso == max) {
				migliori.add(vv);
			}
		}
		int scelto = (int)(Math.random()*migliori.size());
		return migliori.get(scelto);
	}
}
