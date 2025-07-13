package SocketProgramming;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class SocServer {
    public static void main(String[] args) throws Exception{
        System.out.println("SocketProgramming.Server Starting");
        try (ServerSocket ss = new ServerSocket(9999)) {
            System.out.println("Waiting for connection");
            Socket s=ss.accept();//accept any connection request made to server and make a child socket to tha client
            System.out.println("Connection established with client");

            BufferedReader reader=new BufferedReader(new InputStreamReader(s.getInputStream()));
            String st= reader.readLine();
            System.out.println("Clients msg is "+st);
        } catch (Exception e) {
            System.out.println("Port's busy");
        }
    }
}
