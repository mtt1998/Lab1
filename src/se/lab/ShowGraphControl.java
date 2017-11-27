package se.lab;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import se.graph.GraphViz;

public class ShowGraphControl {
  // explain the graph and save as out.jpg
  public void showDirectedGraph(Graph graph) {
    GraphViz gv = new GraphViz();
    gv.addln(gv.startGraph());
    gv.add(graph.getDot());
    gv.add(gv.endGraph());
    gv.increaseDpi();
    String type = "jpg";
    String repesentationType = "dot";
    File out = new File("e:/out." + type); // Windows
    gv.writeGraphToFile(gv.getGraph(gv.getDotSource(), type, repesentationType), out);
    JFrame myframe = new JFrame("Graph");
    JLabel mylabel = new JLabel();
    myframe.setVisible(true);
    mylabel.setIcon(new ImageIcon(gv.getGraph(gv.getDotSource(), type, repesentationType)));
    myframe.add(mylabel);
    myframe.pack();
  }

}