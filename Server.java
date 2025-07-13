package SocketProgramming;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;
import java.rmi.ServerException;
import java.util.Scanner;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket soc = new ServerSocket(9998);
        System.out.printf("Waiting for connection on port %d.......\n", soc.getLocalPort());
        Socket sc = soc.accept();
        Scanner s = new Scanner(System.in);
        System.out.println("Connected with client address =" + sc.getInetAddress().toString() + " port associated with client =" + sc.getPort());
        System.out.println("Type done to end");
        while(true) {
            System.out.println("Server :");
            DataOutputStream ot = new DataOutputStream(sc.getOutputStream());
            String msg = s.next();
            ot.writeUTF(msg);
            System.out.println("Client :");
            DataInputStream in = new DataInputStream(sc.getInputStream());
            String clientmsg = in.readUTF();
            System.out.println(clientmsg);
            if ("done".equalsIgnoreCase(clientmsg)) {
                break;
            }
        }
        soc.close();
        sc.close();
    }
}
