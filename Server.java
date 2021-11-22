import java.io.*;
import java.net.*;

class Server {

    ServerSocket server;
    Socket socket;

    BufferedReader br;
    PrintWriter out;

    public Server() {

        try {
            server = new ServerSocket(7777);
            System.out.println("Server is ready to accept connection");
            System.out.println("Waiting....");
            socket = server.accept();

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out = new PrintWriter(socket.getOutputStream());

            startReading();
            startWrinting();

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    private void startReading() {
        // thread - read karke deta rahega

        Runnable r1 = () -> {
            System.out.println("reader started.....");
            try {
                while (true) {
                    String msg;
                    msg = br.readLine();
                    if (msg.equals("exit")) {
                        System.out.println("Client terminated the chat");
                        socket.close();
                        break;
                    }
                    System.out.println("Client: " + msg);
                }

            } catch (IOException e) {
                // e.printStackTrace();
                System.out.println("Connection closed");
            }
        };

        new Thread(r1).start();

    }

    private void startWrinting() {
        // thread- user se lega and send karega
        System.out.println("Writer Started");
        Runnable r2 = () -> {

            try {
                while (!socket.isClosed()) {

                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));

                    String content = br1.readLine();

                    out.println(content);
                    out.flush();
                    if(content.equals("exit")){
                        socket.close();
                        break;

                    }
                    
                }
                System.out.println("Connection closed");

            } catch (Exception e) {
                e.printStackTrace();
            }

        };

        new Thread(r2).start();
    }

    public static void main(String[] args) {
        System.out.println("This is server...going to start server");
        new Server();
    }
}