package com.test.ipcclientapplication;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import com.test.ipcdemo.Book;
import com.test.ipcdemo.IBookManager;
import com.test.ipcdemo.IOnNewBookArrivedListener;

import java.util.List;

public class IbinderActivity extends AppCompatActivity {
    private IBookManager remote;
    private static final int NEW_BOOK_ARRIVED = 1;
    private static final int NEW_MESSAGE = 2;
    private static final int SOCKET_CONNECTED = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket);

        Intent intent = new Intent();
        intent.setAction("com.test.ipcdemo.ipcservice");
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    private IBinder.DeathRecipient deathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            if (remote == null) return;
            try {
                remote.asBinder().linkToDeath(deathRecipient, 0);
                remote = null;
                Intent intent = new Intent();
                intent.setAction("com.test.ipcdemo.ipcservice");
                bindService(intent, connection, Context.BIND_AUTO_CREATE);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    };

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            IBookManager manager = IBookManager.Stub.asInterface(service);
            try {
                service.linkToDeath(deathRecipient, 0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            remote = manager;
            try {
                manager.addBook(new Book("客户端书籍", 3));
                List<Book> list = manager.getBookList();
                for (Book book : list
                        ) {
                    NLog.error("服务器书籍：" + book);
                }
                manager.registerListener(listener);
                NLog.error("客户端发起注册监听器");
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
        try {
            if (remote != null && remote.asBinder().isBinderAlive()) {
                remote.unregisterListener(listener);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private IOnNewBookArrivedListener listener = new IOnNewBookArrivedListener.Stub() {

        /**
         * Demonstrates some basic types that you can use as parameters
         * and return values in AIDL.
         *
         * @param anInt
         * @param aLong
         * @param aBoolean
         * @param aFloat
         * @param aDouble
         * @param aString
         */
        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public void OnNewBookArrived(Book newbook) throws RemoteException {
            handler.obtainMessage(NEW_BOOK_ARRIVED, newbook).sendToTarget();

        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);

        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case NEW_BOOK_ARRIVED:
                    NLog.error("新书到了:" + msg.obj);
                    break;
                case NEW_MESSAGE:
//                    serviceMessage.setText("服务器消息：" + String.valueOf(msg.obj));
                    break;

                case SOCKET_CONNECTED:
//                    send.setEnabled(true);
                    break;

                default:
                    super.handleMessage(msg);
            }
        }
    };


}
