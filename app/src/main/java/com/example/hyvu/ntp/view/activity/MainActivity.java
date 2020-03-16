package com.example.hyvu.ntp.view.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import com.example.hyvu.ntp.R;
import com.example.hyvu.ntp.thread.AccessAddressThread;
import com.example.hyvu.ntp.util.SntpClient;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private final static String[] ADDRESS = {"2.vn.pool.ntp.org", "0.asia.pool.ntp.org", "1.asia.pool.ntp.org"};
    private final static int COREPOOLSIZE = 3;
    private final static int MAXPOOLSIZE = 4;
    private final static int QUENECAPACITY = 5;

    private long now;
    private TextView textViewTimeSystem;
    private TextView textViewNtp;
    public SntpClient[] sntpClient;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AccessAddressThread.finished = false;
        Log.d("onCreate"," Created");
        textViewTimeSystem = findViewById(R.id.textView_systemTime);
        textViewNtp = findViewById(R.id.textView_timeNTP);
        sntpClient = new SntpClient[3];
        for(int i=0;i<3;i++){
            sntpClient[i]=new SntpClient();
        }
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                COREPOOLSIZE,
                MAXPOOLSIZE,
                1000,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingDeque<Runnable>(QUENECAPACITY));

        for (int i=0;i< sntpClient.length;i++) {
            threadPoolExecutor.execute(new AccessAddressThread(sntpClient[i], ADDRESS[i], getApplicationContext(), new AccessAddressThread.NtpListener() {
                @Override
                public void returnNtpTime(long time) {
                    now = time;
                    SimpleDateFormat df = new SimpleDateFormat("h:mm", Locale.US);
                    textViewNtp.setText(df.format(now));
                    Date date = Calendar.getInstance().getTime();
                    textViewTimeSystem.setText(df.format(date));
                }
            }));
        }
        threadPoolExecutor.shutdown();
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

    @Override
    protected void onResume() {
        super.onResume();
    }
}
