package se.lab;

public class Graph {
  int vertexNum;
  Vertex[] vertexs = new Vertex[100]; // most 100 point
  Edge[][] edges = new Edge[100][100]; // edge[i][j] from i to edges[i][j].to.id

  /**
   * set edges and vertexs status to false.
   */
  public void init() {
    Vertex u = null;
    for (int i = 0; i < vertexNum; i++) {
      u = vertexs[i];
      u.changeStatus(false);
      for (int j = 0; j < u.getDeg(); j++) {
        edges[i][j].changeStatus(false);
      }
    }
  }

  /**
   * existNode.
   * @param word word
   * @return existNode
   */
  public int existNode(String word) {
    word = word.toLowerCase();
    char[] trans = word.toCharArray();
    int len = word.length();
    if (word.endsWith("ing")) {
      len = len - 3;
    } else if (word.endsWith("ied") || word.endsWith("ies")) {
      len = len - 2;
      trans[len - 1] = 'y';
    } else if (word.endsWith("ed")) {
      len = len - 2;
    } else if (word.endsWith("s")) {
      len = len - 1;
    }
    String name = new String(trans, 0, len);
    for (int i = 0; i < vertexNum; i++) {
      if (name.equals(vertexs[i].getName())) {
        return i;
      }
    }
    return -1;
  }

  /**
   * addNode.
   * @param word word
   * @return vertex
   */
  public Vertex addNode(String word) {
    word = word.toLowerCase();
    char[] trans = word.toCharArray();
    int len = word.length();
    if (word.endsWith("ing")) {
      len = len - 3;
    } else if (word.endsWith("ied") || word.endsWith("ies")) {
      len = len - 2;
      trans[len - 1] = 'y';
    } else if (word.endsWith("ed")) {
      len = len - 2;
    } else if (word.endsWith("s")) {
      len = len - 1;
    }
    String name = new String(trans, 0, len);
    int id = existNode(name);
    Vertex res = null;
    if (id == -1) {
      res = new Vertex(name, vertexNum);
      vertexs[vertexNum++] = res;
    } else {
      res = vertexs[id];
    }
    return res;
  }

  /**
   * addEdge.
   * @param word1 endpoint
   * @param word2 endpoint
   */
  public void addEdge(String word1, String word2) {
    Vertex u = addNode(word1);
    Vertex v = addNode(word2);
    boolean flag = true;
    for (int i = 0; i < u.getDeg(); i++) {
      if (edges[u.getId()][i].getTo().getId() == v.getId()) {
        edges[u.getId()][i].addW();
        flag = false;
        break;
      }
    }
    if (flag) {
      edges[u.getId()][u.getDeg()] = new Edge(u, v);
      u.addDeg();
    }
  }

  /**
   * getDot.
   * @return result
   */
  public String getDot() {
    StringBuilder res = new StringBuilder();
    for (int i = 0; i < vertexNum; i++) {
      res.append(vertexs[i].getName() + ";\n");
      for (int j = 0; j < vertexs[i].getDeg(); j++) {
        res.append(edges[i][j].getDot());
      }
    }
    return res.toString();
  }

  /**
   * Dijkstra short way.
   * @param word ??
   * @param dist ??
   * @param bfnode ??
   * @return if the way exist
   */
  public boolean dijkstra(String word, int[] dist, int[] bfnode) {
    int idst = existNode(word);
    if (idst == -1) {
      return false;
    }
    init();
    for (int i = 0; i < vertexNum; i++) {
      dist[i] = 10000; // inf = 10000;
    }
    bfnode[idst] = -1;
    dist[idst] = 0;
    for (int i = 0; i < vertexNum; i++) {
      int mn = 10000; // inf = 10000;
      int loc = 0;
      for (int j = 0; j < vertexNum; j++) {
        if (dist[j] < mn && !vertexs[j].getStatus()) {
          loc = j;
          mn = dist[j];
        }
      }
      vertexs[loc].changeStatus(true);
      for (int j = 0; j < vertexs[loc].getDeg(); j++) {
        Edge e = edges[loc][j];
        int v = e.getTo().getId();
        if (dist[v] > dist[loc] + e.getW()) {
          dist[v] = dist[loc] + e.getW();
          bfnode[v] = loc;
        }
      }
    }
    return true;
  }
}
