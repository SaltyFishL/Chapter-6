package com.byted.camp.todolist.beans;


import com.byted.camp.todolist.R;

import java.util.Date;

/**
 * Created on 2019/1/23.
 *
 * @author xuyingyi@bytedance.com (Yingyi Xu)
 */
public class Note {

    public final long id;
    private Date date;
    private State state;
    private String content;
    private int priority;
    public static final int HIGH_PRIORITY = 1;
    public static final int NORMAL_PRIORITY = 2;
    public static final int LOW_PRIORITY = 3;

    public Note(long id) {
        this.id = id;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public static int getPriorityFromId(int id) {
        int priority = LOW_PRIORITY;
        switch (id) {
            case R.id.bt_high_priority:
                priority = Note.HIGH_PRIORITY;
                break;
            case R.id.bt_normal_priority:
                priority = Note.NORMAL_PRIORITY;
                break;
            case R.id.bt_low_priority:
                priority = Note.LOW_PRIORITY;
                break;
            default:
                break;
        }
        return priority;
    }
}
