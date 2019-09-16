package ody_file_system_server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote { 
	public boolean addOtherServerOnList(String serverIp, int serverPort) throws RemoteException;
	public boolean addOtherServer(String serverIp, int serverPort) throws RemoteException;
	public boolean removeServerOnList(String serverIp, int serverPort) throws RemoteException;
	public boolean removeServer(String serverIp, int serverPort) throws RemoteException;
	public String checkFile(String fileName, String clientIp) throws RemoteException;
	public String searchFile(String fileName, String clientIp) throws RemoteException;
	public byte[] getFile(String fileName) throws RemoteException;
}
