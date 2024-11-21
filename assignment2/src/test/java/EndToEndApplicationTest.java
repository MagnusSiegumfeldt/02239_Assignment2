import org.junit.Before;
import org.junit.Test;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import services.auth.AuthService;
import services.auth.IAuthService;
import services.printer.IPrintService;
import services.printer.PrintService;
import auth.session.*;
import auth.password.*;

public class EndToEndApplicationTest {
  ISessionManager sessionManager;
  IPasswordManager passwordManager;

  @Before
  public void setupRmiServer() throws Exception {
    Registry registry = LocateRegistry.createRegistry(5099);

    String base = "./src/test/res/";

    this.sessionManager = new SessionManager(10);
    this.passwordManager = new PasswordManager(base + "passwords.csv");

    IPrintService printService = new PrintService();

    IPrintService sessionProxiedPrintService = SessionProxy.createProxy(printService,
        sessionManager,
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

    String sessionToken;
    // new

    // loginResult = authServer.login("alice", "alice_password");
    // assertTrue(loginResult);
    sessionToken = authServer.login("alice", "alice_password");

    printServer.print(sessionToken, "Test print 1", "printer1");
    printServer.print(sessionToken, "Test print 2", "printer1");
    printServer.print(sessionToken, "Test print 3", "printer1");
    printServer.topQueue(sessionToken, "printer1", 3);
    printServer.queue(sessionToken, "printer1");
    printServer.restart(sessionToken);

    authServer.logout("alice");

    // new

    // loginResult = authServer.login("bob", "bob_password");
    // assertTrue(loginResult);
    sessionToken = authServer.login("bob", "bob_password");
    printServer.start(sessionToken);
    System.out.println("Test passed: Start operation performed by Bob.");

    printServer.restart(sessionToken);
    printServer.status(sessionToken, "printer1");
    printServer.setConfig(sessionToken, "test", "test1");
    printServer.readConfig(sessionToken, "test");

    authServer.logout("bob");

    // new

    // loginResult = authServer.login("alice", "alice_password");
    // assertTrue(loginResult);
    sessionToken = authServer.login("alice", "alice_password");
    printServer.start(sessionToken);
    System.out.println("Test passed: Start operation performed by Alice.");
    authServer.logout("alice");

    // new

    // loginResult = authServer.login("bob", "bob_password");
    // assertTrue(loginResult);
    sessionToken = authServer.login("bob", "bob_password");
    printServer.stop(sessionToken);
    System.out.println("Test passed: Stop operation performed by Bob.");
    authServer.logout("bob");

    // new

    // loginResult = authServer.login("cecilia", "cecilia_password");
    // assertTrue(loginResult);
    sessionToken = authServer.login("cecilia", "cecilia_password");
    printServer.restart(sessionToken);
    System.out.println("Test passed: Restart operation performed by Cecilia.");
    authServer.logout("cecilia");

    // new

    // loginResult = authServer.login("david", "david_password");
    // assertTrue(loginResult);
    sessionToken = authServer.login("david", "david_password");
    printServer.print(sessionToken, "Document1", "printer1");
    System.out.println("Test passed: Print operation performed by David.");
    authServer.logout("david");

    // new

    // loginResult = authServer.login("alice", "alice_password");
    // assertTrue(loginResult);
    sessionToken = authServer.login("alice", "alice_password");
    printServer.status(sessionToken, "printer1");
    System.out.println("Test passed: Status operation performed by Alice.");
    authServer.logout("alice");

  }
}
