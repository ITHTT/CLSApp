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
import com.tysci.cls.modles.CLSMatchTipOffEntity;
import com.tysci.cls.networks.HttpClientUtil;
import com.tysci.cls.networks.HttpUrls;
import com.tysci.cls.utils.KLog;
import com.tysci.cls.views.adapters.CLSMatchTipOffListAdapter;
import com.tysci.cls.views.widgets.loadmorerecyclerview.AutoLoadMoreRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by Administrator on 2016/7/8.
 */
public class CLSMatchTipOffListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,AutoLoadMoreRecyclerView.OnLoadMoreListener{
    @Bind(R.id.swipeRefresh)
    protected SwipeRefreshLayout swipeRefresh;
    @Bind(R.id.recyclerView)
    protected AutoLoadMoreRecyclerView recyclerView;

    private CLSMatchEntity matchEntity=null;
    private List<CLSMatchTipOffEntity> matchTipOffEntityList=null;
    private CLSMatchTipOffListAdapter adapter=null;

    @Override
    protected int getViewLayoutId() {
        return R.layout.fragment_match_tip_off_list;
    }

    @Override
    protected void initViews(View view, Bundle savedInstanceState) {
        recyclerView.setLayoutManager(new LinearLayoutManager(baseActivity));
        swipeRefresh.setOnRefreshListener(this);
        recyclerView.setOnLoadMoreListener(this);
        Bundle data=getArguments();
        if(data!=null){
            matchEntity=data.getParcelable("match_info");
            if(matchEntity!=null){
                requestMatchTipOffInfo(matchEntity.getId());
            }
        }
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    private void requestMatchTipOffInfo(final int matchId){
        String url= HttpUrls.HTTP_HOST_URL+"match/detail/tips/"+matchId;
        KLog.e("url:"+url);
        HttpClientUtil.getHttpClientUtil().sendGetRequest(Tag, url, 0, new HttpClientUtil.StringResponseCallBack() {
            @Override
            public void onBefore(Request request) {

            }

            @Override
            public void onError(Call call, Exception error) {
                if(matchTipOffEntityList==null){
                    matchTipOffEntityList=new ArrayList<CLSMatchTipOffEntity>();
                    adapter=new CLSMatchTipOffListAdapter(matchTipOffEntityList);
                    recyclerView.setAdapter(adapter);
                }
                recyclerView.setLoadMoreDataFailed(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        recyclerView.setLoadingMore();
                        requestMatchTipOffInfo(matchId);
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
                                JSONArray tipArrays=dataMapObj.getJSONArray("tips");
                                if(tipArrays!=null&&!tipArrays.isEmpty()){
                                    if(matchTipOffEntityList==null){
                                        matchTipOffEntityList=new ArrayList<CLSMatchTipOffEntity>(10);
                                    }
                                    int size=tipArrays.size();
                                    for(int i=0;i<size;i++){
                                        matchTipOffEntityList.add(tipArrays.getObject(i,CLSMatchTipOffEntity.class));
                                    }
                                    if(adapter==null){
                                        adapter=new CLSMatchTipOffListAdapter(matchTipOffEntityList);
                                        recyclerView.setAdapter(adapter);
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
                if(matchTipOffEntityList==null){
                    matchTipOffEntityList=new ArrayList<CLSMatchTipOffEntity>();
                    adapter=new CLSMatchTipOffListAdapter(matchTipOffEntityList);
                    recyclerView.setAdapter(adapter);
                }
                recyclerView.setLoadMoreDataComplete(R.string.tip_load_complete);
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

    }
}
