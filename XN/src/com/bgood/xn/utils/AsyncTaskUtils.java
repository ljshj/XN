package com.bgood.xn.utils;

import android.os.AsyncTask;
import android.os.Build;

public final class AsyncTaskUtils {
    private AsyncTaskUtils() {}

    /** Returns whether the task specified as an argument is running or not.
     * @param theTask {@link AsyncTask} to be checked.
     * @return True if the task is running, false otherwise.
     */
    public static boolean isRunning(final AsyncTask<?, ?, ?> theTask) {
        return (theTask != null && 
                theTask.getStatus().equals(AsyncTask.Status.RUNNING));
    }

    /**
     * Abort the running task.
     *
     * @param theTask
     * @return True if the task has been successfully aborted. False otherwise.
     */
    public static boolean abort(AsyncTask<?, ?, ?> theTask) {
        if (!AsyncTaskUtils.isRunning(theTask)) {
            return false;
        }
        theTask.cancel(true);
        theTask = null;
        return true;
    }
    
    /**
     * Execute async task immediately
     * 
     * @param asyncTask
     */
    public static <Params, Progress, Result> void executeAsyncTask(
            AsyncTask<Params, Progress, Result> task, Params... params) {
        if (Build.VERSION.SDK_INT >= 11) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
        } else {
            task.execute(params);
        }
    }
}


//AsyncTaskUtils.executeAsyncTask(new AsyncTask<Void, Void, Void>() {
//
//	@Override
//	protected Void doInBackground(Void... params) {
//		documentsDaoHelper.delete(delFileId);//删除本地数据库里的对应文件
//		return null;
//	}
//});
