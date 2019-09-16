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
    private Thread Server;
    
    public Client() {
        this.clientIp = "";
        this.clientPort = 1099;
        this.serverIp = "";
        this.serverPort = 1099;
        this.Server = new ServerThread();
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

    public Thread getServer() {
        return Server;
    }

    public void setServer(Thread server) {
        Server = server;
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
            // readServerPort();
        }
    }

    public String searchFile(ServerInterface Server, String fileName) {
        try {
            String serverIp = Server.searchFile(fileName, getClientIp());
            if (serverIp.equals("")) {
                System.out.println("O servidor não possui o arquivo");
            }
            return serverIp;
        } catch (Exception e) {
            System.out.println("Cliente Não Autorizado");
            // System.out.println("Ocorreu um erro com servidor: " + e);
            return "";
        }
    }

    public boolean addInServerList(ServerInterface Server) {
        try {
            return (Server.addOtherServer(clientIp, clientPort) &&
                Server.addOtherServerOnList(clientIp, clientPort));
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
        System.out.println("servermode - para entrar na rede distribuída");
        System.out.println("servermodeoff - para sair da rede distribuída");
        System.out.println("ajuda - para imprimir esse menu");
        System.out.println("sair - para encerrar a aplicacao");
        System.out.println("$---------------------------------------------------$");
    }

    public String readLine() {
        Scanner in = new Scanner(System.in);
        return  in.nextLine();
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
                    String fileServerIp = searchFile(Server, fileName);
                    if (!fileServerIp.equals("")) {
                        System.out.println("Possui o arquivo");
                        try {
                            if (fileServerIp == serverIp) {
                                byte[] data = Server.getFile(fileName);
                                saveFile("teste" + fileName, data);
                            } else {
                                Server = (ServerInterface) Naming.lookup("rmi://" + serverIp + ":" + serverPort + "/FileSystem");
                                byte[] data = Server.getFile(fileName);
                                saveFile("teste" + fileName, data);
                            }
                        } catch (RemoteException e) {
                            System.out.println(e.getMessage());
                            // e.printStackTrace();
                        }
                    } else {
                        System.out.println("Não foi possível baxar o arquivo");
                    }
                } catch (MalformedURLException | RemoteException | NotBoundException e1) {
                    e1.printStackTrace();
                }
            } else if (command.equals("serverip")) {
                readServerIp();
            } else if (command.equals("serverport")) {
                readServerPort();
            } else if (command.equals("servermode")) {
                if (!this.Server.isAlive()) {
                    try {
                        verifyEmptyServerIp();
                        ServerInterface Server;
                        Server = (ServerInterface) Naming.lookup("rmi://" + serverIp + ":" + serverPort + "/FileSystem");
                        if (addInServerList(Server)) {
                            System.out.println("Adicionado na lista de servidores");
                            this.Server.start();
                            System.out.println("Thread do servidor iniciada");
                        } else {
                            System.out.println("Não foi possível adicionar a lista de servidores");
                        }
                    } catch (MalformedURLException | RemoteException | NotBoundException e1) {
                        System.out.println("Ocorreu um erro com o servidor");
                        e1.printStackTrace();
                    }
                } else {
                    System.out.println("O seu servidor já está ligado");
                }
            } else if (command.equals("servermodeoff")) {
                if (this.Server.isAlive()) {
                    this.Server.interrupt();
                } else {
                    System.out.println("O seu servidor já está desligado");
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