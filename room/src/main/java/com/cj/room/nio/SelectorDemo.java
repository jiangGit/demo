package com.cj.room.nio;

import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by liuchunjiang on 2017/6/28.
 */
public class SelectorDemo {

    public static void main(String[] args) throws Exception{
        demo1();
    }

    public static void demo1()throws Exception{
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);//设置非阻塞
        ServerSocket ss = ssc.socket();
        InetSocketAddress address = new InetSocketAddress(5555);
        ss.bind(address);
        Selector selector = Selector.open();
        SelectionKey key1 = ssc.register(selector, SelectionKey.OP_ACCEPT);
        while(true) {
            int readyChannels = selector.select();
            System.out.println("?");
            if(readyChannels == 0) continue;
            Set selectedKeys = selector.selectedKeys();
            Iterator keyIterator = selectedKeys.iterator();
            while(keyIterator.hasNext()) {
                SelectionKey key =(SelectionKey) keyIterator.next();
                if(key.isAcceptable()) {
                    // a connection was accepted by a ServerSocketChannel.
                    ServerSocketChannel ssc2 = (ServerSocketChannel) key.channel();
                    SocketChannel sc = ssc2.accept();
                    sc.configureBlocking(false);
                    sc.register(selector,SelectionKey.OP_READ);

                    System.out.println("a connection was accepted by a ServerSocketChannel");

                } else if (key.isConnectable()) {
                    // a connection was established with a remote server.
                    System.out.println("a connection was established with a remote server.");
                } else if (key.isReadable()) {
                    // a channel is ready for reading
                    // Read the data
                    SocketChannel sc = (SocketChannel) key.channel();
                    ByteBuffer echoBuffer = ByteBuffer.allocate(1024);
                    // Echo data
                    int bytesEchoed = 0;
                    StringBuilder sb = new StringBuilder();
                    while (true) {
                        echoBuffer.clear();

                        int r = sc.read(echoBuffer);

                        if (r <= 0) {
                            break;
                        }

                        echoBuffer.flip();
                        sb.append(new String(echoBuffer.array()));

                        bytesEchoed += r;
                    }

                    System.out.println("Echoed " + bytesEchoed + " from " + sc+" :" +sb.toString());
                    sc.register(selector,SelectionKey.OP_WRITE);
                    System.out.println("a channel is ready for reading");

                } else if (key.isWritable()) {
                    // a channel is ready for writing
                    SocketChannel sc = (SocketChannel) key.channel();
                    ByteBuffer echoBuffer = ByteBuffer.allocate(1024);
                    echoBuffer.put("HTTP/1.1 200 OK\n".getBytes());
                    echoBuffer.put("Connection: close\n".getBytes());
                    echoBuffer.put("Content-Length: 1\n".getBytes());
                    echoBuffer.put("\n\r".getBytes());
                    echoBuffer.put("\n\r".getBytes());
                    echoBuffer.put("abcdfsdfsd\n".getBytes());
                    echoBuffer.flip();
                    sc.write(echoBuffer);
                    sc.close();
                    System.out.println("a channel is ready for writing");
                }
                keyIterator.remove();
            }
        }
    }

}
