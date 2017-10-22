package se.lab;

public class Vertex {
  private String name;
  private int id;
  private int deg;
  boolean vis;

  Vertex(String s, int id) {
    this.name = s;
    this.id = id;
    this.deg = 0;
    this.vis = false;
  }

  public String getName() {
    return name;
  }

  public int getId() {
    return id;
  }

  public int getDeg() {
    return deg;
  }

  public boolean getStatus() {
    return vis;
  }

  public void addDeg() {
    deg++;
  }

  public void changeStatus(boolean s) {
    vis = s;
  }

  @Override
  public int hashCode() {
    return name.hashCode();
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof Vertex) {
      Vertex v = (Vertex) o;
      return name.equals(v.name);
    }
    return super.equals(o);
  }
}
