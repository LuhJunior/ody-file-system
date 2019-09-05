package ody_file_system_client;

public final class App {
    /**
     *
     */
    private static final String LOCALHOST = "localhost";

    public static void main(String[] args) {
        Client c = new Client(LOCALHOST, 1099);
        c.exec();
    }
}
