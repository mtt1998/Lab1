package Lab;
//b2t3
import java.io.*;
import java.util.*;
//import java.awt.*;
import javax.swing.*;
import Lab.Graph;
import graph.GraphViz;

public class Main {
	Graph createDirectedGraph(String filename){
		FileReader freader = null;
		int ch = -1;
		Graph G = null;
		try{
			freader = new FileReader(filename);
			G = new Graph();
			
			String u = null;
			StringBuffer chbuf = new StringBuffer(50);
			while((ch = freader.read()) != -1){
				if(ch == 32 || ch == 46 || ch == 33 || ch == 44 || ch == 63 || ch == 10 || ch == 13){
					if(chbuf.length() != 0){
						if(u == null)
							G.addNode(chbuf.toString());
						else
							G.addEdge(u, chbuf.toString());
						u = chbuf.toString();
						chbuf.setLength(0);
					}
				}else if((ch >= 65 && ch <= 90) || (ch >= 97 && ch <= 122)){
					chbuf.append((char)ch);
				}
			}
			if(chbuf.length() != 0){
				if(u != null){
					G.addEdge(u, chbuf.toString());
				}else
					G.addNode(chbuf.toString());
			}
			
			freader.close();
		}catch(Exception e){
			System.out.println("cannot read the file normally");
			e.printStackTrace();
		}
		return G;
	}
	
	//explain the graph and save as out.jpg
	void showDirectedGraph(Graph G){
		GraphViz gv = new GraphViz();
		gv.addln(gv.start_graph());	
		gv.add(G.getDot());
		gv.add(gv.end_graph());
		gv.increaseDpi();
		String type = "jpg";		
		String repesentationType= "dot";	
		File out = new File("e:/out." + type);    // Windows
		gv.writeGraphToFile(gv.getGraph(gv.getDotSource(), type, repesentationType), out);
		
		JFrame myframe = new JFrame("Graph");
		JLabel mylabel = new JLabel();
		myframe.setVisible(true);
		mylabel.setIcon(new ImageIcon(gv.getGraph(gv.getDotSource(), type, repesentationType)));
		myframe.add(mylabel);
		myframe.pack();
		
	}
	
	String queryBridgeWords(Graph G, String word1, String word2){
		int idu = G.existNode(word1);
		int idv = G.existNode(word2);
		if(idu == -1  && idv == -1){
			return "No \"" + word1 + "\" and \"" + word2 + "\" in the graph!";
		}else if(idu == -1){
			return "No \"" + word1 + "\" in the graph!";
		}else if(idv == -1){
			return "No \"" + word2 + "\" in the graph!";
		}else{
			StringBuilder res = new StringBuilder("");
			for(int i = 0; i < G.vertexs[idu].getDeg(); i++){
				Vertex bridge = G.edges[idu][i].getTo();
				for(int j = 0; j < bridge.getDeg(); j++){
					if(G.edges[bridge.getId()][j].getTo().getId() == idv){
						res.append(bridge.getName() + " ");
					}
				}
			}
			if(res.length() == 0){
				return "No bridges words from \"" + word1 + "\" to \"" + word2 + "\"!";
			}
			return "The bridges words from \"" + word1 + "\" to \"" + word2 + "\" are:" + res.toString();
		}
	}
	String generateNewText(Graph G, String inputText){
		String[] arr = inputText.split(" ");
		StringBuilder res = new StringBuilder("");
		if(arr != null){
			String word1 = arr[0];
			String bridge = null;
			res.append(word1);
			for(int i = 1; i < arr.length; i++){
				res.append(" ");
				bridge = queryBridgeWords(G, word1, arr[i]);
				if(bridge.charAt(0) == 'T'){
					String[] bridgewords = bridge.substring(bridge.lastIndexOf(':') + 1).split(" "); //???
					Random random = new Random();
					int j = random.nextInt() % bridgewords.length;
					res.append(bridgewords[j] + " ");
				}
				res.append(arr[i]);
				word1 = arr[i];
			}
		}
		return res.toString();
	}
	void showPathInGraph(Graph G, String paths){
		String[] colors = {"blue","red","yellow","gold","green","pink","aliceblue","violet",
				"pink","darkgrey","brown","darkorange","antiquewhite","aqua","aquamarine","azure",
				"beige","bisque","black","blanchedalmond",
				"blueviolet","burlywood","cadetblue","chartreuse",
				"chocolate","coral","cornflowerblue","cornsilk","crimson",
				"cyan ","darkcyan","darkgoldenrod","darkgray",
				"darkgreen","darkkhaki","darkmagenta","darkolivegreen",
				"darkorange","darkorchid","darkred","darksalmon","darkseagreen",
				"darkslateblue","darkslategray","darkslategrey","darkturquoise","darkviolet"};

		String[] path = paths.split("\n");
		String u = null;
		String v = null;
		int colorindex = 0;
		GraphViz gv = new GraphViz();
		gv.addln(gv.start_graph());	
		gv.add(G.getDot());
		for(String p: path){
			String[] nodes = p.split(" ");
			if(p.length() == 0)
				continue;
			u = nodes[0];
			for(int i = 1; i < nodes.length - 1; i++){
				v = nodes[i];			
				gv.add(u + "->" + v +"[color =" + colors[colorindex] + "];\n");
				u = v;
			} 
			colorindex++;
			if(colorindex == colors.length)
				colorindex = 0;
		}
		gv.add(gv.end_graph());
		gv.increaseDpi();
		JFrame myframe = new JFrame("Shortest Paths");
		JLabel mylabel = new JLabel();
		myframe.setVisible(true);
		mylabel.setIcon(new ImageIcon(gv.getGraph(gv.getDotSource(), "jpg", "dot")));
		myframe.add(mylabel);
		myframe.pack();
	}
	
	String calcShortestPath(Graph G, String word1, String word2){
		int idst = G.existNode(word1);
		int idend = G.existNode(word2);
		if(idend == -1 || idst == -1)
			return "";
		int[] dist = new int[100]; //vertexNum < 100
		int[] bfnode = new int[100]; //vertexNum < 100
		G.dijkstra(word1, dist, bfnode);
		int[] stk = new int[100]; //vertexNum < 100;
		int top = -1;
		int now = idend;
		while(now != -1){
			stk[++top] = now;
			now = bfnode[now];
		}
		StringBuilder res = new StringBuilder();
		while(top != -1){
			res.append(G.vertexs[stk[top--]].getName());
			res.append(" ");
		}
		res.append(dist[idend]);
		res.append("\n");
		return res.toString();
	}
	

	String showAllShortestPath(Graph G, String word){
		int[] dist = new int[100]; //vertexNum < 100
		int[] bfnode = new int[100]; //vertexNum < 100
		G.dijkstra(word, dist, bfnode);
		int[] stk = new int[100]; //vertexNum < 100;
		int top = -1;
		StringBuilder res = new StringBuilder();
		for(int idend = 0; idend < G.vertexNum; idend++){
			top = -1;
			int now = idend;
			while(now != -1){
				stk[++top] = now;
				now = bfnode[now];
			}
			while(top != -1){
				res.append(G.vertexs[stk[top--]].getName());
				res.append(" ");
			}
			res.append(dist[idend]);
			res.append("\n");
		}
		return res.toString();		
	}
	
	String randomWalk(Graph G){
		G.init();
		Random random = new Random();
		Vertex u = G.vertexs[random.nextInt(G.vertexNum)];
		StringBuilder res = new StringBuilder(u.getName());
		Edge e = null;
		while(true){
			if(u.getDeg() == 0)
				break;
			 e = G.edges[u.getId()][random.nextInt(u.getDeg())];
			 u = e.getTo();
			 res.append(" ");
			 res.append(u.getName());
			 if(e.getStatus()){
				 break;
			 }
			 e.changeStatus(true);
		}
		return res.toString();
	}
}
