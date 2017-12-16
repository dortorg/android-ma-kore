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
import java.util.Vector;

/**
 * Created by Dor on 9/28/2017.
 */

public class Chat2 {
    String contactName;
    Vector<Message> messages = new Vector<Message>();

    //Vector<Message> userMessages = new Vector<Message>();
    //Vector<Message> contactMessages = new Vector<Message>();
    File file;

    public Chat2(String name) {
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

/*
    public synchronized void parseListView(AccessibilityNodeInfo listViewNodeInfo) {
        //  int i = 0;
       // Vector<Message> temp = new Vector<Message>();

        for (int j = 0; j < listViewNodeInfo.getChildCount(); j++) {
            AccessibilityNodeInfo child = listViewNodeInfo.getChild(j);

            if (child.getClassName().toString().contains("ViewGroup")) {

                // switch(child.getChildCount())
                //   {
                // case 2:
                if (child.getChildCount() == 2 &&
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
                        Message message = new Message(Message.MessageType.MESSAGE, content, time, recTime);
                        if (checkIfExist(message) == false) {
                            messages.add(message);
                        }
                        //  flush(time);
                    }
                } else {
                   // handleMedia(child, temp);
                }
            }
        }
        //TODO: save rect time
        setInOutMessage(messages, contactName);
       // flush(messages);

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

*/

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


    /* private void handleMedia(AccessibilityNodeInfo child, Vector<Message> temp) {
         Rect recTime = new Rect();
         switch (child.getChildCount()) {
             case 1:
                 if (handleReciveAndNotDownloadFile(child, recTime, temp)) {
                     break;
                 }
                 break;
             case 2:
                 if (handleReciveAndDownloadImage(child, recTime, temp)) {
                     break;
                 } else if (handleSendAndDownloadFile(child, recTime, temp)) {
                     break;
                 } else if (handleReciveAndDownloadFile(child, recTime, temp)) {
                     break;
                 }

                 break;
             case 3:
                 if (handleReciveAndNotDownloadImage(child, recTime, temp)) {
                     break;
                 } else if (handleSendAndDownloadImage(child, recTime, temp)) {
                     break;
                 } else if (handleSendContact(child, recTime, temp)) {
                     break;
                 } else if (handleReciveContact(child, recTime, temp)) {
                     break;
                 }
                 else if(handleDate(child, recTime, temp))
                 {
                     break;
                 }


                 break;
             case 4:
                 if (handleReciveLocation(child, recTime, temp)) {
                     break;
                 } else if (handleSendLocation(child, recTime, temp)) {
                     break;
                 }
                 break;
             case 5:
                 break;
             case 6:
                 if (handleSendAndNotDownloadVoice(child, recTime, temp)) {
                     break;
                 } else if (handleSendAndDownloadVoice(child, recTime, temp)) {
                     break;
                 } else if (handleReciveAndNotDownloadVoice(child, recTime, temp)) {
                     break;
                 } else if (handleReciveAndDownloadVoice(child, recTime, temp)) {
                     break;
                 }
                 break;
         }
     }

     private boolean handleDate(AccessibilityNodeInfo child, Rect recTime, Vector<Message> temp)
     {
         if(child.getChild(0).getClassName().toString().contains("TextView") &&
                 child.getChild(1).getClassName().toString().contains("TextView") &&
                 child.getChild(2).getClassName().toString().contains("TextView"))
         {
             child.getChild(2).getBoundsInScreen(recTime);
             Message message = new Message(Message.MessageType.MESSAGE, child.getChild(0).getText().toString()  + "\n" + child.getChild(1).getText().toString(), child.getChild(2).getText().toString(), recTime);
             temp.add(message);
             return true;
         }
         return false;

     }
     private boolean handleReciveAndDownloadVoice(AccessibilityNodeInfo child, Rect recTime, Vector<Message> temp)
     {
         if(child.getChild(0).getClassName().toString().contains("ImageButton") &&
                 child.getChild(1).getClassName().toString().contains("View") &&
                 child.getChild(2).getClassName().toString().contains("TextView") &&
                 child.getChild(3).getClassName().toString().contains("TextView")&&
                 child.getChild(4).getClassName().toString().contains("ImageView")&&
                 child.getChild(5).getClassName().toString().contains("ImageView"))
         {
             if(child.getChild(0).getContentDescription().toString().contains("Play") )
             {
                 child.getChild(3).getBoundsInScreen(recTime);
                 Message message = new Message(Message.MessageType.VOICE, "Recive voice ( download )(" +  child.getChild(2).getText().toString() + ")", child.getChild(3).getText().toString(), recTime);
                 temp.add(message);
                 return true;
             }
         }
         return false;
     }
     private boolean handleReciveAndNotDownloadVoice(AccessibilityNodeInfo child, Rect recTime, Vector<Message> temp)
     {
         if(child.getChild(0).getClassName().toString().contains("ImageButton") &&
                 child.getChild(1).getClassName().toString().contains("View") &&
                 child.getChild(2).getClassName().toString().contains("TextView") &&
                 child.getChild(3).getClassName().toString().contains("TextView")&&
                 child.getChild(4).getClassName().toString().contains("TextView")&&
                 child.getChild(5).getClassName().toString().contains("ImageView"))
         {
             if(child.getChild(0).getContentDescription().toString().contains("Play") )
             {
                 child.getChild(4).getBoundsInScreen(recTime);
                 Message message = new Message(Message.MessageType.VOICE, "Recive voice (not download yet)(" +  child.getChild(3).getText().toString() + ")", child.getChild(4).getText().toString(), recTime);
                 temp.add(message);
                 return true;
             }
         }
         return false;
     }
     private boolean handleSendAndDownloadVoice(AccessibilityNodeInfo child, Rect recTime, Vector<Message> temp)
     {
         if(child.getChild(0).getClassName().toString().contains("ImageView") &&
                 child.getChild(1).getClassName().toString().contains("ImageView") &&
                 child.getChild(2).getClassName().toString().contains("ImageButton") &&
                 child.getChild(3).getClassName().toString().contains("View")&&
                 child.getChild(4).getClassName().toString().contains("TextView")&&
                 child.getChild(5).getClassName().toString().contains("TextView"))
         {
             if(child.getChild(1).getContentDescription().toString().contains("You") &&
                     child.getChild(2).getContentDescription().toString().contains("Play") )
             {
                 child.getChild(5).getBoundsInScreen(recTime);
                 Message message = new Message(Message.MessageType.VOICE, "SEND voice (" +  child.getChild(4).getText().toString() + ")", child.getChild(5).getText().toString(), recTime);
                 temp.add(message);
                 return true;
             }
         }
         return false;
     }
     private boolean handleSendAndNotDownloadVoice(AccessibilityNodeInfo child, Rect recTime, Vector<Message> temp)
     {
         if(child.getChild(0).getClassName().toString().contains("ImageView") &&
                 child.getChild(1).getClassName().toString().contains("ImageButton") &&
                 child.getChild(2).getClassName().toString().contains("View") &&
                 child.getChild(3).getClassName().toString().contains("TextView")&&
                 child.getChild(4).getClassName().toString().contains("TextView")&&
                 child.getChild(5).getClassName().toString().contains("TextView"))
         {
             if(child.getChild(0).getContentDescription().toString().contains("You") &&
                     child.getChild(1).getContentDescription().toString().contains("Play") )
             {
                 child.getChild(5).getBoundsInScreen(recTime);
                 Message message = new Message(Message.MessageType.VOICE, "SEND voice (" +  child.getChild(4).getText().toString() + ")", child.getChild(5).getText().toString(), recTime);
                 temp.add(message);
                 return true;
             }
         }
         return false;
     }
     private boolean handleSendLocation(AccessibilityNodeInfo child, Rect recTime, Vector<Message> temp)
     {
         if(child.getChild(1).getClassName().toString().contains("FrameLayout") &&
                 child.getChild(2).getClassName().toString().contains("Button") &&
                 child.getChild(3).getClassName().toString().contains("TextView") &&
                 child.getChild(0).getClassName().toString().contains("ImageView"))
         {
             if(child.getChild(1).getChild(0).getClassName().toString().contains("View") &&
                     child.getChild(1).getChild(0).getContentDescription().toString().contains("GoogleMap"))
             {
                 child.getChild(3).getBoundsInScreen(recTime);
                 Message message = new Message(Message.MessageType.LOCATION, "SEND LOCATION " , child.getChild(3).getText().toString(), recTime);
                 temp.add(message);
                 return true;
             }
         }
         return false;
     }
     private boolean handleReciveLocation(AccessibilityNodeInfo child, Rect recTime, Vector<Message> temp)
     {
         if(child.getChild(0).getClassName().toString().contains("FrameLayout") &&
                 child.getChild(1).getClassName().toString().contains("Button") &&
                 child.getChild(2).getClassName().toString().contains("TextView") &&
                 child.getChild(3).getClassName().toString().contains("ImageView"))
         {
             if(child.getChild(0).getChild(0).getClassName().toString().contains("View") &&
                     child.getChild(0).getChild(0).getContentDescription().toString().contains("GoogleMap"))
             {
                 child.getChild(2).getBoundsInScreen(recTime);
                 Message message = new Message(Message.MessageType.LOCATION, "recive LOCATION " , child.getChild(2).getText().toString(), recTime);
                 temp.add(message);
                 return true;
             }
         }
         return false;
     }
     private boolean handleReciveContact(AccessibilityNodeInfo child, Rect recTime, Vector<Message> temp)
     {
         if(child.getChild(2).getClassName().toString().contains("ImageView") &&
                 child.getChild(0).getClassName().toString().contains("LinearLayout") &&
                 child.getChild(1).getClassName().toString().contains("TextView"))
         {
             AccessibilityNodeInfo nodeInfo = child.getChild(0);
             if(nodeInfo.getChildCount() == 3 &&
                     nodeInfo.getChild(0).getClassName().toString().contains("ImageView") &&
                     nodeInfo.getChild(1).getClassName().toString().contains("TextView") &&
                     nodeInfo.getChild(2).getClassName().toString().contains("TextView"))
             {
                 nodeInfo.getChild(2).getBoundsInScreen(recTime);
                 Message message = new Message(Message.MessageType.CONTACT, "recive contact " + nodeInfo.getChild(1).getText().toString(), nodeInfo.getChild(2).getText().toString(), recTime);
                 temp.add(message);
                 return true;
             }
         }
         return false;
     }
     private boolean handleSendContact(AccessibilityNodeInfo child, Rect recTime, Vector<Message> temp)
     {
         if(child.getChild(0).getClassName().toString().contains("ImageView") &&
                 child.getChild(1).getClassName().toString().contains("LinearLayout") &&
                 child.getChild(2).getClassName().toString().contains("TextView"))
         {
             AccessibilityNodeInfo nodeInfo = child.getChild(1);
             if(nodeInfo.getChildCount() == 3 &&
                     nodeInfo.getChild(0).getClassName().toString().contains("ImageView") &&
                     nodeInfo.getChild(1).getClassName().toString().contains("TextView") &&
                     nodeInfo.getChild(2).getClassName().toString().contains("TextView"))
             {
                 nodeInfo.getChild(2).getBoundsInScreen(recTime);
                 Message message = new Message(Message.MessageType.CONTACT, "send contact " + nodeInfo.getChild(1).getText().toString(), nodeInfo.getChild(2).getText().toString(), recTime);
                 temp.add(message);
                 return true;
             }
         }
         return false;
     }
     private boolean handleReciveAndDownloadFile(AccessibilityNodeInfo child, Rect recTime, Vector<Message> temp)
     {
         if(child.getChild(1).getClassName().toString().contains("ImageView") &&
                 child.getChild(0).getClassName().toString().contains("LinearLayout"))
         {
             AccessibilityNodeInfo nodeInfo = child.getChild(0);
             if(nodeInfo.getChildCount() == 3 &&
                     nodeInfo.getChild(0).getClassName().toString().contains("TextView") &&
                     nodeInfo.getChild(1).getClassName().toString().contains("TextView") &&
                     nodeInfo.getChild(2).getClassName().toString().contains("TextView"))
             {
                 nodeInfo.getChild(2).getBoundsInScreen(recTime);
                 Message message = new Message(Message.MessageType.FILE, "get file from remote " + nodeInfo.getChild(0).getText().toString(), nodeInfo.getChild(2).getText().toString(), recTime);
                 temp.add(message);
                 return true;
             }
         }
         return false;
     }
     private boolean handleReciveAndNotDownloadFile(AccessibilityNodeInfo child, Rect recTime, Vector<Message> temp)
     {
         if(child.getChild(0).getClassName().toString().contains("LinearLayout"))
         {
             AccessibilityNodeInfo nodeInfo = child.getChild(0);
             if(nodeInfo.getChildCount() == 6 &&
                     nodeInfo.getChild(0).getClassName().toString().contains("TextView") &&
                     nodeInfo.getChild(1).getClassName().toString().contains("ImageButton") &&
                     nodeInfo.getChild(1).getContentDescription().toString().contains("Download") &&
                     nodeInfo.getChild(2).getClassName().toString().contains("TextView")&&
                     nodeInfo.getChild(5).getClassName().toString().contains("TextView"))
             {
                 nodeInfo.getChild(5).getBoundsInScreen(recTime);
                 Message message = new Message(Message.MessageType.FILE, "get file here (still not Downloaded) " + nodeInfo.getChild(0).getText().toString(), nodeInfo.getChild(5).getText().toString(), recTime);
                 temp.add(message);
                 return true;
             }
         }
         return false;
     }
     private boolean handleSendAndDownloadFile(AccessibilityNodeInfo child, Rect recTime, Vector<Message> temp)
     {
         if(child.getChild(0).getClassName().toString().contains("ImageView") &&
                 child.getChild(0).getClassName().toString().contains("ImageView") &&
                 child.getChild(1).getClassName().toString().contains("LinearLayout"))
         {
             AccessibilityNodeInfo nodeInfo = child.getChild(1);
             if(nodeInfo.getChild(0).getClassName().toString().contains("TextView") &&
                     nodeInfo.getChild(0).getChildCount() == 3 &&
                     nodeInfo.getChild(1).getClassName().toString().contains("TextView") &&
                     nodeInfo.getChild(2).getClassName().toString().contains("TextView"))

                 nodeInfo.getChild(2).getBoundsInScreen(recTime);
             Message message = new Message(Message.MessageType.FILE, "Send file " + nodeInfo.getChild(2).getText().toString(), nodeInfo.getChild(2).getText().toString(), recTime);
             temp.add(message);
             return true;
         }
         return false;
     }
     private boolean handleReciveAndDownloadImage(AccessibilityNodeInfo child, Rect recTime, Vector<Message> temp)
     {
         if(child.getChild(0).getContentDescription().toString().contains("Open") &&
                 child.getChild(0).getClassName().toString().contains("ImageView"))
         {
             child.getChild(1).getBoundsInScreen(recTime);
             Message message = new Message(Message.MessageType.IMAGE, "recive Image  (Downloaded)", child.getChild(1).getText().toString(), recTime);
             temp.add(message);
             return true;
         }
         return false;
     }
     private boolean handleReciveAndNotDownloadImage(AccessibilityNodeInfo child, Rect recTime, Vector<Message> temp)
     {
         if(child.getChild(0).getClassName().toString().contains("ImageView") && child.getChild(1).getClassName().toString().contains("Button") &&
                 child.getChild(2).getClassName().toString().contains("TextView"))
         {
             child.getChild(2).getBoundsInScreen(recTime);
             Message message = new Message(Message.MessageType.IMAGE, "recive Image  (still not Downloaded)", child.getChild(2).getText().toString(), recTime);
             temp.add(message);
             return true;
         }
         return false;
     }
     private boolean handleSendAndDownloadImage(AccessibilityNodeInfo child, Rect recTime, Vector<Message> temp)
     {
         if(child.getChild(0).getClassName().toString().contains("ImageView") &&
                 child.getChild(0).getClassName().toString().contains("ImageView") &&
                 child.getChild(1).getClassName().toString().contains("ImageView") &&
                 child.getChild(1).getContentDescription().toString().contains("Open") &&
                 child.getChild(2).getClassName().toString().contains("TextView"))
         {
             child.getChild(2).getBoundsInScreen(recTime);
             Message message = new Message(Message.MessageType.IMAGE, "send Image ", child.getChild(2).getText().toString(), recTime);
             temp.add(message);
             return true;
         }
         return false;
     }
 */
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
        for(Message me : messages)
        {
            if(me.isFlush() == false) {
                String str;
               // if (me.isInMessage() == true) {
                str = me.getTime() + " " + me.getContent() + " " + "(" + me.getName() + ")";

                me.setFlush(true);
                flush(str);
            }
        }
    }

    public synchronized void feed(Vector<Message> message)
    {
        for(Message me : message)
        {
            if(checkIfExist(me) == false)
            {
                message.add(me);
            }
            else
            {
                //TODO: check if have info on the time
            }
        }
    }



    private void sortMessages(Vector<Message> messages)
    {

    }
    public void update(Message message)
    {

    }


/*    public synchronized void feed(AccessibilityNodeInfo accessibilityNodeInfo)
    {

        if (accessibilityNodeInfo == null) return ;

        if(accessibilityNodeInfo.getClassName().toString().contains("ListView"))
        {
            parseListView(accessibilityNodeInfo);
        }

        for (int i = 0; i < accessibilityNodeInfo.getChildCount(); i++) {

            AccessibilityNodeInfo nodeInfo = accessibilityNodeInfo.getChild(i);
            feed(nodeInfo);
        }

    }*/
}
