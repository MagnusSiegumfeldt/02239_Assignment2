package services.auth;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import auth.password.IPasswordManager;
import auth.session.ISessionManager;

public class AuthService extends UnicastRemoteObject implements IAuthService {
  private transient final IPasswordManager passwordManager;
  private transient final ISessionManager sessionManager;

  public AuthService(IPasswordManager passwordManager, ISessionManager sessionManager) throws RemoteException {
    this.passwordManager = passwordManager;
    this.sessionManager = sessionManager;
  }

  @Override
  public boolean login(String username, String password) throws RemoteException {
    if (passwordManager.checkLogin(username, password)) {
      sessionManager.set(username);
      return true;
    } else {
      return false;
    }
  }

  @Override
  public boolean logout(String username) throws RemoteException {
    if (sessionManager.getCurrentUser() != null && sessionManager.getCurrentUser().equals(username)) {
      sessionManager.unset();
      System.out.println("Logging out");
      return true;
    }
    return false;
  }

}
