 package auth.session;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Session<T> extends UnicastRemoteObject implements ISession<T>  {
  private T service;
  
  public Session(T service) throws RemoteException {
      this.service = service;
  }
  
  public T session(String token) throws RemoteException {
    System.out.println("HERE");
    return this.service;
  }

}