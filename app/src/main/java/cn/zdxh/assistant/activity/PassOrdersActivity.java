package cn.zdxh.assistant.activity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
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
import cn.zdxh.assistant.adapter.PassOrdersAdapter;
import cn.zdxh.assistant.adapter.PubOrdersAdapter;
import cn.zdxh.assistant.base.BaseActivity;
import cn.zdxh.assistant.bean.User;
import cn.zdxh.assistant.bean.UserAndPublish;
import cn.zdxh.assistant.utils.OkHttpUtils;
import cn.zdxh.assistant.utils.UserUtils;

public class PassOrdersActivity extends BaseActivity {

    private int PASSORDERS=1;
    private int FAIL=2;
    private ImageView back;
    private ListView listView;

    @SuppressLint("HandlerLeak")
    Handler handler=new Handler() {
        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            if (message.what == PASSORDERS){
                    //将请求所得数据传进适配器
                    PassOrdersAdapter passOrdersAdapter=new PassOrdersAdapter((List<UserAndPublish>)message.obj,getApplicationContext());
                    listView.setAdapter(passOrdersAdapter);
            } else if (message.what==FAIL) {
                Toast.makeText(getApplicationContext(),"暂无申请预约的信息",Toast.LENGTH_SHORT).show();
            }
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_orders);
        initView();
        initListener();
        //获取登录的信息
        User user = UserUtils.getUser(getApplicationContext());
        if (user!=null && user.getId()!=null){
            //查询该登录人发布的订单
            queryPassOrders(user.getId());
        }

    }

    public void initView(){
        back=findViewById(R.id.iv_pass_order_back);
        listView=findViewById(R.id.lv_pass_orders);
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

    public void queryPassOrders(final Integer uId){
        new Thread(new Runnable() {
            @Override public void run() {
                //请求数据
                String json= OkHttpUtils.OkHttpGet("http://47.107.126.98:8090/ord/app/"+uId);
                //Gson解析json数据
                Gson gson=new Gson();
                List<UserAndPublish> userAndPublishes = gson.fromJson(json,new TypeToken<List<UserAndPublish>>(){}.getType());
                Message message=Message.obtain();
                if (userAndPublishes!=null && userAndPublishes.size()>0 && userAndPublishes.get(0)!=null){
                    //发送消息，不能在子线程更新UI
                    message.obj=userAndPublishes;
                    message.what=PASSORDERS;
                    handler.sendMessage(message);
                }else {
                    message.what=FAIL;
                    handler.sendMessage(message);
                }

            }
        }).start();//不要忘记start线程
    }
}
