package Lab;
//add line2 in C4
import java.util.*;
import java.awt.*;
import java.lang.*;
//add line2 in B1
class Vertex{
	private String name;
	private int id;
	private int deg;
	boolean vis;
	Vertex(String s, int id){
		this.name = s;
		this.id = id;
		this.deg = 0;
		this.vis = false;
	}
	public String getName(){
		return name;
	}
	public int getId(){
		return id;
	}
	public int getDeg(){
		return deg;
	}
	public boolean getStatus(){
		return vis;
	}
	public void addDeg(){
		deg++;
	}
	public void changeStatus(boolean s){
		vis = s;
	}
	@Override
	public int hashCode(){
		return name.hashCode();
	}
	@Override
	public boolean equals(Object o){
		if(o instanceof Vertex){
			Vertex v = (Vertex)o;
			return name.equals(v.name);
		}
		return super.equals(o);
	}
}
class Edge{
	//private int to;
	private Vertex from;
	private Vertex to;
	private int w;
	private boolean vis;
	Edge(Vertex from,Vertex to){
		this.from = from;
		this.to = to;
		w = 1;
		vis = false;
	}
	public String getDot(){
		StringBuilder res = new StringBuilder(from.getName());
		res.append("->");
		res.append(to.getName());
		res.append(" [label = " + w + "];\n");
		return res.toString();
	}
	public void addW(){
		w++;
	}
	public int getW(){
		return w;
	}
	public boolean getStatus(){
		return vis;
	}
	public void changeStatus(boolean s){
		vis = s;
	}
	public Vertex getTo(){
		return to;
	}
}

public class Graph{
	int vertexNum;
	Vertex[] vertexs = new Vertex[100]; // most 100 point
	Edge[][] edges = new Edge[100][100]; //edge[i][j] from i to edges[i][j].to.id
	public void init(){
		Vertex u = null;
		for(int i = 0; i < vertexNum; i++){
			u = vertexs[i];
			u.changeStatus(false);
			for(int j = 0; j < u.getDeg(); j++){
				edges[i][j].changeStatus(false);
			}
		}
	}
	public int existNode(String word){
		word = word.toLowerCase();
		char[] trans = word.toCharArray();
		int len = word.length();
		if(word.endsWith("ing")){
			len = len - 3;
		}else if(word.endsWith("ied") || word.endsWith("ies")){
			len = len - 2;
			trans[len - 1] = 'y';
		}else if(word.endsWith("ed")){
			len = len - 2;	
		}else if(word.endsWith("s")){
			len = len - 1;
		}
		String name = new String(trans, 0, len);
		for(int i = 0; i < vertexNum; i++){
			if(name.equals(vertexs[i].getName()))
				return i;
		}
		return -1;
	}
	
	public Vertex addNode(String word){
		word = word.toLowerCase();
		char[] trans = word.toCharArray();
		int len = word.length();
		if(word.endsWith("ing")){
			len = len - 3;
		}else if(word.endsWith("ied") || word.endsWith("ies")){
			len = len - 2;
			trans[len - 1] = 'y';
		}else if(word.endsWith("ed")){
			len = len - 2;	
		}else if(word.endsWith("s")){
			len = len - 1;
		}
		String name = new String(trans, 0, len);
		int id = existNode(name);
		Vertex res = null;
		if(id == -1){
			res = new Vertex(name, vertexNum);
			vertexs[vertexNum++] = res;
		}else
			res = vertexs[id];
		return res;
	}
	
	public void addEdge(String word1, String word2){
		Vertex u = addNode(word1);
		Vertex v = addNode(word2);
		boolean flag = true;
		for(int i = 0; i < u.getDeg(); i++){
			if(edges[u.getId()][i].getTo().getId() == v.getId()){
				edges[u.getId()][i].addW();
				flag = false;
				break;
			}
		}
		if(flag){
			edges[u.getId()][u.getDeg()] = new Edge(u, v);
			u.addDeg();
		}
	}
	
	public String getDot(){
		StringBuilder res = new StringBuilder();
		for(int i = 0; i < vertexNum; i++){
			res.append(vertexs[i].getName()+";\n");
			for(int j = 0; j < vertexs[i].getDeg(); j++){
				res.append(edges[i][j].getDot());
			}
		}
		return res.toString();
	}
	public boolean dijkstra(String word, int[] dist, int[] bfnode){
		int idst = existNode(word);
		if(idst == -1)
			return false;
		init();
		for(int i = 0; i < vertexNum; i++){
			dist[i] = 10000; //inf = 10000;
		}
		bfnode[idst] = -1;
		dist[idst] = 0;
		for(int i = 0; i < vertexNum; i++){
			int mn = 10000; // inf = 10000;
			int loc = 0;
			for(int j = 0; j < vertexNum; j++){
				if(dist[j] < mn && !vertexs[j].getStatus()){
					loc = j;
					mn = dist[j];
				}
			}
			vertexs[loc].changeStatus(true);
			for(int j = 0; j < vertexs[loc].getDeg(); j++){
				Edge e = edges[loc][j];
				int v = e.getTo().getId();
				if(dist[v] > dist[loc] + e.getW()){
					dist[v] = dist[loc] + e.getW();
					bfnode[v] = loc;
				}
			}
		}
		return true;
	}		
}
//add line1 in B2
