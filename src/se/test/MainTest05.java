package se.test;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;

import org.junit.Test;

import se.lab.Graph;
import se.lab.CreateGraphControl;
import se.lab.QueryWordControl;

public class MainTest05 {
	@Test
	public void testQueryBridgeWords() throws FileNotFoundException {
		Graph graph = new CreateGraphControl().createDirectedGraph("F:\\test.txt") ;
		String t4 ="The bridges words from \"to\" to \"out\" are:seek get ";
		assertEquals(t4, new QueryWordControl().queryBridgeWords(graph,"to","out"));
	}
}
