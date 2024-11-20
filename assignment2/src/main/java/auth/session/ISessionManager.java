package auth.session;

public interface ISessionManager {
  void set(String username);

  boolean checkSessionPeriod(String username);

  void unset();

  String getCurrentUser();

}