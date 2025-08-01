package com.surajvanshsv.workmanagerapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Constraints;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public static final String KEY_COUNTER_VALUE = "key_count";
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        btn = findViewById(R.id.button);



        /***
         * Once your work is defined, it must be scheduled with the WorkManager
         * service in order to run. WorkManager offers a lot of flexibility in how you schedule your work.
         *       1- You can schedule it to run periodically over an interval of time,
         *       2- or you can schedule it to run only one time.
         *
         * However you choose to schedule the work, you will always use a WorkRequest.
         * While a Worker defines the unit of work, a WorkRequest (and its subclasses) define how
         * and when it should be run. In the simplest case, you can use a OneTimeWorkRequest,
         * as shown in the following example.
         */

        /**
         *   Working with Constraints
         *   Running the worker under certain conditions: Device is connected to WiFi, or Battery is above 50% ....
         */
//        Constraints constraints = new Constraints.Builder()
//                .setRequiresCharging(true)
//                .build();

        /**
         * Data Creation
         * */
        Data data = new Data.Builder().putInt(KEY_COUNTER_VALUE, 500).build();


        WorkRequest countWorkRequest = new
                OneTimeWorkRequest
                        .Builder(DemoWorker.class)
                //    .setConstraints(constraints)
                .setInputData(data)
                .build();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**The exact time that the worker is going to be executed depends on the constraints that are used in your
                 * WorkRequest and on system optimizations.
                 * WorkManager is designed to give the best behavior under these restrictions.
                 * */

                WorkManager.getInstance(getApplicationContext()).enqueue(countWorkRequest);
            }
        });



        // Let's Display the Status of Our Worker
        WorkManager.getInstance(getApplicationContext()).getWorkInfoByIdLiveData(countWorkRequest.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        if (workInfo != null){


                            Toast.makeText(getApplicationContext(),
                                    "Status: "+workInfo.getState().name(),
                                    Toast.LENGTH_SHORT).show();


                            if (workInfo.getState().isFinished()){
                                Data data1 = workInfo.getOutputData();
                                String msg = data1.getString(DemoWorker.KEY_WORKER);
                                Toast.makeText(getApplicationContext(), ""+msg, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });




        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}