import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lib.ArgsParser;
import lib.Calculator;
import lib.ParsedArgs;

public class Server {
  public static void main (String[] args) {
    // parse arguments (server, port, etc...)
    ParsedArgs parsedArgs = ArgsParser.parse(args);

    if (parsedArgs.protocol.toUpperCase().equals("TCP")) {
      runTCP(parsedArgs);
    } else if (parsedArgs.protocol.toUpperCase().equals("UDP")) {
      runUDP(parsedArgs);
    } else {
      System.out.println("Protocolo inválido");
    }
  }

  public static void runTCP (ParsedArgs parsedArgs) {
    // parse arguments (server, port, etc...)
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    try {
      // init tcp server
      ServerSocket server = new ServerSocket(parsedArgs.port);
      System.out.println("⚡ Server running on " + parsedArgs.port);

      // connect to one user
      while (true) {
        Socket socket = server.accept();
        DataInputStream dataIn = new DataInputStream(socket.getInputStream());
        DataOutputStream dataOut = new DataOutputStream(socket.getOutputStream());
  
        // start message flow
        String clientMessage = "";
        while (!clientMessage.equals("exit")) {
          // receive message from client
          clientMessage = dataIn.readUTF();
          System.out.println("> " + socket.getInetAddress() + " client " + "[ " + LocalDateTime.now().format(formatter) + " ] " + parsedArgs.protocol + ": " + clientMessage);
  
          // process result
          String response = String.valueOf(Calculator.calc(clientMessage));

          if (clientMessage.equals("exit")) {
            // send message to client
            dataOut.writeUTF("exit");
            System.out.println("< " + socket.getInetAddress() + " server " + "[ " + LocalDateTime.now().format(formatter) + " ] " + parsedArgs.protocol + ": exit");  
          } else {
            // send message to client
            dataOut.writeUTF(response);
            System.out.println("< " + socket.getInetAddress() + " server " + "[ " + LocalDateTime.now().format(formatter) + " ] " + parsedArgs.protocol + ": " + response);
          }
        }
  
        socket.close();
      }
    } catch (IOException e) {
      System.out.println("ERROR: " + e.getMessage());
    }
  }

  public static void runUDP (ParsedArgs parsedArgs) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    try {
      // init udp server
      DatagramSocket socket = new DatagramSocket(parsedArgs.port);
      System.out.println("⚡ Server running on " + parsedArgs.port);

      byte[] receiveData = new byte[1024 * 10];    // 10kB

      // start receiving messages
      while (true) {
        // receive one package
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        socket.receive(receivePacket);
        String clientMessage = new String(receivePacket.getData(),0, receivePacket.getLength());
        System.out.println("> " + receivePacket.getAddress() + " client " + "[ " + LocalDateTime.now().format(formatter)  + " ] UDP: " + clientMessage);

        // process result
        String response = String.valueOf(Calculator.calc(clientMessage));
        
        // send message
        InetAddress clientAddress = receivePacket.getAddress();
        int clientPort = receivePacket.getPort();
        socket.send(new DatagramPacket(response.getBytes(), response.getBytes().length, clientAddress, clientPort));
        System.out.println("< " + receivePacket.getAddress() + " server " + "[ " + LocalDateTime.now().format(formatter)  + " ] UDP: " + response);
      }
      
    } catch (Exception e) {
      System.out.println("ERROR: " + e.getMessage());
    }
  }
}
