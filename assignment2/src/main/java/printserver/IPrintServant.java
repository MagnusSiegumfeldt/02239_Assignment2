package printserver;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IPrintServant extends Remote {

	public boolean addLogin(String username, String password) throws RemoteException;
	
	public boolean login(String username, String password) throws RemoteException;
	
	public boolean logout(String username) throws RemoteException;

	public boolean print(String filename, String printer) throws RemoteException;

	public boolean queue(String printer) throws RemoteException;

	public boolean topQueue(String printer, int job) throws RemoteException;

	public boolean start() throws RemoteException;

	public boolean stop() throws RemoteException;

	public boolean restart() throws RemoteException;

	public boolean status(String printer) throws RemoteException;

	public boolean readConfig(String parameter) throws RemoteException;

	public boolean setConfig(String parameter, String value) throws RemoteException;
}
