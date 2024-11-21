package auth.session;

public interface ISessionManager {
  boolean checkSessionValid(String token);
  
  String createSession(String username);

  void clearSession();

  String getCurrentUser();

}