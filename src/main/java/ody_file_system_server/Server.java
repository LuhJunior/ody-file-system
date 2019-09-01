package ody_file_system_server;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class Server extends UnicastRemoteObject implements ServerInterface {
    private static final long serialVersionUID = 1L;
    // String ip, clientIp;
    // String msg;
    String root  = "C://Users/yanga/Desktop/files/";
    ArrayList<String> existingServers = new ArrayList<>();
    ArrayList<String> fileList = new ArrayList<>();
    public Server() throws RemoteException {
        super();
        fileList.add("minhapica.txt");
    }
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
        try{
            listOfFiles.add(root+path);
        }catch (Throwable e){
            System.out.println("Não foi possível adcionar a lista\n"+e);
        }
    }
 */
    public boolean searchFile(String name) throws RemoteException {
        // name = root+name;
        File file = new File(name);
        return file.exists();
        /* for (int i = 0; i < fileList.size(); i++) {
            if (fileList.get(i).equals(name)) return true;
        }

        System.out.println("O arquivo não existe");
        return false; */
    }

    public byte[] getFile(String fileName) throws RemoteException {
        try {
            Path path = Paths.get(fileName);
            byte[] data = Files.readAllBytes(path);
            return data;
        } catch (Exception e) {
            System.out.println("Erro Brabo: " + e);
            return null;
        }
    }
}
