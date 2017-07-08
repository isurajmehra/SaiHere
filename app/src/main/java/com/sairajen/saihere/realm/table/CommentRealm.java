package com.sairajen.saihere.realm.table;

import com.sairajen.saihere.model.Comment;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class CommentRealm extends RealmObject {

    @PrimaryKey
    public int id = -1;
    public String name = "";
    public String url = "";
    public String date = "";
    public String content = "";
    public int parent = -1;

    public Comment getOriginal() {
        Comment c = new Comment();
        c.id = id;
        c.name = name;
        c.url = url;
        c.date = date;
        c.content = content;
        c.parent = parent;
        return c;
    }
}
