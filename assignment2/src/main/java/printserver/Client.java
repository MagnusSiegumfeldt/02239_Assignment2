package printserver;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.NotBoundException;

public class Client {
	public static void main(String[] args) throws NotBoundException, MalformedURLException, RemoteException {
		IPrintServant printServer = (IPrintServant) Naming.lookup("rmi://localhost:5099/printserver");
		

		printServer.start();
		System.out.println("Client exit.");
	}
}
