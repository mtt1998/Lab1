package se.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.junit.Test;

import se.lab.Graph;
import se.lab.Main;
public class junittest {
	
	Graph createDirectedGraph(String filename) throws FileNotFoundException {
	    //filename = "C:\\Users\\pww\\Desktop\\noyte.txt";
	    Graph graph = null;
	    if(filename!=null) {
	      String temp = new Scanner(new File(filename)).useDelimiter("\\Z").next().toLowerCase();
	      String[] words = temp.replaceAll("[^a-zA-Z]+", " ").replaceFirst("^ ", "").split(" ");
	      graph = new Graph(words.length + 2);
	      for (int i = 0; i < words.length - 1; i++) {
	            graph.addEdge(words[i], words[i + 1]);
	      }
	    }
	    return graph;
	  }
	@Test
	public void testQueryBridgeWords() throws FileNotFoundException {
		Graph graph = createDirectedGraph("F:\test.txt") ;
		fail("Not yet implemented");
		//assertEqual("12",se.test.queryBridgeWords(graph,"and","to"));
	}

	
	
	
}
