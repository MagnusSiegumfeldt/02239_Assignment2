import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

import roles.AccessControlListManager;

public class TestAccessControlList {
    private AccessControlListManager aclManager;
    
    @Before
    public void setUp() throws Exception {
        this.aclManager = new AccessControlListManager("./src/test/res/acl_tests.txt");
        
    }

    @Test
    public void testACL() {
        assertTrue(this.aclManager.check("erica", "print"));
        assertFalse(this.aclManager.check("erica", "topQueue"));
        assertTrue(this.aclManager.check("erica", "queue"));
        assertFalse(this.aclManager.check("erica", "addLogin"));
        
        assertFalse(this.aclManager.check("bob", "print"));
        assertTrue(this.aclManager.check("bob", "setConfig"));
        assertTrue(this.aclManager.check("bob", "readConfig"));
        assertFalse(this.aclManager.check("bob", "addLogin"));

        assertTrue(this.aclManager.check("alice", "addLogin"));
        assertTrue(this.aclManager.check("alice", "print"));
        assertTrue(this.aclManager.check("alice", "queue"));
        assertTrue(this.aclManager.check("alice", "topQueue"));
        assertTrue(this.aclManager.check("alice", "readConfig"));
    }
}
