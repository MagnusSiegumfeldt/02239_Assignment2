package printserver;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IPrintServant extends Remote {

	public boolean addLogin(String username, String password) throws RemoteException;
	
	public boolean login(String username, String password) throws RemoteException;
	
	public void logout(String username) throws RemoteException;

	public void print(String filename, String printer) throws RemoteException;

	public void queue(String printer) throws RemoteException;

	public void topQueue(String printer, int job) throws RemoteException;

	public void start() throws RemoteException;

	public void stop() throws RemoteException;

	public void restart() throws RemoteException;

	public void status(String printer) throws RemoteException;

	public void readConfig(String parameter) throws RemoteException;

	public void setConfig(String parameter, String value) throws RemoteException;
}
