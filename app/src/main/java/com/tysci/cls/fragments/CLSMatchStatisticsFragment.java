package com.tysci.cls.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tysci.cls.R;
import com.tysci.cls.app.BaseFragment;
import com.tysci.cls.modles.CLSMatchEntity;
import com.tysci.cls.modles.CLSMatchStatisticsEntity;
import com.tysci.cls.networks.HttpClientUtil;
import com.tysci.cls.networks.HttpUrls;
import com.tysci.cls.utils.KLog;
import com.tysci.cls.views.adapters.CLSMatchStatisticsAdapter;
import com.tysci.cls.views.widgets.DividerDecoration;
import com.tysci.cls.views.widgets.loadmorerecyclerview.AutoLoadMoreRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by Administrator on 2016/7/8.
 */
public class CLSMatchStatisticsFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,AutoLoadMoreRecyclerView.OnLoadMoreListener{
    @Bind(R.id.swipeRefresh)
    protected SwipeRefreshLayout swipeRefresh;
    @Bind(R.id.recyclerView)
    protected AutoLoadMoreRecyclerView recyclerView;
    @Bind(R.id.tv_home_team_info)
    protected TextView tvHomeTeamInfo;
    @Bind(R.id.tv_away_team_info)
    protected TextView tvAwayTeamInfo;

    private CLSMatchEntity matchEntity=null;
    private List<CLSMatchStatisticsEntity> matchStatisticsEntityList=null;
    private CLSMatchStatisticsAdapter adapter=null;

    private boolean isRunning=false;
    private Handler handler=new Handler();
    private Runnable task=new Runnable() {
        @Override
        public void run() {
            if(swipeRefresh!=null){
                if(swipeRefresh.isRefreshing()){
                    return;
                }
                if(!isRunning) {
                    isRunning = true;
                    requestMatchStatisticsInfo(matchEntity.getId());
                    handler.postDelayed(task, 60000);
                }
            }
        }
    };

    @Override
    protected int getViewLayoutId() {
        return R.layout.fragment_match_statistics;
    }

    @Override
    protected void initViews(View view, Bundle savedInstanceState) {
        recyclerView.setLayoutManager(new LinearLayoutManager(baseActivity));
        recyclerView.setOnLoadMoreListener(this);
        swipeRefresh.setOnRefreshListener(this);
        Bundle data=getArguments();
        if(data!=null){
            matchEntity=data.getParcelable("match_info");
            if(matchEntity!=null){
                tvHomeTeamInfo.setText(matchEntity.getHomeTeamName());
                tvAwayTeamInfo.setText(matchEntity.getAwayTeamName());
                setRefreshing();
                requestMatchStatisticsInfo(matchEntity.getId());
                handler.postDelayed(task, 60000);
            }
        }
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    private void setRefreshing() {
        swipeRefresh.post(new Runnable() {
            @Override
            public void run() {
                swipeRefresh.setRefreshing(true);
            }
        });
    }

    private void onRefreshCompelete() {
        if(swipeRefresh!=null) {
            swipeRefresh.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (swipeRefresh != null) {
                        swipeRefresh.setRefreshing(false);
                    }
                }
            }, 1000);
        }
    }

    private void requestMatchStatisticsInfo(final int matchId){
        String url= HttpUrls.HTTP_HOST_URL+"match/detail/statistics/"+matchId;
        KLog.e("url:"+url);
        HttpClientUtil.getHttpClientUtil().sendGetRequest(Tag, url, 0, new HttpClientUtil.StringResponseCallBack() {
            @Override
            public void onBefore(Request request) {
                isRunning=true;
            }

            @Override
            public void onError(Call call, Exception error) {
                if(matchStatisticsEntityList==null){
                    matchStatisticsEntityList=new ArrayList<>();
                    adapter=new CLSMatchStatisticsAdapter(matchStatisticsEntityList);
                    recyclerView.setAdapter(adapter);
                }
                if(matchStatisticsEntityList.isEmpty()) {
                    recyclerView.setLoadMoreDataFailed(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            recyclerView.setLoadingMore();
                            requestMatchStatisticsInfo(matchId);
                        }
                    });
                }
            }

            @Override
            public void onSuccess(Call call, String response) {
                KLog.json(response);
                if(!TextUtils.isEmpty(response)){
                    JSONObject obj=JSONObject.parseObject(response);
                    if(obj!=null&&!obj.isEmpty()){
                        int statusCode=obj.getIntValue("code");
                        if(statusCode==200){
                            JSONObject dataMapObj=obj.getJSONObject("data");
                            if (dataMapObj != null && !dataMapObj.isEmpty()) {
                                JSONArray statisArrays=dataMapObj.getJSONArray("statistics");
                                if(statisArrays!=null&&!statisArrays.isEmpty()){
                                    if(matchStatisticsEntityList==null){
                                        matchStatisticsEntityList=new ArrayList<CLSMatchStatisticsEntity>(10);
                                    }
                                    if(!matchStatisticsEntityList.isEmpty()){
                                        matchStatisticsEntityList.clear();
                                    }
                                    int size=statisArrays.size();
                                    for(int i=0;i<size;i++){
                                        matchStatisticsEntityList.add(statisArrays.getObject(i,CLSMatchStatisticsEntity.class));
                                    }
                                    if(adapter==null){
                                        adapter=new CLSMatchStatisticsAdapter(matchStatisticsEntityList);
                                        recyclerView.setAdapter(adapter);
                                        recyclerView.addItemDecoration(new DividerDecoration(baseActivity));
                                    }else{
                                        adapter.notifyDataSetChanged();
                                    }
                                    recyclerView.setLoadMoreDataComplete(R.string.tip_load_complete);
                                    return;
                                }
                            }
                        }
                    }
                }
                if(matchStatisticsEntityList==null){
                    matchStatisticsEntityList=new ArrayList<>();
                    adapter=new CLSMatchStatisticsAdapter(matchStatisticsEntityList);
                    recyclerView.setAdapter(adapter);
                }
                recyclerView.setLoadMoreDataComplete(R.string.tip_load_complete);
            }

            @Override
            public void onFinish(Call call) {
                isRunning=false;
                onRefreshCompelete();
            }
        });
    }

    @Override
    protected boolean isCanceledEventBus() {
        return false;
    }

    @Override
    protected void notifyEvent(String action) {

    }

    @Override
    protected void notifyEvent(String action, Bundle data) {

    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public void onRefresh() {
        requestMatchStatisticsInfo(matchEntity.getId());

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(task);
    }
}
