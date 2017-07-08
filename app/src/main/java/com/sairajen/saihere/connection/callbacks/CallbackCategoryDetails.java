package com.sairajen.saihere.connection.callbacks;

import com.sairajen.saihere.model.Category;
import com.sairajen.saihere.model.Post;

import java.util.ArrayList;
import java.util.List;

public class CallbackCategoryDetails {

    public String status = "";
    public int count = -1;
    public int pages = -1;
    public Category category = null;
    public List<Post> posts = new ArrayList<>();
}
