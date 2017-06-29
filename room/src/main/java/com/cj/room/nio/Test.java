package com.cj.room.nio;

import com.cj.room.http.Response;
import com.cj.room.http.Socket2HttpClient;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liuchunjiang on 2017/6/29.
 */
public class Test {
    public static void main(String[] args) throws IOException {
        Socket2HttpClient client = new Socket2HttpClient("127.0.0.1",9999);
        Map<String,String[]> params = new HashMap<String,String[]>();
        params.put("appType", new String[]{"17"});
        params.put("game", new String[]{"aoqi"});
        Map<String,File> fileMap = new HashMap<>();
        fileMap.put("picdata",new File("C:\\Users\\Public\\Pictures\\Sample Pictures\\game\\g (2).jpg"));
        Response response = client.doPost("sdf",params);
        System.out.println(response.getHeader());
    }
}
