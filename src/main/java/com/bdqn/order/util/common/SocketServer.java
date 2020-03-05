package com.bdqn.order.util.common;

import com.alibaba.fastjson.JSON;
import com.bdqn.order.pojo.UserInfo;
import com.bdqn.order.service.UserService;
import com.rabbitmq.tools.json.JSONUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Data
@Component
@NoArgsConstructor
public class SocketServer {

    @Value("${socket.port}")
    private Integer port;
    private boolean started;
    private ServerSocket serverSocket;
    private ExecutorService executorService = Executors.newCachedThreadPool();

    public static void main(String[] args){
        new SocketServer().start(18089);
    }

    public void start(){
        start(port);
    }
    @Autowired
    private UserService userService;//测试使用


    public void start(Integer port){
        log.info("port: {}, {}", this.port, port);
        try {
            serverSocket =  new ServerSocket(port == null ? this.port : port);
            started = true;
            log.info("Socket服务已启动，占用端口： {}", serverSocket.getLocalPort());
        } catch (IOException e) {
            log.error("端口冲突,异常信息：{}", e);
            System.exit(0);
        }

        int i = 0;
        while (started){
            i = i++;
            try {
                // 接收到socket客户端连接
                Socket socket = serverSocket.accept();
                socket.setKeepAlive(true);

                /**
                // 读取客户端发送过来的数据, 写入bytes数组
                byte[] bytes = new byte[1024];
                socket.getInputStream().read(bytes);
                // 将bytes数组转为String对象
                String msg = new String(bytes, "utf-8");
                log.info("接收到来做客户端的数据: " + msg);
                // 返回数据给到客户端
                socket.getOutputStream().write(("返回数据: " + 1).getBytes("utf-8"));
                **/
                ClientSocket register = SocketHandler.register(socket);
                log.info("客户端已连接，其Key值为：{}", register.getKey());
                UserInfo userInfo = userService.getUserInfoById(5);
                SocketHandler.sendMessage(register, JSON.toJSONString(userInfo));
                if (register != null){
                    executorService.submit(register);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
