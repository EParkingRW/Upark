package com.example.upark.helpers.backend;
import android.util.Log;
import com.example.upark.constants.Const;
import io.socket.client.IO;
import io.socket.client.Socket;

public class SocketHandler {
    static Socket mSocket;

    public static void setSocket(){
        Log.d(SocketHandler.class.getSimpleName(), "setSocket runs");
        try {
// "http://10.0.2.2:3000" is the network your Android emulator must use to join the localhost network on your computer
// "http://localhost:3000/" will not work
// If you want to use your physical phone you could use the your ip address plus :3000
// This will allow your Android Emulator and physical device at your home to connect to the server
            mSocket = IO.socket(Const.BACKEND_URL);
            Log.d(SocketHandler.class.getSimpleName(), "Socket connect successful");
        } catch (Exception e) {
            Log.d(SocketHandler.class.getSimpleName(), e.getMessage() +"");
        }
    }
    public static Socket getSocket(){
        return mSocket;
    }
    public static void establishConnection(){
        mSocket.connect();
    }
    public static void closeConnection() {
        mSocket.disconnect();
    }
}
