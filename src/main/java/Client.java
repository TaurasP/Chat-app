import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Client {

    public static void main(String[] args){
        final Socket clientSocket; // socket used by client to send and receive data from server
        final BufferedReader in;   // object to read data from socket
        final PrintWriter out;     // object to write data into socket
        final Scanner sc = new Scanner(System.in); // object to read data from user's keyboard
        try {
            clientSocket = new Socket("127.0.0.1",8080);
            out = new PrintWriter(clientSocket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            Thread sender = new Thread(new Runnable() {
                String msg;
                @Override
                public void run() {
                    System.out.println("*********** CHAT ***********");
                    System.out.println("* Enter EXIT to quit chat. *");
                    System.out.println("****************************");
                    while(true){
                        msg = sc.nextLine();
                        if(msg.equalsIgnoreCase("exit")) {
                            try {
                                out.close();
                                clientSocket.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        out.println(msg);
                        out.flush();
                    }
                }
            });
            sender.start();
            Thread receiver = new Thread(new Runnable() {
                String msg;
                @Override
                public void run() {
                    try {
                        msg = in.readLine();
                        while(msg!=null){
                            String timeStamp = new SimpleDateFormat("HH:mm:ss").format(new Date());
                            System.out.println("Server (" + timeStamp + "): " + msg);
                            msg = in.readLine();
                        }
                        System.out.println("Server out of service.");
                        out.close();
                        clientSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            receiver .start();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}