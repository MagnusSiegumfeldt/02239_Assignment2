package auth.session;

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

  public boolean check(String username) {
    if (username.equals(this.username) && this.sessionExp.compareTo(LocalDateTime.now()) > 0) {
      return true;
    }
    return false;
  }

  public void unset() {

  }

  public String getCurrentUser() {
    return username;
  }

}
