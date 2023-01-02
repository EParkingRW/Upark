package com.example.upark;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.example.upark.helpers.B;
import com.example.upark.helpers.backend.GarageBackend;
import com.example.upark.helpers.backend.SocketHandler;
import com.example.upark.models.Garage;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.function.Consumer;

import io.socket.client.Socket;

public class BackendService extends Service {
//    private final SocketBackend socketBackend;
    private final String TAG = this.getClass().getSimpleName();
    private Socket mSocket;
    public BackendService() {
//        socketBackend = new SocketBackend();
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Consumer<Garage> onGarage = (garage -> {
            Log.d("garage_message", garage.toString());
        });
        B.getInstance().getGarages(this,onGarage);

        initSocket();
        return super.onStartCommand(intent, flags, startId);
    }
    public void initSocket(){
        SocketHandler.setSocket();
        SocketHandler.establishConnection();
        mSocket = SocketHandler.getSocket();
        mSocket.on("garage", args -> {
            Log.d(TAG, Arrays.toString(args));
            Arrays.stream(args).forEach(eachGarage ->{
                try {
                    Log.d(TAG, "type of object :"+ eachGarage.getClass().getSimpleName());
                    JSONObject garageJson = ((JSONObject) eachGarage).getJSONObject("data");
                    B.getInstance().addGarage(GarageBackend.getGarageFromJson(garageJson));


                }catch (Exception e){
                    Log.e(TAG, e.getMessage()+"");
                }
            });
        });
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}