package com.example.hyvu.ntp.thread;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.example.hyvu.ntp.util.SntpClient;
import com.example.hyvu.ntp.view.activity.MainActivity;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class AccessAddressThread implements Runnable {
    private SntpClient sntpClient;
    private String address;
    private Context context;

    public AccessAddressThread(SntpClient sntpClient, String address, Context context) {
        this.sntpClient = sntpClient;
        this.address = address;
        this.context = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("ServiceCast")
    @Override
    public void run() {
        AccessNTP(address);
    }

    // TODO declare modifier
    @RequiresApi(api = Build.VERSION_CODES.M)
    void AccessNTP(String address) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        sntpClient.requestTime(address, 3000, connectivityManager.getActiveNetwork());
    }

}
