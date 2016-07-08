package com.tysci.cls.fragments;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tysci.cls.R;
import com.tysci.cls.app.BaseFragment;
import com.tysci.cls.modles.CLSMatchEntity;
import com.tysci.cls.networks.HttpClientUtil;
import com.tysci.cls.networks.HttpUrls;
import com.tysci.cls.views.adapters.CLSMatchAdapter;
import com.tysci.cls.views.widgets.loadmorerecyclerview.AutoLoadMoreRecyclerView;
import com.tysci.cls.views.widgets.recyclerviewstickyheader.StickyHeaderDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by Administrator on 2016/7/7.
 */
public class CLSMatchListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{
    @Bind(R.id.swipeRefresh)
    protected SwipeRefreshLayout swipeRefresh;
    @Bind(R.id.recyclerView)
    protected AutoLoadMoreRecyclerView recyclerView;

    private List<CLSMatchEntity> matchEntityList=null;
    private CLSMatchAdapter adapter=null;

    @Override
    protected int getViewLayoutId() {
        return R.layout.fragment_match_list;
    }

    @Override
    protected void initViews(View view, Bundle savedInstanceState) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        swipeRefresh.setOnRefreshListener(this);
        setRefreshing();
        requestMatchListInfos();
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


    @Override
    protected View getLoadingTargetView() {
        return null;
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

    private void requestMatchListInfos(){
        String url= HttpUrls.HTTP_HOST_URL+"match/list";
        HttpClientUtil.getHttpClientUtil().sendGetRequest(Tag, url, 60, new HttpClientUtil.StringResponseCallBack() {
            @Override
            public void onBefore(Request request) {

            }

            @Override
            public void onError(Call call, Exception error) {

            }

            @Override
            public void onSuccess(Call call, String response) {
                if(!TextUtils.isEmpty(response)){
                    JSONObject obj=JSONObject.parseObject(response);
                    if(obj!=null&&!obj.isEmpty()){
                        int statusCode=obj.getIntValue("statusCode");
                        if(statusCode==200){
                            JSONObject dataObj=obj.getJSONObject("dataMap");
                            if(dataObj!=null&&!dataObj.isEmpty()){
                                JSONArray matchArrays=dataObj.getJSONArray("teamMatchs");
                                if(matchArrays!=null&&!matchArrays.isEmpty()){
                                    if(matchEntityList==null){
                                        matchEntityList=new ArrayList<CLSMatchEntity>(matchArrays.size());
                                    }
                                    getMatchListDatas(matchArrays,matchEntityList);
                                    if(adapter==null){
                                        adapter=new CLSMatchAdapter(matchEntityList);
                                        StickyHeaderDecoration decoration=new StickyHeaderDecoration(adapter);
                                        recyclerView.setAdapter(adapter);
                                        recyclerView.addItemDecoration(decoration);
                                    }else{
                                        adapter.notifyDataSetChanged();
                                    }
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onFinish(Call call) {
                onRefreshCompelete();
            }
        });
    }


    private void getMatchListDatas(JSONArray datas,List list){
        int size=datas.size();
        for(int i=0;i<size;i++){
            JSONObject data=datas.getJSONObject(i);
            if(data!=null&&!data.isEmpty()){
                String groupName=data.getString("groupName");
                String matchDate=data.getString("matchDate");
                JSONArray matchArrays=data.getJSONArray("matchs");
                if(matchArrays!=null&&!matchArrays.isEmpty()){
                    int lenth=matchArrays.size();
                    for(int j=0;j<lenth;j++){
                        CLSMatchEntity match=matchArrays.getObject(j, CLSMatchEntity.class);
                        match.setGroupName(groupName);
                        match.setMatchDateWeek(matchDate);
                        list.add(match);
                    }
                }
            }
        }
    }

    @Override
    public void onRefresh() {
        requestMatchListInfos();
    }
}
