package me.elrhezzalimanal.jobschedulerdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;

import static me.elrhezzalimanal.jobschedulerdemo.SampleJobService.BUNDLE_NUMBER_KEY;

public class MainActivity extends AppCompatActivity {

    private static final int  DOWNLOAD_JOB_ID = 201;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initJobScheduler();
    }

    private void initJobScheduler() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ComponentName componentName = new ComponentName(this, SampleJobService.class);
            PersistableBundle bundle = new PersistableBundle();
            bundle.putInt(BUNDLE_NUMBER_KEY,10);
            JobInfo.Builder builder = new JobInfo.Builder(DOWNLOAD_JOB_ID, componentName)
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                    .setExtras(bundle)
                    .setPersisted(true);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                builder.setPeriodic(15*60*1000, 30*60*1000);
            }else {
                builder.setPeriodic(15*60*1000);
            }

            JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
            scheduler.schedule(builder.build());
        }
    }
}