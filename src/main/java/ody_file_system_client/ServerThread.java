package ody_file_system_client;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

import ody_file_system_server.Server;;

public class ServerThread extends Thread {
  public void run () {
    try {
      LocateRegistry.createRegistry(1099);
      Naming.rebind("FileSystem", new Server());
      System.out.println("Servidor Iniciado!");
    } catch (Exception e) {
      System.out.println("Erro no servidor: " + e);
    }
  }
}
