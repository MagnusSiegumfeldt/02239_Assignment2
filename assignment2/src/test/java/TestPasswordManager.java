import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

import auth.password.PasswordManager;

public class TestPasswordManager {
  private PasswordManager passwordManager;

  @Before
  public void setUp() throws Exception {
    this.passwordManager = new PasswordManager("./src/test/res/password_tests.csv");
    this.passwordManager.clear();
  }

  @Test
  public void testPasswordSet() {
    assertFalse(this.passwordManager.checkLogin("user1", "test"));
    this.passwordManager.createLogin("user1", "test");
    assertTrue(this.passwordManager.checkLogin("user1", "test"));
    assertFalse(this.passwordManager.checkLogin("user2", "test123"));
    assertFalse(this.passwordManager.checkLogin("user1", "wrong"));
    this.passwordManager.createLogin("user2", "test123");
    assertTrue(this.passwordManager.checkLogin("user2", "test123"));
  }
}
