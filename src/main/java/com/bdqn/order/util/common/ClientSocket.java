package com.bdqn.order.util.common;

import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

/**
 * @author zhoujian
 * 自定义封装的连接的客户端
 */
@Slf4j
@Data
public class ClientSocket implements Runnable{

    private Socket socket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private String key;
    private String message;

    @SneakyThrows
    @Override
    public void run() {
        int i = 0;
        //每5秒进行一次客户端连接，判断是否需要释放资源
        while (true){
            i += 1;
            try {
                TimeUnit.SECONDS.sleep(5);
                byte[] b = new byte[1024];
                socket.getInputStream().read(b);
                log.info("这是第"+i+"接收信息:" + new String(b, "utf-8"));

                socket.getOutputStream().write(("这是第"+i+"次发送信息").getBytes("utf-8"));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            /**
            if (SocketHandler.isSocketClosed(this)){
                log.info("客户端已关闭,其Key值为：{}", this.getKey());
                //关闭对应的服务端资源
                SocketHandler.close(this);
                break;
            }
             */
        }
    }
}