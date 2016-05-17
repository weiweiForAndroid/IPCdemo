package com.test.ipcclientapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import static com.test.ipcclientapplication.R.id.button2;

public class SocketActivity extends Activity {

    private Button send;
    private Button toIbinder;
    private EditText message;
    private TextView serviceMessage;
    private Socket mClientSocket;
    private PrintWriter writer;
    BufferedReader br;
    Socket socket = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        message = (EditText) findViewById(R.id.editText);
        toIbinder = (Button) findViewById(button2);
        toIbinder.setOnClickListener(new View.OnClickListener() {
            /**
             * Called when a view has been clicked.
             *
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SocketActivity.this,IbinderActivity.class));

            }
        });
        serviceMessage = (TextView) findViewById(R.id.textView);
        send = (Button) findViewById(R.id.button);
        send.setOnClickListener(new View.OnClickListener() {
            /**
             * Called when a view has been clicked.
             *
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.button) {
                    String msg = message.getText().toString();
                    NLog.error("准备发送的消息：" + msg);
                    writer.println(msg);
                    writer.flush();
                }
            }
        });
        new Thread() {
            @Override
            public void run() {
                connectedTcpServer();
            }
        }.start();

    }





    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    private void connectedTcpServer() {

        while (socket == null) {
            try {
                NLog.error("准备连接" + socket);

                socket = new Socket("localhost", 8688);
                NLog.error("连接成功" + socket);
                send.setEnabled(true);

                mClientSocket = socket;
                writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(mClientSocket.getOutputStream())));
                br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                mClientSocket = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    public void onStop() {
        super.onStop();
        if (mClientSocket != null) {
            try {
                mClientSocket.shutdownInput();
                mClientSocket.close();
                IOUtil.close(writer);
                IOUtil.close(br);
                socket.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
