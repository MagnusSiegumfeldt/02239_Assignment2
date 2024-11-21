package services.auth;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import auth.password.IPasswordManager;
import auth.session.ISessionManager;

public class AuthService extends UnicastRemoteObject implements IAuthService {
  private final transient IPasswordManager passwordManager;
  private final transient ISessionManager sessionManager;

  public AuthService(IPasswordManager passwordManager, ISessionManager sessionManager) throws RemoteException {
    this.passwordManager = passwordManager;
    this.sessionManager = sessionManager;
  }

  @Override
  public String login(String username, String password) throws RemoteException {
    if(sessionManager.getCurrentUser() != null) {
      throw new RemoteException("Another user is already logged in");
    }

    if (passwordManager.checkLogin(username, password)) {
      return sessionManager.createSession(username);
    } else {
      throw new RemoteException("Password or username is incorrect");
    }
  }

  @Override
  public boolean logout(String username) throws RemoteException {
    if (sessionManager.getCurrentUser() != null && sessionManager.getCurrentUser().equals(username)) {
      sessionManager.clearSession();
      System.out.println("Logging out");
      return true;
    }
    return false;
  }

}
