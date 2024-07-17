import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lib.ArgsParser;
import lib.ParsedArgs;

public class Server {
  public static void main(String[] args) {
    // parse arguments (server, port, etc...)
    ParsedArgs parsedArgs = ArgsParser.parse(args);
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
  
        // comunication
        String clientMessage = "";
        while (!clientMessage.equals("exit")) {
          clientMessage = dataIn.readUTF();
          System.out.println("> " + socket.getInetAddress() + " client " + "[ " + LocalDateTime.now().format(formatter) + " ] " + parsedArgs.protocol + ": " + clientMessage);
  
          dataOut.writeUTF(clientMessage);
          System.out.println("< " + socket.getInetAddress() + " server " + "[ " + LocalDateTime.now().format(formatter) + " ] " + parsedArgs.protocol + ": " + clientMessage);  
        }
  
        socket.close();
      }
    } catch (IOException e) {
      System.out.println("ERROR: " + e.getMessage());
    }
  }
}
