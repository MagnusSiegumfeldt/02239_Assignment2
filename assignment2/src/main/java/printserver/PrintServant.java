package printserver;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class PrintServant extends UnicastRemoteObject implements IPrintServant {
    public PrintServant() throws RemoteException {
        super();
    }

    public void start() throws RemoteException {
        System.out.println("Server started.");
    }
}
