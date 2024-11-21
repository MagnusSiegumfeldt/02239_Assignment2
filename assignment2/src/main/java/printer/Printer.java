package printer;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

public class Printer {
  HashMap<String, ArrayList<String>> queues = new HashMap<String, ArrayList<String>>();
  HashMap<String, String> config = new HashMap<String, String>();

  boolean running;

  public Printer() {
    this.running = false;
  }

  public void print(String filename, String printer) throws RemoteException {
    System.out.println("Printer: \"" + printer + "\" printed file: \"" + filename + "\"");
  }

  public void queue(String printer) throws RemoteException {
    ArrayList<String> p = queues.get(printer);
    if (p == null) {
      System.out.println("Printer: \"" + printer + "\" is empty!");
      return;
    }

    for (String s : p) {
      System.out.print(s.toString());
    }

  }

  public void topQueue(String printer, int job) throws RemoteException {
    ArrayList<String> p = queues.get(printer);
    if (p == null) {
      System.out.println("Printer: \"" + printer + "\" is empty!");
      return;
    }

    String j = p.remove(job);
    p.add(0, j);
  }

  public void start() throws RemoteException {
    System.out.println("Server started.");
    this.running = true;
  }

  public void stop() throws RemoteException {
    System.out.println("Server stopped.");
    this.running = false;
  }

  public void restart() throws RemoteException {
    this.stop();
    this.queues.clear();
    this.start();
  }

  public void status(String printer) throws RemoteException {
    System.out.println("Printer: \"" + printer + "\" is currently ..." + "VERY HAPPY");
  }

  public void readConfig(String parameter) throws RemoteException {
    String cfg = config.get(parameter);
    System.out.println(cfg != null ? cfg : "No config for " + parameter);
  }

  public void setConfig(String parameter, String value) throws RemoteException {
    config.put(parameter, value);
  }
}
