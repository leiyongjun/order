package com.bdqn.order.util;


import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Author: zhouhe
 * @Date: 2019/4/10 16:56
 */
public class SocketServer {

    private static class ClientHandler implements Runnable {

        private Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                //封装输入流（接收客户端的流）
                BufferedInputStream bis = new BufferedInputStream(
                        socket.getInputStream());
                DataInputStream dis = new DataInputStream(bis);
                byte[] bytes = new byte[1]; // 一次读取一个byte

                System.out.println("bytes:"+bytesToHexString(bytes));

                String ret = "";
                while (dis.read(bytes) != -1) {
                    ret += bytesToHexString(bytes) + "";
                    if (dis.available() == 0) { //一个请求
                        System.out.println(socket.getRemoteSocketAddress() + ":" + ret);
                        System.out.println("转换为字符串后:"+hexStringToString(ret));
                        ret = "";
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                System.out.println("client is over");
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * byte[]数组转换为16进制的字符串
     *
     * @param bytes 要转换的字节数组
     * @return 转换后的结果
     */
    public static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    /**
     * 16进制转换成为string类型字符串
     * 这个方法中文会乱码，字母和数字都不会乱码
     *
     * @Author zhouhe
     * @param s
     * @return
     */
    public static String hexStringToString(String s) {
        if (s == null || s.equals("")) {
            return null;
        }
        s = s.replace(" ", "");
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            s = new String(baKeyword, "UTF-8");
            new String();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return s;
    }


    //测试方法
    public static void main(String[] args) {
        ServerSocket server = null;
        try {
            server = new ServerSocket(10010);
            while (true) {
                System.out.println("listening...");

                Socket socket = server.accept();
                System.out.println("连接客户端地址：" + socket.getRemoteSocketAddress());
                System.out.println("connected...");
                ClientHandler handler = new ClientHandler(socket);
                Thread t = new Thread(handler);
                t.start();

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (server != null) {
                try {
                    server.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}