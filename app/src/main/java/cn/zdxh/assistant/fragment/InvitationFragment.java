package cn.zdxh.assistant.fragment;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cn.zdxh.assistant.R;
import cn.zdxh.assistant.activity.InvitationDetailActivity;
import cn.zdxh.assistant.adapter.InvitationsAdapter;
import cn.zdxh.assistant.bean.Invitation;
import cn.zdxh.assistant.bean.PageIns;
import cn.zdxh.assistant.utils.OkHttpUtils;


public class InvitationFragment extends Fragment {

    private int INVITATIONS=1;
    private List<Invitation> invitations;
    private ListView listView;
    private SmartRefreshLayout refreshLayout;

    @SuppressLint("HandlerLeak")
    Handler handler=new Handler() {
        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            if (message.what == INVITATIONS){
                //将请求所得数据传进适配器
                InvitationsAdapter invitationsAdapter=new InvitationsAdapter(invitations,getContext());
                listView.setAdapter(invitationsAdapter);
                //初始化监听器,传入树洞的id，然后在另外一个activity就根据这个id查询树洞详情
                initListener();
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //初始化控件
        View view = inflater.inflate(R.layout.fragment_invitation, container, false);
        listView=view.findViewById(R.id.lv_invs);
        refreshLayout = view.findViewById(R.id.srl_fragment_inv);
        invitations=new ArrayList<>();

        //请求获取所有的树洞，可以分页
        queryInvitations();

        return view;
    }

    public void initListener(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getActivity(), InvitationDetailActivity.class);
                Bundle bundle=new Bundle();
                Invitation invitationBudle = invitations.get(position);
                bundle.putSerializable("invitation",invitationBudle);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        //刷新的监听事件
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                //请求数据
                queryInvitations();
                refreshLayout.finishRefresh();  //刷新完成
            }
        });

    }


    public void queryInvitations() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //请求数据
                String json = OkHttpUtils.OkHttpGet("http://47.107.126.98:8090/invs/0/100");
                //Gson解析json数据
                Gson gson = new Gson();
                PageIns pageIns = gson.fromJson(json, PageIns.class);
                invitations=pageIns.getContent();
                //发送消息，不能在子线程更新UI
                Message message = Message.obtain();
                message.obj = invitations;
                message.what = INVITATIONS;
                handler.sendMessage(message);
            }
        }).start();
    }

    @Override
    public void onResume() {
        super.onResume();
        queryInvitations();
    }
}
