package ody_file_system_client;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Scanner;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import ody_file_system_server.ServerInterface;

public class Client {
    // private String ip;
    // private ArrayList<String> fileList = new ArrayList<>();
    ServerInterface Server;

    public Client(String ipServer, int port) {
        try {
            Server = (ServerInterface) Naming.lookup("rmi://" + ipServer + ":" + port + "/FileSystem");
        } catch (MalformedURLException | RemoteException | NotBoundException e) {
			e.printStackTrace();
		}
    }

    public boolean searchFile(String fileName) {
        try {
            if (Server.searchFile(fileName)) {
                return true;
            } else {
                System.out.println("O servidor não possui o arquivo");
                return false;
            }
        } catch (Exception e) {
            System.out.println("Servidor falhou: " + e);
            return false;
        }
    }

    public boolean saveFile(String fileName, byte [] data) {
        File file = new File(fileName);
        // byte [] bytes = data.getBytes();
        try { 
            OutputStream os = new FileOutputStream(file); 
            os.write(data); 
            System.out.println("Arquivo salvo com sucesso!"); 
            os.close(); 
            return true;
        } catch (Exception e) { 
            System.out.println("Exception: " + e);
            return false;
        }
    }

    public void menu() {
        System.out.println("*-------------------------------------------*");
        System.out.println("buscar - para buscar um arquivo");
        System.out.println("ajuda - para imprimir esse menu");
        System.out.println("sair - para encerrar a aplicacao");
        System.out.println("*-------------------------------------------*");
    }

    public String readLine() {
        Scanner in = new Scanner(System.in);
        String cmd = in.nextLine();
        return cmd;
    }

    public void exec() {
        menu();
        while (true) {
            System.out.print("> ");
            String command = readLine();
            if (command.equals("buscar")) {
                System.out.println("Digite o nome do arquivo:");
                String fileName = readLine();
                if (searchFile(fileName)) {
                    System.out.println("Possui o arquivo");
                    try {
                        byte[] data = Server.getFile(fileName);
                        saveFile("teste" + fileName, data);
                    } catch (RemoteException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("Chora viado");
                }
            } else if (command.equals("ajuda")) {
                System.out.println("\n\n\n\n\n\n\n");
                menu();
            } else if (command.equals("sair")) {
                break;
            } else {
                System.out.println("Comando não reconhecido!");
            }
        }
    }
    
}