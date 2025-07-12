package SocketProgramming;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server1 extends Thread {
    private JTextArea textArea;
    private JTextField textField;
    private PrintWriter writer;
    private BufferedReader reader;

    public Server1(InputStream input, OutputStream output) {
        reader = new BufferedReader(new InputStreamReader(input));
        writer = new PrintWriter(output, true);
    }

    @Override
    public void run() {
        createGUI();
        // Start reader thread
        new Thread(() -> {
            try {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.equalsIgnoreCase("exit")) break;
                    textArea.append("Client: " + line + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void createGUI() {
        ImageIcon icon1Raw = new ImageIcon("E:/ExceptionHandling(W3RESOURCE)/Practise  random/src/SocketProgramming/chat.png");
        Image icon1Image = icon1Raw.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        ImageIcon icon1 = new ImageIcon(icon1Image);
        ImageIcon icon2Raw = new ImageIcon("E:\\ExceptionHandling(W3RESOURCE)\\Practise  random\\src\\SocketProgramming\\send.png");
        Image icon2Image = icon2Raw.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        ImageIcon icon2 = new ImageIcon(icon2Image);
        JFrame frame = new JFrame("Server Chat");
        frame.setSize(500, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));
        frame.getContentPane().setBackground(Color.WHITE);

        JLabel headingLabel = new JLabel("  Chat Application",icon1,JLabel.LEFT);
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

        JPanel inputPanel = new JPanel(new BorderLayout(10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        textField = new JTextField("Type your message...");
        textField.setFont(new Font("SansSerif", Font.PLAIN, 16));
        textField.setForeground(Color.GRAY);

        textField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (textField.getText().equals("Type your message...")) {
                    textField.setText("");
                    textField.setForeground(Color.BLACK);
                }
            }

            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setText("Type your message...");
                    textField.setForeground(Color.GRAY);
                }
            }
        });

        JButton button = new JButton(icon2);
        button.setPreferredSize(new Dimension(60, 40));
        button.setBackground(new Color(30, 144, 255));
        button.setToolTipText("Send message");
        button.setFocusable(false);

        button.addActionListener(e -> sendMessage());
        textField.addActionListener(e -> sendMessage());

        inputPanel.add(textField, BorderLayout.CENTER);
        inputPanel.add(button, BorderLayout.EAST);
        frame.add(inputPanel, BorderLayout.SOUTH);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void sendMessage() {
        String msg = textField.getText().trim();
        if (!msg.isEmpty() && !msg.equalsIgnoreCase("Type your message...")) {
            writer.println(msg);
            textArea.append("Me: " + msg + "\n");
            textField.setText("");
        }
    }

    public static void main(String[] args) {
        System.out.println("Server started...");
        try (ServerSocket serverSocket = new ServerSocket(9999)) {
            System.out.println("Waiting for client...");
            Socket socket = serverSocket.accept();
            System.out.println("Connected to client: " + socket);
            Server1 server = new Server1(socket.getInputStream(), socket.getOutputStream());
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
