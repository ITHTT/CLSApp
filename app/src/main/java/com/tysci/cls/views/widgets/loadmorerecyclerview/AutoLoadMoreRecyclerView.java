package com.tysci.cls.views.widgets.loadmorerecyclerview;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.tysci.cls.utils.KLog;


/**
 * Created by Administrator on 2016/3/30.
 */
public class AutoLoadMoreRecyclerView extends RecyclerView {
    private Context context;

    /**是否能够自动加载更多*/
    private boolean isLoadMore=true;
    /**数据是否加载完成*/
    protected boolean isLoadFinished = false;
    /**是否正在加载更多数据*/
    protected boolean isLoadMoreing = false;
    /**是否正在刷新*/
    protected boolean isRefreshing=false;

    /**添加的头部视图*/
    private View mHeaderView = null;
    /**添加的底部视图*/
    private View mFootView = null;
    /**加载更多的视图*/
    private LoadMoreFooterView loadMoreFooterView;
    /**加载更多的监听*/
    private OnLoadMoreListener loadMoreListener;

    private Adapter mAdapter;
    private Adapter mWrapAdapter;

    public static final int TYPE_HEADER =  -4;
    public static final int TYPE_NORMAL =  0;
    public static final int TYPE_FOOTER =  -3;


    public AutoLoadMoreRecyclerView(Context context) {
        super(context);
        initViews(context);
    }

    public AutoLoadMoreRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public AutoLoadMoreRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initViews(context);
    }

    private void initViews(Context context){
        this.context=context;
    }

    /**
     * 添加头部视图
     * @param view
     */
    public void addHeaderView(View view) {
        mHeaderView=view;
    }

    /**
     * 添加底部视图
     * @param view
     */
    public void addFooterView(final View view) {
        mFootView=view;
    }

    /**
     * 设置加载更多的监听事件，并设置加载更多的视图
     * @param loadMoreListener
     */
    public void setOnLoadMoreListener(OnLoadMoreListener loadMoreListener){
        this.isLoadMore=true;
        this.loadMoreListener=loadMoreListener;
        if(loadMoreFooterView==null){
            loadMoreFooterView=new LoadMoreFooterView(context);
            LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            loadMoreFooterView.setLayoutParams(layoutParams);
        }
        if(mFootView==null) {
            mFootView=loadMoreFooterView;
        }
    }


    @Override
    public void setAdapter(Adapter adapter) {
        mAdapter  = adapter;
        mWrapAdapter = new WrapAdapter(mHeaderView, mFootView, adapter);
        super.setAdapter(mWrapAdapter);
        mAdapter.registerAdapterDataObserver(mDataObserver);
    }

    /**
     * 设置正在加载的状态
     */
    public void setLoadingMore(){
        isLoadMoreing = true;
        if(loadMoreFooterView!=null){
            loadMoreFooterView.setLoadingMoreState();
        }
    }

    /**
     * 设置开始准备加载的状态
     */
    public void setStartLoadMore(){
        KLog.e("设置加载状态...");
        isLoadMoreing=false;
        isLoadFinished=false;
        if(loadMoreFooterView!=null){
            loadMoreFooterView.setLoadingMoreState();
        }
    }

    public void setLoadMoreViewBackground(int color){
        if(loadMoreFooterView!=null){
            loadMoreFooterView.setBackgroundColor(color);
        }
    }

    /**
     * 设置本次加载更多完成
     */
    public void setLoadMoreComplete(){
        isLoadMoreing=false;
    }

    /**
     * 表示没有更多的数据可加载的状态
     */
    public void setLoadMoreDataComplete(){
        isLoadFinished=true;
        isLoadMoreing=false;
        if(loadMoreFooterView!=null){
            loadMoreFooterView.setLoadMoreDataFinishedState(false);
            loadMoreFooterView.setOnClickListener(null);
        }
    }

    public void setRefreshingTip(String tip){
        if(loadMoreFooterView!=null){
            loadMoreFooterView.setLoadFinishedTip(tip);
            loadMoreFooterView.setLoadMoreDataFinishedState(true);
        }
    }

    public void setRefreshingTip(int tip){
        if(loadMoreFooterView!=null){
            loadMoreFooterView.setLoadFinishedTip(tip);
            loadMoreFooterView.setLoadMoreDataFinishedState(true);
        }
    }

    public void setRefreshing(){
        this.isRefreshing=true;
    }

    public boolean isRefreshing(){
        return this.isRefreshing;
    }

    public void setRefreshComplete(){
        this.isRefreshing=false;
        if(loadMoreFooterView!=null){
            if(!isLoadFinished) {
                loadMoreFooterView.setLoadingMoreState();
            }else{
                loadMoreFooterView.setLoadFinishedTip("没有更多数据了...");
                loadMoreFooterView.setLoadMoreDataFinishedState(true);
            }
        }
    }

    public void setLoadMoreDataComplete(String tip){
        isLoadFinished=true;
        isLoadMoreing=false;
//        KLog.e("isLoadFinished:"+isLoadFinished);
//        KLog.e("isLoadMoreing:"+isLoadMoreing());
        if(loadMoreFooterView!=null){
            loadMoreFooterView.setLoadFinishedTip(tip);
            loadMoreFooterView.setLoadMoreDataFinishedState(true);
            loadMoreFooterView.setOnClickListener(null);
        }
    }

    public void setLoadMoreDataComplete(int tip){
        isLoadFinished=true;
        isLoadMoreing=false;
//        KLog.e("isLoadFinished:"+isLoadFinished);
//        KLog.e("isLoadMoreing:"+isLoadMoreing());
        if(loadMoreFooterView!=null){
            loadMoreFooterView.setLoadFinishedTip(tip);
            loadMoreFooterView.setLoadMoreDataFinishedState(true);
            loadMoreFooterView.setOnClickListener(null);
        }
    }



    public void setLoadMoreDataFailed(){
        isLoadMoreing = false;
        isRefreshing=false;
        if(loadMoreFooterView!=null){
            loadMoreFooterView.setLoadFailedTip("加载失败，可点击重试");
            loadMoreFooterView.setLoadMoreFailedState();
            loadMoreFooterView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isLoadMoreing && loadMoreListener != null) {
                        setLoadingMore();
                        loadMoreListener.onLoadMore();
                    }
                }
            });
        }
    }

    public void setLoadMoreDataFailed(View.OnClickListener listener){
        isLoadMoreing = false;
        isRefreshing=false;
        if(loadMoreFooterView!=null){
            loadMoreFooterView.setLoadFailedTip("加载失败，可点击重试");
            loadMoreFooterView.setLoadMoreFailedState();
            loadMoreFooterView.setOnClickListener(listener);
        }
    }

    /**
     * 处理滚动到底部自动加载更多
     * @param state
     */
    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        if (state == RecyclerView.SCROLL_STATE_IDLE && loadMoreListener != null && !isLoadMoreing && isLoadMore) {
            LayoutManager layoutManager = getLayoutManager();
            int lastVisibleItemPosition;
            if (layoutManager instanceof GridLayoutManager) {
                lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                int[] into = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
                ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(into);
                lastVisibleItemPosition = findMax(into);
            //} //else if(layoutManager instanceof com.tysci.gameathletics.views.widgets.superslim.LayoutManager){
                //lastVisibleItemPosition=((com.tysci.gameathletics.views.widgets.superslim.LayoutManager) layoutManager).findLastCompletelyVisibleItemPosition();
            }else{
                lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
            }
            if (layoutManager.getChildCount() > 0
                    && lastVisibleItemPosition >= layoutManager.getItemCount() - 1) {
                if (mFootView!=null) {
                    mFootView.setVisibility(VISIBLE);
                }
                KLog.e("开始尝试加载更多...");
                if(isCanLoadMore()){
                    setLoadingMore();
                    isLoadMoreing=true;
                    KLog.e("开始加载更多", "启动加载更多...");
                    loadMoreListener.onLoadMore();
                }
            }
        }
    }

    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    public boolean isLoadMoreing(){
        return this.isLoadMoreing;
    }

    /**
     * 是否可以加载更多
     * @return
     */
    private boolean isCanLoadMore(){
        KLog.e("isLoading:" + isLoadMoreing + " isLoadFinished:" + isLoadFinished);
        if(isRefreshing){
            return false;
        }

        if(!isLoadMore){
            return false;
        }
        if(mFootView==null){
            return false;
        }
        if(getAdapter()==null){
            return false;
        }
        if(isLoadMoreing){
            return false;
        }
        if(isLoadFinished){
            return false;
        }
        return true;
    }

    private final AdapterDataObserver mDataObserver = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            mWrapAdapter.notifyDataSetChanged();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            mWrapAdapter.notifyItemRangeChanged(positionStart, itemCount, payload);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeRemoved(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            mWrapAdapter.notifyItemMoved(fromPosition, toPosition);
        }
    };

    private class WrapAdapter extends Adapter<ViewHolder> {

        private Adapter adapter;

        private View mHeaderView;

        private View mFootView;

        private int headerPosition = 0;

        public WrapAdapter(View headerView, View footView, Adapter adapter) {
            this.adapter = adapter;
            this.mHeaderView = headerView;
            this.mFootView = footView;
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
            LayoutManager manager = recyclerView.getLayoutManager();
            if(manager instanceof GridLayoutManager) {
                final GridLayoutManager gridManager = ((GridLayoutManager) manager);
                gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        return (getItemViewType(position) == TYPE_HEADER || getItemViewType(position) == TYPE_FOOTER)
                                ? gridManager.getSpanCount() : 1;
                    }
                });
            }
        }

        @Override
        public void onViewAttachedToWindow(ViewHolder holder) {
            super.onViewAttachedToWindow(holder);
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if(lp != null
                    && lp instanceof StaggeredGridLayoutManager.LayoutParams
                    &&  (isHeader( holder.getLayoutPosition()) || isFooter( holder.getLayoutPosition())) ) {
                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
                p.setFullSpan(true);
            }
        }

        public boolean isHeader(int position) {
            return position == 0&&mHeaderView!=null;
        }

        /**
         * 当前布局是否为Footer
         *
         * @param position
         * @return
         */
        public boolean isFooter(int position) {
            return position == getItemCount()-1 &&mFootView!=null;
        }

        public boolean isRefreshHeader(int position) {
            return position == 0 ;
        }

        public int getHeadersCount() {
            return mHeaderView!=null?1:0;
        }

        public int getFootersCount() {
            return mFootView!=null?1:0;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == TYPE_HEADER) {
                return new SimpleViewHolder(mHeaderView);
                //headerPosition++;
            } else if (viewType == TYPE_FOOTER) {
                return new SimpleViewHolder(mFootView);
            }
            return adapter.onCreateViewHolder(parent, viewType);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            if (isHeader(position)) {
                return;
            }
            int adjPosition = position - getHeadersCount();
            int adapterCount;
            if (adapter != null) {
                adapterCount = adapter.getItemCount();
                if (adjPosition>=0&&adjPosition < adapterCount) {
                    adapter.onBindViewHolder(holder, adjPosition);
                    return;
                }
            }
        }

        @Override
        public int getItemCount() {
            if (adapter != null) {
                return getHeadersCount() + getFootersCount() + adapter.getItemCount();
            } else {
                return getHeadersCount() + getFootersCount();
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (isHeader(position)) {
                return TYPE_HEADER;
            }
            if(isFooter(position)){
                return TYPE_FOOTER;
            }
            int adjPosition = position - getHeadersCount();;
            int adapterCount;
            if (adapter != null) {
                adapterCount = adapter.getItemCount();
                if (adjPosition < adapterCount) {
                    return adapter.getItemViewType(adjPosition);
                }
            }
            return TYPE_NORMAL;
        }

        @Override
        public long getItemId(int position) {
            if (adapter != null && position >= getHeadersCount()) {
                int adjPosition = position - getHeadersCount();
                int adapterCount = adapter.getItemCount();
                if (adjPosition < adapterCount) {
                    return adapter.getItemId(adjPosition);
                }
            }
            return -1;
        }

        private class SimpleViewHolder extends ViewHolder {
            public SimpleViewHolder(View itemView) {
                super(itemView);
            }
        }
    }

    /**
     * 加载更多的接口
     */
    public interface OnLoadMoreListener {
        /**开始加载下一页*/
        public void onLoadMore();
    }

}
