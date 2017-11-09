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

public class Main {
  private JFrame myframe = new JFrame("Graph");
  private JLabel mylabel = new JLabel();
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

  // explain the graph and save as out.jpg
  void showDirectedGraph(Graph graph) {
    GraphViz gv = new GraphViz();
    gv.addln(gv.startGraph());
    gv.add(graph.getDot());
    gv.add(gv.endGraph());
    gv.increaseDpi();
    String type = "jpg";
    String repesentationType = "dot";
    File out = new File("e:/out." + type); // Windows
    gv.writeGraphToFile(gv.getGraph(gv.getDotSource(), type, repesentationType), out);
    myframe.setVisible(true);
    mylabel.setIcon(new ImageIcon(gv.getGraph(gv.getDotSource(), type, repesentationType)));
    myframe.add(mylabel);
    myframe.pack();
  }

  public String queryBridgeWords(Graph graph, String word1, String word2) {
    int idu = graph.existNode(word1);
    int idv = graph.existNode(word2);
    if (idu == -1 && idv == -1) {
      return "No \"" + word1 + "\" and \"" + word2 + "\" in the graph!";
    } else if (idu == -1) {
      return "No \"" + word1 + "\" in the graph!";
    } else if (idv == -1) {
      return "No \"" + word2 + "\" in the graph!";
    } else {
      StringBuilder res = new StringBuilder("");
      for (int i = 0; i < graph.vertexs[idu].getDeg(); i++) {
        Vertex bridge = graph.edges[idu][i].getTo();
        for (int j = 0; j < bridge.getDeg(); j++) {
          if (graph.edges[bridge.getId()][j].getTo().getId() == idv) {
            res.append(bridge.getName() + " ");
          }
        }
      }
      if (res.length() == 0) {
        return "No bridges words from \"" + word1 + "\" to \"" + word2 + "\"!";
      }
      return "The bridges words from \"" + word1 + "\" to \"" + word2 + "\" are:" + res.toString();
    }
  }

  String generateNewText(Graph graph, String inputText) {
    String[] arr = inputText.split(" ");
    StringBuilder res = new StringBuilder("");
    if (arr != null) {
      String word1 = arr[0];
      String bridge = null;
      res.append(word1);
      for (int i = 1; i < arr.length; i++) {
        res.append(" ");
        bridge = queryBridgeWords(graph, word1, arr[i]);
        if (bridge.charAt(0) == 'T') {
          String[] bridgewords = bridge.substring(bridge.lastIndexOf(':') + 1).split(" "); // ???
          int j = new Random().nextInt(bridgewords.length);
          res.append(bridgewords[j] + " ");
        }
        res.append(arr[i]);
        word1 = arr[i];
      }
    }
    return res.toString();
  }

  void showPathInGraph(Graph graph, String paths) {
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

  String calcShortestPath(Graph graph, String word1, String word2) {
    int idst = graph.existNode(word1);
    int idend = graph.existNode(word2);
    if (idend == -1 || idst == -1) {
      return "";
    }
    int[] dist = new int[graph.vertexNum]; // vertexNum < graph.vertexNum
    int[] bfnode = new int[graph.vertexNum]; // vertexNum < graph.vertexNum
    graph.dijkstra(word1, dist, bfnode);
    int[] stk = new int[graph.vertexNum]; // vertexNum < graph.vertexNum;
    int top = -1;
    int now = idend;
    while (now != -1) {
      stk[++top] = now;
      now = bfnode[now];
    }
    StringBuilder res = new StringBuilder();
    while (top != -1) {
      res.append(graph.vertexs[stk[top--]].getName());
      res.append(" ");
    }
    res.append(dist[idend]);
    res.append("\n");
    return res.toString();
  }

  String showAllShortestPath(Graph graph, String word) {
    int[] dist = new int[graph.vertexNum]; // vertexNum < graph.vertexNum
    int[] bfnode = new int[graph.vertexNum]; // vertexNum < graph.vertexNum
    graph.dijkstra(word, dist, bfnode);
    int[] stk = new int[graph.vertexNum]; // vertexNum < graph.vertexNum;
    int top = -1;
    StringBuilder res = new StringBuilder();
    for (int idend = 0; idend < graph.vertexNum; idend++) {
      top = -1;
      int now = idend;
      while (now != -1) {
        stk[++top] = now;
        now = bfnode[now];
      }
      while (top != -1) {
        res.append(graph.vertexs[stk[top--]].getName());
        res.append(" ");
      }
      res.append(dist[idend]);
      res.append("\n");
    }
    return res.toString();
  }

  String randomWalk(Graph graph) {
    graph.init();
    Random random = new Random();
    Vertex u = graph.vertexs[random.nextInt(graph.vertexNum)];
    StringBuilder res = new StringBuilder(u.getName());
    Edge e = null;
    while (true) {
      if (u.getDeg() == 0) {
        break;
      }
      e = graph.edges[u.getId()][random.nextInt(u.getDeg())];
      u = e.getTo();
      res.append(" ");
      res.append(u.getName());
      if (e.getStatus()) {
        break;
      }
      e.changeStatus(true);
    }
    return res.toString();
  }
}
