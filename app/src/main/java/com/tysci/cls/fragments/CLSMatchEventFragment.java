package com.tysci.cls.fragments;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tysci.cls.R;
import com.tysci.cls.app.BaseFragment;
import com.tysci.cls.modles.CLSMatchEntity;
import com.tysci.cls.modles.CLSMatchEventEntity;
import com.tysci.cls.modles.CLSMatchEventImageInfoEntity;
import com.tysci.cls.networks.HttpClientUtil;
import com.tysci.cls.networks.HttpUrls;
import com.tysci.cls.utils.KLog;
import com.tysci.cls.views.adapters.CLSMatchEventAdapter;
import com.tysci.cls.views.adapters.CLSMatchEventImageInfoAdapter;
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
public class CLSMatchEventFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,AutoLoadMoreRecyclerView.OnLoadMoreListener{
    @Bind(R.id.tv_home_team_name)
    protected TextView tvHomeTeamName;
    @Bind(R.id.tv_away_team_name)
    protected TextView tvAwayTeamName;
    @Bind(R.id.swipeRefresh)
    protected SwipeRefreshLayout swipeRefresh;
    @Bind(R.id.recyclerView)
    protected AutoLoadMoreRecyclerView recyclerView;
    @Bind(R.id.gv_event_images)
    protected GridView gvEventImages;

    private CLSMatchEntity matchEntity;
    private List<CLSMatchEventEntity> matchEventEntityList=null;
    private CLSMatchEventAdapter adapter=null;

    @Override
    protected int getViewLayoutId() {
        return R.layout.fragment_match_event;
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
                tvHomeTeamName.setText(matchEntity.getHomeTeamName());
                tvAwayTeamName.setText(matchEntity.getAwayTeamName());
                setRefreshing();
                requestMatchEventInfo(matchEntity.getId());
                requestMatchEventImageInfo();
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

    private void requestMatchEventInfo(final int matchId){
        String url= HttpUrls.HTTP_HOST_URL+"match/detail/events/"+matchId;
        KLog.e("Url:"+url);
        HttpClientUtil.getHttpClientUtil().sendGetRequest(Tag, url, 0, new HttpClientUtil.StringResponseCallBack() {
            @Override
            public void onBefore(Request request) {

            }

            @Override
            public void onError(Call call, Exception error) {
                if(matchEventEntityList==null){
                    matchEventEntityList=new ArrayList<CLSMatchEventEntity>();
                    adapter=new CLSMatchEventAdapter(matchEventEntityList);
                    recyclerView.setAdapter(adapter);
                }
                recyclerView.setLoadMoreDataFailed(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        recyclerView.setLoadingMore();
                        requestMatchEventInfo(matchId);
                    }
                });

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
                            if(dataMapObj!=null&&!dataMapObj.isEmpty()){
                                JSONArray datas=dataMapObj.getJSONArray("events");
                                if(datas!=null&&!datas.isEmpty()){
                                    List<CLSMatchEventEntity>dataList=getMatchEventDatas(matchEntity,datas);
                                    if(dataList!=null){
                                        if(matchEventEntityList==null){
                                            matchEventEntityList=new ArrayList<CLSMatchEventEntity>(dataList.size());
                                        }
                                        if(!matchEventEntityList.isEmpty()){
                                            matchEventEntityList.clear();
                                        }
                                        matchEventEntityList.addAll(dataList);
                                        if(adapter==null){
                                            adapter=new CLSMatchEventAdapter(matchEventEntityList);
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
                }
                if(matchEventEntityList==null){
                    matchEventEntityList=new ArrayList<CLSMatchEventEntity>();
                    adapter=new CLSMatchEventAdapter(matchEventEntityList);
                    recyclerView.setAdapter(adapter);
                }
                recyclerView.setLoadMoreDataComplete(R.string.tip_load_complete);
            }

            @Override
            public void onFinish(Call call) {
                onRefreshCompelete();
            }
        });
    }

    private List<CLSMatchEventEntity> getMatchEventDatas(CLSMatchEntity matchEntity,JSONArray datas) {
        if (datas != null && !datas.isEmpty()) {
            int homeTeamId=matchEntity.getHomeTeamId();
            int awayTeamId=matchEntity.getAwayTeamId();
            int size=datas.size();
            List<CLSMatchEventEntity> eventEntityList=new ArrayList<>(size);
            int matchTime=0;
            int time;
            int teamId;
            JSONObject item;
            CLSMatchEventEntity entity=null;
            homeTeamId=371;
            awayTeamId=372;
            for(int i=0;i<size;i++){
                item=datas.getJSONObject(i);
                time=item.getIntValue("time");
                teamId=item.getIntValue("teamId");
                if(i==0){
                    matchTime=time;
                    entity=new CLSMatchEventEntity();
                    entity.setTime(time);
                }else if(matchTime!=time){
                    if(entity!=null){
                        eventEntityList.add(entity);
                        matchTime=time;
                        entity=new CLSMatchEventEntity();
                        entity.setTime(time);
                    }
                }else{
                    if(teamId==homeTeamId){
                        if(entity!=null){
                            if(!TextUtils.isEmpty(entity.getHomePlayerName())){
                                eventEntityList.add(entity);
                                matchTime=time;
                                entity=new CLSMatchEventEntity();
                                entity.setTime(time);
                            }
                        }
                    }else if(teamId==awayTeamId){
                        if(!TextUtils.isEmpty(entity.getAwayPlayerName())){
                            eventEntityList.add(entity);
                            matchTime=time;
                            entity=new CLSMatchEventEntity();
                            entity.setTime(time);
                        }
                    }
                }
                if(teamId==homeTeamId){
                    entity.setHomePlayerName(item.getString("playerName"));
                    entity.setHomeEventImage(item.getString("eventImage"));
                }else if(teamId==awayTeamId){
                    entity.setAwayPlayerName(item.getString("playerName"));
                    entity.setAwayEventImage(item.getString("eventImage"));
                }

                if(i==size-1){
                    if(entity!=null){
                        eventEntityList.add(entity);
                    }
                }
            }
            return eventEntityList;
        }
        return null;
    }



    private void requestMatchEventImageInfo(){
        String url=HttpUrls.HTTP_HOST_URL+"match/detail/event/image";
        HttpClientUtil.getHttpClientUtil().sendGetRequest(Tag, url, 0, new HttpClientUtil.StringResponseCallBack() {
            @Override
            public void onBefore(Request request) {

            }

            @Override
            public void onError(Call call, Exception error) {

            }

            @Override
            public void onSuccess(Call call, String response) {
                KLog.json(response);
                if(!TextUtils.isEmpty(response)){
                    JSONObject obj=JSONObject.parseObject(response);
                    if(obj!=null&&!obj.isEmpty()){
                        int statusCode=obj.getIntValue("statusCode");
                        if(statusCode==200){
                            JSONObject dataMapObj=obj.getJSONObject("dataMap");
                            if(dataMapObj!=null&&!dataMapObj.isEmpty()){
                                JSONArray eventImages=dataMapObj.getJSONArray("eventImages");
                                if(eventImages!=null&&!eventImages.isEmpty()){
                                    List<CLSMatchEventImageInfoEntity> eventImageInfoEntityList=new ArrayList<CLSMatchEventImageInfoEntity>(8);
                                    int size=eventImages.size();
                                    for(int i=0;i<size;i++){
                                        eventImageInfoEntityList.add(eventImages.getObject(i,CLSMatchEventImageInfoEntity.class));
                                    }
                                    CLSMatchEventImageInfoAdapter adapter=new CLSMatchEventImageInfoAdapter(eventImageInfoEntityList);
                                    gvEventImages.setAdapter(adapter);
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onFinish(Call call) {

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
        requestMatchEventImageInfo();
        requestMatchEventInfo(matchEntity.getId());

    }
}
