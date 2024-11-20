package services.printer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

import printer.Printer;

public class PrintService extends UnicastRemoteObject implements IPrintService {
  Printer printerManager = new Printer();
  HashMap<String, String> config = new HashMap<String, String>();

  boolean running;

  public PrintService() throws RemoteException {
    super();
    this.running = false;
  }

  public void print(String filename, String printer) throws RemoteException {
    printerManager.print(filename, printer);
  }

  public void queue(String printer) throws RemoteException {
    printerManager.queue(printer);
  }

  public void topQueue(String printer, int job) throws RemoteException {
    printerManager.topQueue(printer, job);
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
    this.printerManager.clear();
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
