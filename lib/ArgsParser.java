package lib;

public class ArgsParser {
  public static ParsedArgs parse(String[] args) {
    // detault values
    String protocol = "TCP";
    String server = "localhost";
    String port = "1234";

    // parse args (protocol, server, port)
    for (int i = 0; i < args.length; i += 2) {
      if (args[i].equals("protocol")) {
        protocol = args[i + 1];
      } else if (args[i].equals("server")) {
        server = args[i + 1];
      } else if (args[i].equals("port")) {
        port = args[i + 1];
      }
    }

    return new ParsedArgs(protocol, server, Integer.parseInt(port));
  }
}


