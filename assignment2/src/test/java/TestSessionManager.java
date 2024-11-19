import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

import session.SessionManager;

public class TestSessionManager {
    private SessionManager sessionManager;
    
    @Before
    public void setUp() throws Exception {
        this.sessionManager = new SessionManager(3);
    }

    @Test
    public void testSessionSet() {
        this.sessionManager.set("user1");
        assertTrue(this.sessionManager.check("user1"));
        assertFalse(this.sessionManager.check("user2"));

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {}
        assertFalse(this.sessionManager.check("user1"));
    }
}
