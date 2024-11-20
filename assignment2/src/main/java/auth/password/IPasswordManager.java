package auth.password;

public interface IPasswordManager {
  boolean checkLogin(String username, String password);

  boolean createLogin(String username, String password);

  void clear();
}