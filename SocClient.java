package SocketProgramming;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class SocClient extends Thread {
    int port=9999;
    Socket s;

    {
        try {
            s = new Socket("localhost",port);
        } catch (IOException e) {

        }
    }

    @Override
    public void run() {
        try{

            BufferedReader r=new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter out=new PrintWriter(new OutputStreamWriter(s.getOutputStream()));
            read(r);
            write(out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void write(PrintWriter out)throws Exception {
        while (true) {
            out.write(new Scanner(System.in).next());
            out.flush();
            if(new Scanner(System.in).next().equalsIgnoreCase("exit")){
                s.close();
            }
        }
    }

    private void read(BufferedReader r) throws IOException {
        while (true) {
            String lines = r.readLine();
            System.out.println("Server : " + lines);
            if (lines.equalsIgnoreCase("exit")) {
                System.out.println("Server terminated");
                s.close();
                break;
            }
        }
    }

    public static void main(String[] args) throws Exception {
        SocClient c=new SocClient();
        System.out.println("Client started");
        c.start();
    }
}
