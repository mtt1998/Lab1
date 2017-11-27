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

public class ShowPathControl {

  public void showPathInGraph(Graph graph, String paths) {
    String[] colors = {"blue", "red", "yellow", "gold", "green", "pink", "aliceblue", "violet",
        "pink", "darkgrey", "brown", "darkorange", "antiquewhite", "aqua", "aquamarine", "azure",
        "beige", "bisque", "black", "blanchedalmond", "blueviolet", "burlywood", "cadetblue",
        "chartreuse", "chocolate", "coral", "cornflowerblue", "cornsilk", "crimson", "cyan ",
        "darkcyan", "darkgoldenrod", "darkgray", "darkgreen", "darkkhaki", "darkmagenta",
        "darkolivegreen", "darkorange", "darkorchid", "darkred", "darksalmon", "darkseagreen",
        "darkslateblue", "darkslategray", "darkslategrey", "darkturquoise", "darkviolet"};

    String[] path = paths.split("\n");
    String u = null;
    String v = null;
    int colorindex = 0;
    GraphViz gv = new GraphViz();
    gv.addln(gv.startGraph());
    gv.add(graph.getDot());
    for (String p : path) {
      String[] nodes = p.split(" ");
      if (p.length() == 0) {
        continue;
      }
      u = nodes[0];
      for (int i = 1; i < nodes.length - 1; i++) {
        v = nodes[i];
        gv.add(u + "->" + v + "[color =" + colors[colorindex] + "];\n");
        u = v;
      }
      colorindex++;
      if (colorindex == colors.length) {
        colorindex = 0;
      }
    }
    gv.add(gv.endGraph());
    gv.increaseDpi();
    JFrame myframe = new JFrame("Shortest Paths");
    JLabel mylabel = new JLabel();
    myframe.setVisible(true);
    mylabel.setIcon(new ImageIcon(gv.getGraph(gv.getDotSource(), "jpg", "dot")));
    myframe.add(mylabel);
    myframe.pack();
  }
}
