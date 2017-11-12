package se.test;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;

import org.junit.Test;

import se.lab.Graph;
import se.lab.Main;

public class MainTest2 {

	@Test
	public void testGenerateNewText() throws FileNotFoundException {
		Graph graph = new Main().createDirectedGraph("F:\\test.txt") ;	
		String t6 = "explore strange new";
		String t = "explore new";
		assertEquals(t6,new Main().generateNewText(graph,t));
	}

}
