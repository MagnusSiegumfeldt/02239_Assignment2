package printserver;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

import auth.PasswordManager;
import printer.PrinterManager;
import session.SessionManager;
import accesscontrol.*;

public class PrintServant extends UnicastRemoteObject implements IPrintServant {
	PrinterManager printerManager = new PrinterManager();
	HashMap<String, String> config = new HashMap<String, String>();

	boolean running;
	PasswordManager passManager;
	SessionManager sessManager;
	IAccessControl accessControl;

	public PrintServant() throws RemoteException {
		this("password.csv", "rbac.txt");
	}
	public PrintServant(String passwordFile, String accessControlFile) throws RemoteException {
		super();
		this.running = false;
		this.passManager = new PasswordManager(passwordFile);
		this.sessManager = new SessionManager(1);
		// this.accessControl = new AccessControlListManager(accessControlFile);
		this.accessControl = new RoleBasedAccessControlManager(accessControlFile);
	}

	private boolean authorize(String operation) {
		if (!this.sessManager.active()) {
			System.err.println("Please login first.");
			return false;
		}
		if (!this.accessControl.check(this.sessManager.user(), operation)) {
			System.err.println("Access denied.");
			return false;
		} 
		return true;
	}

	public boolean addLogin(String username, String password) throws RemoteException {
		if (!authorize("addLogin")) {
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
	public boolean logout(String username) throws RemoteException {
		if (this.sessManager.active()) {
			this.sessManager.unset();
			System.out.println("Logged out.");
		} else {
			System.out.println("Could not logout.");
		}
		return true;
	}

	public boolean print(String filename, String printer) throws RemoteException {
		if (!authorize("print")) {
			return false;
		}
		printerManager.print(filename, printer);
		return true;
	}

	public boolean queue(String printer) throws RemoteException {
		if (!authorize("queue")) {
			return false;
		}
		printerManager.queue(printer);
		return true;
	}

	public boolean topQueue(String printer, int job) throws RemoteException {
		if (!authorize("topQueue")) {
			return false;
		}
		printerManager.topQueue(printer, job);
		return true;
	}

	public boolean start() throws RemoteException {
		if (!authorize("start")) {
			return false;
		}
		System.out.println("Server started.");
		this.running = true;
		return true;
	}

	public boolean stop() throws RemoteException {
		if (!authorize("stop")) {
			return false;
		}
		System.out.println("Server stopped.");
		this.running = false;
		return true;
	}

	public boolean restart() throws RemoteException {
		if (!authorize("restart")) {
			return false;
		}
		this.stop();
		this.printerManager.clear();
		this.start();
		return true;
	}

	public boolean status(String printer) throws RemoteException {
		if (!authorize("status")) {
			return false;
		}
		System.out.println("Printer: \"" + printer + "\" is currently ...");
		return true;
	}

	public boolean readConfig(String parameter) throws RemoteException {
		if (!authorize("readConfig")) {
			return false;
		}
		String cfg = config.get(parameter);
		System.out.println(cfg != null ? cfg : "No config for " + parameter);
		return true;
	}

	public boolean setConfig(String parameter, String value) throws RemoteException {
		if (!authorize("setConfig")) {
			return false;
		}
		config.put(parameter, value);
		return true;
	}
}
