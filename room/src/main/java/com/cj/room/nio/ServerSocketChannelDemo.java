package com.cj.room.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * Created by liuchunjiang on 2017/6/29.
 */
public class ServerSocketChannelDemo {
    public static void main(String[] args) {

    }

    //非阻塞
    public static void demo1() throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        serverSocketChannel.socket().bind(new InetSocketAddress(9999));
        serverSocketChannel.configureBlocking(false);

        while(true){
            SocketChannel socketChannel =
                    serverSocketChannel.accept();

            if(socketChannel != null){
                //do something with socketChannel...
            }
        }
    }

    //阻塞
    public static void demo2() throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        serverSocketChannel.socket().bind(new InetSocketAddress(9999));

        while(true){
            SocketChannel socketChannel =serverSocketChannel.accept();

            //do something with socketChannel...
        }
    }

}
