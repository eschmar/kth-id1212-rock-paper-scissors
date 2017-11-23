package io.eschmann.net;

import io.eschmann.net.common.Observer;
import io.eschmann.net.server.MessageHandler;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class CommunicationServer implements Runnable {
    private InetAddress localAddress;
    private InetSocketAddress address;
    public Observer observer;

    ServerSocketChannel serverChannel;
    Selector selector;

    /**
     * Opens a new socket on a random port.
     */
    public CommunicationServer() {
        try {
            // Identify local ip address
            localAddress = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return;
        }

        try {
            // set up first channel
            serverChannel = ServerSocketChannel.open();
            ServerSocket serverSocket = serverChannel.socket();
            address = new InetSocketAddress(0);
            serverSocket.bind(address);

            // non-blocking!
            serverChannel.configureBlocking(false);

            selector = Selector.open();
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);

            System.out.println("Listening on port: " + getPort());
        } catch (Exception e) {
            System.out.println("Could not create serverSocket.");
            System.out.println(e.getStackTrace());
        }
    }

    public String getIpAddress() {
        return localAddress.getHostAddress();
    }

    public Integer getPort() throws IOException {
        if (serverChannel != null && serverChannel.isOpen()) {
            return serverChannel.socket().getLocalPort();
        }

        return null;
    }

    /**
     *  Starts a new thread accepting socket connections.
     */
    public void start() {
        new Thread(this).start();
    }

    /**
     * Start handling messages and finally close socket.
     */
    @Override
    public void run() {
        while (true) {
            try {
                selector.select();
            } catch (IOException e) {
                System.out.println("Selector failed.");
                System.out.println(e.getStackTrace());
                break;
            }

            Set<SelectionKey> readyKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = readyKeys.iterator();

            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();

                if (!key.isValid()) continue;

                try {
                    // accept connection
                    if (key.isAcceptable()) {
                        this.handleAcceptableKey(key);
                    // read from a channel
                    }else if (key.isReadable()) {
                        this.handleReadableKey(key);
                    // write to a channel
                    }else if (key.isWritable()) {
                        this.handleWriteableKey(key);
                    // establish new connection
                    }else if (key.isConnectable()) {
                        // do something
                        System.out.println("Connection not yet implemented.");
                    }
                } catch (IOException e) {
                    System.out.println("Key handling failed. Trying to close the channel..");
                    System.out.println(e.getStackTrace());
                    key.cancel();

                    try {
                        key.channel().close();
                    } catch (IOException closeException) {
                        System.out.println("Could not close channel.");
                        System.out.println(closeException.getStackTrace());
                    }
                }
            }
        }
    }

    private void handleAcceptableKey(SelectionKey key) throws IOException {
        ServerSocketChannel server = (ServerSocketChannel) key.channel();
        SocketChannel clientChannel = server.accept();
        System.out.println("Accepted connection from " + clientChannel);
        clientChannel.configureBlocking(false);

        clientChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));






//        ServerSocketChannel server = (ServerSocketChannel) key.channel();
//        SocketChannel client = server.accept();
//        System.out.println("Accepted connection from " + client);
//        client.configureBlocking(false);
//        SelectionKey key2 = client.register(selector, SelectionKey.
//                OP_WRITE);
//        ByteBuffer buffer = ByteBuffer.allocate(74);
//        buffer.put(rotation, 0, 72);
//        buffer.put((byte) '\r');
//        buffer.put((byte) '\n');
//        buffer.flip();
//        key2.attach(buffer);

        // ----

//        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
//        SocketChannel clientChannel = serverSocketChannel.accept();
//        clientChannel.configureBlocking(false);
//        ClientHandler handler = new ClientHandler(this, clientChannel);
//        clientChannel.register(selector, SelectionKey.OP_WRITE, new Client(handler, contr.
//                getConversation()));
//        clientChannel.setOption(StandardSocketOptions.SO_LINGER, LINGER_TIME); //Close will probably
        //block on some JVMs.
        // clientChannel.socket().setSoTimeout(TIMEOUT_HALF_HOUR); Timeout is not supported on
        // socket channels. Could be implemented using a separate timer that is checked whenever the
        // select() method in the main loop returns.
    }

    private void handleReadableKey(SelectionKey key) throws IOException {
        SocketChannel clientChannel = (SocketChannel) key.channel();
        ByteBuffer buffer = (ByteBuffer) key.attachment();

        // read from channel into buffer
        clientChannel.read(buffer);
        key.interestOps(SelectionKey.OP_WRITE);
    }

    private void handleWriteableKey(SelectionKey key) throws IOException {
        SocketChannel clientChannel = (SocketChannel) key.channel();
        ByteBuffer buffer = (ByteBuffer) key.attachment();

        buffer.flip();
        // write to channel from buffer
        clientChannel.write(buffer);

        if (buffer.hasRemaining()) {
            buffer.compact();
        }else {
            buffer.clear();
        }

        key.interestOps(SelectionKey.OP_READ);


//        SocketChannel client = (SocketChannel) key.channel();
//        ByteBuffer buffer = (ByteBuffer) key.attachment();
//        if (!buffer.hasRemaining()) {
//            // Refill the buffer with the next line
//            buffer.rewind();
//            // Get the old first character
//            int first = buffer.get();
//            // Get ready to change the data in the buffer
//            buffer.rewind();
//            // Find the new first characters position in rotation
//            int position = first - ' ' + 1;
//            // copy the data from rotation into the buffer
//            buffer.put(rotation, position, 72);
//            // Store a line break at the end of the buffer
//            buffer.put((byte) '\r');
//            buffer.put((byte) '\n');
//            // Prepare the buffer for writing
//            buffer.flip();
//        }
//        client.write(buffer);
    }

//            try {
//            while (true) {
//                // handle messages
//                new MessageHandler(serverSocket.accept(), observer).handle();
//            }
//        } catch (IOException e) {
//            System.out.println("ReceiverServer experienced an error and will terminate! " + e.getMessage());
//        } finally {
//            terminate();
//        }

    /**
     * Tries to terminate the server.
     */
    public void terminate() {
        try {
            serverChannel.close();
        } catch (IOException e) {
            System.out.println("Could not close server channel.");
            System.out.println(e.getStackTrace());
        }
    }
}
