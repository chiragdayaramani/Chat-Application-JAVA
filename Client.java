import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.net.Socket;

import java.awt.*;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;


public class Client extends JFrame {

    Socket socket;

    BufferedReader br;
    PrintWriter out;

    private JLabel heading = new JLabel("Client Area");
    private JTextArea messageArea = new JTextArea();
    private JTextField messageInput = new JTextField();
    private Font font = new Font("Roboto", Font.PLAIN, 20);

    public Client() {
        try {
            System.out.println("Sending request to server");
            socket = new Socket("127.0.0.1", 7777);
            System.out.println("Connection done");

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());

            createGUI();
            handleEvents();
            startReading();
            // startWrinting();

        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    private void handleEvents() {

        messageInput.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub
                // System.out.println("Key released "+e.getKeyCode());
                if(e.getKeyCode()==10){
                    System.out.println("You have pressed enter button");
                    String contentToSend=messageInput.getText();
                    messageArea.append("Me: "+contentToSend+"\n");
                    out.println(contentToSend);
                    out.flush();
                    messageInput.setText(" ");
                    messageInput.requestFocus();
                }

            }

        });
    }

    private void createGUI() {

        this.setTitle("Client Messanger");
        this.setSize(600, 600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // components
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);
        heading.setIcon(new ImageIcon("icon.png"));
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        messageArea.setEditable(false);
        

        messageInput.setHorizontalAlignment(SwingConstants.CENTER);

        this.setLayout(new BorderLayout());

        this.add(heading, BorderLayout.NORTH);
        JScrollPane jScrollPane=new JScrollPane(messageArea);
        this.add(jScrollPane, BorderLayout.CENTER);
        this.add(messageInput, BorderLayout.SOUTH);

        this.setVisible(true);

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
                        JOptionPane.showMessageDialog(this,"Server Terminated the chat");
                        messageInput.setEnabled(false);
                        socket.close();
                        break;
                    }
                    // System.out.println("Server: " + msg);
                    messageArea.append("Server: "+msg+"\n");
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
                    if (content.equals("exit")) {
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
