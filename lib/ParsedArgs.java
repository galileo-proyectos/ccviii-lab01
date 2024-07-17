package lib;

public class ParsedArgs {
  public String protocol;
  public String server;
  public int port;

  public ParsedArgs (String protocol, String server, int port) {
    super();
    this.protocol = protocol;
    this.server = server;
    this.port = port;
  }
}
