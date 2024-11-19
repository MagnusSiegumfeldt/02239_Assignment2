import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import auth.roles.AccessControlListManager;
import auth.roles.MissingRequiredAccessException;

public class TestAccessControlList {
  private AccessControlListManager aclManager;

  @Before
  public void setUp() throws Exception {
    this.aclManager = new AccessControlListManager("./src/test/res/acl_tests.txt");

  }

  @Test
  public void testACL() throws MissingRequiredAccessException {
    assertTrue(this.aclManager.check("erica", "print"));
    assertTrue(this.aclManager.check("erica", "queue"));

    assertTrue(this.aclManager.check("bob", "setConfig"));
    assertTrue(this.aclManager.check("bob", "readConfig"));

    assertTrue(this.aclManager.check("alice", "addLogin"));
    assertTrue(this.aclManager.check("alice", "print"));
    assertTrue(this.aclManager.check("alice", "queue"));
    assertTrue(this.aclManager.check("alice", "topQueue"));
    assertTrue(this.aclManager.check("alice", "readConfig"));
  }

  @Test(expected = MissingRequiredAccessException.class)
  public void testAccessInvalidException() throws MissingRequiredAccessException {
    this.aclManager.check("erica", "topQueue");
  }

  @Test(expected = MissingRequiredAccessException.class)
  public void testAccessInvalidException2() throws MissingRequiredAccessException {
    this.aclManager.check("erica", "addLogin");
  }

  @Test(expected = MissingRequiredAccessException.class)
  public void testAccessInvalidException3() throws MissingRequiredAccessException {
    this.aclManager.check("bob", "print");
  }

  @Test(expected = MissingRequiredAccessException.class)
  public void testAccessInvalidException4() throws MissingRequiredAccessException {
    this.aclManager.check("bob", "addLogin");
  }
}
