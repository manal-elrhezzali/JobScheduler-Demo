package me.elrhezzalimanal.jobschedulerdemo;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.AsyncTask;
import android.os.Build;
import android.os.PersistableBundle;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class SampleJobService extends JobService {
    private static final String TAG = "SampleJobService";

    public static final String BUNDLE_NUMBER_KEY = "number";
    private DownloadAsyncTask asyncTask;
    private JobParameters parameters;

    @Override
    public boolean onStartJob(JobParameters params) {
        parameters = params;
        PersistableBundle bundle = params.getExtras();
        int number = bundle.getInt(BUNDLE_NUMBER_KEY, -1);
        asyncTask = new DownloadAsyncTask();
        if (number != -1) {
            asyncTask.execute(1000);
        }

//        return false;//by returning false you mean that you have successfully handled the job
        // and you don't want to re-schedule it

        //in this example we handed the job to the asyncTask so we aren't sure
        // if its handled successfully or not ,that is why we're going to return true
        return true;
    }

    // this method gets called when the android system kills your job
// for example your job might get killed if another job with higher priority comes in
//or if the criteria you defined for your job is no longer satisfied
// for example if you schedule your job whenever you have a network connection,
// if the connection is lost the onStopJob will be called
    @Override
    public boolean onStopJob(JobParameters params) {
        if (null != asyncTask) {
            if (!asyncTask.isCancelled()) {
                asyncTask.cancel(true);
            }
        }
        Log.d(TAG, "onStopJob: Job Cancelled !");
        return true;  // specifies if you want to re-schedule your job after it was cancelled
                      // returning false means you don't want to re-schedule it
                      // for example if you schedule your job whenever you have a network connection,
                      // if the connection is lost the onStopJob will be called,
                      // and if you return true the job will be re-scheduled
                      // once the connection is restored
    }


    private class DownloadAsyncTask extends AsyncTask<Integer, Integer, String> {

        @Override
        protected String doInBackground(Integer... integers) {
            for (int i = 0; i < integers[0]; i++) {
                publishProgress(i);
                SystemClock.sleep(1000);
            }
            return "DownloadAsyncTask is finished";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            Log.d(TAG, "onProgressUpdate: the value of i was " + values[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d(TAG, "onPostExecute: got this passed to it as a parameter " + s);
            //use this method to finish the job whenever the task is completed
            jobFinished(parameters, true);//passing true means you want to reschedule the job
        }
    }
}
