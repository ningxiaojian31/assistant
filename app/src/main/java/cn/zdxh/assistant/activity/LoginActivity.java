package cn.zdxh.assistant.activity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
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
import cn.zdxh.assistant.bean.User;
import cn.zdxh.assistant.utils.OkHttpUtils;

public class LoginActivity extends BaseActivity {

    private int SUCCESS=1;
    private int FAIL=2;
    private ImageView back;
    private EditText username;
    private EditText password;
    private Button login;

    @SuppressLint("HandlerLeak")
    Handler handler=new Handler() {
        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            if (message.what == SUCCESS){
                User user=(User) message.obj;
                Toast.makeText(getApplicationContext(),"登录成功！",Toast.LENGTH_SHORT).show();
                //存储登录的用户的各种信息
                SharedPreferences sp=getSharedPreferences("admin",MODE_PRIVATE);
                SharedPreferences.Editor editor=sp.edit();
                editor.putString("id",String.valueOf(user.getId()) );
                editor.putString("username",user.getUserLoginname());
                editor.putString("password",user.getUserPassword());
                editor.putString("name",user.getUserName());
                editor.putString("image",user.getUserImage());
                editor.putString("studentID",user.getUserStudentID());
                editor.putString("wxNumber",user.getUserWxNumber());
                editor.commit();
                finish();
            }else if (message.what==FAIL){
                Toast.makeText(getApplicationContext(),"用户名或者密码错误！",Toast.LENGTH_SHORT).show();
            }
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initListener();
    }

    public void initView(){
        back=findViewById(R.id.iv_login_back);
        username=findViewById(R.id.et_username_login);
        password=findViewById(R.id.et_password_login);
        login=findViewById(R.id.btn_login);
    }

    public void initListener(){
        //返回
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //登录
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernameStr = username.getText().toString().trim();
                String passwordStr = password.getText().toString().trim();
                if (!"".equals(usernameStr) && !"".equals(passwordStr)){
                    User user=new User();
                    user.setUserLoginname(usernameStr);
                    user.setUserPassword(passwordStr);
                    checkLogin(user);//请求登录
                }else {
                    Toast.makeText(getApplicationContext(),"用户名或密码不能为空！",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void checkLogin(final User user){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Gson gson=new Gson();
                String json = gson.toJson(user);
                String resultStr = OkHttpUtils.OkHttpPost("http://47.107.126.98:8090/user/log", json);
                User userResult = gson.fromJson(resultStr, User.class);
                Message message = Message.obtain();
                if (userResult!=null){
                    message.obj=userResult;
                    message.what=SUCCESS;
                    handler.sendMessage(message);
                }else {
                    message.what=FAIL;
                    handler.sendMessage(message);
                }

            }
        }).start();
    }

}
