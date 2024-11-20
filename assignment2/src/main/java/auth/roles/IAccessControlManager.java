package auth.roles;

public interface IAccessControlManager {
  public boolean check(String username, String resource) throws MissingRequiredAccessException;
}
