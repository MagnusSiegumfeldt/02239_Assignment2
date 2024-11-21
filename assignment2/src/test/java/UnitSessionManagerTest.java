import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

import auth.session.ISessionManager;
import auth.session.SessionManager;

public class UnitSessionManagerTest {
  private ISessionManager sessionManager;

  @Before
  public void setUp() throws Exception {
    this.sessionManager = new SessionManager(4);
  }

  @Test
  public void testSessionSet() {
    String token = this.sessionManager.createSession("user1");
    assertTrue(this.sessionManager.checkSessionValid(token));
    assertFalse(this.sessionManager.checkSessionValid("RANDOM TOKEN"));

    try {
      Thread.sleep(5000);
    } catch (InterruptedException e) {
    }
    assertFalse(this.sessionManager.checkSessionValid(token));
  }
}
