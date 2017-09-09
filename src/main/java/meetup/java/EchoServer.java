package meetup.java;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class EchoServer {

  public static void main(String[] args) throws IOException {
    Selector selector = Selector.open();
    ServerSocketChannel serverSocket = ServerSocketChannel.open();
    serverSocket.bind(new InetSocketAddress("localhost", 0x678));
    serverSocket.configureBlocking(false);
    serverSocket.register(selector, SelectionKey.OP_ACCEPT);
    ByteBuffer buffer = ByteBuffer.allocate(256);

    while (true) {
      System.out.println("selector.select()");
      selector.select();
      Set<SelectionKey> selectedKeys = selector.selectedKeys();
      System.out.println("selector.selectedKeys()");
      Iterator<SelectionKey> iter = selectedKeys.iterator();
      while (iter.hasNext()) {

        SelectionKey key = iter.next();
        System.out.println("selector key: " + key);

        if (key.isAcceptable()) {
          System.out.println("new client");
          SocketChannel client = serverSocket.accept();
          client.configureBlocking(false);
          client.register(selector, SelectionKey.OP_READ);
        }

        if (key.isReadable()) {
          SocketChannel client = (SocketChannel) key.channel();
          int reads = client.read(buffer);
          if (reads > 0) {
            System.out.println("sending response");
            buffer.flip();
            client.write(buffer);
            buffer.clear();
          } else{
            System.out.println("closing client");
            client.close();
          }
        }
        iter.remove();
      }
    }
  }

}
