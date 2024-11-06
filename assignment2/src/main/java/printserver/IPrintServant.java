package printserver;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IPrintServant extends Remote {
    /*public void print(String filename, String printer);
    public void queue(String printer);
    public void topQueue(String printer, int job);*/
    public void start() throws RemoteException;
    /*public void stop();
    public void restart();
    public void status();
    public void readConfig(String parameter);
    public void setConfig(String parameter, String value);*/
}
