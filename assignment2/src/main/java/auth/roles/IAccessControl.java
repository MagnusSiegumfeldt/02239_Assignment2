package auth.roles;

public interface IAccessControl {
  public boolean check(String username, String resource) throws MissingRequiredAccessException;
}
