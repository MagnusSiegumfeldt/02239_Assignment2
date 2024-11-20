package printer;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

public class Printer {
  HashMap<String, ArrayList<String>> queues = new HashMap<String, ArrayList<String>>();

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

  public void clear() throws RemoteException {
    this.queues.clear();
  }

  public void status(String printer) throws RemoteException {
    System.out.println("Printer: \"" + printer + "\" is currently ..." + "VERY HAPPY");
  }
}
