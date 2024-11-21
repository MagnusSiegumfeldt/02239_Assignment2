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
    this.sessionManager.createSession("user1");
    assertTrue(this.sessionManager.checkSessionValid("user1"));
    assertFalse(this.sessionManager.checkSessionValid("user2"));

    try {
      Thread.sleep(5000);
    } catch (InterruptedException e) {
    }
    assertFalse(this.sessionManager.checkSessionValid("user1"));
  }
}
