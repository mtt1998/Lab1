package se.lab;

public class Edge {
  private Vertex from;
  private Vertex to;
  private int www;
  private boolean vis;

  Edge(Vertex from, Vertex to) {
    this.from = from;
    this.to = to;
    www = 1;
    vis = false;
  }

  /**
   * generate Dot file lines.
   * @return result
   */
  public String getDot() {
    StringBuilder res = new StringBuilder(from.getName());
    res.append("->");
    res.append(to.getName());
    res.append(" [label = " + www + "];\n");
    return res.toString();
  }

  public void addW() {
    www++;
  }

  public int getW() {
    return www;
  }

  public boolean getStatus() {
    return vis;
  }

  public void changeStatus(boolean s) {
    vis = s;
  }

  public Vertex getTo() {
    return to;
  }
}