package services.auth;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IAuthService extends Remote {

  public boolean login(String username, String password) throws RemoteException;

  public boolean logout(String username) throws RemoteException;

}