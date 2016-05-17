package com.test.ipcdemo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainActivity extends Activity {
    private String[] messages = new String[]{"你好！", "我册那fuck！", "什么鬼！"};
    private AtomicBoolean Aboolean = new AtomicBoolean(true);
    private TextView message;
    PrintWriter writer;
    BufferedReader in;
    ServerSocket serverSocket = null;
    private android.os.Handler handler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            message.setText(msg.obj.toString());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        message = (TextView) findViewById(R.id.textview);
        new Thread(new TcpSocketRunnable()).start();
    }

    private class TcpSocketRunnable implements Runnable {

        /**
         * Starts executing the active part of the class' code. This method is
         * called when a thread is started that has been created with a class which
         * implements {@code Runnable}.
         */
        @Override
        public void run() {

            try {
                serverSocket = new ServerSocket(8688);
            } catch (IOException e) {
                e.printStackTrace();
            }
            while (Aboolean.get()) {
                try {
                    final Socket client = serverSocket.accept();
                    new Thread() {
                        @Override
                        public void run() {

                            try {
                                responeClient(client);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

        private void responeClient(Socket socket) throws IOException {
            //用于接受客户端信息
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //用于向客户端发送消息
            writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            writer.println("欢迎来到聊天室");
            while (Aboolean.get()) {
                String str = in.readLine();
                NLog.error("从客户端发来消息：" + str);
                if (str == null) {
                    //客户端连接

                    break;
                }
                Message message = new Message();
                message.obj = str;
                handler.sendMessage(message);
//                message.setText(str + "");
                int i = new Random().nextInt(messages.length);
                String msg = messages[i];
                writer.println(msg);
                writer.flush();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        IOUtil.close(in);
        IOUtil.close(writer);
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
