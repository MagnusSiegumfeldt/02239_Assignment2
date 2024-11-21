
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.rmi.RemoteException;

import org.junit.Before;
import org.junit.Test;

import auth.password.IPasswordManager;
import auth.password.PasswordManager;
import auth.session.ISessionManager;
import auth.session.SessionManager;
import services.auth.AuthService;
import services.auth.IAuthService;

public class IntegrationAuthServiceTest {
  private IAuthService service;
  private IPasswordManager passwordManager;
  private ISessionManager sessionManager;

  private final int DURATION = 1;

  @Before
  public void setUp() throws Exception {
    this.passwordManager = new PasswordManager("./src/test/res/password_tests.csv");
    this.sessionManager = new SessionManager(DURATION);

    this.service = new AuthService(passwordManager, sessionManager);
  }

  @Test
  public void testLoginSuccess() throws RemoteException {
    assertTrue(this.passwordManager.checkLogin("user1", "test"));
    this.service.login("user1", "test");
    assertEquals(this.sessionManager.getCurrentUser(), "user1");
    assertTrue(this.sessionManager.checkSessionValid("user1"));
  }

  @Test
  public void testLogoutSuccess() throws RemoteException {
    assertTrue(this.passwordManager.checkLogin("user1", "test"));
    this.service.login("user1", "test");
    assertEquals(this.sessionManager.getCurrentUser(), "user1");
    assertTrue(this.sessionManager.checkSessionValid("user1"));

    assertTrue(this.service.logout("user1"));
    assertEquals(this.sessionManager.getCurrentUser(), null);
    assertFalse(this.sessionManager.checkSessionValid("user1"));
  }

  @Test(expected = RemoteException.class)
  public void testLoginFailInvalidCrendentials() throws RemoteException {
    this.service.login("user1", "wrong password");
  }

  @Test(expected = RemoteException.class)
  public void testLogoutFail() throws RemoteException {
    assertFalse(this.service.logout("user1"));
  }
}
