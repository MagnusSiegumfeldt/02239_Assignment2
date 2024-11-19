package printserver;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

import auth.PasswordManager;
import printer.PrinterOrchestrator;

public class PrintServant extends UnicastRemoteObject implements IPrintServant {
	PrinterOrchestrator orchestrator = new PrinterOrchestrator();
	HashMap<String, String> config = new HashMap<String, String>();

	boolean running;

	public PrintServant() throws RemoteException {
		super();
		this.running = false;

		try {
			System.out.println("Got this far");
			this.login();
		} catch (Exception e) {
		
		}
	}

	public boolean login() throws IOException {
		System.out.println("Got this far as well");

		PasswordManager pwManager = new PasswordManager();
		pwManager.checkLogin("Magnus", "Yay123");
		pwManager.checkLogin("Magnus", "Noo123");
		pwManager.checkLogin("magnus", "test123");
		//pwManager.createLogin("magnus", "test123");
		System.out.println("Got here btw");

		return true;
	}

	public void print(String filename, String printer) throws RemoteException {
		orchestrator.print(filename, printer);
	}

	public void queue(String printer) throws RemoteException {
		orchestrator.queue(printer);
	}

	public void topQueue(String printer, int job) throws RemoteException {
		orchestrator.topQueue(printer, job);
	}

	public void start() throws RemoteException {
		System.out.println("Server started.");
		
		this.running = true;
	}

	public void stop() throws RemoteException {
		System.out.println("Server stopped.");
		this.running = false;
	}

	public void restart() throws RemoteException {
		this.stop();
		this.orchestrator.clear();
		this.start();
	}

	public void status(String printer) throws RemoteException {
		System.out.println("Printer: \"" + printer + "\" is currently ..." + "VERY HAPPY");
	}

	public void readConfig(String parameter) throws RemoteException {
		String cfg = config.get(parameter);
		System.out.println(cfg != null ? cfg : "No config for " + parameter);
	}

	public void setConfig(String parameter, String value) throws RemoteException {
		config.put(parameter, value);
	}
}
