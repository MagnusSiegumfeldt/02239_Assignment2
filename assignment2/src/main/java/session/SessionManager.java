package session;

import java.time.LocalDateTime; 

public class SessionManager {
    String username;
    LocalDateTime sessionTime;
    LocalDateTime sessionExp;

    int sessionLength;

    public SessionManager(int sessionLength) {
        this.sessionLength = sessionLength;
    }

    public void set(String username) {
        this.username = username;
        this.sessionTime = LocalDateTime.now();
        this.sessionExp = this.sessionTime.plusSeconds(this.sessionLength);
    }

    public boolean active() {
        if (this.sessionExp.compareTo(LocalDateTime.now()) > 0) {
            return true;
        }
        return false;
    }

    public String user() {
        return username;
    }

    public void unset() {

    }
}
