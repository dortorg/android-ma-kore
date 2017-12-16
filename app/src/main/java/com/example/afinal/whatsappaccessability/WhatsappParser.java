package com.example.afinal.whatsappaccessability;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import java.util.EmptyStackException;


/**
 * Created by Dor on 9/17/2017.
 */

public class WhatsappParser extends BaseScreenBlocker {

    private AccessibilityService context;
    private AccessibilityNodeInfo accessibilityNodeInfo;
    HandleChats handleChats;
    public WhatsappParser(AccessibilityService context)
    {
        this.context = context;
        handleChats = new HandleChats(context);
    }
    @Override
    protected synchronized boolean isRelevantScreen(AccessibilityEvent event) {

        if(event.getPackageName() != null) {
            if (event.getPackageName().toString().contains("whatsapp") &&
                    findContentDescription(context.getRootInActiveWindow(), false, "Emoji")) {
                return true;
            }
        }


            return false;
    }

    @Override
    protected void onUnblockDetected() {

    }

    @Override
    public void run() {


        Log.e("dordor", "whatsapp");
      //  Map m1 = new HashMap();
        try {
            if (isGroupChatPage(context.getRootInActiveWindow(), false) == false)
                handleChats.feed(context.getRootInActiveWindow());
        }catch(Exception e)
        {
            throw new EmptyStackException();
        }
         // Log.e("TAG-dor",toStringHierarchy(context.getRootInActiveWindow(), 0));
      /*  AccessibilityNodeInfo child1 = context.getRootInActiveWindow().getChild(0); // navigate up LinearLayout
        AccessibilityNodeInfo child2 = context.getRootInActiveWindow().getChild(1);// LinearLayout
        AccessibilityNodeInfo child2_1 = child2.getChild(0);//name textView
        //AccessibilityNodeInfo child2_2 = child2.getChild(1);

        AccessibilityNodeInfo child3 = context.getRootInActiveWindow().getChild(2);// Video Call ImageButton
        AccessibilityNodeInfo child4 = context.getRootInActiveWindow().getChild(3);//Call ImageButton
        AccessibilityNodeInfo child5 = context.getRootInActiveWindow().getChild(4); // More options ImageView
        AccessibilityNodeInfo child6 = context.getRootInActiveWindow().getChild(5);// ListView*/
      /*  AccessibilityNodeInfo child6_1 = child6.getChild(0);//viewGroup
        AccessibilityNodeInfo child6_1_1 = child6_1.getChild(0);//message
        AccessibilityNodeInfo child6_1_2 = child6_1.getChild(1);//time
      //  AccessibilityNodeInfo child6_1_3 = child6_1.getChild(2);//time
        //child6_1_1.getText().getClass().getName();

        AccessibilityNodeInfo child6_2 = child6.getChild(1);//viewGroup
        AccessibilityNodeInfo child6_2_1 = child6_2.getChild(0);//message
        AccessibilityNodeInfo child6_2_2 = child6_2.getChild(1);//time

        AccessibilityNodeInfo child6_3 = child6.getChild(2);//viewGroup
        AccessibilityNodeInfo child6_3_1 = child6_3.getChild(0);//message
        AccessibilityNodeInfo child6_3_2 = child6_3.getChild(1);//time

        AccessibilityNodeInfo child6_4 = child6.getChild(3);//viewGroup
        AccessibilityNodeInfo child6_4_1 = child6_4.getChild(0);//message
        AccessibilityNodeInfo child6_4_2 = child6_4.getChild(1);//time

        AccessibilityNodeInfo child6_5 = child6.getChild(4);//viewGroup
        AccessibilityNodeInfo child6_5_1 = child6_5.getChild(0);//message
        AccessibilityNodeInfo child6_5_2 = child6_5.getChild(1);//time

        AccessibilityNodeInfo child6_6 = child6.getChild(5);//viewGroup
        AccessibilityNodeInfo child6_6_1 = child6_6.getChild(0);//message
        AccessibilityNodeInfo child6_6_2 = child6_6.getChild(1);//time

        AccessibilityNodeInfo child6_7 = child6.getChild(6);//viewGroup
        AccessibilityNodeInfo child6_7_1 = child6_7.getChild(0);//message
        AccessibilityNodeInfo child6_7_2 = child6_7.getChild(1);//time
       // AccessibilityNodeInfo child6_7_2_1 = child6_7_1.getChild(0);//time*//**//*



        AccessibilityNodeInfo child6_8 = child6.getChild(7);//viewGroup
        AccessibilityNodeInfo child6_8_1 = child6_8.getChild(0);//message
        AccessibilityNodeInfo child6_8_2 = child6_8.getChild(1);//time
*/

      //  AccessibilityNodeInfo child7 = context.getRootInActiveWindow().getChild(6);//Emoji
      //  AccessibilityNodeInfo child8 = context.getRootInActiveWindow().getChild(7);//EditText
       // AccessibilityNodeInfo child9 = context.getRootInActiveWindow().getChild(8);// Attach ImageButton
       // AccessibilityNodeInfo child10 = context.getRootInActiveWindow().getChild(9);//Camera ImageButton
       // AccessibilityNodeInfo child11 = context.getRootInActiveWindow().getChild(10);//Voice Message ImageButton

      /*  String number = getPhoneNumber(accessibilityNodeInfo.getText().toString(), context);
        if(!number.contentEquals("Unsaved"))
            Toast.makeText(context,accessibilityNodeInfo.getText().toString() + ": " + number, Toast.LENGTH_LONG).show();*/

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
    public String getPhoneNumber(String name, Context context) {
        String ret = null;
        String selection = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" like'%" + name +"%'";
        String[] projection = new String[] { ContactsContract.CommonDataKinds.Phone.NUMBER};
        Cursor c = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                projection, selection, null, null);
        if (c.moveToFirst()) {
            ret = c.getString(0);
        }
        c.close();
        if(ret==null)
            ret = "Unsaved";
        return ret;
    }
    public boolean isChatPage(AccessibilityNodeInfo info)
    {
        for(int i = 0; i < info.getChildCount(); i++)
        {
            if(info.getChild(i).getClassName().toString().contains("ListView"))
            {
                AccessibilityNodeInfo child6_1_1 = info.getChild(i).getChild(0);//message
                if(child6_1_1 != null) {
                    if (child6_1_1.getText().getClass().getName().contains("SpannableString")) {

                    }
                }

            }
        }
        return false;
    }
    public boolean isGroupChatPage(AccessibilityNodeInfo info, boolean flag)
    {
        if (info == null || flag == true) return flag;

        if(info.getText() != null) {

            if((info.getText().toString().contains("You") ||
                    info.getText().toString().contains("tap here for group info")) &&
                    info.getParent().getClassName().toString().contains("LinearLayout"))
            {
               // Toast.makeText(context,info.getText().toString(),Toast.LENGTH_LONG).show();
                flag = true;
            }
        }
        for (int i = 0; i < info.getChildCount(); i++) {

            AccessibilityNodeInfo nodeInfo = info.getChild(i);
            flag = isGroupChatPage(nodeInfo, flag);
        }
        return flag;
    }

    private String toStringHierarchy(AccessibilityNodeInfo info, int depth) {
        if (info == null) return "";

        String result = "";
 /*       for (int i = 0; i < depth; i++) {
            result += "  ";
        }*/
        //if(info.getText() != null) {
            // if(info.getText().toString().contains("Moshe"))
            result += info.getClassName().toString() + " " + info.getChildCount() + "\n";
            //parent = info.getParent();
            //AccessibilityNodeInfo pparent = parent.getParent();
            // secondChild = pparent.getChild(3);*/


    //    }
        for (int i = 0; i < info.getChildCount(); i++) {

            AccessibilityNodeInfo nodeInfo = info.getChild(i);
            result +=  toStringHierarchy(nodeInfo, depth + 1);
        }

        return result;
    }

}
