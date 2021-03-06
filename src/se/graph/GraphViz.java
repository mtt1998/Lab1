package se.graph;

/* $Id$ */
/*
 ******************************************************************************
 * * (c) Copyright Laszlo Szathmary * * This program is free software; you can redistribute it
 * and/or modify it * under the terms of the GNU Lesser General Public License as published by * the
 * Free Software Foundation; either version 2.1 of the License, or * (at your option) any later
 * version. * * This program is distributed in the hope that it will be useful, but * WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY * or FITNESS FOR A PARTICULAR
 * PURPOSE. See the GNU Lesser General Public * License for more details. * * You should have
 * received a copy of the GNU Lesser General Public License * along with this program; if not, write
 * to the Free Software Foundation, * Inc., 675 Mass Ave, Cambridge, MA 02139, USA. * *
 ******************************************************************************
 */


import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;


public class GraphViz {
  static final Logger LOGGER = Logger.getLogger(GraphViz.class.getName());
  /**
   * Detects the client's operating system.
   */
  private final static String OSNAME = System.getProperty("os.name").replaceAll("\\s", "");

  /**
   * The image size in dpi. 96 dpi is normal size. Higher values are 10% higher each. Lower values
   * 10% lower each. dpi patch by Peter Mueller
   */
  private final int[] dpiSizes =
      {46, 51, 57, 63, 70, 78, 86, 96, 106, 116, 128, 141, 155, 170, 187, 206, 226, 249};

  /**
   * Define the index in the image size array.
   */
  private int currentDpiPos = 7;

  /**
   * Increase the image size (dpi).
   */
  public void increaseDpi() {
    if (this.currentDpiPos < (this.dpiSizes.length - 1)) {
      ++this.currentDpiPos;
    }
  }

  /**
   * Decrease the image size (dpi).
   */
  public void decreaseDpi() {
    if (this.currentDpiPos > 0) {
      --this.currentDpiPos;
    }
  }

  public int getImageDpi() {
    return this.dpiSizes[this.currentDpiPos];
  }

  /**
   * The source of the graph written in dot language.
   */
  private StringBuilder graph = new StringBuilder();

  private String tempDir;

  private String executable;

  /**
   * Convenience Constructor with default OS specific pathes creates a new GraphViz object that will
   * contain a graph. Windows: executable = e:/graphviz/bin/dot.exe tempDir = e:/graphviz/temp
   * MacOs: executable = /usr/local/bin/dot tempDir = /tmp Linux: executable = /usr/bin/dot tempDir
   * = /tmp
   */
  public GraphViz() {
    if (GraphViz.OSNAME.contains("Windows")) {
      this.tempDir = "e:/graphviz/temp";
      this.executable = "e:/graphviz/bin/dot.exe";
    } else if (GraphViz.OSNAME.equals("MacOSX")) {
      this.tempDir = "/tmp";
      this.executable = "/usr/local/bin/dot";
    } else if (GraphViz.OSNAME.equals("Linux")) {
      this.tempDir = "/tmp";
      this.executable = "/usr/bin/dot";
    }
  }

  /**
   * Configurable Constructor with path to executable dot and a temp dir.
   * 
   * @param executable absolute path to dot executable
   * @param tempDir absolute path to temp directory
   */
  public GraphViz(String executable, String tempDir) {
    this.executable = executable;
    this.tempDir = tempDir;
  }

  /**
   * Returns the graph's source description in dot language.
   * 
   * @return Source of the graph in dot language.
   */
  public String getDotSource() {
    return this.graph.toString();
  }

  /**
   * Adds a string to the graph's source (without newline).
   */
  public void add(String line) {
    this.graph.append(line);
  }

  /**
   * Adds a string to the graph's source (with newline).
   */
  public void addln(String line) {
    this.graph.append(line + "\n");
  }

  /**
   * Adds a newline to the graph's source.
   */
  public void addln() {
    this.graph.append('\n');
  }

  public void clearGraph() {
    this.graph = new StringBuilder();
  }

  /**
   * Returns the graph as an image in binary format.
   * 
   * @param dotSource Source of the graph to be drawn.
   * @param type Type of the output image to be produced, e.g.: gif, dot, fig, pdf, ps, svg, png.
   * @param representationType Type of how you want to represent the graph:
   *        <ul>
   *        <li>dot</li>
   *        <li>neato</li>
   *        <li>fdp</li>
   *        <li>sfdp</li>
   *        <li>twopi</li>
   *        <li>circo</li>
   *        </ul>
   * @see http://www.graphviz.org under the Roadmap title
   * @return A byte array containing the image of the graph.
   */
  public byte[] getGraph(String dotSource, String type, String representationType) {
    File dot;
    byte[] imgStream = null;

    try {
      dot = writeDotSourceToFile(dotSource);
      if (dot != null) {
        imgStream = getImgStream(dot, type, representationType);
        if (LOGGER.isLoggable(Level.WARNING) && dot.delete() == false) {
          LOGGER.warning("Warning: " + dot.getAbsolutePath() + " could not be deleted!");
        }
        return imgStream;
      }
      return null;
    } catch (java.io.IOException ioe) {
      return null;
    }
  }

  /**
   * Writes the graph's image in a file.
   * 
   * @param img A byte array containing the image of the graph.
   * @param file Name of the file to where we want to write.
   * @return Success: 1, Failure: -1
   */
  public int writeGraphToFile(byte[] img, String file) {
    File to = new File(file);
    return writeGraphToFile(img, to);
  }

  /**
   * Writes the graph's image in a file.
   * 
   * @param img A byte array containing the image of the graph.
   * @param to A File object to where we want to write.
   * @return Success: 1, Failure: -1
   */
  public int writeGraphToFile(byte[] img, File to) {
    try {
      FileOutputStream fos = new FileOutputStream(to);
      fos.write(img);
      fos.close();
    } catch (Exception ioe) {
      return -1;
    }
    return 1;
  }

  /**
   * It will call the external dot program, and return the image in binary format.
   * 
   * @param dot Source of the graph (in dot language).
   * @param type Type of the output image to be produced, e.g.: gif, dot, fig, pdf, ps, svg, png.
   * @param representationType Type of how you want to represent the graph:
   *        <ul>
   *        <li>dot</li>
   *        <li>neato</li>
   *        <li>fdp</li>
   *        <li>sfdp</li>
   *        <li>twopi</li>
   *        <li>circo</li>
   *        </ul>
   * @see http://www.graphviz.org under the Roadmap title
   * @return The image of the graph in .gif format.
   */
  private byte[] getImgStream(File dot, String type, String representationType) {

    File img;
    byte[] imgStream = null;

    try {
      img = File.createTempFile("graph_", "." + type, new File(this.tempDir));
      Runtime rt = Runtime.getRuntime();

      // patch by Mike Chenault
      // representation type with -K argument by Olivier Duplouy
      String[] args = {executable, "-T" + type, "-K" + representationType,
          "-Gdpi=" + dpiSizes[this.currentDpiPos], dot.getAbsolutePath(), "-o",
          img.getAbsolutePath()};
      Process p = rt.exec(args);
      p.waitFor();
      FileInputStream in = new FileInputStream(img.getAbsolutePath());
      imgStream = new byte[in.available()];
      in.read(imgStream);
      // Close it if we need to
      if (in != null) {
        in.close();
      }

      if (LOGGER.isLoggable(Level.WARNING) && img.delete() == false) {
        LOGGER.warning("Warning: " + img.getAbsolutePath() + " could not be deleted!");
      }
    } catch (java.io.IOException ioe) {
      if (LOGGER.isLoggable(Level.WARNING)) {

        LOGGER.warning("Error:    in I/O processing of tempfile in dir " + tempDir + "\n");
        LOGGER.warning("       or in calling external command");
      }
      ioe.printStackTrace();
    } catch (java.lang.InterruptedException ie) {
      LOGGER.warning("Error: the execution of the external program was interrupted");
      ie.printStackTrace();
    }

    return imgStream;
  }

  /**
   * Writes the source of the graph in a file, and returns the written file as a File object.
   * 
   * @param str Source of the graph (in dot language).
   * @return The file (as a File object) that contains the source of the graph.
   */
  private File writeDotSourceToFile(String str) throws java.io.IOException {
    File temp;
    temp = File.createTempFile("graph_", ".dot.tmp", new File(tempDir));
    try {
      FileWriter fout = new FileWriter(temp);
      fout.write(str);
      fout.close();
    } catch (Exception e) {

      LOGGER.warning("Error: I/O error while writing the dot source to temp file!");
      return null;
    }
    return temp;
  }

  /**
   * Returns a string that is used to start a graph.
   * 
   * @return A string to open a graph.
   */
  public String startGraph() {
    return "digraph G {";
  }

  /**
   * Returns a string that is used to end a graph.
   * 
   * @return A string to close a graph.
   */
  public String endGraph() {
    return "}";
  }

  /**
   * Takes the cluster or subgraph id as input parameter and returns a string that is used to start
   * a subgraph.
   * 
   * @return A string to open a subgraph.
   */
  public String startSubgraph(int clusterid) {
    return "subgraph cluster_" + clusterid + " {";
  }

  /**
   * Returns a string that is used to end a graph.
   * 
   * @return A string to close a graph.
   */
  public String endSubgraph() {
    return "}";
  }

  /**
   * Read a DOT graph from a text file.
   *
   * @param input Input text file containing the DOT graph source.
   */
  public void readSource(String input) {
    StringBuilder sb = new StringBuilder();

    try {
      FileInputStream fis = new FileInputStream(input);
      DataInputStream dis = new DataInputStream(fis);
      BufferedReader br = new BufferedReader(new InputStreamReader(dis, StandardCharsets.UTF_8));
      String line;
      while ((line = br.readLine()) != null) {
        sb.append(line);
      }
      dis.close();
      br.close();
    } catch (Exception e) {
      if (LOGGER.isLoggable(Level.WARNING)) {
        LOGGER.warning("Error: " + e.getMessage());
      }
    }
    this.graph = sb;
  }
} // end of class GraphViz

