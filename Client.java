import lib.ArgsParser;
import lib.ParsedArgs;

import java.net.Socket;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

import java.util.Scanner;

import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Client {
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
    // to read from keyboard
    Scanner sc = new Scanner(System.in);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    try {
      Socket socket;
      DataOutputStream dataOut;
      DataInputStream dataIn;

      // try connection
      socket = new Socket();
      socket.connect(new InetSocketAddress(parsedArgs.server, parsedArgs.port));
      System.out.println("⚡ Connection success!");

      dataOut = new DataOutputStream(socket.getOutputStream());
      dataIn = new DataInputStream(socket.getInputStream());
  
      String userMessage = "";
      while (!userMessage.equals("exit")) {
        // read message from keyboard
        userMessage = sc.nextLine();

        // send message to server
        dataOut.writeUTF(userMessage);
        System.out.println("< " + socket.getLocalAddress().getHostAddress() + " client " + "[ " + LocalDateTime.now().format(formatter)  + " ] " + parsedArgs.protocol + ": " + userMessage);

        // receive message to server
        String serverMessage = dataIn.readUTF();
        System.out.println("> " + socket.getInetAddress().getHostAddress() + " server " + "[ " + LocalDateTime.now().format(formatter) + " ] " + parsedArgs.protocol + ": " + serverMessage);
      }

      dataOut.close();
      socket.close();
    } catch (Exception e) {
      System.out.println("ERROR: " + e.getMessage());
    } finally {
      sc.close();
    }
  }
  public static void runUDP (ParsedArgs parsedArgs) {
    // to read from keyboard
    Scanner sc = new Scanner(System.in);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    byte[] receiveData = new byte[1024 * 10];    // 10kB

    try {
      DatagramSocket socket = new DatagramSocket();;
      DatagramPacket packet;

      String userMessage = "";
      while (!userMessage.equals("exit")) {
        // read message from keyboard
        userMessage = sc.nextLine();

        // build packet
        InetAddress serverAddress = InetAddress.getByName(parsedArgs.server);
        int serverPort = parsedArgs.port;
        packet = new DatagramPacket(userMessage.getBytes(), userMessage.getBytes().length, serverAddress, serverPort);
        
        // send packet
        socket.send(packet);
        System.out.println("< " + socket.getLocalAddress().getHostAddress() + " client " + "[ " + LocalDateTime.now().format(formatter)  + " ] UDP: " + userMessage);
      
        // receive a response
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        socket.receive(receivePacket);
        String serverMessage = new String(receivePacket.getData(),0, receivePacket.getLength());
        System.out.println("> " + socket.getInetAddress().getHostAddress() + " server " + "[ " + LocalDateTime.now().format(formatter)  + " ] UDP: " + serverMessage);
      }

      socket.close();
    } catch (Exception e) {
      System.out.println("ERROR: " + e.getMessage());
    } finally {
      sc.close();
    }
  }
}
