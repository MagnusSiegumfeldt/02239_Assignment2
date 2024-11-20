package auth.session;

import java.time.LocalDateTime;

public class SessionManager implements ISessionManager {
  String username;
  LocalDateTime sessionTime;
  LocalDateTime sessionExp;

  private final int sessionLength;

  public SessionManager(int sessionLength) {
    this.sessionLength = sessionLength;
  }

  @Override
  public void set(String username) {
    this.username = username;
    this.sessionTime = LocalDateTime.now();
    this.sessionExp = this.sessionTime.plusSeconds(this.sessionLength);
  }

  @Override
  public boolean checkSessionPeriod(String username) {
    if (username.equals(this.username) && this.sessionExp.compareTo(LocalDateTime.now()) > 0) {
      return true;
    }
    return false;
  }

  @Override
  public void unset() {
    this.username = null;
    this.sessionTime = null;
    this.sessionExp = null;
  }

  @Override
  public String getCurrentUser() {
    return username;
  }

}
