package codice;

import java.util.ArrayList;
import java.util.Arrays;

import it.uniupo.graphLib.DirectedGraph;
import it.uniupo.graphLib.Edge;
import it.uniupo.graphLib.GraphInterface;
import it.uniupo.labAlgo2.NotAllNodesReachedException;

public class DFS {
	
	private boolean[] visitati;
	private GraphInterface currentG;
	private GraphInterface alberoVisita;
	private ArrayList<Integer> risultato;
	private boolean cycle;
	private int cycleStart;
	private int cycleEnd;
	private int padri[];
	private int nodesReached;
	private int[] connCpts;
	private int nCC;

	public DFS(GraphInterface g) {
		visitati = new boolean[g.getOrder()];
		currentG = g;
		alberoVisita = currentG.create();
		risultato = new ArrayList<Integer>();
		cycle = false;
		cycleStart = cycleEnd = -1;
		padri = new int[g.getOrder()];
		Arrays.fill(padri, -1);
		nodesReached = 1;
		connCpts = new int[g.getOrder()];
		Arrays.fill(connCpts, -1);
		
		nCC = 0;
	}
	
	private void visita(int sorgente){		
		visitati[sorgente] = true;
		for(Integer neighbor:  currentG.getNeighbors(sorgente) ){
			if(visitati[neighbor] == false) {
				alberoVisita.addEdge(sorgente, neighbor);
				padri[neighbor] = sorgente;
				visita(neighbor);
				nodesReached++;
				connCpts[neighbor] = nCC;
			}
			else if(!risultato.contains(neighbor)){
				if(cycle == false) {
					cycleStart = neighbor;
					cycleEnd = sorgente;
				}
				cycle = true;
			}
		}
		
		risultato.add(sorgente);
	}
	
	private void visitaExc(int sorgente) throws NotAllNodesReachedException{
		checkIllArgExc(sorgente);		
		
		setUp();
		
		visita(sorgente);
		
		checkAllNodeExc();		
	}
	
	private void checkIllArgExc(int sorgente) {
		if(sorgente < 0 || sorgente > currentG.getOrder() - 1)
			throw new /*java.lang.*/IllegalArgumentException();		
	}
	
	private void setUp() {
		Arrays.fill(visitati, false);
		alberoVisita = currentG.create(); // new DirectedGraph("" + currentG.getOrder()); //se non c'e' altro modo di eliminare gli archi
		risultato.clear();
		cycle = false;
		cycleStart = cycleEnd = -1;
		Arrays.fill(padri, -1);
		nodesReached = 1;
		Arrays.fill(connCpts, -1);
		nCC = 0;
	}
	
	private void checkAllNodeExc() throws NotAllNodesReachedException{
		boolean tuttiRaggiunti = true;
		int i = 0;
		while(tuttiRaggiunti && i < visitati.length ) {
			tuttiRaggiunti = tuttiRaggiunti && visitati[i++];
		}
		
		if(!tuttiRaggiunti) 
			throw new NotAllNodesReachedException("La visita DFS non ha raggiunto tutti i nodi");
	}
	
	private void visitaCompleta() throws NotAllNodesReachedException {
		setUp();
		//connCpts[0] = 0;
		for(int i = 0; i < currentG.getOrder(); i++) {
			if(!visitati[i]) {
				visita(i);
				connCpts[i] = nCC;
				nCC++;
				
			}
			
		}
		
		
		checkAllNodeExc();	
	}
	
	
	public GraphInterface getTree(int sorgente) throws NotAllNodesReachedException{
		visitaExc(sorgente);		
		return alberoVisita;
	}
	
	public ArrayList<Integer> getNodesInOrderPostVisit(int sorgente) throws NotAllNodesReachedException{
		visitaExc(sorgente);
		
		System.out.println();
		for(Integer el: risultato)
			System.out.println(el);
		System.out.println();
		
		return risultato;
	}
	
	public GraphInterface getForest() throws NotAllNodesReachedException{
		visitaCompleta();
		
//		for(Integer el: risultato)
//			System.out.println(el);
		
		return alberoVisita;
	}
	
	public boolean hasDirCycle() {
		try {
			visitaCompleta();
		} catch (NotAllNodesReachedException e) {
			e.printStackTrace();
		}
		
		return cycle;
	}
	
	public ArrayList<Integer> getDirCycle(){
		try {
			visitaCompleta();
		} catch (NotAllNodesReachedException e) {
			e.printStackTrace();
		}
		
		ArrayList<Integer> ciclo = new ArrayList<Integer>();
		if(cycle) {
			int tmp = cycleEnd;
			ciclo.add(0, cycleStart);
			ciclo.add(0, tmp);
			while(tmp != -1 && padri[tmp] != cycleStart) {
				ciclo.add(0, padri[tmp]);
				tmp = padri[tmp];
			}
			ciclo.add(0, cycleStart);
		}
		
		for(Integer el: padri)
			System.out.println(el);
		
		System.out.println("Cicli");
		
		for(Integer el: ciclo)
			System.out.println(el);
		
		System.out.println("fine cicli");
		
		return !ciclo.isEmpty() ? ciclo : null;
	}
	
	public boolean isConnected() {
		for(int i = 0; i < currentG.getOrder(); i++) {
			setUp();		
			visita(i);
//			if(alberoVisita.getEdgeNum() != currentG.getEdgeNum() - 1)
			if(nodesReached != currentG.getOrder())
				return false;
		}
		return true;
	}
	
	public int[] connectedComponents() {
		try {
			visitaCompleta();
		} catch (NotAllNodesReachedException e) {
			e.printStackTrace();
		}
		
		
		return connCpts;
	}
	

}
