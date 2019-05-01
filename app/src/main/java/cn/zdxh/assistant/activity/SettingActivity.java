package cn.zdxh.assistant.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import cn.zdxh.assistant.R;
import cn.zdxh.assistant.base.BaseActivity;

public class SettingActivity extends BaseActivity {

    private ImageView back;
    private Button outLogin;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
        initListener();
    }

    public void initView(){
        back=findViewById(R.id.iv_setting_back);
        outLogin=findViewById(R.id.btn_out_login);
    }

    public void initListener(){
        //返回
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //退出登录
        outLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //清空登录信息
                SharedPreferences preferences = getApplication().getSharedPreferences("admin", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.commit();
                //返回到登录页面
                Intent intent=new Intent(getApplicationContext(), LoginActivity. class);
                startActivity(intent);

            }
        });
    }


}
