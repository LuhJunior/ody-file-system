package ody_file_system_server;
import java.util.ArrayList;
import java.io.File;
import java.net.MalformedURLException;
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
    public Server() throws RemoteException { }
    /* public Server(String msg) throws RemoteException {
        this.msg = msg;
    } 

    public String getMsg() {
        return msg;
    }*/

    /* public void setIp(String ip) {
        this.ip = ip;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    } */

    /* public ArrayList<String> getExistingServers() {
        return existingServers;
    }

    public void setExistingServers(ArrayList<String> existingServers) {
        this.existingServers = existingServers;
    }

    public ArrayList<String> getListOfFiles() {
        return listOfFiles;
    }

    public void setListOfFiles(ArrayList<String> listOfFiles) {
        this.listOfFiles = listOfFiles;
    }
    public static void reader(String path) throws IOException {
        BufferedReader buffRead = new BufferedReader(new FileReader(path));
        String line = "";
        while (true) {
            if (line != null) {
                System.out.println(line);

            } else
                break;
            line = buffRead.readLine();
        }
        buffRead.close();
    }

    public static void writer(String path) throws IOException {
        BufferedWriter buffWrite = new BufferedWriter(new FileWriter(path));
        String line = "";
        Scanner in = new Scanner(System.in);
        System.out.println("Escreva algo: ");
        line = in.nextLine();
        buffWrite.append(line + "\n");
        buffWrite.close();
    }
    public void addFileToList(String path){
        System.out.println(root+path);
        try {
            listOfFiles.add(root+path);
        } catch (Throwable e) {
            System.out.println("Não foi possível adcionar a lista\n"+e);
        }
    }
 */
    public boolean addOtherServer(String serverIp, int serverPort) throws RemoteException {
        return existingServers.add(new ServerInfo(serverIp, serverPort));
    }

    public boolean searchFile(String fileName) throws RemoteException {
        File file = new File("files/" + fileName);
        if (file.exists()) {
            return true;
        } else {
            for (ServerInfo otherServerInfo : existingServers) {
                try {
                    ServerInterface otherServer = (ServerInterface) Naming
                            .lookup("rmi://" + otherServerInfo.getServerIp() + ":"
                            + otherServerInfo.getServerPort() + "/FileSystem");
                    if (otherServer.searchFile(fileName)) {
                        return true;
                    }
                } catch (MalformedURLException | NotBoundException e) {
					e.printStackTrace();
				}
            }
            return false;
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
