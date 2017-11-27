package se.test;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;

import org.junit.Test;

import se.lab.Graph;
import se.lab.CreateGraphControl;
import se.lab.GenerateTextControl;

public class MainTest4 {
	
	@Test 
	public void testGenerateNewText() throws FileNotFoundException {
		Graph graph = new CreateGraphControl().createDirectedGraph("F:\\test.txt") ;	
		String t6 = "";
		String t = "";
		assertEquals(t6,new GenerateTextControl().generateNewText(graph,t));
	}
}
