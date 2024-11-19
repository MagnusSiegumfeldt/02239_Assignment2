package printserver;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.NotBoundException;

public class Client {
	public static void main(String[] args) throws NotBoundException, MalformedURLException, RemoteException {
		IPrintServant printServer = (IPrintServant) Naming.lookup("rmi://localhost:5099/printserver");
		
		// User[] users;

		//User jens = new User(janitor)

		/*

		User <- printAccess



		

		manager <- superUser + technician + addLogin
		technician <- start,stop, restart, inspect;(status,	setConfig, readConfig)
		
		
		manager <- technician, superuser, addLogin
		technician <- starter,configManager
		superUser <- basicUser, topQueue, restart
		basicUser <- print, queue
		
		starter <- start, stop, restart
		configManager <- status, setConfig, readConfig


		class user
		class powerUser extends user

		class statusAccess;
		class starter;
		class configManager extends statusAccess;


		class Janitor : User {
			Janitor(printserver) {
				this.printserver = printserver;
			}
			print() {
				printserver.print();
			}
			...
		}

		User jens = new Janitor(printserver);
		jens.print();
		
		printserver.print();
		
		printServer.start();*/
		//boolean loginSuccess = printServer.login("magnus", "test123");
		

		




	}
}
