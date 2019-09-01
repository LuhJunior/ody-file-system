package ody_file_system_client;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.Scanner;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import ody_file_system_server.ServerInterface;

public class Client {
    private String clientIp, serverIp;
    private int clientPort, serverPort;
    
    public Client() {
        this.clientIp = "";
        this.clientPort = 1099;
        this.serverIp = "";
        this.serverPort = 1099;
    }

    public Client(String clientIp, int clientPort) {
        this.clientIp = clientIp;
        this.clientPort = clientPort;
        this.serverIp = "";
        this.serverPort = 1099;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public int getClientPort() {
        return clientPort;
    }

    public void setClientPort(int clientPort) {
        this.clientPort = clientPort;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    
    public void readServerIp() {
        System.out.println("Digite o ip do servidor:");
        String serverIp = readLine();
        setServerIp(serverIp);
    }
    
    public void readServerPort() {
        System.out.println("Digite a porta do servidor:");
        int serverPort = Integer.parseInt(readLine());
        setServerPort(serverPort);
    }
    
    public void verifyEmptyServerIp() {
        while (serverIp.equals("")) {
            readServerIp();
            readServerPort();
        }
    }

    public boolean searchFile(ServerInterface Server, String fileName) {
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

    public boolean addInServerList(ServerInterface Server) {
        try {
            return Server.addOtherServer(clientIp, clientPort);
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean saveFile(String fileName, byte [] data) {
        File file = new File(fileName);
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
        System.out.println("$---------------------------------------------------$");
        System.out.println("buscar - para buscar um arquivo");
        System.out.println("serverip - para definir o ip do servidor");
        System.out.println("serverport - para definir a porta do servidor");
        System.out.println("servermode - para entrat na rede distribuída");
        System.out.println("ajuda - para imprimir esse menu");
        System.out.println("sair - para encerrar a aplicacao");
        System.out.println("$---------------------------------------------------$");
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
                verifyEmptyServerIp();
                try {
                    ServerInterface Server;
                    Server = (ServerInterface) Naming.lookup("rmi://" + serverIp + ":" + serverPort + "/FileSystem");
                    System.out.println("Digite o nome do arquivo:");
                    String fileName = readLine();
                    if (searchFile(Server, fileName)) {
                        System.out.println("Possui o arquivo");
                        try {
                            byte[] data = Server.getFile(fileName);
                            saveFile("teste" + fileName, data);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    } else {
                        System.out.println("Chora viado");
                    }
                } catch (MalformedURLException | RemoteException | NotBoundException e1) {
                    e1.printStackTrace();
                }
            } else if (command.equals("serverip")) {
                readServerIp();
            } else if (command.equals("serverport")) {
                readServerPort();
            } else if (command.equals("servermode")) {
                verifyEmptyServerIp();
                try {
                    ServerInterface Server;
                    Server = (ServerInterface) Naming.lookup("rmi://" + serverIp + ":" + serverPort + "/FileSystem");
                    if (addInServerList(Server)) {
                        System.out.println("Adicionado na lista de servidores");
                    } else {
                        System.out.println("Chora viado");
                    }
                } catch (MalformedURLException | RemoteException | NotBoundException e1) {
                    e1.printStackTrace();
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