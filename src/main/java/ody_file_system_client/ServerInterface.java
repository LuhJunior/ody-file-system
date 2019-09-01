package ody_file_system_client;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote { 

	public boolean searchFile(String fileName) throws RemoteException;
     
}
