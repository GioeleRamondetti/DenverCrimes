package it.polito.tdp.crimes.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {
	private Graph<String,DefaultWeightedEdge> grafo;
	private double pesoMedio;
	private int contatore;
	private int nArchi;
	private int nVerici;
	private List<String> best;
	// non uso mappa dato che ho stringjhe
	private EventsDao dao;
	public Model() {
		dao=new EventsDao();
	}
	
	public int getnArchi() {
		return nArchi;
	}

	public int getnVerici() {
		return nVerici;
	}

	public void creaGrafo(String categoria,int mese) {
		// setto con month di my sql
		this.pesoMedio=0;
		this.contatore=0;
		grafo= new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		// add verici 
		Graphs.addAllVertices(grafo, dao.getVertici(categoria, mese));
		// add archi
		
		for(Adiacenza a: dao.getArchi(categoria, mese)) {
			Graphs.addEdgeWithVertices(grafo, a.getV1(), a.getV2(),a.getPeso());
			contatore++;
			pesoMedio=pesoMedio+a.getPeso();
		}
		pesoMedio=pesoMedio/contatore;
		this.nArchi=this.grafo.edgeSet().size();
		this.nVerici=this.grafo.vertexSet().size();
		// riempio la tendina controllo che non sia vuoto
		System.out.println("grafo creato");
		System.out.println("vartici "+this.grafo.vertexSet().size());
		System.out.println("archi "+this.grafo.edgeSet().size());
	}
	
	public List<Adiacenza> getArchiMaggiorePesoMedio(){
		System.out.println("peso medio "+this.pesoMedio);
		List<Adiacenza> res=new ArrayList<>();
		for(DefaultWeightedEdge a: grafo.edgeSet()) {
			if(this.grafo.getEdgeWeight(a)>this.pesoMedio) {
				res.add(new Adiacenza(this.grafo.getEdgeSource(a),this.grafo.getEdgeTarget(a),(int)(this.grafo.getEdgeWeight(a))));
				
			}
		}
		return res;
	}
	
	public List<String> getpercorsomax(String sorgente,String destinazione){
		best=new LinkedList<>();
		List<String> parziale =new LinkedList<>();
		parziale.add(sorgente);
		cerca(parziale,destinazione);
		return best;
	}
	private void cerca(List<String> parziale, String destinazione) {
		// TODO Auto-generated method stub
		// condizione terminazione
		if(parziale.get(parziale.size()-1).equals(destinazione)) {
			if(parziale.size()>best.size()) {
				best=new ArrayList<>(parziale);
			}
			return;
			
		}
		// scorro i vicini dell'ultimo inserito ed esploro
		 for(String v :Graphs.neighborListOf(grafo, parziale.get(parziale.size()-1))) {
			 if(!parziale.contains(v)) {
				 // evito cicli
				 parziale.add(v);
				 cerca(parziale, destinazione);
				 parziale.remove(parziale.size()-1);
				 }
		 }
		 
		
	}
	public List<String> cercacategorie() {
		return dao.getcategorie();
	}

	public List<Adiacenza>  getArchi() {
		System.out.println("peso medio "+this.pesoMedio);
		List<Adiacenza> res=new ArrayList<>();
		for(DefaultWeightedEdge a: grafo.edgeSet()) {
			res.add(new Adiacenza(this.grafo.getEdgeSource(a),this.grafo.getEdgeTarget(a),(int)(this.grafo.getEdgeWeight(a))));
		}
		return res;
	}
}
