package services.printer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import printer.Printer;

public class PrintService extends UnicastRemoteObject implements IPrintService {
  Printer printerManager = new Printer();

  public PrintService() throws RemoteException {
    super();
  }

  public void print(String token, String filename, String printer) throws RemoteException {
    printerManager.print(filename, printer);
  }

  public void queue(String token, String printer) throws RemoteException {
    printerManager.queue(printer);
  }

  public void topQueue(String token, String printer, int job) throws RemoteException {
    printerManager.topQueue(printer, job);
  }

  public void start(String token) throws RemoteException {
    printerManager.start();
  }

  public void stop(String token) throws RemoteException {
    printerManager.stop();
  }

  public void restart(String token) throws RemoteException {
    printerManager.restart();
  }

  public void status(String token, String printer) throws RemoteException {
    printerManager.status(printer);
  }

  public void readConfig(String token, String parameter) throws RemoteException {
    printerManager.readConfig(parameter);
  }

  public void setConfig(String token, String parameter, String value) throws RemoteException {
    printerManager.setConfig(parameter, value);
  }
}
