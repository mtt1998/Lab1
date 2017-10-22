package se.lab;

public class MyThread extends Thread {

  String path;
  MyFrame fr;
  boolean stop = false;

  MyThread(MyFrame fr, String path) {
    this.fr = fr;
    this.path = path;
  }

  public void quit() {
    this.stop = true;
  }


  /**
   * run.
   * @see java.lang.Thread#run()
   */
  public void run() {
    if (fr != null && path != null) {
      String[] nodes = path.split(" ");
      fr.tfres.setText("");
      for (String s : nodes) {
        if (stop == true) {
          break;
        }
        fr.tfres.append(s);
        fr.tfres.append(" ");
        try {
          sleep(500);
        } catch (InterruptedException e) {
          ;
        }
      }

    }
  }
}