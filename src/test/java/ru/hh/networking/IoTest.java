package ru.hh.networking;

import org.testng.annotations.Test;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class IoTest {
  @Test(expectedExceptions = SocketTimeoutException.class)
  public void soTimeoutTest() throws IOException, InterruptedException {
    int port = 5658;
    ServerSocket serverSocket = new ServerSocket();
    Thread serverThread = new Thread(() -> {
      try {
        serverSocket.bind(new InetSocketAddress(port));
        while (!serverSocket.isClosed()) {
          try (Socket socket = serverSocket.accept()) {
            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            while (!socket.isInputShutdown()) {
              String data = inputStream.readUTF();
              // TODO: 1 line removed
              outputStream.writeUTF(data + data);
              outputStream.flush();
            }
          }
        }
      } catch (IOException e) { // TODO: line can be modified
        e.printStackTrace();
      }
    });

    Socket socket = null;
    try {
      serverThread.start();
      socket = new Socket("localhost", port);
      // TODO: 1 line removed
      DataOutputStream out = new DataOutputStream(socket.getOutputStream());
      DataInputStream in = new DataInputStream(socket.getInputStream());
      out.writeUTF("lol");
      String answer = in.readUTF();
    } finally {
      socket.close();
      serverSocket.close();
      serverThread.join();
    }
  }
}
