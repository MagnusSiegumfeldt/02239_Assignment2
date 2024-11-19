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
