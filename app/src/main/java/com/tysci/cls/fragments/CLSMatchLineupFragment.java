package com.tysci.cls.fragments;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tysci.cls.R;
import com.tysci.cls.app.BaseFragment;
import com.tysci.cls.modles.CLSMatchEntity;
import com.tysci.cls.modles.CLSMatchLineupEntity;
import com.tysci.cls.networks.HttpClientUtil;
import com.tysci.cls.networks.HttpUrls;
import com.tysci.cls.utils.KLog;
import com.tysci.cls.views.adapters.CLSMatchLineupAdapter;
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
public class CLSMatchLineupFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,AutoLoadMoreRecyclerView.OnLoadMoreListener{
    @Bind(R.id.swipeRefresh)
    protected SwipeRefreshLayout swipeRefresh;
    @Bind(R.id.recyclerView)
    protected AutoLoadMoreRecyclerView recyclerView;

    private CLSMatchEntity matchEntity;
    private List<CLSMatchLineupEntity> lineupEntityList=null;
    private CLSMatchLineupAdapter adapter=null;

    @Override
    protected int getViewLayoutId() {
        return R.layout.fragment_match_lineup;
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
                setRefreshing();
                requestMatchLineupInfo(matchEntity.getId());
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

    private void requestMatchLineupInfo(final int matchId){
        String url= HttpUrls.HTTP_HOST_URL+"match/detail/lineups/"+matchId;
        KLog.e("url:"+url);
        HttpClientUtil.getHttpClientUtil().sendGetRequest(Tag, url, 0, new HttpClientUtil.StringResponseCallBack() {
            @Override
            public void onBefore(Request request) {

            }

            @Override
            public void onError(Call call, Exception error) {
                if(lineupEntityList==null){
                    lineupEntityList=new ArrayList<CLSMatchLineupEntity>();
                    adapter=new CLSMatchLineupAdapter(lineupEntityList);
                    recyclerView.setAdapter(adapter);
                }
                recyclerView.setLoadMoreDataFailed(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        recyclerView.setLoadingMore();
                        requestMatchLineupInfo(matchId);
                    }
                });
            }

            @Override
            public void onSuccess(Call call, String response) {
                KLog.json(response);
                JSONObject obj=JSONObject.parseObject(response);
                if(obj!=null&&!obj.isEmpty()){
                    int statusCode=obj.getIntValue("code");
                    if(statusCode==200){
                        JSONObject dataMap=obj.getJSONObject("data");
                        if(dataMap!=null&&!dataMap.isEmpty()){
                            JSONObject lineupObj=dataMap.getJSONObject("lineups");
                            if(lineupObj!=null&&!lineupObj.isEmpty()){
                                JSONObject awayTeamObj= lineupObj.getJSONObject("awayTeam");
                                JSONObject homeTeamObj=lineupObj.getJSONObject("homeTeam");

                                JSONArray homeFirstLineups=null;
                                JSONArray homeSecondLineups=null;
                                JSONArray awayFirstLineups=null;
                                JSONArray awaySecondLineups=null;

                                if(awayTeamObj!=null&&!awayTeamObj.isEmpty()){
                                    awayFirstLineups=awayTeamObj.getJSONArray("firstLineup");
                                    awaySecondLineups=awayTeamObj.getJSONArray("secondLineup");
                                }

                                if(homeTeamObj!=null&&!homeTeamObj.isEmpty()){
                                    homeFirstLineups=homeTeamObj.getJSONArray("firstLineup");
                                    homeSecondLineups=homeTeamObj.getJSONArray("secondLineup");
                                }

                                List<CLSMatchLineupEntity> firstLineups=getMatchFirstLineup(homeFirstLineups,awayFirstLineups);
                                List<CLSMatchLineupEntity> secondLineups=getMatchSecondLineups(homeSecondLineups,awaySecondLineups);

                                if(firstLineups!=null||secondLineups!=null){
                                    if(lineupEntityList==null){
                                        lineupEntityList=new ArrayList<CLSMatchLineupEntity>();
                                    }
                                    if(!lineupEntityList.isEmpty()){
                                        lineupEntityList.clear();
                                    }
                                    if(firstLineups!=null){
                                        lineupEntityList.addAll(firstLineups);
                                    }
                                    if(secondLineups!=null){
                                        lineupEntityList.addAll(secondLineups);
                                    }

                                    if(adapter==null){
                                        adapter=new CLSMatchLineupAdapter(lineupEntityList);
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
                if(lineupEntityList==null){
                    lineupEntityList=new ArrayList<CLSMatchLineupEntity>();
                    adapter=new CLSMatchLineupAdapter(lineupEntityList);
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

    private List<CLSMatchLineupEntity> getMatchFirstLineup(JSONArray homeLineups,JSONArray awayLineups){
        if(homeLineups!=null||awayLineups!=null||!homeLineups.isEmpty()||!awayLineups.isEmpty()){
            int homeSize=0;
            if(homeLineups!=null){
                homeSize=homeLineups.size();
            }
            int awaySize=0;
            if(awayLineups!=null){
                awaySize=awayLineups.size();
            }
            List<CLSMatchLineupEntity> datas=null;
            CLSMatchLineupEntity lineupEntity=null;
            if(homeSize>0||awaySize>0){
                datas=new ArrayList<>();
                /**添加标题栏数据*/
                lineupEntity=new CLSMatchLineupEntity();
                lineupEntity.setHomePlayerName(matchEntity.getHomeTeamName());

                lineupEntity.setAwayPlayerName(matchEntity.getAwayTeamName());
                lineupEntity.setType(1);
                datas.add(lineupEntity);
            }
            for(int i=0;i<homeSize||i<awaySize;i++){
                lineupEntity=new CLSMatchLineupEntity();
                if(homeLineups!=null&&i<homeSize){
                    JSONObject homeObj=homeLineups.getJSONObject(i);
                    if(homeObj!=null&&!homeObj.isEmpty()){
                        lineupEntity.setHomePlayerId(homeObj.getIntValue("playerId"));
                        lineupEntity.setHomePlayerNo(homeObj.getIntValue("playerNo"));
                        lineupEntity.setHomePlayerName(homeObj.getString("playerName"));
                    }
                }
                if(awayLineups!=null&&i<awaySize){
                    JSONObject awayObj=awayLineups.getJSONObject(i);
                    if(awayObj!=null&&!awayObj.isEmpty()){
                        lineupEntity.setAwayPlayerId(awayObj.getIntValue("playerId"));
                        lineupEntity.setAwayPlayerNo(awayObj.getIntValue("playerNo"));
                        lineupEntity.setAwayPlayerName(awayObj.getString("playerName"));
                    }
                }
                lineupEntity.setType(2);
                datas.add(lineupEntity);
            }
            return datas;
        }
        return null;
    }

    private List<CLSMatchLineupEntity> getMatchSecondLineups(JSONArray homeLineups,JSONArray awayLineups){
        if(homeLineups!=null||awayLineups!=null||!homeLineups.isEmpty()||!awayLineups.isEmpty()){
            int homeSize=0;
            if(homeLineups!=null){
                homeSize=homeLineups.size();
            }
            int awaySize=0;
            if(awayLineups!=null){
                awaySize=awayLineups.size();
            }
            List<CLSMatchLineupEntity> datas=null;
            CLSMatchLineupEntity lineupEntity=null;
            if(homeSize>0||awaySize>0){
                datas=new ArrayList<>();
                /**添加替补标题栏数据*/
                lineupEntity=new CLSMatchLineupEntity();
                lineupEntity.setType(3);
                datas.add(lineupEntity);
            }

            for(int i=0;i<homeSize||i<awaySize;i++){
                lineupEntity=new CLSMatchLineupEntity();
                if(homeLineups!=null&&i<homeSize){
                    JSONObject homeObj=homeLineups.getJSONObject(i);
                    if(homeObj!=null&&!homeObj.isEmpty()){
                        lineupEntity.setHomePlayerId(homeObj.getIntValue("playerId"));
                        lineupEntity.setHomePlayerNo(homeObj.getIntValue("playerNo"));
                        lineupEntity.setHomePlayerName(homeObj.getString("playerName"));
                    }
                }

                if(awayLineups!=null&&i<awaySize){
                    JSONObject awayObj=awayLineups.getJSONObject(i);
                    if(awayObj!=null&&!awayObj.isEmpty()){
                        lineupEntity.setAwayPlayerId(awayObj.getIntValue("playerId"));
                        lineupEntity.setAwayPlayerNo(awayObj.getIntValue("playerNo"));
                        lineupEntity.setAwayPlayerName(awayObj.getString("playerName"));
                    }
                }
                lineupEntity.setType(2);
                datas.add(lineupEntity);
            }
            return datas;
        }
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

    @Override
    public void onRefresh() {
        requestMatchLineupInfo(matchEntity.getId());
    }

    @Override
    public void onLoadMore() {

    }
}
