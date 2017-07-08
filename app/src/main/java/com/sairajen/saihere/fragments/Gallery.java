package com.sairajen.saihere.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.sairajen.saihere.R;
import com.sairajen.saihere.activity.Home;
import com.sairajen.saihere.adapter.ImagePostList;
import com.sairajen.saihere.connection.API;
import com.sairajen.saihere.connection.RestAdapter;
import com.sairajen.saihere.connection.callbacks.CallbackCategoryDetails;
import com.sairajen.saihere.helper.Const;
import com.sairajen.saihere.helper.NetworkCheck;
import com.sairajen.saihere.model.Category;
import com.sairajen.saihere.model.Post;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *@author Gmonetix
 */
public class Gallery extends Fragment {

    private View view;
    private RecyclerView recyclerView;

    private ImagePostList mAdapter;
    private SwipeRefreshLayout swipe_refresh;
    private Call<CallbackCategoryDetails> callbackCall = null;

    // extra obj
    private Category category;

    private int post_total = 0;
    private int failed_page = 0;

    public Gallery() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_gallery, container, false);

        for (int i = 0; i< Home.categoryList.size(); i++){
            if (Home.categoryList.get(i).id == Const.CATEGORY_IMAGES) {
                category = Home.categoryList.get(i);
            }
        }

        post_total = category.post_count;

        swipe_refresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        //set data and list adapter
        mAdapter = new ImagePostList(getActivity(), recyclerView, new ArrayList<Post>());
        recyclerView.setAdapter(mAdapter);

        // detect when scroll reach bottom
        mAdapter.setOnLoadMoreListener(new ImagePostList.OnLoadMoreListener() {
            @Override
            public void onLoadMore(int current_page) {
                if (post_total > mAdapter.getItemCount() && current_page != 0) {
                    int next_page = current_page + 1;
                    requestAction(next_page);
                } else {
                    mAdapter.setLoaded();
                }
            }
        });

        // on swipe list
        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (callbackCall != null && callbackCall.isExecuted()) {
                    callbackCall.cancel();
                }
                mAdapter.resetListData();
                requestAction(1);
            }
        });

        requestAction(1);

        return view;
    }

    private void displayApiResult(final List<Post> posts) {
        mAdapter.insertData(posts);
        swipeProgress(false);
        if (posts.size() == 0) {
            showNoItemView(true);
        }
    }

    private void requestPostApi(final int page_no) {
        API api = RestAdapter.createAPI();
        callbackCall = api.getCategoryDetailsByPage(category.id, page_no, Const.POST_PER_REQUEST);
        callbackCall.enqueue(new Callback<CallbackCategoryDetails>() {
            @Override
            public void onResponse(Call<CallbackCategoryDetails> call, Response<CallbackCategoryDetails> response) {
                CallbackCategoryDetails resp = response.body();
                if (resp != null && resp.status.equals("ok")) {
                    displayApiResult(resp.posts);
                } else {
                    onFailRequest(page_no);
                }
            }

            @Override
            public void onFailure(Call<CallbackCategoryDetails> call, Throwable t) {
                if (!call.isCanceled()) onFailRequest(page_no);
            }

        });
    }

    private void onFailRequest(int page_no) {
        failed_page = page_no;
        mAdapter.setLoaded();
        swipeProgress(false);
        if (NetworkCheck.isConnect(getActivity())) {
            showFailedView(true, getString(R.string.failed_text));
        } else {
            showFailedView(true, getString(R.string.no_internet_text));
        }
    }

    private void requestAction(final int page_no) {
        showFailedView(false, "");
        showNoItemView(false);
        if (page_no == 1) {
            swipeProgress(true);
        } else {
            mAdapter.setLoading();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                requestPostApi(page_no);
            }
        }, Const.DELAY_TIME);
    }

    private void showFailedView(boolean show, String message) {
        View lyt_failed = (View) view.findViewById(R.id.lyt_failed);
        ((TextView) view.findViewById(R.id.failed_message)).setText(message);
        if (show) {
            recyclerView.setVisibility(View.GONE);
            lyt_failed.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            lyt_failed.setVisibility(View.GONE);
        }
        ((Button) view.findViewById(R.id.failed_retry)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestAction(failed_page);
            }
        });
    }

    private void showNoItemView(boolean show) {
        View lyt_no_item = (View) view.findViewById(R.id.lyt_no_item);
        ((TextView) view.findViewById(R.id.no_item_message)).setText("no post");
        if (show) {
            recyclerView.setVisibility(View.GONE);
            lyt_no_item.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            lyt_no_item.setVisibility(View.GONE);
        }
    }

    private void swipeProgress(final boolean show) {
        if (!show) {
            swipe_refresh.setRefreshing(show);
            return;
        }
        swipe_refresh.post(new Runnable() {
            @Override
            public void run() {
                swipe_refresh.setRefreshing(show);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        swipeProgress(false);
        if (callbackCall != null && callbackCall.isExecuted()) {
            callbackCall.cancel();
        }
    }

}
