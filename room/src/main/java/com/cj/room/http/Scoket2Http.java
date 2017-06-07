package com.cj.room.http;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.Socket;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liuchunjiang on 2017/6/6.
 */
public class Scoket2Http {

    public static String doGet(String host,int port,String path){
        try {
            Socket s = new Socket(host, port);
            OutputStreamWriter osw = new OutputStreamWriter(s.getOutputStream());
            StringBuffer sb = new StringBuffer();
            sb.append("GET "+path+" HTTP/1.1\r\n");
            sb.append("Host: "+host+":"+port+"\r\n");
            sb.append("Connection: Keep-Alive\r\n");
            sb.append("User-Agent: god view\r\n");
            //注，这是关键的关键，忘了这里让我搞了半个小时。这里一定要一个回车换行，表示消息头完，不然服务器会等待
            sb.append("\r\n");
            osw.write(sb.toString());
            osw.flush();
            //--输出服务器传回的消息的头信息
            InputStream is = s.getInputStream();
            String line = IOUtils.toString(is,"utf-8");
            System.out.println(line);
            //关闭流
            is.close();
            s.close();
            return  line;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  null;
    }

    public static String doPost(String host,int port,String path,Map<String,List<String>> params){
        try {
            final String newLine = "\r\n";
            // 定义数据分隔线
            Socket s = new Socket(host, port);
            OutputStreamWriter osw = new OutputStreamWriter(s.getOutputStream());
            StringBuffer sb = new StringBuffer();
            sb.append("POST "+path+" HTTP/1.1"+newLine);
            sb.append("Host: "+host+":"+port+newLine);
            sb.append("Connection: Keep-Alive"+newLine);
            sb.append("User-Agent: god view"+newLine);
            sb.append("Content-Type: application/x-www-form-urlencoded"+newLine);
            StringBuffer data = new StringBuffer();
            for (String key:params.keySet()) {
                List<String> list = params.get(key);
                for (String val:list) {
                    if (data.length() > 0){
                        data.append("&");
                    }
                    data.append(key+"="+URLEncoder.encode(val,"utf-8"));

                }
            }
            System.out.println(data.toString());
            sb.append("Content-Length: "+data.length()+newLine);
            sb.append(newLine);
            sb.append(data.toString());
            osw.write(sb.toString());
            osw.flush();
            //--输出服务器传回的消息的头信息
            InputStream is = s.getInputStream();
            String line = IOUtils.toString(is,"utf-8");
            System.out.println(line);
            //关闭流
            is.close();
            s.close();
            return  line;

        }catch (Exception e){

        }
        return null;
    }

    public static String doPostFile(String host, int port, String path, Map<String,List<String>> params, Map<String,File> fileMap){
        try {
            final String newLine = "\r\n";
            final String boundaryPrefix = "--";
            // 定义数据分隔线
            String BOUNDARY = "======7d4a6d158c9";
            Socket s = new Socket(host, port);
            OutputStream out = s.getOutputStream();
          //  OutputStreamWriter osw = new OutputStreamWriter(out);
            StringBuffer sb = new StringBuffer();
            sb.append("POST "+path+" HTTP/1.1"+newLine);
            sb.append("Host: "+host+":"+port+newLine);
            sb.append("Connection: Keep-Alive"+newLine);
            sb.append("User-Agent: god view"+newLine);
            sb.append("Content-Type: multipart/form-data; boundary="+ BOUNDARY+newLine);
            StringBuffer data = new StringBuffer();

            for (String key:params.keySet()) {
                List<String> list = params.get(key);
                for (String val:list) {
                    data.append(boundaryPrefix+BOUNDARY+newLine);
                    data.append("Content-Disposition: form-data; name=\""+key+"\""+newLine);
                    data.append("Content-Type: text/plain; charset=UTF-8"+newLine);
                    data.append("Content-Transfer-Encoding: 8bit"+newLine);
                    data.append(newLine);
                    data.append(val+newLine);
                }
            }

            int l = 0;
            List<StringBuffer> sbList = new ArrayList<>();
            List<byte[]> bList = new ArrayList<>();
            for (String key:fileMap.keySet()){
                StringBuffer data2 = new StringBuffer();
                data2.append(boundaryPrefix+BOUNDARY+newLine);
                File file = fileMap.get(key);
                data2.append("Content-Disposition: form-data; name=\""+key+"\"; filename=\""+file.getName()+"\""+newLine);
                data2.append("Content-Type: application/octet-stream"+newLine);
                data2.append("Content-Transfer-Encoding: binary"+newLine);
                data2.append(newLine);
                byte[] bytes = IOUtils.toByteArray(new FileInputStream(file));
                l+= bytes.length;
                l+= data2.toString().getBytes().length;
                l+= newLine.getBytes().length;
               sbList.add(data2);
               bList.add(bytes);
                // 最后添加换行
                //data2.append(newLine);

            }
            l+=(boundaryPrefix+BOUNDARY+"--").getBytes().length;
            sb.append("Content-Length: "+(data.toString().getBytes().length + l)+newLine);
            sb.append(newLine);
            sb.append(data.toString());
            //osw.write(sb.toString());
            out.write(sb.toString().getBytes());
            System.out.println(sb.toString());
            //osw.flush();
            for (int i = 0 ;i< sbList.size();i++){
               // osw.write(sbList.get(i).toString());
               // osw.flush();
                out.write(sbList.get(i).toString().getBytes());
                out.write(bList.get(i));
               // out.flush();
               // osw.write(newLine);
                out.write(newLine.getBytes());
            }
           // osw.write(boundaryPrefix+BOUNDARY+"--");
           // osw.flush();
            out.write((boundaryPrefix+BOUNDARY+"--").getBytes());
            out.flush();
            //--输出服务器传回的消息的头信息
            InputStream is = s.getInputStream();
            String line = IOUtils.toString(is,"utf-8");
            System.out.println(line);
            //关闭流
            is.close();
            s.close();
            return  line;

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    public static void main(String[] args) {
        //doGet("service.100bt.com",80,"/test.jsp");
       /*
        Map<String,List<String>> params = new HashMap<String,List<String>>();
        List<String> list = new ArrayList<>();
        list.add("5");
        list.add("哈哈");
        params.put("appType", list);
        params.put("test", list);
        doPost("service.100bt.com",80,"/wx/getAccessToken.json",params);
        */
        Map<String,List<String>> params = new HashMap<String,List<String>>();
        List<String> list = new ArrayList<>();
        list.add("6");
        params.put("appType", list);
        Map<String,File> fileMap = new HashMap<>();
        fileMap.put("uploadResource",new File("C:\\Users\\Public\\Pictures\\Sample Pictures\\game\\g (2).jpg"));
        doPostFile("pjacms.100bt.com",80,"/UploadResourceByAjax.action",params,fileMap);
    }

}



