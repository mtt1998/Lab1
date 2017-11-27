package se.lab;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;

import se.graph.GraphViz;

public class MyFrame extends Frame {
  private static final long serialVersionUID = 1L;
  MyThread th = null;
  Graph graph = null;
  Button btnInput;
  Button btnSave;
  Button btnquery;
  Button btngen;
  Button btnpath;
  Button btnrandom;
  Button btnstop;
  TextField tfword1;
  TextField tfword2;
  TextArea tfres;
  Label lb1;
  Label lb2;
  Label lb3;
  Panel pn;
  Panel pn1;

  /**
   * MyFrame.
   * @param name ?
   */
  MyFrame(String name) {
    super(name);
  }

  /**
   * launchFrame.
   */
  public void launchFrame() {
    lb1 = new Label("from/text:");
    tfword1 = new TextField();
    lb2 = new Label("to:");
    tfword2 = new TextField();
    lb3 = new Label("Result:");
    tfres = new TextArea();
    pn = new Panel();
    pn1 = new Panel();
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

    setBounds(200, 200, 200, 200);
    setLayout(new BorderLayout(5, 5));
    pn.setLayout(new GridLayout(1, 4));
    pn1.setLayout(new GridLayout(7, 1));

    lb1.setBackground(Color.LIGHT_GRAY);
    lb2.setBackground(Color.LIGHT_GRAY);
    lb3.setBackground(Color.LIGHT_GRAY);
    lb1.setFont(new Font("", 1, 30));
    lb2.setFont(new Font("", 1, 30));
    lb3.setFont(new Font("", 1, 30));
    btnInput.setFont(new Font("", 1, 25));
    btnSave.setFont(new Font("", 1, 25));
    btnquery.setFont(new Font("", 1, 25));
    btngen.setFont(new Font("", 1, 25));
    btnpath.setFont(new Font("", 1, 25));
    btnrandom.setFont(new Font("", 1, 25));
    btnstop.setFont(new Font("", 1, 25));
    tfword1.setFont(new Font("", 1, 25));
    tfword2.setFont(new Font("", 1, 25));
    tfres.setFont(new Font("", 1, 25));
    pn.add(lb1);
    pn.add(tfword1);
    pn.add(lb2);
    pn.add(tfword2);

    add("North", pn);
    add("West", lb3);
    add("Center", tfres);

    pn1.add(btnInput);
    pn1.add(btnSave);
    pn1.add(btnquery);
    pn1.add(btngen);
    pn1.add(btnpath);
    pn1.add(btnrandom);
    pn1.add(btnstop);
    add("East", pn1);

    pack();
    setVisible(true);
    this.addWindowListener(new MyWindowMonitor());

  }
  

  class MyWindowMonitor extends WindowAdapter {
    public void windowClosing(WindowEvent e) {
      setVisible(false);
      System.exit(0);
    }
  }

  class MyMonitor implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      if (th != null) {
        th.quit();
      }
      if (e.getActionCommand().equals("Graph")) {
        JFileChooser jfc = new JFileChooser();
        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        jfc.showDialog(new JLabel(), "ѡ��");
        File file = jfc.getSelectedFile();
        if (file != null) {
          try {
            graph = new CreateGraphControl().createDirectedGraph(file.getAbsolutePath());
          } catch (FileNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
          }
        }
      } else if (e.getActionCommand().equals("save")) {
        if (graph != null) {
            new ShowGraphControl().showDirectedGraph(graph);
        }
      } else if (e.getActionCommand().equals("query")) {
        String word1 = tfword1.getText();
        String word2 = tfword2.getText();
        if (graph != null && word1.length() != 0 && word2.length() != 0) {
          tfres.setText(new QueryWordControl().queryBridgeWords(graph, word1, word2));
        } else {
          tfres.setText("error!");
        }
      } else if (e.getActionCommand().equals("generate")) {
        String word1 = tfword1.getText();
        if (graph != null && word1.length() != 0) {
          tfres.setText(new GenerateTextControl().generateNewText(graph, word1));
        } else {
          tfres.setText("error!");
        }
      } else if (e.getActionCommand().equals("shortest path")) {
        String word1 = tfword1.getText();
        String word2 = tfword2.getText();
        if (graph != null && word1.length() != 0 && word2.length() != 0) {
          String paths = new CalPathControl().calcShortestPath(graph, word1, word2);
          tfres.setText(paths);
          new ShowPathControl().showPathInGraph(graph, paths);
        } else if (graph != null && word1.length() != 0) {
          String paths = new CalPathControl().calcAllShortestPath(graph, word1);
          tfres.setText(paths);
          new ShowPathControl().showPathInGraph(graph, paths);
        } else {
          tfres.setText("error!");
        }
      } else if (e.getActionCommand().equals("Random walk")) {
        if (graph != null) {
          th = new MyThread(MyFrame.this, new RandomWalkControl().randomWalk(graph));
          th.start();
        }
      } else if (e.getActionCommand().equals("stop")) {
        if (th != null) {
          th.quit();
        }
      }
    }
  }

  public static void main(String[] args) {
    new MyFrame("GUI").launchFrame();
  }
  
}
