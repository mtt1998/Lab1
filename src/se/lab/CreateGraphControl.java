package se.lab;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;
import se.lab.Graph;

public class CreateGraphControl {

  public Graph createDirectedGraph(String filename) throws FileNotFoundException {
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
}