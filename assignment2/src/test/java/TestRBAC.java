import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import auth.roles.MissingRequiredAccessException;
import auth.roles.RoleBasedAccessControlManager;

public class TestRBAC {
  private RoleBasedAccessControlManager manager;

  @Before
  public void setUp() throws Exception {
    this.manager = new RoleBasedAccessControlManager(
        "./src/test/res/rbac/roles.txt",
        "./src/test/res/rbac/hierarchy.txt",
        "./src/test/res/rbac/user_roles.txt",
        "./src/test/res/rbac/permissions.txt");
  }

  @Test
  public void testRBACSuccess() throws MissingRequiredAccessException {
    // Manager Permissions
    assertTrue(this.manager.check("alice", "addLogin"));

    // Technician Permissions (inherited by Manager)
    assertTrue(this.manager.check("alice", "start"));
    assertTrue(this.manager.check("bob", "stop"));
    assertTrue(this.manager.check("bob", "restart"));
    assertTrue(this.manager.check("alice", "status"));
    assertTrue(this.manager.check("bob", "setConfig"));
    assertTrue(this.manager.check("alice", "readConfig"));

    // PowerUser Permissions (inherited by Manager)
    assertTrue(this.manager.check("cecilia", "topQueue"));
    assertTrue(this.manager.check("alice", "restart"));
    assertTrue(this.manager.check("cecilia", "restart"));

    // BasicUser Permissions (inherited by PowerUser and Manager)
    assertTrue(this.manager.check("cecilia", "print"));
    assertTrue(this.manager.check("cecilia", "queue"));
    assertTrue(this.manager.check("alice", "print"));
    assertTrue(this.manager.check("alice", "queue"));

    // Direct BasicUser Test
    assertTrue(this.manager.check("erica", "print"));
    assertTrue(this.manager.check("david", "queue"));
  }

  @Test(expected = MissingRequiredAccessException.class)
  public void testAccessInvalidException_erica_topQueue() throws MissingRequiredAccessException {
    this.manager.check("erica", "topQueue");
  }

  @Test(expected = MissingRequiredAccessException.class)
  public void testAccessInvalidException_david_restart() throws MissingRequiredAccessException {
    this.manager.check("david", "restart");
  }

  @Test(expected = MissingRequiredAccessException.class)
  public void testAccessInvalidException_cecilia_addLogin() throws MissingRequiredAccessException {
    this.manager.check("cecilia", "addLogin");
  }

  @Test(expected = MissingRequiredAccessException.class)
  public void testAccessInvalidException_bob_topQueue() throws MissingRequiredAccessException {
    this.manager.check("bob", "topQueue");
  }

  @Test(expected = MissingRequiredAccessException.class)
  public void testAccessInvalidException_george_setConfig() throws MissingRequiredAccessException {
    this.manager.check("george", "setConfig");
  }

  @Test(expected = MissingRequiredAccessException.class)
  public void testAccessInvalidException_erica_start() throws MissingRequiredAccessException {
    this.manager.check("erica", "start");
  }

  @Test(expected = MissingRequiredAccessException.class)
  public void testAccessInvalidException_david_addLogin() throws MissingRequiredAccessException {
    this.manager.check("david", "addLogin");
  }

  @Test(expected = MissingRequiredAccessException.class)
  public void testAccessInvalidException_george_readConfig() throws MissingRequiredAccessException {
    this.manager.check("george", "readConfig");
  }
}
