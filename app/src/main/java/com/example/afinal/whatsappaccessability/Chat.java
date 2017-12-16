package com.example.afinal.whatsappaccessability;

import android.graphics.Rect;
import android.os.Environment;
import android.support.v4.app.NotificationCompatSideChannelService;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityRecord;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.EmptyStackException;
import java.util.Vector;

/**
 * Created by Dor on 9/28/2017.
 */

public class Chat {
    String contactName;

    public Vector<Message> getMessages() {
        return messages;
    }

    Vector<Message> messages = new Vector<Message>();

    //Vector<Message> userMessages = new Vector<Message>();
    //Vector<Message> contactMessages = new Vector<Message>();
    File file;

    public Chat(String name) {
        contactName = name;


        file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/mywhatsapp", contactName);
        FileOutputStream outputStream;
        try {
            outputStream = new FileOutputStream(file, true);
            outputStream.write(contactName.getBytes());
            outputStream.write("\n".getBytes());
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void updatePriority(Vector<Message> oldMes, Vector<Message> newMes)
    {
        for(Message oldMessage : oldMes)
        {
            for(Message newMessage : newMes)
            {
                if(oldMessage.getContent().contentEquals(newMessage.getContent()) &&
                        oldMessage.getTime().contentEquals(newMessage.getTime()))
                {
                    oldMessage.addPri(newMessage.getTimePri());
                }
            }
        }
    }

    private boolean checkIfExist(Message message)
    {
        if(!messages.isEmpty()) {
            for (Message mes : messages) {
                if (message.getContent().contentEquals(mes.getContent()) && message.getTime().contentEquals(mes.getTime())) {
                    return true;
                }
            }
        }
        return false;
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


    public synchronized void flush(/*Vector<Message> messages*/)
    {
        try {
            for (Message me : messages) {
                if (me.isFlush() == false) {
                    String str = null;
                    // if (me.isInMessage() == true) {
                    if (!me.getSampleTime().isEmpty()) {
                        flush(me.getSampleTime());
                    }

                    str = me.getTime() + " " + me.getContent() + " " + "(" + me.getName() + ")";

                    me.setFlush(true);
                    flush(str);
                }
            }
        }catch(Exception e)
        {
            throw new EmptyStackException();
        }
    }

    public synchronized void feed(Vector<Message> message)
    {
        updatePriority(messages,message);

        for(Message me : message)
        {
            if(checkIfExist(me) == false)
            {
                messages.add(me);
            }
        }
        if(messages.size() > 3) {
            setInOutMessage(messages, contactName);
            flush();
        }
    }



    private void sortMessages(Vector<Message> messages)
    {

    }
    public void update(Message message)
    {

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
