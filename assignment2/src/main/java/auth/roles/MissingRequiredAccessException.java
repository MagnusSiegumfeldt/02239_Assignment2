package auth.roles;

import java.rmi.RemoteException;

public class MissingRequiredAccessException extends RemoteException {

  public MissingRequiredAccessException() {
    super();
  }

  public MissingRequiredAccessException(String message) {
    super(message);
  }
}