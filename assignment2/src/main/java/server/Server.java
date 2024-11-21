package server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import auth.password.IPasswordManager;
import auth.password.PasswordManager;
import auth.session.ISessionManager;
import auth.session.SessionManager;
import auth.session.SessionProxy;
import services.auth.AuthService;
import services.auth.IAuthService;
import services.printer.IPrintService;
import services.printer.PrintService;

public class Server {
  public static void main(String[] args) throws RemoteException {
    Registry registry = LocateRegistry.createRegistry(5099);

    ISessionManager sessionManager = new SessionManager(10);
    IPasswordManager passwordManager = new PasswordManager("assignment2/config/passwords.csv");

    // Print service

    IPrintService printService = new PrintService();

    IPrintService sessionProxiedPrintService = SessionProxy.createProxy(printService,
        sessionManager,
        IPrintService.class);

    // Auth service
    IAuthService authService = new AuthService(passwordManager, sessionManager);

    registry.rebind("printserver", sessionProxiedPrintService);
    registry.rebind("auth", authService);
  }
}
