package com.test.ipcclientapplication;

import java.io.Closeable;
import java.io.IOException;

/**
 * 实现了Closeable 接口的都可以在这里关闭。
 */
public class IOUtil {
    public  static void close(Closeable closeable){
        if (null != closeable){
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
