package com.cj.room.character;

import java.nio.ByteBuffer;

/**
 * Created by liuchunjiang on 2017/6/6.
 */
public class Utf8Util {

    /**
     * 过滤非普通utf8字符（空格开始到4位utf8字符之间）
     * @param text
     * @return
     * @throws Exception
     */
    public static String filterOffUtf8Mb4(String text) throws Exception {
        byte[] bytes = text.getBytes("UTF-8");
        ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
        int i = 0;
        int n = 0;
        while (i < bytes.length) {
            short b = bytes[i];
            if (b >= 0) {
                if (b >=32) {
                    buffer.put(bytes[i++]);
                }else {
                    i++;
                    n++;
                }
                continue;
            }
            b += 256;
            if ((b ^ 0xC0) >> 4 == 0) {
                buffer.put(bytes, i, 2);
                i += 2;
            }
            else if ((b ^ 0xE0) >> 4 == 0) {
                buffer.put(bytes, i, 3);
                i += 3;
            }
            else if ((b ^ 0xF0) >> 4 == 0) {
                i += 4;
                n += 4;
            }else {
                i++;
                n++;
            }
        }
        buffer.flip();
       //如果没有指定offset和limit，会带一些值为x0 的非法字符，因为这里是直接返回个数组，而flip方法没有做截取操作
        return new String(buffer.array(),0,bytes.length-n, "UTF-8");
    }
}
