package ody_file_system_server;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public final class App {
    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(1099);
            Naming.rebind("FileSystem", new Server());
            System.out.println("Servidor Iniciado!");
        } catch (Exception e) {
            System.out.println("Erro no servidor: " + e);
        }
    }
}
