package SocketProgramming;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client1 extends Thread {
    InputStream readerStream;
    OutputStream writerStream;
    PrintWriter writer;
    JTextArea textArea;
    JTextField textField;

    public Client1(InputStream r, OutputStream o) {
        this.readerStream = r;
        this.writerStream = o;
        this.writer = new PrintWriter(writerStream, true);
    }

    @Override
    public void run() {
        // Start GUI
        new createGuI();

        // Start Reader and Writer threads
        Reader r = new Reader(readerStream, textArea);
        Writer w = new Writer(writerStream, textField, textArea);
        r.start();
        w.start();
        try {
            r.join();
            w.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    class createGuI {
        JFrame frame;
        JButton button;
        JPanel inputPanel;
        JLabel headingLabel;

        public createGuI() {
            frame = new JFrame("Client Chat");
            frame.setSize(500, 600);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout(10, 10));
            frame.getContentPane().setBackground(Color.WHITE);


            ImageIcon icon1Raw = new ImageIcon("E:/ExceptionHandling(W3RESOURCE)/Practise  random/src/SocketProgramming/chat.png");
            Image icon1Image = icon1Raw.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            ImageIcon icon1 = new ImageIcon(icon1Image);

            ImageIcon icon2Raw = new ImageIcon("E:\\ExceptionHandling(W3RESOURCE)\\Practise  random\\src\\SocketProgramming\\send.png");
            Image icon2Image = icon2Raw.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            ImageIcon icon2 = new ImageIcon(icon2Image);
            headingLabel = new JLabel("  Chat Application",icon1, JLabel.LEFT);
            headingLabel.setFont(new Font("Arial Black", Font.BOLD, 22));
            headingLabel.setOpaque(true);
            headingLabel.setBackground(new Color(30, 144, 255));
            headingLabel.setForeground(Color.WHITE);
            headingLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            frame.add(headingLabel, BorderLayout.NORTH);

            // Text Area
            textArea = new JTextArea();
            textArea.setEditable(false);
            textArea.setFont(new Font("SansSerif", Font.PLAIN, 16));
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            frame.add(scrollPane, BorderLayout.CENTER);

            // Bottom Panel
            inputPanel = new JPanel(new BorderLayout(10, 10));
            inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            textField = new JTextField("Type your message...");
            textField.setFont(new Font("SansSerif", Font.PLAIN, 16));
            textField.setForeground(Color.GRAY);

            textField.addFocusListener(new java.awt.event.FocusAdapter() {
                public void focusGained(java.awt.event.FocusEvent e) {
                    if (textField.getText().equals("Type your message...")) {
                        textField.setText("");
                        textField.setForeground(Color.BLACK);
                    }
                }

                public void focusLost(java.awt.event.FocusEvent e) {
                    if (textField.getText().isEmpty()) {
                        textField.setText("Type your message...");
                        textField.setForeground(Color.GRAY);
                    }
                }
            });

            button = new JButton(icon2);
            button.setPreferredSize(new Dimension(60, 40));
            button.setBackground(new Color(30, 144, 255));
            button.setToolTipText("Send message");
            button.setFocusable(false);

            inputPanel.add(textField, BorderLayout.CENTER);
            inputPanel.add(button, BorderLayout.EAST);
            frame.add(inputPanel, BorderLayout.SOUTH);


            frame.setLocationRelativeTo(null);
            frame.setVisible(true);


            button.addActionListener(e -> sendMessage());

            // Enter key also sends message
            textField.addActionListener(e -> sendMessage());
        }

        public void sendMessage() {
            String msg = textField.getText().trim();
            if (!msg.isEmpty() && !msg.equalsIgnoreCase("Type your message...")) {
                writer.println(msg);
                textArea.append("Me: " + msg + "\n");
                textField.setText("");
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("Client started ......");
        try {
            Socket s = new Socket("localhost", 9999);
            Client1 c = new Client1(s.getInputStream(), s.getOutputStream());
            c.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
class Reader extends Thread {
    BufferedReader br;
    JTextArea textArea;

    public Reader(InputStream r, JTextArea textArea) {
        br = new BufferedReader(new InputStreamReader(r));
        this.textArea = textArea;
    }

    @Override
    public void run() {
        while (true) {
            try {
                String lines = br.readLine();
                if (lines == null || lines.equalsIgnoreCase("exit")) {
                    break;
                }
                textArea.append("Server: " + lines + "\n");
            } catch (IOException e) {
                break;
            }
        }
    }
}
class Writer extends Thread {
    Scanner sc = new Scanner(System.in);
    PrintWriter p;
    JTextField input;
    JTextArea output;

    public Writer(OutputStream o, JTextField input, JTextArea output) {
        p = new PrintWriter(o, true);
        this.input = input;
        this.output = output;
    }

    @Override
    public void run() {
        while (true) {
            String msg = sc.nextLine();
            p.println(msg);
            if (msg.equalsIgnoreCase("exit")) break;
        }
    }
}
