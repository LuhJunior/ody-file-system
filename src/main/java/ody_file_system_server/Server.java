package ody_file_system_server;
import java.util.ArrayList;
import java.io.File;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Server extends UnicastRemoteObject implements ServerInterface {
    private static final long serialVersionUID = 1L;
    ArrayList<ServerInfo> existingServers = new ArrayList<>();
    ArrayList<String> fileList = new ArrayList<>();

    public Server() throws RemoteException {
    }

    public boolean addOtherServerOnList(String serverIp, int serverPort) throws RemoteException {
        return existingServers.add(new ServerInfo(serverIp, serverPort));
    }
    
    public boolean addOtherServer(String serverIp, int serverPort) throws RemoteException {
        for (ServerInfo otherServerInfo : existingServers) {
            try {
                ServerInterface otherServer = (ServerInterface) Naming.lookup("rmi://" + otherServerInfo.getServerIp()
                + ":" + otherServerInfo.getServerPort() + "/FileSystem");
                otherServer.addOtherServerOnList(serverIp, serverPort);
            } catch (MalformedURLException | NotBoundException e) {
                System.out.println("Erro brabo: " + e);
                return false;
            }
        }
        return true;
    }

    public String searchFile(String fileName) throws RemoteException {
        File file = new File("files/" + fileName);
        if (file.exists()) {
            try {
                return InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                e.printStackTrace();
                return "";
            }
        } else {
            for (ServerInfo otherServerInfo : existingServers) {
                try {
                    ServerInterface otherServer = (ServerInterface) Naming
                            .lookup("rmi://" + otherServerInfo.getServerIp() + ":"
                            + otherServerInfo.getServerPort() + "/FileSystem");
                    if (!otherServer.searchFile(fileName).equals("")) {
                        try {
                            return InetAddress.getLocalHost().getHostAddress();
                        } catch (UnknownHostException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (MalformedURLException | NotBoundException e) {
                    e.printStackTrace();
                    return "";
				}
            }
            return "";
        }
    }

    public byte[] getFile(String fileName) throws RemoteException {
        try {
            Path path = Paths.get("files/" + fileName);
            byte[] data = Files.readAllBytes(path);
            return data;
        } catch (Exception e) {
            System.out.println("Erro Brabo: " + e);
            return null;
        }
    }
}
