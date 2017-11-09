package se.test;
import static org.junit.Assert.*;

import java.io.FileNotFoundException;

import org.junit.Test;

import se.lab.Graph;
import se.lab.Main;
public class MainTest {
	@Test
	public void testQueryBridgeWords() throws FileNotFoundException {
		Graph graph = new Main().createDirectedGraph("F:\\test.txt") ;	
		String t6 = "No \"wwww\" in the graph!";
		assertEquals(t6,new Main().queryBridgeWords(graph,"to","wwww"));
	}

}
