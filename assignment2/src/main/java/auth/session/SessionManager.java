package auth.session;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

import org.mindrot.jbcrypt.BCrypt;

public class SessionManager implements ISessionManager {
  String username;
  LocalDateTime sessionTime;
  LocalDateTime sessionExp;
  String sessionToken;

  private final int sessionLength;

  public SessionManager(int sessionLength) {
    this.sessionLength = sessionLength;
  }

  @Override
  public boolean checkSessionValid(String token) {
    if (!token.equals(sessionToken)) {
      return false;
    }

    if (this.sessionExp.compareTo(LocalDateTime.now()) > 0) {
      return true;
    }

    this.clearSession();
    return false;
  }

  @Override
  public String createSession(String username) {
    this.username = username;
    this.sessionTime = LocalDateTime.now();
    this.sessionExp = this.sessionTime.plusSeconds(this.sessionLength);
    this.sessionToken = hashToken(generateRandomToken());
    return this.sessionToken;
  }

  @Override
  public void clearSession() {
    this.username = null;
    this.sessionTime = null;
    this.sessionExp = null;
    this.sessionToken = null;
  }

  @Override
  public String getCurrentUser() {
    return username;
  }

  private static String generateRandomToken() {
    SecureRandom secureRandom = new SecureRandom();
    byte[] tokenBytes = new byte[24];
    secureRandom.nextBytes(tokenBytes);
    return Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes);
  }

  public static String hashToken(String token) {
    return BCrypt.hashpw(token, BCrypt.gensalt());
  }

}
