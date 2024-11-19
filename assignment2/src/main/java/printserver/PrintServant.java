package printserver;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

import auth.PasswordManager;
import printer.PrinterManager;
import roles.AccessControlListManager;
import roles.IAccessControl;
import session.SessionManager;

public class PrintServant extends UnicastRemoteObject implements IPrintServant {
	PrinterManager printerManager = new PrinterManager();
	HashMap<String, String> config = new HashMap<String, String>();

	boolean running;
	PasswordManager passManager;
	SessionManager sessManager;
	IAccessControl accessControl;

	public PrintServant() throws RemoteException {
		this("password.csv", "acl.txt");
	}
	public PrintServant(String passwordFile, String aclFile) throws RemoteException {
		super();
		this.running = false;
		this.passManager = new PasswordManager(passwordFile);
		this.sessManager = new SessionManager(1);
		this.accessControl = new AccessControlListManager(aclFile);
	}

	public boolean addLogin(String username, String password) throws RemoteException{
		if (!this.accessControl.check(this.sessManager.getCurrentUser(), "addLogin")) {
			System.err.println("Access denied.");
			return false;
		} 
		return this.passManager.createLogin(username, password);
	}

	public boolean login(String username, String password) throws RemoteException {
		boolean loginSuccess = this.passManager.checkLogin(username, password);
		if (loginSuccess) {
			this.sessManager.set(username);
		}
		return loginSuccess;
	}
	public void logout(String username) throws RemoteException{
		if (this.sessManager.check(username)) {
			this.sessManager.unset();
			System.out.println("Logged out.");
		} else {
			System.out.println("Could not logout.");
		}
	}

	public void print(String filename, String printer) throws RemoteException {
		if (!this.accessControl.check(this.sessManager.getCurrentUser(), "print")) {
			System.err.println("Access denied.");
			return;
		} 
		printerManager.print(filename, printer);
		
	}

	public void queue(String printer) throws RemoteException {
		if (!this.accessControl.check(this.sessManager.getCurrentUser(), "queue")) {
			System.err.println("Access denied.");
			return;
		} 
		printerManager.queue(printer);
	}

	public void topQueue(String printer, int job) throws RemoteException {
		if (!this.accessControl.check(this.sessManager.getCurrentUser(), "topQueue")) {
			System.err.println("Access denied.");
			return;
		} 
		printerManager.topQueue(printer, job);
	}

	public void start() throws RemoteException {
		if (!this.accessControl.check(this.sessManager.getCurrentUser(), "print")) {
			System.err.println("Access denied.");
			return;
		} 
		System.out.println("Server started.");
		this.running = true;
	}

	public void stop() throws RemoteException {
		if (!this.accessControl.check(this.sessManager.getCurrentUser(), "print")) {
			System.err.println("Access denied.");
			return;
		} 
		System.out.println("Server stopped.");
		this.running = false;
	}

	public void restart() throws RemoteException {
		if (!this.accessControl.check(this.sessManager.getCurrentUser(), "print")) {
			System.err.println("Access denied.");
			return;
		} 
		this.stop();
		this.printerManager.clear();
		this.start();
	}

	public void status(String printer) throws RemoteException {
		if (!this.accessControl.check(this.sessManager.getCurrentUser(), "print")) {
			System.err.println("Access denied.");
			return;
		} 
		System.out.println("Printer: \"" + printer + "\" is currently ..." + "VERY HAPPY");
	}

	public void readConfig(String parameter) throws RemoteException {
		if (!this.accessControl.check(this.sessManager.getCurrentUser(), "print")) {
			System.err.println("Access denied.");
			return;
		} 
		String cfg = config.get(parameter);
		System.out.println(cfg != null ? cfg : "No config for " + parameter);
	}

	public void setConfig(String parameter, String value) throws RemoteException {
		if (!this.accessControl.check(this.sessManager.getCurrentUser(), "print")) {
			System.err.println("Access denied.");
			return;
		} 
		config.put(parameter, value);
	}
}
