package se.test;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;

import org.junit.Test;
import se.lab.Graph;
import se.lab.CreateGraphControl;
import se.lab.QueryWordControl;

public class MainTest02 {
	@Test
	public void testQueryBridgeWords() throws FileNotFoundException {
		Graph graph = new CreateGraphControl().createDirectedGraph("F:\\test.txt") ;
		String t2 ="No bridges words from \"explore\" to \"you\"!";
		assertEquals(t2, new QueryWordControl().queryBridgeWords(graph,"explore","you"));
	}
}
