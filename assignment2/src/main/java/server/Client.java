package server;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;

import services.auth.IAuthService;
import services.printer.IPrintService;

import java.rmi.NotBoundException;

public class Client {
  public static void main(String[] args) throws NotBoundException, MalformedURLException, RemoteException {
    IPrintService printServer = (IPrintService) Naming.lookup("rmi://localhost:5099/printserver");
    IAuthService authServer = (IAuthService) Naming.lookup("rmi://localhost:5099/auth");

    // Creating new users
    // PasswordManager passwordManager = new PasswordManager();
    // passwordManager.createLogin("alice", "alice_password");
    // passwordManager.createLogin("bob", "bob_password");
    // passwordManager.createLogin("cecilia", "cecilia_password");
    // passwordManager.createLogin("david", "david_password");
    // passwordManager.createLogin("erica", "erica_password");
    // passwordManager.createLogin("fred", "fred_password");
    // passwordManager.createLogin("george", "george_password");

    String sessionToken = authServer.login("alice", "alice_password");
    // printServer.setSessionToken(sessionToken);
    printServer.print("Test print 1", "printer1");
    printServer.print("Test print 1", "printer1");
    printServer.print("Test print 2", "printer1");
    printServer.print("Test print 3", "printer1");
    printServer.topQueue("printer1", 3);
    printServer.queue("printer1");
    authServer.logout("alice");

  }
}
