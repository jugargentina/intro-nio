package meetup.java;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class EchoClient {
  private SocketChannel client;

  public void stop() throws IOException {
    client.close();
  }

  public EchoClient() throws IOException {
    client = SocketChannel.open(new InetSocketAddress("localhost", 0x678));
  }

  public String sendMessage(String msg) throws IOException {
    ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
    client.write(buffer);
    buffer.clear();
    client.read(buffer);
    String response = new String(buffer.array()).trim();
    buffer.clear();
    return response;
  }

  public static void main(String[] args) throws IOException {
    EchoClient client = new EchoClient();
    System.out.println(client.sendMessage("Java Meetup"));
  }
}
