package se.lab;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Logger;


public class CalPathControl {
  public String calcShortestPath(Graph graph, String word1, String word2) {
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

  public String calcAllShortestPath(Graph graph, String word) {
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
}