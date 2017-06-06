package com.cj.room.character;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by liuchunjiang on 2017/6/6.
 */
public class FileCode {
    public static String getFileCode(File source) throws Exception {
        FileInputStream fi=new FileInputStream(source);
        String encoding="UTF-8";
        byte[] buffer=new byte[2];
        int len;
        if((len=fi.read(buffer))!=-1) {
            if(len>=2){
                if(buffer[0]==-17&&buffer[1]==-69){
                    if((byte)fi.read()==-65){
                        encoding="BOMUTF-8";
                    }
                }else if((buffer[0]==-1&&buffer[1]==-2)){
                    encoding="UTF-16LE";
                }else if(buffer[0]==-2&&buffer[1]==-1){
                    encoding="UTF-16BE";
                }else if(isUTF8(source)){
                    encoding="UTF-8";
                }else{
                    encoding="GBK";
                }
            }
        }
        fi.close();
        return encoding;
    }
    private static boolean isUTF8(File source) throws Exception {
        FileInputStream fi = new FileInputStream(source);
        int b;
        while((b=fi.read())!=-1){
            if((b&0xF8)==0xF0){
                return checkIsUTF8(fi,3);
            }else if((b&0xF0)==0xE0){
                return checkIsUTF8(fi,2);
            }else if((b&0xE0)==0xC0){
                if(!checkIsUTF8(fi,1))return false;
            }else if((b&0x80)==0x80){
                return false;
            }
        }

        return true;
    }
    private static boolean checkIsUTF8(FileInputStream fi,int times) throws IOException {
        int b;
        int i=0;
        while(i<times){
            b=fi.read();
            if(b==-1||(b&0xC0)!=0x80){
                break;
            }
            i++;
        }
        if(i!=times){
            return false;
        }
        return true;
    }
}
