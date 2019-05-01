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

public class RegisterActivity extends BaseActivity {

    private int SUCCESS=1;
    private int FAIL=2;
    private ImageView back;
    private EditText username;
    private EditText password;
    private EditText name;
    private EditText studentID;
    private EditText wxNumber;
    private Button register;

    @SuppressLint("HandlerLeak")
    Handler handler=new Handler() {
        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            if (message.what == SUCCESS){
                User user=(User) message.obj;
                Toast.makeText(getApplicationContext(),"注册成功！",Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getApplicationContext(),"该用户已经注册！",Toast.LENGTH_SHORT).show();
            }
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        initListener();
    }

    public void initView(){
        back=findViewById(R.id.iv_register_back);
        username=findViewById(R.id.et_username_register);
        password=findViewById(R.id.et_password_register);
        name=findViewById(R.id.et_name_register);
        studentID=findViewById(R.id.et_studentID_register);
        wxNumber=findViewById(R.id.et_wxNumber_register);
        register=findViewById(R.id.btn_register);
    }

    public void initListener(){
        //返回
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //点击注册
        register.setOnClickListener(new View.OnClickListener() {
            private String usernameStr;
            private String passwordStr;
            private String nameStr;
            private String studentIDStr;
            private String wxNumberStr;
            @Override
            public void onClick(View v) {
                usernameStr=username.getText().toString().trim();
                passwordStr=password.getText().toString().trim();
                nameStr=name.getText().toString().trim();
                studentIDStr=studentID.getText().toString().trim();
                wxNumberStr=wxNumber.getText().toString().trim();
                if (!"".equals(usernameStr) && !"".equals(passwordStr) && !"".equals(nameStr) &&
                        !"".equals(studentIDStr) && !"".equals(wxNumberStr)){
                    User user=new User();
                    user.setUserLoginname(usernameStr);
                    user.setUserPassword(passwordStr);
                    user.setUserName(nameStr);
                    user.setUserStudentID(studentIDStr);
                    user.setUserWxNumber(wxNumberStr);
                    register(user);
                }else {
                    Toast.makeText(getApplicationContext(),"选项不能为空！",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void register(final User user){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Gson gson=new Gson();
                String json = gson.toJson(user);
                String resultStr = OkHttpUtils.OkHttpPost("http://47.107.126.98:8090/user/reg", json);
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
