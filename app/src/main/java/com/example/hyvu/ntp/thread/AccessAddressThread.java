package com.example.hyvu.ntp.thread;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.example.hyvu.ntp.util.SntpClient;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class AccessAddressThread implements Runnable {
    private SntpClient sntpClient;
    private String address;
    private Context context;
    private NtpListener ntpListener;

    public static boolean finished = false;

    private static final Object lock = new Object();

    public AccessAddressThread(SntpClient sntpClient, String address, Context context,NtpListener ntpListener) {
        this.sntpClient = sntpClient;
        this.address = address;
        this.context = context;
        this.ntpListener = ntpListener;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("ServiceCast")
    @Override
    public void run() {
        AccessNTP(address);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void AccessNTP(String address) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        if(finished!=true){
            if(sntpClient.requestTime(address, 3000, connectivityManager.getActiveNetwork())){
                synchronized (lock) {
                    if (finished != true) {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Log.d("AccessAddressThread", "abc");
                        finished = true;
                        ntpListener.returnNtpTime(sntpClient.getNtpTime());
                    }
                }
            }
        }
    }
    public interface NtpListener{
        void returnNtpTime(long time);
    }
}
