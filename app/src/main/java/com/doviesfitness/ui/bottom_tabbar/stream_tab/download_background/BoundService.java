package com.doviesfitness.ui.bottom_tabbar.stream_tab.download_background;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.util.Random;

public class BoundService extends Service {
    public BoundService() {
    }//Instance of inner class created to provide access  to public methods in this class
    private final IBinder localBinder = new MyBinder();
    @Override
    public IBinder onBind(Intent intent) {

        return localBinder;
    }

    /**
     This method is  Called when activity have disconnected from a particular interface published by the service.
     Note: Default implementation of the  method just  return false */    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    /**
     * Called when an activity is connected to the service, after it had
     * previously been notified that all had disconnected in its
     * onUnbind method.  This will only be called by system if the implementation of onUnbind method was overridden to return true.
     */
    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    /**
     * Called by the system to notify a Service that it is no longer     used and is being removed.  The
     * service should clean up any resources it holds (threads,       registered
     * receivers, etc) at this point.  Upon return, there will be no more calls
     * in to this Service object and it is effectively dead.
     */    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    // This method creates a random number and return it
     public int randomGenerator(){
        Random randomNumber = new Random();

        int luckyNumber = randomNumber.nextInt();

        return luckyNumber;

    }
    public class MyBinder extends Binder {

      public   BoundService getService() {
            return BoundService.this;

        }
    }
}