package ody_file_system_server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote { 

	public boolean addOtherServer(String serverIp, int serverPort) throws RemoteException;
	public boolean searchFile(String fileName) throws RemoteException;
	public byte[] getFile(String fileName) throws RemoteException;
     
}
