package com.example.afinal.whatsappaccessability;

import android.util.Log;
import android.view.accessibility.AccessibilityEvent;



public abstract class BaseScreenBlocker implements Runnable{
    protected final String TAG = "myaccessability";
    private final int HITS_THRESHOLD = 10;
    private final long TIMEOUT = 10 * 1000;
    protected long firstHitTime;
    protected int hits;
    protected boolean isUnblocked;
    private boolean shouldBypass;
    protected Runnable blockHandler;

    public BaseScreenBlocker() {
        firstHitTime = 0;
        hits = 0;
        isUnblocked = false;
        setBlockHandler(this);
    }

    public void setBlockHandler(Runnable handler) {
        blockHandler = handler;
    }

    protected abstract boolean isRelevantScreen(AccessibilityEvent event);

    protected int getHitsThreshold() {
        return HITS_THRESHOLD;
    }

    protected long getTimeout() {
        return TIMEOUT;
    }

    public void feed(AccessibilityEvent event) {
;       if (isRelevantScreen(event)) {
            Log.e("AccessibilityService", "" + event.getEventType());
            onScreenEnter();
        } else {
            onSwitchToOtherScreen();
        }
    }

    private void onSwitchToOtherScreen() {
        Log.e(TAG, "onSwitchToOtherScreen");
        if (shouldBypass)
            return;

        isUnblocked = false;
    }

    private void onScreenEnter() {
        Log.e(TAG, "onScreenEnter");
        shouldBypass = false;
        if (isUnblocked) {
            Log.e(TAG, "Unblocked  mode => pass");
            return;
        }

        final long diff = System.currentTimeMillis() - firstHitTime;
        Log.e(TAG, "Diff: " + diff + " timeout: " + getTimeout());

        if (diff > getTimeout()) {
            Log.e(TAG, "timeout - start counting and perform BACK");
            firstHitTime = System.currentTimeMillis();
            hits = 1;
            performBlock();
        } else {
            hits++;
            Log.e(TAG, "Hits: " + hits + " Threshold: " + getHitsThreshold());
            if (hits >= getHitsThreshold()) {
                Log.e(TAG, "HitThreshold reached => Changed UNBLOCKED state and raise callback");
                isUnblocked = true;
                onUnblockDetected();
                hits = 0;
                firstHitTime = 0;
            } else {
                Log.e(TAG, "No enough hits yet.. perform BACK " + hits);
                isUnblocked = false;
                performBlock();
            }
        }
    }

    protected abstract void onUnblockDetected();

    protected void performBlock() {
        if (blockHandler != null) {
            blockHandler.run();
        }
    }

    public void bypass() {
        shouldBypass = true;
    }

}