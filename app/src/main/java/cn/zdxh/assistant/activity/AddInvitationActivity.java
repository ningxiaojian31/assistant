package cn.zdxh.assistant.activity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.google.gson.Gson;


import cn.zdxh.assistant.R;
import cn.zdxh.assistant.base.BaseActivity;
import cn.zdxh.assistant.bean.Invitation;
import cn.zdxh.assistant.bean.User;
import cn.zdxh.assistant.utils.OkHttpUtils;
import cn.zdxh.assistant.utils.UserUtils;

public class AddInvitationActivity extends BaseActivity {

    private int SEND=1;
    private ImageView imageBack;
    private EditText addEdit;
    private Button sendBtn;

    @SuppressLint("HandlerLeak")
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==SEND){
                //请求数据成功后更新UI
                Toast.makeText(getApplicationContext(),(String)msg.obj,Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_invitation);
        initView();
        initListener();
    }

    public void initView(){
        imageBack=findViewById(R.id.iv_inv_add_back);
        addEdit=findViewById(R.id.et_inv_add);
        sendBtn=findViewById(R.id.btn_inv_add);
    }

    public void initListener(){
        //返回键
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //发送按钮
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = UserUtils.getUser(getApplicationContext());
                if (user!=null && user.getId()!=null){
                    String editStr = addEdit.getText().toString().trim();
                    Gson gson=new Gson();
                    //封装要传输的json对象
                    Invitation invitation=new Invitation();
                    invitation.setInvLaud(0);
                    invitation.setInvContent(editStr);
                    final String json = gson.toJson(invitation);//对象转json
                    if (!"".equals(editStr)){
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String resultStr = OkHttpUtils.OkHttpPost("http://47.107.126.98:8090/inv", json);
                                Message message = Message.obtain();
                                message.what=SEND;
                                message.obj=resultStr;
                                handler.sendMessage(message);
                            }
                        }).start();
                    }else {
                        Toast.makeText(getApplicationContext(),"发布内容不能为空！",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getApplicationContext(),"亲！登录后才能发布树洞哦~~~",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
