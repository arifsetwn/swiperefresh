package com.swipe.adinda.swiperefreshandloadmore;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

/**
 * Created by adinda on 27/09/16.
 */

public class LoadMoreListView extends ListView implements AbsListView.OnScrollListener {
    private static final String TAG = LoadMoreListView.class.getSimpleName();

    private OnScrollListener mOnScrollListener;
    private LayoutInflater minflater;
    private View mFooterView;
    private View mLoadMoreStatusView;
    private OnLoadMoreListener  mOnLoadMoreListener;
    private boolean mIsLoadingMore = false;
    private int mCurrentScrollState;

    public LoadMoreListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LoadMoreListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        mFooterView = (RelativeLayout) minflater.inflate(R.layout.loadmore_view, this, false);
        mLoadMoreStatusView = mFooterView.findViewById(R.id.load_more_progress_bar);
        addFooterView(mFooterView);
        setLoading(false);

        super.setAdapter(adapter);
    }

    private void init(Context context) {
        minflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        super.setOnScrollListener(this);
    }

    public void setLoadMoreStatusView(View v, int statusViewid) {
        removeFooterView(mFooterView);
        mFooterView = v;
        mLoadMoreStatusView = mFooterView.findViewById(statusViewid);
        addFooterView(mFooterView);
    }

    public void setLoadMoreStatusView(View v) {
        removeFooterView(mFooterView);
        mFooterView = v;
        mLoadMoreStatusView = mFooterView.findViewById(R.id.load_more_progress_bar);
        addFooterView(mFooterView);
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        mOnLoadMoreListener = onLoadMoreListener;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {

        if (mOnScrollListener != null) {
            mOnScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
        if (visibleItemCount == totalItemCount) {
            if (mLoadMoreStatusView != null) {
                mLoadMoreStatusView.setVisibility(View.GONE);
            }
            return;
        }

        if (mOnLoadMoreListener != null) {

            boolean loadMore = firstVisibleItem + visibleItemCount >= totalItemCount;

            if (!mIsLoadingMore && loadMore
                    && mCurrentScrollState != SCROLL_STATE_IDLE) {
                setLoading(true);
                onLoadMore();
            }

        }
    }
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        mCurrentScrollState = scrollState;

        if (mOnScrollListener != null) {
            mOnScrollListener.onScrollStateChanged(view, scrollState);
        }
    }

    public void setLoading(boolean loading) {
        Log.d(TAG, "setLoading: " + loading);

        mIsLoadingMore = loading;
        mLoadMoreStatusView.setVisibility(loading ? View.VISIBLE : View.GONE);
    }

    public void onLoadMore() {
        Log.d(TAG, "onLoadMore");
        if (mOnLoadMoreListener != null) {
            mOnLoadMoreListener.onLoadMore();
        }
    }

    public void onLoadMoreComplete() {
        setLoading(false);
    }

    public interface OnLoadMoreListener {
        public void onLoadMore();
    }
}