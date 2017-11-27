package se.test;
import static org.junit.Assert.*;

import java.io.FileNotFoundException;

import org.junit.Test;

import se.lab.Graph;
import se.lab.CreateGraphControl;
import se.lab.QueryWordControl;

public class MainTest08 {
	@Test
	public void testQueryBridgeWords() throws FileNotFoundException {
		Graph graph = new CreateGraphControl().createDirectedGraph("F:\\test.txt") ;	
		String t6 = "No \"www\" in the graph!";
		assertEquals(t6, new QueryWordControl().queryBridgeWords(graph,"www","to"));
	}

}
