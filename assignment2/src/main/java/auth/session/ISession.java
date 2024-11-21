package auth.session;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ISession<T> extends Remote   {
  public T session(String token) throws RemoteException ;
}


// Usage

// ISession<PrintService>
// which would add the session method to the interface