package ody_file_system_client;

public final class App {
    public static void main(String[] args) {
        Client c = new Client("localhost", 1099);
        c.exec();
    }
}
