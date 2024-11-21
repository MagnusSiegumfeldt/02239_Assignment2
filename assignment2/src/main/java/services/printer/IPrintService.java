package services.printer;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IPrintService extends Remote {
  public void print(String token, String filename, String printer) throws RemoteException;

  public void queue(String token, String printer) throws RemoteException;

  public void topQueue(String token, String printer, int job) throws RemoteException;

  public void start(String token) throws RemoteException;

  public void stop(String token) throws RemoteException;

  public void restart(String token) throws RemoteException;

  public void status(String token, String printer) throws RemoteException;

  public void readConfig(String token, String parameter) throws RemoteException;

  public void setConfig(String token, String parameter, String value) throws RemoteException;
}
