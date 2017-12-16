package com.example.afinal.whatsappaccessability;

import android.accessibilityservice.AccessibilityService;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

/**
 * Created by Dor on 9/3/2017.
 */

public class MyAccessibilityService extends AccessibilityService {
    static int flag =0;
    AccessibilityNodeInfo parent;
    AccessibilityNodeInfo secondChild;
    TextChangeAccessibility textChangeAccessibility = new TextChangeAccessibility();
    private static final String TAG = "AccessibilityService";
//PhoneAdministratorsBlocker phoneAdministratorsBlocker = new PhoneAdministratorsBlocker();
//String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/dor";

    //TextChangeAccessibility textChangeAccessibility = new TextChangeAccessibility();
WhatsappParser whatsappParser = new WhatsappParser(this);
   // AccessibilityServiceBlocker accessibilityServiceBlocker = new AccessibilityServiceBlocker();
    @Override
    public void onCreate() {

    }

    @Override
    public void onInterrupt() {
    }




    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {




        //PhoneAdministratorsBlocker.getInstance(this).run();
        //Toast.makeText(this,"fuckkkkkkkk",Toast.LENGTH_LONG).show();
        //if(event.getEventType() == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED)
        {
           // if(flag == 0)
            {
                flag++;
                //Toast.makeText(this,"start",Toast.LENGTH_LONG).show();
            }
            try {
                whatsappParser.feed(event);
            }catch (Exception e)
            {
                Toast.makeText(this,"Ooops",Toast.LENGTH_LONG).show();
            }
            //textChangeAccessibility.load(event,this);
        }
      //  AccessibilityEvent.TEXT
     /*  if (event.getPackageName().toString().contains("whatsapp")) {

          if (isFlipkartProdcutDetailPage(event.getSource())) {

            }

       }*/

        /*switch (event.getEventType()) {
            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED: {
                if (event.getPackageName().toString().contains("whatsapp")) {
                    if(findContactByName("ליאל"))
                    {
                        Toast.makeText(this,"nice",Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(this,"fuckkkkkkkk",Toast.LENGTH_LONG).show();

                    }*/
               // }
           // }

  //      }
            // Log.i(TAG, "getText: " + event.getSource().getText().toString());
            //  Log.i(TAG, "getContentDescription: " + event.getSource().getContentDescription());/*

       /* if(event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            PhoneAdministratorsBlocker.getInstance(this).feed(event);
        }*/


            // AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED;


            //final int eventType = event.getEventType();
            //Toast.makeText(this,event.getText().toString(),Toast.LENGTH_LONG ).show();

            // Log.i(TAG, "TEXT: " + event.getPackageName().toString());
            //  Log.i(TAG, "TEXT: " + event.getText().toString());
            //textChangeAccessibility.notifyEvent();
     /*   switch(event.getEventType())
        {
            case AccessibilityEvent.TYPE_VIEW_CLICKED:
                Log.i(TAG, "TEXT: " + event.getText().toString());
                break;
        }*/
            //
            //Toast.makeText(this,event.getPackageName(),Toast.LENGTH_LONG ).show();

            //accessibilityServiceBlocker.feed(event, this);


            //textChangeAccessibility.load(event, this);
    }

    public boolean findContactByName(String name)
    {
        ContentResolver contentResolver = this.getContentResolver();
        Uri uri = ContactsContract.Data.CONTENT_URI;
        String[] projection = new String[] { ContactsContract.PhoneLookup._ID };
        String selection = ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME + " = ?";
        String[] selectionArguments = { name };
        Cursor cursor = contentResolver.query(uri, projection, selection, selectionArguments, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                return true;
            }
        }


        return false;
    }


    public boolean isFlipkartProdcutDetailPage(AccessibilityNodeInfo nodeInfo) {
        //Use the node info tree to identify the proper content.
        //For now we'll just log it to logcat.
        Log.w("TAG", toStringHierarchy(nodeInfo, 0));
      //  Toast.makeText(this,toStringHierarchy(nodeInfo, 0),Toast.LENGTH_LONG).show();
        return true;
    }



    private String toStringHierarchy(AccessibilityNodeInfo info, int depth) {
        if (info == null) return "";

        String result = "|";
        for (int i = 0; i < depth; i++) {
            result += "  ";
        }
        if(info.getText() != null) {
           // if(info.getText().toString().contains("Moshe"))
            result += info.getViewIdResourceName();
             //parent = info.getParent();
            //AccessibilityNodeInfo pparent = parent.getParent();
            // secondChild = pparent.getChild(3);*/


        }
        for (int i = 0; i < info.getChildCount(); i++) {

            AccessibilityNodeInfo nodeInfo = info.getChild(i);
            result += "\n" + toStringHierarchy(nodeInfo, depth + 1);
        }

        return result;
    }

    public File getTempFile(Context context, String url) {
        File file = null;
        try {
            String fileName = Uri.parse(url).getLastPathSegment();
            file = File.createTempFile(fileName, null, context.getCacheDir());
        } catch (IOException e) {
            // Error while creating file
        }
        return file;
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }





}