package server;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;

import services.auth.IAuthService;
import services.printer.IPrintService;

import java.rmi.NotBoundException;

public class Client {
  public static void main(String[] args) throws NotBoundException, MalformedURLException, RemoteException {
    IAuthService authServer = (IAuthService) Naming.lookup("rmi://localhost:5099/auth");
    String token = authServer.login("alice", "alice_password");

    IPrintService printServer = (IPrintService) Naming.lookup("rmi://localhost:5099/printserver");

    printServer.print(token, "Test print 1", "printer1");
    printServer.print(token, "Test print 1", "printer1");
    printServer.print(token, "Test print 2", "printer1");
    printServer.print(token, "Test print 3", "printer1");
    printServer.topQueue(token, "printer1", 3);
    printServer.queue(token, "printer1");

    authServer.logout("alice");
  }
}

// Creating new users
// PasswordManager passwordManager = new PasswordManager();
// passwordManager.createLogin("alice", "alice_password");
// passwordManager.createLogin("bob", "bob_password");
// passwordManager.createLogin("cecilia", "cecilia_password");
// passwordManager.createLogin("david", "david_password");
// passwordManager.createLogin("erica", "erica_password");
// passwordManager.createLogin("fred", "fred_password");
// passwordManager.createLogin("george", "george_password");
