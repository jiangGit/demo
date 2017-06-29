package com.cj.room.nio;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by liuchunjiang on 2017/6/19.
 */
public class ChannelDemo {

    public static void main(String[] args) throws Exception {
        demo3();
    }

    public static void demo1()throws Exception {
        RandomAccessFile aFile = new RandomAccessFile("E:\\home\\pja\\config\\dcweb_local.xml", "rw");
        FileChannel inChannel = aFile.getChannel();

        ByteBuffer buf = ByteBuffer.allocate(48);

        int bytesRead = inChannel.read(buf);
        while (bytesRead != -1) {

            System.out.println("Read " + bytesRead);
            buf.flip();

            while(buf.hasRemaining()){
                System.out.print((char) buf.get());
            }

            buf.clear();
            bytesRead = inChannel.read(buf);
        }
        aFile.close();
    }

    public static void demo2() throws Exception {
        RandomAccessFile fromFile = new RandomAccessFile("E:\\home\\pja\\config\\dcweb_local.xml", "rw");
        FileChannel      fromChannel = fromFile.getChannel();

        RandomAccessFile toFile = new RandomAccessFile("E:\\home\\tmp\\toFile.txt", "rw");
        FileChannel      toChannel = toFile.getChannel();

        long position = 0;
        long count = fromChannel.size();

        toChannel.transferFrom(fromChannel,position,count);
        fromFile.close();
        toFile.close();
    }

    public static void demo3()throws Exception{
        RandomAccessFile toFile = new RandomAccessFile("E:\\tmp\\demo3.txt", "rw");
        FileChannel      toChannel = toFile.getChannel();
        ByteBuffer header = ByteBuffer.allocate(128);
        ByteBuffer body   = ByteBuffer.allocate(1024);

        header.put("head\n".getBytes());
        header.flip();
        body.put("body\n".getBytes());
        body.flip();
        ByteBuffer[] bufferArray = { header, body };

        toChannel.write(bufferArray);
        toFile.close();
    }

}
