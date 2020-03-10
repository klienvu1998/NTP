package com.example.hyvu.ntp.view.activity;

// TODO organize import
import android.net.ConnectivityManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hyvu.ntp.R;
import com.example.hyvu.ntp.thread.AccessAddressThread;
import com.example.hyvu.ntp.util.SntpClient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    // TODO declare modifiers
    final static int corePoolSize=3;
    final static int maxPoolSize=4;
    final static int queneCapacity=5;

    long now;
    // TODO camel case
    TextView tv_timeSystem;
    TextView tv_NTP;
    SntpClient sntpClient;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO constants should not be defined here
        final String[] address={"2.vn.pool.ntp.org","0.asia.pool.ntp.org","1.asia.pool.ntp.org"};

        // TODO spacing
        tv_timeSystem=findViewById(R.id.textView_systemTime);
        tv_NTP=findViewById(R.id.textView_timeNTP);
        sntpClient = new SntpClient();

        // TODO line break
        ThreadPoolExecutor threadPoolExecutor=new ThreadPoolExecutor(corePoolSize,maxPoolSize,1000,TimeUnit.MILLISECONDS,new LinkedBlockingDeque<Runnable>(queneCapacity));
        // TODO read the warning
        for(int i=0;i<address.length;i++) {
            threadPoolExecutor.execute(new AccessAddressThread(sntpClient, address[i], getApplicationContext()));
        }

        // TODO are you serious?
        while(true) {
            if (threadPoolExecutor.getCompletedTaskCount() > 0) {
                threadPoolExecutor.shutdownNow();
                now = sntpClient.getNtpTime();
                // TODO read the warning
                SimpleDateFormat df = new SimpleDateFormat("h:mm");
                tv_NTP.setText(df.format(now));
                Date date = Calendar.getInstance().getTime();
                tv_timeSystem.setText(df.format(date));
                break;
            }
        }
//        Thread thread = new Thread(){
//            @Override
//            public void run() {
//                super.run();
//                AccessNTP();
//            }
//        };
//        thread.start();
//        try {
//            thread.join();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

//        ExecutorService executorService=Executors.newFixedThreadPool(3);
//        Set<Callable<String>> callables = new HashSet<>();
//        callables.add(new Callable<String>() {
//            @Override
//            public String call() throws Exception {
//                AccessNTP("0.asia.pool.ntp.org");
//                return "0.asia.pool.ntp.org";
//            }
//        });
//        callables.add(new Callable<String>() {
//            @Override
//            public String call() throws Exception {
//                AccessNTP("2.vn.pool.ntp.org");
//                return "2.vn.pool.ntp.org";
//            }
//        });
//        callables.add(new Callable<String>() {
//            @Override
//            public String call() throws Exception {
//                AccessNTP("1.asia.pool.ntp.org");
//                return "1.asia.pool.ntp.org";
//            }
//        });
//
//        try {
//            String result=executorService.invokeAny(callables);
//            Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        executorService.shutdown();

    }

//    @RequiresApi(api = Build.VERSION_CODES.M)
//    void AccessNTP(String address) {
//        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
//        sntpClient.requestTime(address, 3000, connectivityManager.getActiveNetwork());
//    }


}
