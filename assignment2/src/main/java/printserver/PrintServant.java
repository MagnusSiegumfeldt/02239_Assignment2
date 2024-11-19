package printserver;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

import auth.password.PasswordManager;
import auth.roles.AccessControlListManager;
import auth.roles.IAccessControl;
import auth.session.SessionManager;
import printer.PrinterManager;

public class PrintServant extends UnicastRemoteObject implements IPrintServant {
  PrinterManager printerManager = new PrinterManager();
  HashMap<String, String> config = new HashMap<String, String>();

  boolean running;
  PasswordManager passManager;
  SessionManager sessManager;
  IAccessControl accessControl;

  public PrintServant() throws RemoteException {
    this("password.csv", "rbac.txt");
  }

  public PrintServant(String passwordFile, String aclFile) throws RemoteException {
    super();
    this.running = false;
    this.passManager = new PasswordManager(passwordFile);
    this.sessManager = new SessionManager(1);
    this.accessControl = new AccessControlListManager(aclFile);
  }

  public boolean addLogin(String username, String password) throws RemoteException {
    System.out.println("Got here");
    this.accessControl.check(this.sessManager.getCurrentUser(), "addLogin");
    return this.passManager.createLogin(username, password);
  }

  public boolean login(String username, String password) throws RemoteException {
    boolean loginSuccess = this.passManager.checkLogin(username, password);
    if (loginSuccess) {
      this.sessManager.set(username);
    }
    return loginSuccess;
  }

  public boolean logout(String username) throws RemoteException {
    if (this.sessManager.check(username)) {
      this.sessManager.unset();
      System.out.println("Logged out.");
      return true;
    } else {
      System.out.println("Could not logout.");
      return false;
    }
  }

  public boolean print(String filename, String printer) throws RemoteException {
    this.accessControl.check(this.sessManager.getCurrentUser(), "print");

    printerManager.print(filename, printer);
    return true;
  }

  public boolean queue(String printer) throws RemoteException {
    this.accessControl.check(this.sessManager.getCurrentUser(), "queue");

    printerManager.queue(printer);
    return true;
  }

  public boolean topQueue(String printer, int job) throws RemoteException {
    this.accessControl.check(this.sessManager.getCurrentUser(), "topQueue");
    printerManager.topQueue(printer, job);
    return true;
  }

  public boolean start() throws RemoteException {
    this.accessControl.check(this.sessManager.getCurrentUser(), "start");

    System.out.println("Server started.");
    this.running = true;
    return true;
  }

  public boolean stop() throws RemoteException {
    this.accessControl.check(this.sessManager.getCurrentUser(), "stop");

    System.out.println("Server stopped.");
    this.running = false;
    return true;
  }

  public boolean restart() throws RemoteException {
    this.accessControl.check(this.sessManager.getCurrentUser(), "restart");

    this.stop();
    this.printerManager.clear();
    this.start();
    return true;
  }

  public boolean status(String printer) throws RemoteException {
    this.accessControl.check(this.sessManager.getCurrentUser(), "status");

    System.out.println("Printer: \"" + printer + "\" is currently ..." + "VERY HAPPY");
    return true;
  }

  public boolean readConfig(String parameter) throws RemoteException {
    this.accessControl.check(this.sessManager.getCurrentUser(), "readConfig");

    String cfg = config.get(parameter);
    System.out.println(cfg != null ? cfg : "No config for " + parameter);
    return true;
  }

  public boolean setConfig(String parameter, String value) throws RemoteException {
    this.accessControl.check(this.sessManager.getCurrentUser(), "setConfig");

    config.put(parameter, value);
    return true;
  }
}
