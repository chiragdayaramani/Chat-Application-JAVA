import java.io.*;
import java.net.Socket;

public class Client {

    Socket socket;

    BufferedReader br;
    PrintWriter out;

    public Client() {
        try {
            System.out.println("Sending request to server");
            socket = new Socket("127.0.0.1", 7777);
            System.out.println("Connection done");

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out = new PrintWriter(socket.getOutputStream());

            startReading();
            startWrinting();

        } catch (Exception e) {
            // TODO: handle exception
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
                        System.out.println("Server terminated the chat");
                        socket.close();
                        break;
                    }
                    System.out.println("Server: " + msg);
                }

            } catch (IOException e) {
                // e.printStackTrace();
                System.out.println("Connection closed!!");
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
                System.out.println("Connection is closed!!");

            } catch (Exception e) {
                e.printStackTrace();

            }

        };

        new Thread(r2).start();
    }

    public static void main(String[] args) {
        System.out.println("This is client");
        new Client();
    }

}
