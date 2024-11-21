package services.auth;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IAuthService extends Remote {

  public String login(String username, String password) throws RemoteException;

  public void logout(String username) throws RemoteException;

}
