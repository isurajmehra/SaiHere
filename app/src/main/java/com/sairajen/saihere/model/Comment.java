package com.sairajen.saihere.model;

import com.sairajen.saihere.realm.table.CommentRealm;

import java.io.Serializable;

public class Comment implements Serializable {

    public int id = -1;
    public String name = "";
    public String url = "";
    public String date = "";
    public String content = "";
    public int parent = -1;

    public CommentRealm getObjectRealm() {
        CommentRealm c = new CommentRealm();
        c.id = id;
        c.name = name;
        c.url = url;
        c.date = date;
        c.content = content;
        c.parent = parent;
        return c;
    }

}
