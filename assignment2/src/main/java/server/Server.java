package server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import auth.password.IPasswordManager;
import auth.password.PasswordManager;
import auth.roles.AccessControlProxy;
import auth.roles.IAccessControlManager;
import auth.roles.RoleBasedAccessControlManager;
import auth.session.ISessionManager;
import auth.session.Session;
import auth.session.SessionManager;
import auth.session.SessionProxy;
import auth.session.ISession;
import services.auth.AuthService;
import services.auth.IAuthService;
import services.printer.IPrintService;
import services.printer.PrintService;

public class Server {
  public static void main(String[] args) throws RemoteException {
    Registry registry = LocateRegistry.createRegistry(5099);

    String base = "assignment2/config/";

    ISessionManager sessionManager = new SessionManager(10);
    IPasswordManager passwordManager = new PasswordManager("assignment2/config/passwords.csv");

    // Print service

    // IAccessControlManager accessControlManager = new
    // AccessControlListManager(base +
    // "acl/acl.txt");

    IAccessControlManager accessControlManager = new RoleBasedAccessControlManager(
        base + "/rbac/roles.txt",
        base + "/rbac/hierarchy.txt",
        base + "/rbac/user_roles.txt",
        base + "/rbac/permissions.txt");

    IPrintService printService = new PrintService();
    IPrintService accessControlProxiedPrintService = AccessControlProxy.createProxy(printService,
        accessControlManager,
        sessionManager,
        IPrintService.class);

    IPrintService sessionProxiedPrintService = SessionProxy.createProxy(accessControlProxiedPrintService,
        sessionManager,
        IPrintService.class);

    ISession<IPrintService> sesh = new Session<IPrintService>(sessionProxiedPrintService);

    // Auth service
    IAuthService authService = new AuthService(passwordManager, sessionManager);

    registry.rebind("printserver", sesh);
    registry.rebind("auth", authService);
  }
}
