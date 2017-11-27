package se.lab;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Logger;

import se.lab.QueryWordControl;

public class GenerateTextControl {
	
  public String generateNewText(Graph graph, String inputText) {
    String[] arr = inputText.split(" ");
    StringBuilder res = new StringBuilder("");
    if (arr != null) {
      String word1 = arr[0];
      String bridge = null;
      res.append(word1);
      for (int i = 1; i < arr.length; i++) {
        res.append(" ");
        int idu = graph.existNode(word1);
        int idv = graph.existNode(arr[i]);
        if(idu != -1 && idv != -1) {
        	bridge = graph.queryBridgeWord(idu, idv);
        }
        if (bridge.length() == 0) {
          String[] bridgewords = bridge.split(" "); // ???
          int j = new Random().nextInt(bridgewords.length);
          res.append(bridgewords[j] + " ");
        }
        res.append(arr[i]);
        word1 = arr[i];
      }
    }
    return res.toString();
  }

}
