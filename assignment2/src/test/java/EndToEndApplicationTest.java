import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import services.auth.AuthService;
import services.auth.IAuthService;
import services.printer.IPrintService;
import services.printer.PrintService;
import auth.session.*;
import auth.password.*;
import auth.roles.*;

public class EndToEndApplicationTest {
  ISessionManager sessionManager;
  IPasswordManager passwordManager;

  @Before
  public void setupRmiServer() throws Exception {
    Registry registry = LocateRegistry.createRegistry(5099);

    String base = "./src/test/res/";

    this.sessionManager = new SessionManager(10);
    this.passwordManager = new PasswordManager(base + "passwords.csv");

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
        passwordManager,
        IPrintService.class);

    // Auth service
    IAuthService authService = new AuthService(passwordManager, sessionManager);

    registry.rebind("printserver", sessionProxiedPrintService);
    registry.rebind("auth", authService);
  }

  @Test
  public void testClientOperations() throws Exception {
    IPrintService printServer = (IPrintService) Naming.lookup("rmi://localhost:5099/printserver");
    IAuthService authServer = (IAuthService) Naming.lookup("rmi://localhost:5099/auth");

    boolean loginResult;
    boolean logoutResult;

    // new

    loginResult = authServer.login("alice", "alice_password");
    assertTrue(loginResult);

    printServer.print("Test print 1", "printer1");
    printServer.print("Test print 2", "printer1");
    printServer.print("Test print 3", "printer1");
    printServer.topQueue("printer1", 3);
    printServer.queue("printer1");
    printServer.restart();

    logoutResult = authServer.logout("alice");
    assertTrue(logoutResult);

    // new

    loginResult = authServer.login("bob", "bob_password");
    assertTrue(loginResult);
    printServer.start();
    System.out.println("Test passed: Start operation performed by Bob.");

    printServer.restart();
    printServer.status("printer1");
    printServer.setConfig("test", "test1");
    printServer.readConfig("test");

    logoutResult = authServer.logout("bob");
    assertTrue(logoutResult);

    // new

    loginResult = authServer.login("alice", "alice_password");
    assertTrue(loginResult);
    printServer.start();
    System.out.println("Test passed: Start operation performed by Alice.");
    logoutResult = authServer.logout("alice");
    assertTrue(logoutResult);

    // new

    loginResult = authServer.login("bob", "bob_password");
    assertTrue(loginResult);
    printServer.stop();
    System.out.println("Test passed: Stop operation performed by Bob.");
    logoutResult = authServer.logout("bob");
    assertTrue(logoutResult);

    // new

    loginResult = authServer.login("cecilia", "cecilia_password");
    assertTrue(loginResult);
    printServer.restart();
    System.out.println("Test passed: Restart operation performed by Cecilia.");
    logoutResult = authServer.logout("cecilia");
    assertTrue(logoutResult);

    // new

    loginResult = authServer.login("david", "david_password");
    assertTrue(loginResult);
    printServer.print("Document1", "printer1");
    System.out.println("Test passed: Print operation performed by David.");
    logoutResult = authServer.logout("david");
    assertTrue(logoutResult);

    // new

    loginResult = authServer.login("alice", "alice_password");
    assertTrue(loginResult);
    printServer.status("printer1");
    System.out.println("Test passed: Status operation performed by Alice.");
    logoutResult = authServer.logout("alice");
    assertTrue(logoutResult);
  }
}
