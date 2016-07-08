package com.tysci.cls.modles.event;

import android.os.Bundle;
import android.util.SparseArray;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Administrator on 2016/6/2.
 */
public class EventObject {
    /**接收者*/
    private SparseArray<Class> receivers;
    /**数据*/
    private Bundle data;
    /**动作*/
    private String eventAction;

    public EventObject(){
        receivers=new SparseArray<>();
        data=new Bundle();
    }

    public void setEventAction(String eventAction){
        this.eventAction=eventAction;
    }

    /**
     * 添加接收者
     * @param clss
     */
    public void addReceiver(Class... clss){
        for(Class cls:clss){
            int key=receivers.size();
            receivers.put(key,cls);
        }
    }

    public SparseArray<Class> getReceivers(){
        return receivers;
    }

    public Bundle getData(){
        return data;
    }

    public String getEventAction(){
        return eventAction;
    }

    public static void postEventObject(EventObject eventObject,String action){
        eventObject.setEventAction(action);
        EventBus.getDefault().post(eventObject);
    }

    public static void postEventObject(EventObject eventObject){
        //eventObject.setEventAction(action);
        EventBus.getDefault().post(eventObject);
    }
}
