package com.houz.chef.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.houz.chef.R;


/*
 * Copyright (C) 2012 Fabian Leon Ortega <http://orleonsoft.blogspot.com/,
 *  http://yelamablog.blogspot.com/>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

public class LoadMoreExpandableListView extends ExpandableListView implements OnScrollListener {

    private static final String TAG = "LoadMoreListView";

    /**
     * Listener that will receive notifications every time the list scrolls.
     */
    private OnScrollListener mOnScrollListener;
    private LayoutInflater mInflater;

    // footer view
    private RelativeLayout mFooterView;
    // private TextView mLabLoadMore;
    private ProgressBar mProgressBarLoadMore;

    // Header View
    private RelativeLayout mHeaderView;
    private ProgressBar mProgressBarPullRefresh;

    // Listener to process load more items when user reaches the end of the list
    private OnLoadMoreListener mOnLoadMoreListener;

    // To know if the list is loading more items
    private boolean mIsLoadingMore = false, mIsRefreshing = false;
    private int mCurrentScrollState;

    protected int mRefreshState;
    private int mRefreshViewHeight;

    public LoadMoreExpandableListView(Context context) {
        super(context);
        init(context);
    }

    public LoadMoreExpandableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LoadMoreExpandableListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {

        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mHeaderView = (RelativeLayout) mInflater.inflate(
                R.layout.pull_to_refresh_header, this, false);
        mProgressBarPullRefresh = (ProgressBar) mHeaderView
                .findViewById(R.id.pull_refresh_progressBar);
        // mProgressBarPullRefresh
        // .setProgressDrawable(new MaterialProgressDrawable(
        // getContext(), mProgressBarPullRefresh));

        mRefreshOriginalTopPadding = mHeaderView.getPaddingTop();

        addHeaderView();

        // footer
        mFooterView = (RelativeLayout) mInflater.inflate(
                R.layout.load_more_footer, this, false);
        /*
         * mLabLoadMore = (TextView) mFooterView
		 * .findViewById(R.id.load_more_lab_view);
		 */
        mProgressBarLoadMore = (ProgressBar) mFooterView
                .findViewById(R.id.load_more_progressBar);
        addFooterView();

        super.setOnScrollListener(this);

        measureView(mHeaderView);
        mRefreshViewHeight = mHeaderView.getMeasuredHeight();
    }

    public void addHeaderView() {
        addHeaderView(mHeaderView);
    }

    public void addFooterView() {
        addFooterView(mFooterView);
    }

    public void removeHeaderView() {
        removeHeaderView(mHeaderView);
    }

    public void removeFooterView() {
        removeFooterView(mFooterView);
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);
    }

    /**
     * Set the listener that will receive notifications every time the list
     * scrolls.
     *
     * @param l The scroll listener.
     */
    @Override
    public void setOnScrollListener(OnScrollListener l) {
        mOnScrollListener = l;
    }

    /**
     * Register a callback to be invoked when this list reaches the end (last
     * item be visible)
     *
     * @param onLoadMoreListener The callback to run.
     */

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        mOnLoadMoreListener = onLoadMoreListener;
    }

    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {

        if (mOnScrollListener != null) {
            mOnScrollListener.onScroll(view, firstVisibleItem,
                    visibleItemCount, totalItemCount);
        }

        if (mOnLoadMoreListener != null) {

            if (visibleItemCount == totalItemCount) {
                mProgressBarLoadMore.setVisibility(View.GONE);
                // mLabLoadMore.setVisibility(View.GONE);
                return;
            }

            boolean loadMore = firstVisibleItem + visibleItemCount >= totalItemCount;

            if (!mIsLoadingMore && loadMore
                    && mCurrentScrollState != SCROLL_STATE_IDLE) {
                mProgressBarLoadMore.setVisibility(View.VISIBLE);
                // mLabLoadMore.setVisibility(View.VISIBLE);
                mIsLoadingMore = true;
                onLoadMore();
            }

        }

        /** For Pull To Refresh Layout */

        // if (mCurrentScrollState == SCROLL_STATE_TOUCH_SCROLL
        // /* && mRefreshState != REFRESHING */) {
        // if (firstVisibleItem == 0) {
        // mProgressBarPullRefresh.setVisibility(View.VISIBLE);
        // if ((mHeaderView.getBottom() >= mRefreshViewHeight + 20 ||
        // mHeaderView
        // .getTop() >= 0) && mRefreshState != RELEASE_TO_REFRESH) {
        // mRefreshState = RELEASE_TO_REFRESH;
        // } else if (mHeaderView.getBottom() < mRefreshViewHeight + 20
        // && mRefreshState != PULL_TO_REFRESH) {
        // mRefreshState = PULL_TO_REFRESH;
        // }
        // }
        // }

    }

    public void onScrollStateChanged(AbsListView view, int scrollState) {

        // bug fix: listview was not clickable after scroll
        if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
            view.invalidateViews();
        }

        mCurrentScrollState = scrollState;

        if (mOnScrollListener != null) {
            mOnScrollListener.onScrollStateChanged(view, scrollState);
        }

    }

    public void onLoadMore() {
        Log.d(TAG, "onLoadMore");
        if (mOnLoadMoreListener != null) {
            mOnLoadMoreListener.onLoadMore();
        } else {
            onLoadMoreComplete();
        }
    }

    /**
     * Notify the loading more operation has finished
     */
    public void onLoadMoreComplete() {
        mIsLoadingMore = false;
        mProgressBarLoadMore.setVisibility(View.GONE);
    }

    /**
     * Interface definition for a callback to be invoked when list reaches the
     * last item (the user load more items in the list)
     */
    public interface OnLoadMoreListener {
        /**
         * Called when the list reaches the last item (the last item is visible
         * to the user)
         */
        public void onLoadMore();
    }

    // Hack for Staff App

    boolean expanded = false;

    public boolean isExpanded() {
        return expanded;
    }

    private OnRefreshListener mOnRefreshListener;

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // HACK! TAKE THAT ANDROID!
        if (isExpanded()) {
            // Calculate entire height by providing a very large height hint.
            // But do not use the highest 2 bits of this integer; those are
            // reserved for the MeasureSpec mode.
            int expandSpec = MeasureSpec.makeMeasureSpec(
                    Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
            super.onMeasure(widthMeasureSpec, expandSpec);

            ViewGroup.LayoutParams params = getLayoutParams();
            params.height = getMeasuredHeight();
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    /**
     * Register a callback to be invoked when this list should be refreshed.
     *
     * @param onRefreshListener The callback to run.
     */
    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        mOnRefreshListener = onRefreshListener;
    }

    /**
     * Resets the list to a normal state after a refresh.
     */
    public void onRefreshComplete() {
        mIsRefreshing = false;
        mRefreshState = TAP_TO_REFRESH;
        resetHeaderPadding();
        mProgressBarPullRefresh.setVisibility(View.GONE);
    }

    /**
     * Interface definition for a callback to be invoked when list should be
     * refreshed.
     */
    public interface OnRefreshListener {
        /**
         * Called when the list should be refreshed.
         * <p/>
         * A call to {@link PullToRefreshListView #onRefreshComplete()} is
         * expected to indicate that the refresh has completed.
         */
        public void onRefresh();
    }

    private boolean blockOnRefresh = false;

    public void blockOnRefresh(boolean blockOnRefresh) {
        this.blockOnRefresh = blockOnRefresh;
    }

    public boolean canRefresh() {
        return blockOnRefresh;
    }

    public void onRefresh() {
        Log.d(TAG, "onRefresh");

        if (mOnRefreshListener != null) {
            mOnRefreshListener.onRefresh();
        } else {
            onRefreshComplete();
        }
    }

    private static final int TAP_TO_REFRESH = 1;
    private static final int PULL_TO_REFRESH = 2;
    private static final int RELEASE_TO_REFRESH = 3;
    protected static final int REFRESHING = 4;

    private boolean canRefresh = false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int y = (int) event.getY();
        if (!blockOnRefresh) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_UP:
                    // if (!isVerticalScrollBarEnabled()) {
                    // setVerticalScrollBarEnabled(true);
                    // }
                    if (canRefresh && getFirstVisiblePosition() == 0
                            && mCurrentScrollState != SCROLL_STATE_IDLE) {
                        if ((mHeaderView.getBottom() >= mRefreshViewHeight || mHeaderView
                                .getTop() >= 0)) {
                            onRefresh();
                        } else {
                            onRefreshComplete();
                        }
                    } else {
                        onRefreshComplete();
                    }
                    break;
                case MotionEvent.ACTION_DOWN:
                    // mLastMotionY = y;
                    if (getFirstVisiblePosition() == 0) {
                        canRefresh = true;
                    } else {
                        canRefresh = false;
                    }
                    mLastMotionY = y;
                    break;
                case MotionEvent.ACTION_MOVE:
                    applyHeaderPadding(event);
                    if (canRefresh && !mIsRefreshing
                            && mCurrentScrollState != SCROLL_STATE_IDLE) {
                        mProgressBarPullRefresh.setVisibility(View.VISIBLE);
                        mIsRefreshing = true;
                        mRefreshState = RELEASE_TO_REFRESH;
                        // onRefresh();
                    }
                    break;
            }
            // }else{
            // return false;
        }
        return super.onTouchEvent(event);
    }

    private void measureView(View child) {
        ViewGroup.LayoutParams p = child.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
        int lpHeight = p.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
                    MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0,
                    MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }

    private int mLastMotionY;
    private int mRefreshOriginalTopPadding;

    private void applyHeaderPadding(MotionEvent ev) {
        // getHistorySize has been available since API 1
        int pointerCount = ev.getHistorySize();

        for (int p = 0; p < pointerCount; p++) {
            if (mRefreshState == RELEASE_TO_REFRESH) {
                if (isVerticalFadingEdgeEnabled()) {
                    setVerticalScrollBarEnabled(false);
                }

                int historicalY = (int) ev.getHistoricalY(p);

                // Calculate the padding to apply, we divide by 1.7 to
                // simulate a more resistant effect during pull.
                int topPadding = (int) (((historicalY - mLastMotionY) - mRefreshViewHeight) / 1.7);

                mHeaderView.setPadding(mHeaderView.getPaddingLeft(),
                        topPadding, mHeaderView.getPaddingRight(),
                        mHeaderView.getPaddingBottom());
            }
        }
    }

    /**
     * Sets the header padding back to original size.
     */
    private void resetHeaderPadding() {
        mHeaderView.setPadding(mHeaderView.getPaddingLeft(),
                mRefreshOriginalTopPadding, mHeaderView.getPaddingRight(),
                mHeaderView.getPaddingBottom());
    }
}
