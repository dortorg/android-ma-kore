package com.example.afinal.whatsappaccessability;

import android.accessibilityservice.AccessibilityService;
import android.graphics.Rect;
import android.os.Environment;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * Created by Dor on 10/2/2017.
 */

public class HandleChats
{
    static int count = 0;

    static Map<String, Chat> chats;
    File dir;
    AccessibilityService context;
    public HandleChats(AccessibilityService context)
    {
        this.context = context;
        chats = new HashMap<String, Chat>();
        dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/mywhatsapp");

        if(!dir.exists())
        {
            dir.mkdir();
        }
    }


    static void flush()
    {
        for (Map.Entry<String, Chat> entry : chats.entrySet())
        {
            entry.getValue().flush();
            //System.out.println(entry.getKey() + "/" + entry.getValue());
        }
    }




    public synchronized void feed(AccessibilityNodeInfo info) {
        try {
            if (info == null) return;


            if (info.getClassName().toString().contains("LinearLayout") && info.getChildCount() == 1) {
                AccessibilityNodeInfo child2 = info.getChild(0);
                String name = child2.getText().toString();
                Log.e("dordor", name);

                if (!chats.containsKey(name))
                    chats.put(child2.getText().toString(), new Chat(child2.getText().toString()));
                //chats.get(name).feed(context.getRootInActiveWindow());
                parse(context.getRootInActiveWindow(), name);
            }

            for (int i = 0; i < info.getChildCount(); i++) {

                AccessibilityNodeInfo nodeInfo = info.getChild(i);
                feed(nodeInfo);
            }

            return;
        }
        catch(Exception e)
        {
            throw new EmptyStackException();
        }
    }


    public static void addMessage(Message message, String name)
    {
        Vector<Message> messages = new Vector<Message>();
        messages.add(message);
        chats.get(name).feed(messages);
        flush();
    }


    private void parse(AccessibilityNodeInfo nodeInfo, String name)
    {
        if (nodeInfo == null) return ;
        if(nodeInfo.getClassName().toString().contains("ListView"))
        {
            parseListView(nodeInfo,name);
        }

        for (int i = 0; i < nodeInfo.getChildCount(); i++) {
            AccessibilityNodeInfo nodeInfo1 = nodeInfo.getChild(i);
            parse(nodeInfo1, name);
        }

        return;
    }

    public synchronized void parseListView(AccessibilityNodeInfo listViewNodeInfo, String name) {
        //  int i = 0;
         Vector<Message> messages = new Vector<Message>();

        for (int j = 0; j < listViewNodeInfo.getChildCount(); j++) {
            AccessibilityNodeInfo child = listViewNodeInfo.getChild(j);

            if (child.getClassName().toString().contains("ViewGroup")) {

                // switch(child.getChildCount())
                //   {
                // case 2:
                if ((child.getChildCount() == 2 || child.getChildCount() == 3) &&
                        child.getChild(0).getClassName().toString().contains("TextView") &&
                        child.getChild(1).getClassName().toString().contains("TextView")) {
                    Rect recTime = new Rect(), recMessage = new Rect();

                    AccessibilityNodeInfo messageChild = child.getChild(0);
                    AccessibilityNodeInfo timeChild = child.getChild(1);
                    messageChild.getBoundsInScreen(recMessage);
                    timeChild.getBoundsInScreen(recTime);

                    if (messageChild.getText() != null && timeChild.getText() != null) {
                        String content = messageChild.getText().toString();
                        String time = timeChild.getText().toString();
                        Message message = new Message(Message.MessageType.MESSAGE,Message.PriorityTime.NONE, content, time, recTime);
                        //if (checkIfExist(message) == false) {
                           // message.setTime(time);
                          message.setSampleTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS").format(new Date()));
                          message.setTimestamp(new Date().getTime());
                            messages.add(message);
                        //}
                        //  flush(time);
                    }
                } else {
                    // handleMedia(child, temp);
                }
            }
        }
        //TODO: save rect time
        //setInOutMessage(messages, name);
        // flush(messages);
        chats.get(name).feed(messages);
        //flush();
    }

    private synchronized void setInOutMessage(Vector<Message> messages, String contactName)
    {
        boolean flag = false;
        boolean end = false;

        if(messages.isEmpty())
        {
            return;
        }
        int j = 0;
        Rect temp = messages.get(j).getRectTime();
        int left, right;

        left = temp.left;
        right = temp.right;
        while(flag == false && end == false) {
            left = messages.get(j).getRectTime().left;
            right = messages.get(j).getRectTime().right;
            for (int i = 0; i < messages.size(); i++) {
                if(i != j) {
                    if (messages.get(i).getRectTime().left == left && messages.get(i).getRectTime().right == right) {
                        flag = true;
                    }
                }
            }
            j++;
            if(j >= messages.size())
                end = true;
        }
        if(end == true)
        {
            for (int i = 0; i < messages.size(); i++)
            {
                messages.get(i).setInMessage(true);
                messages.get(i).setName(contactName);
            }
        }
        else
        {
            for (int i = 0; i < messages.size(); i++)
            {
                if (messages.get(i).getRectTime().left == left && messages.get(i).getRectTime().right == right) {

                    messages.get(i).setInMessage(false);
                    messages.get(i).setName("me");
                }
                else
                {
                    messages.get(i).setInMessage(true);
                    messages.get(i).setName(contactName);
                }
            }
        }
    }
}
