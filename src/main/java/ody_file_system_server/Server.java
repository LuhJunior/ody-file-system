package ody_file_system_server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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
  // ArrayList<String> fileList = new ArrayList<>();
  Map<String, ArrayList<String>> permissions = new HashMap<>();

  public Server() throws RemoteException {
    loadFilePermissions();
  }

  public Map<String, ArrayList<String>> getPermissions() {
    return permissions;
  }

  public void setPermissions(Map<String, ArrayList<String>> permissions) {
    this.permissions = permissions;
  }

  public void loadFilePermissions() {
    File file = new File("config/permissions.txt");
    if (!file.exists()) {
      try {
        file.createNewFile();
      } catch (IOException e) {
        System.out.println("Erro brabo: " + e);
        e.printStackTrace();
      }
    } else {
      try {
        Scanner buffer = new Scanner(file);
        while (buffer.hasNextLine()) {
          String line = buffer.nextLine();
          String[] filePermissions = line.split(" ");
          String fileName = filePermissions[0];
          ArrayList<String> authorizedIps = new ArrayList<>();
          for (int i = 1; i < filePermissions.length; i++) {
            authorizedIps.add(filePermissions[i]);
          }
          this.permissions.put(fileName, authorizedIps);
        }
        buffer.close();
      } catch (FileNotFoundException e) {
        System.out.println("Erro brabo: " + e);
        e.printStackTrace();
      }
    }
  }

  public boolean addOtherServerOnList(String serverIp, int serverPort) throws RemoteException {
    return existingServers.add(new ServerInfo(serverIp, serverPort));
  }
  
  public boolean addOtherServer(String serverIp, int serverPort) throws RemoteException {
    if (!existingServers.contains(new ServerInfo(serverIp, serverPort))) {
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
    } else {
      System.out.println("O cliente já está cadastrado na rede");
    }
    return true;
  }

  public boolean removeServerOnList(String serverIp, int serverPort) throws RemoteException {
    return existingServers.remove(new ServerInfo(serverIp, serverPort));
  }
  
  public boolean removeServer(String serverIp, int serverPort) throws RemoteException {
    if (!existingServers.contains(new ServerInfo(serverIp, serverPort))) {
      for (ServerInfo otherServerInfo : existingServers) {
        try {
          ServerInterface otherServer = (ServerInterface) Naming.lookup("rmi://" + otherServerInfo.getServerIp()
          + ":" + otherServerInfo.getServerPort() + "/FileSystem");
          otherServer.removeServerOnList(serverIp, serverPort);
        } catch (MalformedURLException | NotBoundException e) {
          System.out.println("Erro brabo: " + e);
          return false;
        }
      }
    } else {
      System.out.println("O cliente não está na rede");
    }
    return true;
  }

  public String checkFile(String fileName, String clientIp) throws RemoteException {
    File file = new File("files/" + fileName);
    if (file.exists()) {
      try {
        ArrayList<String> authIps = this.permissions.get(fileName);
        if (authIps == null) {
          return InetAddress.getLocalHost().getHostAddress();
        } else if (authIps.contains(clientIp)) {
          return InetAddress.getLocalHost().getHostAddress();
        } else {
          // System.out.println("Cliente não autorizado");
          throw new RemoteException("Cliente não autorizado", new Throwable("Cliente não autorizado"));
        }
      } catch (UnknownHostException e) {
        // e.printStackTrace();
        return "";
      }
    }
    return "";
  }

  public String searchFile(String fileName, String clientIp) throws RemoteException {
    String serverFileIp = checkFile(fileName, clientIp);
    if (serverFileIp.equals("")) {
      for (ServerInfo otherServerInfo : existingServers) {
        try {
          ServerInterface otherServer = (ServerInterface) Naming
            .lookup("rmi://" + otherServerInfo.getServerIp() + ":"
            + otherServerInfo.getServerPort() + "/FileSystem");
          if (!otherServer.checkFile(fileName, clientIp).equals("")) {
            return otherServerInfo.getServerIp();
          }
        } catch (MalformedURLException | NotBoundException e) {
          e.printStackTrace();
          return "";
        }
      }
    }
    return serverFileIp;
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
