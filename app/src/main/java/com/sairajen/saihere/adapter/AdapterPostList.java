package com.sairajen.saihere.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sairajen.saihere.R;
import com.sairajen.saihere.helper.Const;
import com.sairajen.saihere.helper.Funcs;
import com.sairajen.saihere.model.Post;
import com.sairajen.saihere.helper.Tools;

import java.util.ArrayList;
import java.util.List;

public class AdapterPostList extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    private List<Post> items = new ArrayList<>();

    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;

    private Context ctx;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, Post obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    // Provide post_list_row suitable constructor (depends on the kind of dataset)
    public AdapterPostList(Context context, RecyclerView view, List<Post> items) {
        this.items = items;
        ctx = context;
        lastItemViewDetector(view);
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        // each data item is just post_list_row string in this case
        public TextView title;
        public TextView short_content;
        public TextView date;
        public Button shareBtn;
        public ImageView image;
        public LinearLayout lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.title);
            short_content = (TextView) v.findViewById(R.id.short_content);
            date = (TextView) v.findViewById(R.id.date);
            shareBtn = (Button) v.findViewById(R.id.share);
            image = (ImageView) v.findViewById(R.id.image);
            lyt_parent = (LinearLayout) v.findViewById(R.id.lyt_parent);
        }
    }


    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_list_row, parent, false);
            vh = new OriginalViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_loading, parent, false);
            vh = new ProgressViewHolder(v);
        }
        return vh;
    }

    // Replace the contents of post_list_row view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            final Post p = items.get(position);
            OriginalViewHolder vItem = (OriginalViewHolder) holder;
            vItem.title.setText(Html.fromHtml(p.title));

            if (p.excerpt.equals("") || p.excerpt == null)
                vItem.short_content.setVisibility(View.GONE);
            else
            vItem.short_content.setText(Html.fromHtml(p.excerpt));

            vItem.date.setText(Tools.getFormatedDateSimple(p.date));

            vItem.shareBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Funcs.shareLink(ctx,p.url);
                }
            });

            Tools.displayImageThumbnail(ctx, p, vItem.image);

            vItem.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, p, position);
                    }
                }
            });
        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return this.items.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    public void insertData(List<Post> items) {
        setLoaded();
        int positionStart = getItemCount();
        int itemCount = items.size();
        this.items.addAll(items);
        notifyItemRangeInserted(positionStart, itemCount);
    }

    public void setLoaded() {
        loading = false;
        for(int i = 0; i< getItemCount(); i++){
            if(items.get(i) == null){
                items.remove(i);
                notifyItemRemoved(i);
            }
        }
    }

    public void setLoading() {
        if (getItemCount() != 0) {
            this.items.add(null);
            notifyItemInserted(getItemCount() - 1);
            loading = true;
        }
    }

    public void resetListData() {
        this.items = new ArrayList<>();
        notifyDataSetChanged();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    private void lastItemViewDetector(RecyclerView recyclerView) {
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            final LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    int lastPos = layoutManager.findLastVisibleItemPosition();
                    if (!loading && lastPos == getItemCount() - 1 && onLoadMoreListener != null) {
                        if (onLoadMoreListener != null) {
                            int current_page = getItemCount() / Const.POST_PER_REQUEST;
                            onLoadMoreListener.onLoadMore(current_page);
                        }
                        loading = true;
                    }
                }
            });
        }
    }

    public interface OnLoadMoreListener {
        void onLoadMore(int current_page);
    }

}