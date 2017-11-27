package se.lab;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Logger;

public class QueryWordControl{

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
      String res = graph.queryBridgeWord(idu, idv);
      if (res.length() == 0) {
        return "No bridges words from \"" + word1 + "\" to \"" + word2 + "\"!";
      }
      return "The bridges words from \"" + word1 + "\" to \"" + word2 + "\" are:" + res.toString();
    }
  }

}