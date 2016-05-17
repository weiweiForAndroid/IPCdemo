package com.test.ipcdemo;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class IPCService extends Service {

    private CopyOnWriteArrayList<Book> mBookList = new CopyOnWriteArrayList<>();
    /**
     * 存储远程回调数据。
     */
    private RemoteCallbackList<IOnNewBookArrivedListener> listeners = new RemoteCallbackList<>();
    private AtomicBoolean Aboolean = new AtomicBoolean(false);
    private Binder mBinder = new IBookManager.Stub() {
        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public void addBook(Book book) throws RemoteException {
            if (book != null) {
                mBookList.add(book);
                NLog.error("客户端添书" + book);
            }
        }

        @Override
        public List<Book> getBookList() throws RemoteException {

            return mBookList;
        }

        @Override
        public void registerListener(IOnNewBookArrivedListener listener) throws RemoteException {
            listeners.register(listener);
        }

        @Override
        public void unregisterListener(IOnNewBookArrivedListener listener) throws RemoteException {
            listeners.unregister(listener);
        }
    };

    public IPCService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mBookList.add(new Book("IPC1", 1));
        mBookList.add(new Book("IPC2", 2));
        for (Book book : mBookList
                ) {
            NLog.error("服务端自主添加图书：" + book);
        }
        /*每5秒添加一本图书*/

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!Aboolean.get()) {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    int bookId = mBookList.size() + 1;
                    Book book = new Book("newBook" + bookId, bookId);
                    try {
                        onNewBookArrived(book);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    private void onNewBookArrived(Book book) throws RemoteException {
        mBookList.add(book);
        final int N = listeners.beginBroadcast();
        for (int i = 0; i < N; i++) {
            IOnNewBookArrivedListener listener = listeners.getBroadcastItem(i);
            if (listener != null) {
                listener.OnNewBookArrived(book);
            }
        }
        listeners.finishBroadcast();
    }

}
