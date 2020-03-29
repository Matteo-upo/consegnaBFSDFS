package test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import codice.DFS;
import it.uniupo.graphLib.DirectedGraph;
import it.uniupo.graphLib.GraphInterface;
import it.uniupo.graphLib.UndirectedGraph;
import it.uniupo.graphLib.UtilitiesForTests;
import it.uniupo.labAlgo2.NotAllNodesReachedException;

class DFSTest {

	@Test
	void testCreate() {
		GraphInterface grafo = new DirectedGraph(3);
		DFS dfsTest = new DFS(grafo);
		assertNotNull(dfsTest);
	}
	
	@Test
	void testScoperti() {
		GraphInterface grafo = new UndirectedGraph("2;0 1");
		DFS dfsTest = new DFS(grafo);
		
		GraphInterface grafo2 = new DirectedGraph("3;0 1;1 2;2 0");
		DFS dfsTest2 = new DFS(grafo2);
		assertDoesNotThrow( () -> dfsTest2.getTree(0));
	}
	
	@Test
	void testNumeroArchi() {
		GraphInterface grafo = new DirectedGraph("2;0 1");
		DFS dfsTest = new DFS(grafo);
		assertEquals(2, grafo.getOrder());
		assertDoesNotThrow( () -> assertEquals(grafo.getOrder() - 1, dfsTest.getTree(0).getEdgeNum()));
		
		GraphInterface grafoO4N = new DirectedGraph("4;0 2;2 3;3 1; 1 0");
		DFS dfsTest4N = new DFS(grafoO4N);
		assertEquals(4, grafoO4N.getOrder());
		assertDoesNotThrow( () -> assertEquals(grafoO4N.getOrder() - 1, dfsTest4N.getTree(0).getEdgeNum()));
		
		GraphInterface grafoNO4N = new UndirectedGraph("4;0 2;2 3;3 1; 1 0");
		DFS dfsTestNO4N = new DFS(grafoNO4N);
		assertEquals(4, grafoNO4N.getOrder());
		assertDoesNotThrow( () -> assertEquals(grafoNO4N.getOrder() - 1, dfsTestNO4N.getTree(3).getEdgeNum()));
	}
	
	@Test
	void testArchiDFS() {
		GraphInterface grafo = new UndirectedGraph("4;0 2;2 3;3 1; 1 0");
		DFS dfsTest = new DFS(grafo);


		assertEquals(4, grafo.getOrder());
		assertDoesNotThrow( () -> assertEquals(grafo.getOrder() - 1, dfsTest.getTree(2).getEdgeNum())); //3 archi
		assertDoesNotThrow( () -> assertEquals(3, dfsTest.getTree(2).getEdgeNum())); //3 archi
		
		assertDoesNotThrow( () -> assertTrue(dfsTest.getTree(2).hasEdge(2, 0) && dfsTest.getTree(2).hasEdge(0, 1) && dfsTest.getTree(2).hasEdge(1,3) || (dfsTest.getTree(2).hasEdge(2, 3) && dfsTest.getTree(2).hasEdge(3, 1) && dfsTest.getTree(2).hasEdge(1,0) ) ));
		
		assertDoesNotThrow( () -> assertTrue(dfsTest.getTree(2).hasEdge(0, 1) && dfsTest.getTree(2).hasEdge(1, 3)));
		assertDoesNotThrow( () -> assertFalse(dfsTest.getTree(2).hasEdge(2, 0) && dfsTest.getTree(2).hasEdge(2, 3)));
		assertDoesNotThrow( () -> assertFalse(dfsTest.getTree(2).hasEdge(2, 0) && dfsTest.getTree(2).hasEdge(2, 3)));
	}
	
	@Test
	void testInitAlbero() {
		GraphInterface grafo = new UndirectedGraph("4;0 2;2 3;3 1; 1 0");
		DFS dfsTest = new DFS(grafo);
		GraphInterface a1;
		try {
			a1 = dfsTest.getTree(2);
		} catch (NotAllNodesReachedException e) {
			a1=null;
			e.printStackTrace();
		}
		GraphInterface a2;
		try {
			a2 = dfsTest.getTree(1);
		} catch (NotAllNodesReachedException e) {
			a2=null;
			e.printStackTrace();
		}
		
		assertEquals(3, grafo.getOrder() - 1);
		assertEquals(grafo.getOrder() - 1, a1.getEdgeNum());
		assertEquals(grafo.getOrder() - 1, a2.getEdgeNum());
		assertFalse(a1.hasEdge(2, 0) && a1.hasEdge(2, 3) && a2.hasEdge(1, 0) && a2.hasEdge(1, 3));
		assertTrue(a2.hasEdge(2, 0) && a2.hasEdge(2, 3) && a1.hasEdge(1, 0) && a1.hasEdge(1, 3));
		
	}

	@Test
	void testIllArgExc(){
		GraphInterface grafo = new UndirectedGraph("4;0 2;2 3;3 1; 1 0");
		DFS dfsTest = new DFS(grafo);
		
		assertThrows(IllegalArgumentException.class, () -> {
				GraphInterface aV = dfsTest.getTree(8);
			}
		);
		
		assertThrows(IllegalArgumentException.class, () -> {
				GraphInterface aV = dfsTest.getTree(-1);
			}
		);
		
		try {
			GraphInterface aV = dfsTest.getTree(0);
		} catch (NotAllNodesReachedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			dfsTest.getTree(-3);
			fail("Avrebbe dovuto lanciare un’eccezione");
		} 
		catch (IllegalArgumentException e) {
		} 
		catch (Throwable e) {
			fail("Avrebbe dovuto lanciare una IllegalArgumentException e non una " + e);
		}
		
	}
	
	@Test
	void testNoAlNodReachedExc() {
		GraphInterface grafo = new UndirectedGraph("4;0 2;2 3");
		DFS dfsTest = new DFS(grafo);
		assertThrows(NotAllNodesReachedException.class, () -> {
				GraphInterface aV = dfsTest.getTree(2);
			}
		);
		
		GraphInterface grafo2 = new DirectedGraph("4;0 2;2 3;1 3;1 0");
		DFS dfsTest2 = new DFS(grafo2);
		assertThrows(NotAllNodesReachedException.class, () -> {
				GraphInterface aV = dfsTest2.getTree(2);
			}
		);
		
		GraphInterface grafoO4N = new DirectedGraph("4;0 2;2 3;3 1; 1 0");
		DFS dfsTest4N = new DFS(grafoO4N);
		assertDoesNotThrow( () -> {
				GraphInterface aV = dfsTest4N.getTree(2);
			}
		);
		
	}
	
	@Test
	void testNoAlNodReachedExc2() {
		GraphInterface grafo = new UndirectedGraph("3;0 1;1 2;2 0");
		DFS dfsTest = new DFS(grafo);
		
				try {
					GraphInterface aV = dfsTest.getTree(2);
				} catch (NotAllNodesReachedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
		
	}
	
	@Test
	void testNodeOrdPostVisit() {
		GraphInterface grafo = new DirectedGraph("11;4 1;1 8;8 4;1 0;0 8;0 6;5 0;2 5;0 2;8 2;2 7;7 8;2 3;3 9;9 10");
		DFS dfsTest = new DFS(grafo);
		ArrayList<Integer> ord;
		GraphInterface tree;
		
		try {
			tree = dfsTest.getTree(1);
		} catch (NotAllNodesReachedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
			try {
				ord = dfsTest.getNodesInOrderPostVisit(1);
			} catch (NotAllNodesReachedException e) {
				ord = null;
				e.printStackTrace();
			}
		
		
		assertEquals(grafo.getOrder(), ord.size());
		
		GraphInterface grafoO4N = new DirectedGraph("4;0 2;2 3;3 1; 1 0");
		DFS dfsTest4N = new DFS(grafoO4N);
		
		try {
			assertEquals(2, dfsTest4N.getNodesInOrderPostVisit(2).get(3));
			assertTrue(dfsTest4N.getNodesInOrderPostVisit(2).get(0) == 0 || dfsTest4N.getNodesInOrderPostVisit(2).get(0) == 3);
			assertTrue(dfsTest4N.getNodesInOrderPostVisit(2).get(0) != 2 || dfsTest4N.getNodesInOrderPostVisit(2).get(0) == 1);
			assertEquals(1, dfsTest4N.getNodesInOrderPostVisit(2).get(1));
			assertEquals(grafoO4N.getOrder(), dfsTest4N.getNodesInOrderPostVisit(2).size());
			
		} catch (NotAllNodesReachedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	@Test
	void testgetForest() {
		GraphInterface grafo = new UndirectedGraph("6;1 2;2 3");
		DFS dfsTest = new DFS(grafo);
		GraphInterface forest;
		
		try {
			forest = dfsTest.getForest();
		} catch (NotAllNodesReachedException e) {
			forest = null;
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertEquals(grafo.getOrder(), forest.getOrder());
		assertTrue(!forest.hasEdge(0, 1));
		assertTrue(!forest.hasEdge(0, 2));
		assertTrue(!forest.hasEdge(0, 3));
		assertTrue(!forest.hasEdge(0, 4));
		assertTrue(!forest.hasEdge(0, 5));
		
		assertTrue(forest.hasEdge(1, 2));
		assertTrue(!forest.hasEdge(1, 3));
		assertTrue(!forest.hasEdge(1, 4));
		assertTrue(!forest.hasEdge(1, 5));
		
		
		assertTrue(forest.hasEdge(2, 3));
		assertTrue(!forest.hasEdge(2, 4));
		assertTrue(!forest.hasEdge(2, 5));
		
		assertTrue(!forest.hasEdge(3, 4));
		assertTrue(!forest.hasEdge(3, 5));
		
		assertTrue(!forest.hasEdge(4, 5));	
		
		
		assertTrue(!forest.hasEdge(0, 0));
		assertTrue(!forest.hasEdge(1, 1));
		assertTrue(!forest.hasEdge(2, 2));
		assertTrue(!forest.hasEdge(3, 3));
		
		
		
		
		GraphInterface grafo2 = new DirectedGraph("4;3 2;3 0;0 1;2 1");
		DFS dfsTest2 = new DFS(grafo2);
		GraphInterface forest2;
		
		try {
			forest2 = dfsTest2.getForest();
		} catch (NotAllNodesReachedException e) {
			forest2 = null;
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertEquals(grafo2.getOrder(), forest2.getOrder());
		assertTrue(forest2.hasEdge(0, 1));
		assertTrue(!forest2.hasEdge(0, 2));
		assertTrue(!forest2.hasEdge(0, 3));
		
		assertTrue(!forest2.hasEdge(1, 0));
		assertTrue(!forest2.hasEdge(1, 2));
		assertTrue(!forest2.hasEdge(1, 3));
		
		assertTrue(!forest2.hasEdge(2, 0));
		assertTrue(!forest2.hasEdge(2, 1));
		assertTrue(!forest2.hasEdge(2, 3));
		
		assertTrue(!forest2.hasEdge(3, 0));
		assertTrue(!forest2.hasEdge(3, 1));
		assertTrue(!forest2.hasEdge(3, 2));
		
		assertTrue(!forest2.hasEdge(0, 0));
		assertTrue(!forest2.hasEdge(1, 1));
		assertTrue(!forest2.hasEdge(2, 2));
		assertTrue(!forest2.hasEdge(3, 3));
		
		try {
			assertEquals(4, dfsTest2.getTree(3).getOrder());
		} catch (NotAllNodesReachedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		GraphInterface grafo3 = new DirectedGraph("4;2 3;3 0;0 1;1 2");
		DFS dfsTest3 = new DFS(grafo3);
		GraphInterface forest3;
		try {
			forest3 = dfsTest3.getForest();
		} catch (NotAllNodesReachedException e) {
			forest3 = null;
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertTrue(forest3.getOrder() == grafo3.getOrder());
		
		try {
			assertTrue(dfsTest3.getTree(0).getOrder() == grafo3.getOrder());
		} catch (NotAllNodesReachedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			assertTrue(dfsTest3.getTree(2).getOrder() == grafo3.getOrder());
		} catch (NotAllNodesReachedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	@Test
	void testHasCycle() {
		GraphInterface grafo = new DirectedGraph("4;0 2;2 3;3 1; 1 0");
		DFS dfsTest = new DFS(grafo);
		
		assertTrue(dfsTest.hasDirCycle());
		
		GraphInterface grafo2 = new DirectedGraph("4;0 2;2 3;3 1; 0 1");
		DFS dfsTest2 = new DFS(grafo2);
		
		assertTrue(!dfsTest2.hasDirCycle());
		
		GraphInterface grafo1N = new DirectedGraph("1");
		DFS dfsTest1N = new DFS(grafo1N);
		
		assertTrue(!dfsTest1N.hasDirCycle());		
		
		GraphInterface grafo2N = new DirectedGraph("2;0 1");
		DFS dfsTest2N = new DFS(grafo2N);
		
		assertTrue(!dfsTest2N.hasDirCycle());
		
		GraphInterface grafo3N = new DirectedGraph("3;1 0;1 2;0 2");
		DFS dfsTest3N = new DFS(grafo3N);
		
		assertTrue(!dfsTest3N.hasDirCycle());
		
		GraphInterface grafo3NC = new DirectedGraph("3;0 2;2 1;1 0");
		DFS dfsTest3NC = new DFS(grafo3NC);
		
		assertTrue(dfsTest3NC.hasDirCycle());
		
		GraphInterface grafo5NC = new DirectedGraph("5;4 0;4 1;4 2;3 4;2 3");
		DFS dfsTest5NC = new DFS(grafo5NC);
		
		assertTrue(dfsTest5NC.hasDirCycle());
		
		
	}
	
	@Test
	void testBackwardCycle() {
		GraphInterface grafo = new DirectedGraph("4;0 2;2 3;3 1; 1 0");
		DFS dfsTest = new DFS(grafo);
		
		assertEquals(4+1, dfsTest.getDirCycle().size());
		
		GraphInterface grafo2 = new DirectedGraph("4;0 2;2 3;3 1; 0 1");
		DFS dfsTest2 = new DFS(grafo2);
		
		assertNull(dfsTest2.getDirCycle());
		
		GraphInterface grafo1N = new DirectedGraph("1");
		DFS dfsTest1N = new DFS(grafo1N);
		
		assertNull(dfsTest1N.getDirCycle());		
		
		GraphInterface grafo2N = new DirectedGraph("2;0 1");
		DFS dfsTest2N = new DFS(grafo2N);
		
		assertNull(dfsTest2N.getDirCycle());
		
		GraphInterface grafo3N = new DirectedGraph("3;1 0;1 2;0 2");
		DFS dfsTest3N = new DFS(grafo3N);
		
		assertNull(dfsTest3N.getDirCycle());
		
		GraphInterface grafo3NC = new DirectedGraph("3;0 2;2 1;1 0");
		DFS dfsTest3NC = new DFS(grafo3NC);
		
		assertTrue(dfsTest3NC.getDirCycle().size() == 3+1);
		
		GraphInterface grafo5NC = new DirectedGraph("5;4 0;4 1;4 2;3 4;2 3");
		DFS dfsTest5NC = new DFS(grafo5NC);
		
		assertTrue(dfsTest5NC.getDirCycle().size() == 3+1);
		
		GraphInterface grafo5NCNC = new DirectedGraph("6;0 1;1 2;5 4;4 3;3 5");//5 nodi con ciclo non connesso
		DFS dfsTest5NCNC = new DFS(grafo5NCNC);
		
		assertTrue(dfsTest5NCNC.getDirCycle().size() == 3+1);
		
		
	}
	
	
	@Test
	void testIsConnected() {
		GraphInterface grafo = new UndirectedGraph("4;0 2;2 3;3 1; 1 0");
		DFS dfsTest = new DFS(grafo);
		
		assertTrue(dfsTest.isConnected());
		
		GraphInterface grafo2 = new DirectedGraph("4;0 2;2 3;3 1; 0 1");
		DFS dfsTest2 = new DFS(grafo2);
		
		assertTrue(!dfsTest2.isConnected());
		
		GraphInterface grafo3 = new UndirectedGraph("1");
		DFS dfsTest3 = new DFS(grafo3);
		
		assertTrue(dfsTest3.isConnected());
		
		GraphInterface grafoNC = new UndirectedGraph("2");
		DFS dfsTestNC = new DFS(grafoNC);
		
		assertTrue(!dfsTestNC.isConnected());
		
		GraphInterface grafoC = new UndirectedGraph("2;1 0");
		DFS dfsTestC = new DFS(grafoC);
		
		assertTrue(dfsTestC.isConnected());
		
	}
	
	
	@Test
	void testConnectedComponents() {
		
		GraphInterface grafo = new DirectedGraph("1");
		DFS dfsTest = new DFS(grafo);
		assertEquals(0, dfsTest.connectedComponents()[0]);
		
		
		GraphInterface grafoNC = new UndirectedGraph("2");
		DFS dfsTestNC = new DFS(grafoNC);
		
		assertEquals(0,dfsTestNC.connectedComponents()[0]);
		assertEquals(1,dfsTestNC.connectedComponents()[1]);
		
		GraphInterface grafoC = new UndirectedGraph("2;1 0");
		DFS dfsTestC = new DFS(grafoC);
		
		assertEquals(0, dfsTestC.connectedComponents()[0]);
		assertEquals(0, dfsTestC.connectedComponents()[1]);
		
		
		GraphInterface grafo5NCNC = new UndirectedGraph("6;0 1;1 2;5 4;4 3;3 5");//6 nodi con ciclo non connesso
		DFS dfsTest5NCNC = new DFS(grafo5NCNC);
		
		assertEquals(0, dfsTest5NCNC.connectedComponents()[0]);
		assertEquals(0, dfsTest5NCNC.connectedComponents()[1]);
		assertEquals(0, dfsTest5NCNC.connectedComponents()[2]);
		assertEquals(1, dfsTest5NCNC.connectedComponents()[3]);
		assertEquals(1, dfsTest5NCNC.connectedComponents()[4]);
		assertEquals(1, dfsTest5NCNC.connectedComponents()[5]);
		
	}
	
	
	
	
	
	

}












































