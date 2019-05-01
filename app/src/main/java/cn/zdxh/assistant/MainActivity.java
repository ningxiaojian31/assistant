package cn.zdxh.assistant;


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

import cn.zdxh.assistant.activity.AddInvitationActivity;
import cn.zdxh.assistant.activity.AddPublishActivity;
import cn.zdxh.assistant.adapter.MainFragmentAdapter;
import cn.zdxh.assistant.base.BaseActivity;
import cn.zdxh.assistant.fragment.InvitationFragment;
import cn.zdxh.assistant.fragment.MyFragment;
import cn.zdxh.assistant.fragment.PublishFragment;

public class MainActivity extends BaseActivity {

    private ViewPager viewpager;
    private TabLayout tableLayout;
    private List<Fragment> mFragments;
    private List<String> titleDatas;
    private ImageView addView;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化控件
        initView();
        //初始化监听器
        initListener();
        //初始化Tab
        initTab();
        //初始化Fragment
        initFragment();
        //运行时权限
        List<String> list = new ArrayList<String>();//集中管理权限，一次性申请权限
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            list.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!list.isEmpty()) {//有权限要申请时
            String[] permissions = list.toArray(new String[list.size()]);//集合转数组
            ActivityCompat.requestPermissions(this, permissions, 1);//一次性申请权限
        }
    }
    public void initView(){
        viewpager=findViewById(R.id.vp_main);
        tableLayout=findViewById(R.id.tl_main);
        addView=findViewById(R.id.iv_add);
    }
    //右上角的加号控件
    public void initListener(){
        addView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 列表对话框
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("选择要发布的信息")
                            .setItems(new String[]{"发布树洞", "发布拼饭/伞"}, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    if (which==0){
                                        Intent intent=new Intent(getApplicationContext(), AddInvitationActivity.class);
                                        startActivity(intent);
                                    }else if (which==1){
                                        Intent intent=new Intent(getApplicationContext(),AddPublishActivity.class);
                                        startActivity(intent);
                                    }
                                }
                            })
                            .show();

            }
        });
    }
    //fragment中的tab要显示的内容
    public void initTab(){
        titleDatas=new ArrayList<String>();
        titleDatas.add("树洞");
        titleDatas.add("拼友");
        titleDatas.add("我的");
    }
    //初始化Fragment
    public void initFragment(){
        mFragments = new ArrayList<Fragment>();
        mFragments.add(new InvitationFragment());
        mFragments.add(new PublishFragment());
        mFragments.add(new MyFragment());
        //初始化adapter
        MainFragmentAdapter adapter = new MainFragmentAdapter(getSupportFragmentManager(), mFragments,titleDatas);
        tableLayout.setupWithViewPager(viewpager);
        // 将适配器和ViewPager结合
        viewpager.setAdapter(adapter);

    }

    //处理运行时权限的回调函数
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    int result = 1;
                    if (result == grantResults.length) {//这里的判断有点问题
                        Toast.makeText(getApplicationContext(), "已允许所需权限", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "需要允许所有权限才能体验软件~", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "发生未知错误", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }




}
