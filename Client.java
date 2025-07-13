package SocketProgramming;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args)throws Exception {
        Socket sco=new Socket(InetAddress.getLocalHost(),9998);
        Scanner sc=new Scanner(System.in);
        System.out.println("Type done to end");
       while(true) {
            DataInputStream in = new DataInputStream(sco.getInputStream());
            System.out.println("Server :");
            System.out.println(in.readUTF());
            System.out.println("Client :");
            DataOutputStream out = new DataOutputStream(sco.getOutputStream());
            String msg= sc.next();
            out.writeUTF(msg);
            if("done".equalsIgnoreCase(msg))
                break;
        }
       sco.close();
    }
}
