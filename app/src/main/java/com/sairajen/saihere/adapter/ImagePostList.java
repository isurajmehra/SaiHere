package com.sairajen.saihere.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import com.sairajen.saihere.R;
import com.sairajen.saihere.helper.Const;
import com.sairajen.saihere.helper.Funcs;
import com.sairajen.saihere.helper.Tools;
import com.sairajen.saihere.model.Post;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Gmonetix
 */

public class ImagePostList extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    private List<Post> items = new ArrayList<>();

    private boolean loading;
    private ImagePostList.OnLoadMoreListener onLoadMoreListener;

    private Context ctx;

    public ImagePostList(Context context, RecyclerView view, List<Post> items) {
        this.items = items;
        ctx = context;
        lastItemViewDetector(view);
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public Button shareBtn;
        public ImageView image;
        public LinearLayout lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);
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
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_list_row, parent, false);
            vh = new ImagePostList.OriginalViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_loading, parent, false);
            vh = new ImagePostList.ProgressViewHolder(v);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ImagePostList.OriginalViewHolder) {
            final Post p = items.get(position);
            ImagePostList.OriginalViewHolder vItem = (ImagePostList.OriginalViewHolder) holder;

            vItem.shareBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Funcs.shareLink(ctx,p.url);
                }
            });

            Tools.displayImageThumbnail(ctx, p, vItem.image);

        } else {
            ((ImagePostList.ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }

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

    public void setOnLoadMoreListener(ImagePostList.OnLoadMoreListener onLoadMoreListener) {
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