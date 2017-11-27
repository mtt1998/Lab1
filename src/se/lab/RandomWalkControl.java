package se.lab;

import java.io.IOException;
import java.util.Random;


public class RandomWalkControl{
  public String randomWalk(Graph graph) {
    graph.init();
    return graph.randomWalk();
  }
 }