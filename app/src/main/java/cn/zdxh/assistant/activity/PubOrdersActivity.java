package cn.zdxh.assistant.activity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import cn.zdxh.assistant.R;
import cn.zdxh.assistant.adapter.PubOrdersAdapter;
import cn.zdxh.assistant.base.BaseActivity;
import cn.zdxh.assistant.bean.User;
import cn.zdxh.assistant.bean.UserAndPublish;
import cn.zdxh.assistant.utils.OkHttpUtils;
import cn.zdxh.assistant.utils.UserUtils;

public class PubOrdersActivity extends BaseActivity {

    private int PUBORDERS=1;
    private int FAIL=2;
    private ImageView back;
    private ListView listView;

    @SuppressLint("HandlerLeak")
    Handler handler=new Handler() {
        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            if (message.what == PUBORDERS){
                    //将请求所得数据传进适配器
                    PubOrdersAdapter pubOrdersAdapter=new PubOrdersAdapter((List<UserAndPublish>)message.obj,getApplicationContext());
                    listView.setAdapter(pubOrdersAdapter);
            }else if (message.what==FAIL) {
                Toast.makeText(getApplicationContext(),"暂无发布预约的信息",Toast.LENGTH_SHORT).show();
            }
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pub_orders);
        initView();
        initListener();
        //获取登录的信息
        User user = UserUtils.getUser(getApplicationContext());
        if (user!=null && user.getId()!=null){
            //查询该登录人发布的订单
            queryPubOrders(user.getId());
        }


    }

    public void initView(){
        back=findViewById(R.id.iv_pub_order_back);
        listView=findViewById(R.id.lv_pub_orders);
    }

    public void initListener(){
        //返回
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void queryPubOrders(final Integer uId){
        new Thread(new Runnable() {
            @Override public void run() {
                //请求数据
                String json= OkHttpUtils.OkHttpGet("http://47.107.126.98:8090/ord/pub/"+uId);
                //Gson解析json数据
                Gson gson=new Gson();
                List<UserAndPublish> userAndPublishes = gson.fromJson(json,new TypeToken<List<UserAndPublish>>(){}.getType());
                Message message=Message.obtain();
                //发送消息，不能在子线程更新UI
                if (userAndPublishes!=null && userAndPublishes.size()>0 && userAndPublishes.get(0)!=null){
                    message.obj=userAndPublishes;
                    message.what=PUBORDERS;
                    handler.sendMessage(message);
                }else {
                    message.what=FAIL;
                    handler.sendMessage(message);
                }

            }
        }).start();//不要忘记start线程
    }
}
