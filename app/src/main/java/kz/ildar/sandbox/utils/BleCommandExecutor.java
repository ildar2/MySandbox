package kz.ildar.sandbox.utils;

import androidx.annotation.NonNull;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class BleCommandExecutor implements CommandExecutor {
    private final BleDevice bleDevice;
    private final Executor executor = Executors.newFixedThreadPool(10);
    private final Map<String, Queue<Function1<? super String, Unit>>> callbacks = new ConcurrentHashMap<>();

    public BleCommandExecutor(BleDevice bleDevice) {
        this.bleDevice = bleDevice;
    }

    @Override
    public void requestAsync(@NonNull String command, @NonNull Function1<? super String, Unit> callback) {
        Queue<Function1<? super String, Unit>> callbacksQueue = callbacks.get(command);
        if (callbacksQueue != null) {
            callbacksQueue.add(callback);
        } else {
            callbacksQueue = new ConcurrentLinkedQueue<>();
            callbacksQueue.add(callback);
            callbacks.put(command, callbacksQueue);

            executor.execute(() -> {
                String result = bleDevice.requestSync(command);
                Queue<Function1<? super String, Unit>> cbQueue = callbacks.remove(command);
                if (cbQueue != null) {
                    for (Function1<? super String, Unit> cb : cbQueue) {
                        cb.invoke(result);
                    }
                }
            });
        }
    }
}