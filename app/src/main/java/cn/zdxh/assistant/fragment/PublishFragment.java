package cn.zdxh.assistant.fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import cn.zdxh.assistant.R;
import cn.zdxh.assistant.adapter.PublishsAdapter;
import cn.zdxh.assistant.bean.PagePub;
import cn.zdxh.assistant.bean.Publish;
import cn.zdxh.assistant.utils.OkHttpUtils;


public class PublishFragment extends Fragment {

    private int PUBLISHS=1;
    private List<Publish> publishes;
    private ListView listView;
    private SmartRefreshLayout refreshLayout;

    @SuppressLint("HandlerLeak")
    Handler handler=new Handler() {
        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            if (message.what == PUBLISHS){
                //将请求所得数据传进适配器
                PublishsAdapter publishsAdapter=new PublishsAdapter(publishes,getContext());
                listView.setAdapter(publishsAdapter);

            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_publish, container, false);
        listView=view.findViewById(R.id.lv_pubs);
        refreshLayout=view.findViewById(R.id.srl_fragment_pub);
        initListener();
        publishes=new ArrayList<>();
        //初始化数据
        queryPublishs();
        return view;
    }

    public void initListener(){
        //刷新的监听事件
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                //请求数据
                queryPublishs();
                refreshLayout.finishRefresh();  //刷新完成
            }
        });
    }


    public void queryPublishs() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //请求数据
                String json = OkHttpUtils.OkHttpGet("http://47.107.126.98:8090/pubs/0/100");
                //Gson解析json数据
                Gson gson = new Gson();
                PagePub pagePub = gson.fromJson(json, PagePub.class);
                publishes=pagePub.getContent();
                //发送消息，不能在子线程更新UI
                Message message = Message.obtain();
                message.obj = publishes;
                message.what = PUBLISHS;
                handler.sendMessage(message);
            }
        }).start();
    }

    @Override
    public void onResume() {
        super.onResume();
        //发布之后更新数据
        queryPublishs();
    }
}
