package Lab;
//add line2 in C4
import Lab.Main;
import Lab.Graph;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.*;
//add line3 in B1
class MyThread extends Thread{
	String path;
	MyFrame fr;
	MyThread(MyFrame fr, String path){
		this.fr = fr;
		this.path = path;
	}
	public void run(){
		if(fr != null && path != null){
			String[] nodes = path.split(" ");
			fr.tfres.setText("");
			for(String s : nodes){
				fr.tfres.append(s);
				fr.tfres.append(" ");
				try{
					sleep(500);
				}catch(InterruptedException e){
				}
			}
		}
	}
}
public class Gui {
	public static void main(String[] args) {
		new MyFrame("GUI").launchFrame();;
	}	
}

class MyFrame extends Frame{
	MyThread th = null;
	Main control = new Main();
	Graph G = null;
	Button btnInput, btnSave, btnquery, btngen, btnpath, btnrandom, btnstop;
	TextField tfword1, tfword2;
	TextArea tfres;
	Label lb1, lb2, lb3;
	Panel pn;
	Panel pn1;
	MyFrame(String name){
		super(name);
	}
	public void launchFrame(){
		lb1 = new Label("from/text:");
		tfword1 = new TextField();
		lb2 = new Label("to:");
		tfword2 = new TextField();
		lb3 = new Label("Result:");
		tfres = new TextArea();
		pn =new Panel();
		pn1 =new Panel();
		btnInput = new Button("Graph");
		btnSave = new Button("save");
		btnquery = new Button("query");
		btngen = new Button("generate");
		btnpath = new Button("shortest path");
		btnrandom = new Button("Random walk");
		btnstop = new Button("stop");
		
		MyMonitor bh = new MyMonitor();
		btnInput.addActionListener(bh);
		btnSave.addActionListener(bh);
		btnquery.addActionListener(bh);
		btngen.addActionListener(bh);
		btnpath.addActionListener(bh);
		btnrandom.addActionListener(bh);
		btnstop.addActionListener(bh);
		
		setBounds(200,200,200,200);
		setLayout(new BorderLayout(5,5));
		pn.setLayout(new GridLayout(1,4));
		pn1.setLayout(new GridLayout(7,1));
		
		lb1.setBackground(Color.LIGHT_GRAY);
		lb2.setBackground(Color.LIGHT_GRAY);
		lb3.setBackground(Color.LIGHT_GRAY);
		lb1.setFont(new Font("",1,30));
		lb2.setFont(new Font("",1,30));
		lb3.setFont(new Font("",1,30));
		btnInput.setFont(new Font("",1,25));
		btnSave.setFont(new Font("",1,25));
		btnquery.setFont(new Font("",1,25));
		btngen.setFont(new Font("",1,25));
		btnpath.setFont(new Font("",1,25));
		btnrandom.setFont(new Font("",1,25));
		btnstop.setFont(new Font("",1,25));
		tfword1.setFont(new Font("",1,25));
		tfword2.setFont(new Font("",1,25));
		tfres.setFont(new Font("",1,25));
		pn.add(lb1);
		pn.add(tfword1);
		pn.add(lb2);
		pn.add(tfword2);
		
		add("North",pn);
		add("West",lb3);
		add("Center",tfres);
		
		pn1.add(btnInput);
		pn1.add(btnSave);
		pn1.add(btnquery);
		pn1.add(btngen);
		pn1.add(btnpath);
		pn1.add(btnrandom);
		pn1.add(btnstop);
		add("East",pn1);
		
		pack();
		setVisible(true);
		this.addWindowListener(new MyWindowMonitor());
		
	}
	class MyWindowMonitor extends WindowAdapter{
		public void windowClosing(WindowEvent e){
			setVisible(false);
			System.exit(0);
		}
	}

	class MyMonitor implements ActionListener{
		public void actionPerformed(ActionEvent e){
			if(e.getActionCommand().equals("Graph")){
				JFileChooser jfc = new JFileChooser();
				jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				jfc.showDialog(new JLabel(), "Ñ¡Ôñ");
				File file = jfc.getSelectedFile();
				if(file != null)
					G = control.createDirectedGraph(file.getAbsolutePath());
			}
			else if(e.getActionCommand().equals("save")){
				if(G != null){
					control.showDirectedGraph(G);
				}
			}else if(e.getActionCommand().equals("query")){
				String word1 = tfword1.getText();
				String word2 = tfword2.getText();
				if(G != null &&word1.length() != 0 && word2.length() != 0){
					tfres.setText(control.queryBridgeWords(G, word1, word2));
				}else{
					tfres.setText("error!");
				}
			}else if(e.getActionCommand().equals("generate")){
				String word1 = tfword1.getText();
				if(G != null && word1.length() != 0){
					tfres.setText(control.generateNewText(G, word1));
				}else{
					tfres.setText("error!");
				}
			}else if(e.getActionCommand().equals("shortest path")){
				String word1 = tfword1.getText();
				String word2 = tfword2.getText();
				if(G != null && word1.length() != 0 && word2.length() != 0){
					String paths = control.calcShortestPath(G, word1, word2);
					tfres.setText(paths);
					control.showPathInGraph(G, paths);
				}else if(G != null && word1.length() != 0){
					String paths = control.showAllShortestPath(G, word1);
					tfres.setText(paths);
					control.showPathInGraph(G, paths);
				}else{
					tfres.setText("error!");
				}
			}else if(e.getActionCommand().equals("Random walk")){
				if(G != null){
					th = new MyThread(MyFrame.this,control.randomWalk(G));
					th.start();
				}
			}else if(e.getActionCommand().equals("stop")){
				if(th != null)
					th.stop();
			}
		}
	}
}
