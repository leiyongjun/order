package com.bdqn.order.util.common;


import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.Socket;
import java.util.UUID;

@Slf4j
public class ChatClient {

    public static void main(String[] args) throws IOException {
        String host = "127.0.0.1";
        int port = 18089;

        //与服务端建立连接
        Socket socket = new Socket(host, port);
        socket.setOOBInline(true);

        //建立连接后获取输出流
        DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
        DataInputStream inputStream = new DataInputStream(socket.getInputStream());
        String uuid = UUID.randomUUID().toString();
        log.info("uuid: {}", uuid);
        outputStream.write(uuid.getBytes());
        DataInputStream inputStream1 = new DataInputStream(socket.getInputStream());
        String content = "";
        int i = 0;
        while (true){
            i += 1;
            byte[] buff = new byte[1024];
            inputStream.read(buff);
            String buffer = new String(buff, "utf-8");
            content = buffer;
            log.info("info: {}", content);

            outputStream.write(("send data : " + i).getBytes());
//            File file = new File("json.json");
//            FileWriter fileWriter = new FileWriter(file);
//            fileWriter.write(content);
//            fileWriter.flush();
        }
    }
}