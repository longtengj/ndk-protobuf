package com.xc.platform.protocol.support;


public class PlatformProvider {
    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("platform-lib");
    }


    public PlatformProvider() {
    }

    public static int platformProviderInit() {

        return platformInit();
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public static native PlatformProvider getPlatformProviderInsance();

    public static native int platformInit();

    /**
     * C调用java中的带两个参数的方法
     *
     * @param msg
     * @param msgType
     */
    public void notifyMsgCallBack(byte[] msg, int msgType) {


    }

}
