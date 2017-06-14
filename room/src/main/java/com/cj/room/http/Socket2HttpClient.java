package com.cj.room.http;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.Socket;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liuchunjiang
 */
public class Socket2HttpClient {

    private Socket socket;
    private String host;
    private int port;
    private  boolean isClose = false;

    public Socket2HttpClient(String host,int port) throws IOException {
        this.host = host;
        this.port = port;
        socket = new Socket(host,port);
        socket.setKeepAlive(true);
    }

    public  Response doGet(String path) throws IOException {
            return doGet(path,new HashMap<String,String>());
    }

    public  Response doGet(String path,Map<String,String> header) throws IOException {
        OutputStreamWriter osw = new OutputStreamWriter(socket.getOutputStream());
        StringBuilder sb = new StringBuilder();
        sb.append("GET "+path+" HTTP/1.1\r\n");
        sb.append("Host: "+host+":"+port+"\r\n");
        sb.append("Connection: Keep-Alive\r\n");
        sb.append("User-Agent: god view\r\n");
        for (String key:header.keySet()){
            sb.append(String.format("%s: %s\r\n",key,header.get(key)));
        }
        //这里一定要一个回车换行，表示消息头完，不然服务器会等待
        sb.append("\r\n");
        osw.write(sb.toString());
        osw.flush();
        return  getContent();
    }

    public  Response doPost(String path,Map<String,String[]> params) throws IOException {
        return doPost(path,params,new HashMap<String,String>());
    }

    public  Response doPost(String path,Map<String,String[]> params,Map<String,String> header) throws IOException {
        final String newLine = "\r\n";
        OutputStreamWriter osw = new OutputStreamWriter(socket.getOutputStream());
        StringBuilder sb = new StringBuilder();
        sb.append("POST "+path+" HTTP/1.1"+newLine);
        sb.append("Host: "+host+":"+port+newLine);
        sb.append("Connection: Keep-Alive"+newLine);
        sb.append("User-Agent: god view"+newLine);
        sb.append("Content-Type: application/x-www-form-urlencoded"+newLine);
        for (String key:header.keySet()){
            sb.append(String.format("%s: %s\r\n",key,header.get(key)));
        }
        StringBuilder data = new StringBuilder();
        for (String key:params.keySet()) {
            String[] list = params.get(key);
            for (String val:list) {
                if (data.length() > 0){
                    data.append("&");
                }
                data.append(key+"="+ URLEncoder.encode(val,"utf-8"));

            }
        }
        sb.append("Content-Length: "+data.length()+newLine);
        sb.append(newLine);
        sb.append(data.toString());
        osw.write(sb.toString());
        osw.flush();
        return  getContent();
    }

    public  Response doPostFile( String path, Map<String,String[]> params, Map<String,File> fileMap) throws IOException {
            return doPostFile(path,params,fileMap,new HashMap<String,String>());
    }

    public  Response doPostFile( String path, Map<String,String[]> params, Map<String,File> fileMap,Map<String,String> header) throws IOException {
        final String newLine = "\r\n";
        final String boundaryPrefix = "--";
        // 定义数据分隔线
        String BOUNDARY = "======7d4a6d158c9";
        OutputStream out = socket.getOutputStream();
        StringBuilder sb = new StringBuilder();
        sb.append("POST "+path+" HTTP/1.1"+newLine);
        sb.append("Host: "+host+":"+port+newLine);
        sb.append("Connection: Keep-Alive"+newLine);
        sb.append("User-Agent: god view"+newLine);
        sb.append("Content-Type: multipart/form-data; boundary="+ BOUNDARY+newLine);
        for (String key:header.keySet()){
            sb.append(String.format("%s: %s\r\n",key,header.get(key)));
        }
        StringBuilder data = new StringBuilder();

        for (String key:params.keySet()) {
            String[] list = params.get(key);
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
        List<StringBuilder> sbList = new ArrayList<>();
        List<byte[]> bList = new ArrayList<>();
        for (String key:fileMap.keySet()){
            StringBuilder data2 = new StringBuilder();
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
        }
        l+=(boundaryPrefix+BOUNDARY+"--").getBytes().length;
        sb.append("Content-Length: "+(data.toString().getBytes().length + l)+newLine);
        sb.append(newLine);
        sb.append(data.toString());
        out.write(sb.toString().getBytes());
        for (int i = 0 ;i< sbList.size();i++){
            out.write(sbList.get(i).toString().getBytes());
            out.write(bList.get(i));
            // 最后添加换行
            out.write(newLine.getBytes());
        }
        out.write((boundaryPrefix+BOUNDARY+"--").getBytes());
        out.flush();
        return getContent();
    }

    private Response getContent() throws IOException {
        InputStream is = socket.getInputStream();
        Response response = new Response();
        int rl = 0;
        StringBuilder headerSb= new StringBuilder();
        while (true){
            String  line = readLine(is);
            if (headerSb.length() == 0){
                String[] arr=line.split(" ");
                response.setCode(Integer.parseInt(arr[1]));
            }
            headerSb.append(line+"\n");
            if (line.contains("Content-Length:")){
                rl = Integer.parseInt(line.substring(16));
            }
            if (line.contains("Transfer-Encoding: chunked")){
                rl = -1;
            }
            if (line.contains("Connection: close")){
                this.isClose = true;
            }
            if (line.isEmpty()){
                //读到空行，就是请求头结束了
                break;
            }
        }
        response.setHeader(headerSb.toString());
        if (rl>=0){
            StringBuilder sb = new StringBuilder();
            byte[] bytes = new byte[rl];
            int m = is.read(bytes,0,rl);
            sb.append(new String(bytes,0,m));
            while (m<rl){
                int k =is.read(bytes,0,rl-m);
                m+=k;
                sb.append(new String(bytes,0,k));
            }
            response.setContent(sb.toString());
            if (isClose){
                close();
            }
            return response;
        }else {
            //chunked的情况
            StringBuilder sb =new StringBuilder();
            int n= 0;
            String line = readLine(is);
            while (line.isEmpty()){
                line = readLine(is);
            }
            n= Integer.valueOf(line,16);
            do{
                byte[] bytes = new byte[n];
                int m =is.read(bytes,0,n);
                sb.append(new String(bytes,0,m));
               // System.out.println(m+"/"+n);
                //可能没读完n个byte，然后就返回了，要继续读完
                while (m<n){
                    int k =is.read(bytes,0,n-m);
                    m+=k;
                  //  System.out.println(m+"/"+n);
                    sb.append(new String(bytes,0,k));
                }
                line = readLine(is);
                while (line.isEmpty()){
                    line = readLine(is);
                }
                n= Integer.valueOf(line,16);
                if (n ==0){
                    readLine(is);
                }
            }while (n>0);
            response.setContent(sb.toString());
            if (isClose){
                close();
            }
            return response;
        }

    }

    private String readLine(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        while (true){
            byte[] bytes = new byte[1];
            is.read(bytes);
            String s = new String(bytes);
            short b = bytes[0];
            if (s.equals("\n")|| b == 0){
                break;
            }
            sb.append(s);
        }
        if (sb.toString().isEmpty()){
            return  readLine(is);
        }
        return sb.toString().trim();
    }

    public void close() throws IOException {
        this.isClose = true;
        socket.close();
    }

    public boolean isClose(){
        return isClose;
    }

    public Socket getSocket(){
        return socket;
    }

    public static void main(String[] args) {
        try {
            Socket2HttpClient client = new Socket2HttpClient("www.100bt.com",80);
            Map<String,String[]> params = new HashMap<String,String[]>();
            params.put("appType", new String[]{"17"});
            params.put("game", new String[]{"aoqi"});
            Map<String,File> fileMap = new HashMap<>();
            fileMap.put("picdata",new File("C:\\Users\\Public\\Pictures\\Sample Pictures\\game\\g (2).jpg"));
            Response content = null;
            try {
               // content = client.doPostFile("/gameUploadImage.action",params,fileMap);
               // System.out.println(content.getCode());
            }catch (Exception e){
                System.out.println(client.getSocket().isClosed());
                System.out.println(client.getSocket().isConnected());
                System.out.println(client.getSocket().isInputShutdown());
            }

           // System.out.println(content.getContent());
            for (int i=0;i<10000;i++){
                content = client.doGet("/xiaoshuo/");
                System.out.println(content.getCode());
            }

            System.out.println(content.getCode());
            System.out.println(content.getHeader());
          //  System.out.println(content.getContent());
            content = client.doGet("/shop/gameTools.json?gameType=6");
            System.out.println(content.getCode());
            System.out.println(content.getHeader());
            params = new HashMap<String,String[]>();
            params.put("gameType", new String[]{"6"});
            content = client.doPost("/shop/gameTools.html",params);
            System.out.println(content.getCode());
          //  System.out.println(content.getContent());
            content = client.doPost("/shop/gameTools.html",params);
            System.out.println(content.getCode());
           // System.out.println(content.getContent());
            client.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
