package cn.zdxh.assistant.fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.io.File;
import java.util.TooManyListenersException;

import cn.zdxh.assistant.R;
import cn.zdxh.assistant.activity.LoginActivity;
import cn.zdxh.assistant.activity.PassOrdersActivity;
import cn.zdxh.assistant.activity.PubOrdersActivity;
import cn.zdxh.assistant.activity.RegisterActivity;
import cn.zdxh.assistant.activity.SettingActivity;
import cn.zdxh.assistant.bean.User;
import cn.zdxh.assistant.utils.OkHttpUtils;
import cn.zdxh.assistant.utils.UserUtils;
import id.zelory.compressor.Compressor;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;


public class MyFragment extends Fragment {

    private int UPLOAD=1;
    private Uri uri;//选择后的图片路径
    private String picPath="";
    private ImageView picture;
    private TextView username;
    private TextView register;
    private TextView pubOrders;
    private TextView passOrders;
    private TextView setting;

    @SuppressLint("HandlerLeak")
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==UPLOAD){
                //请求数据成功后更新UI
                Glide.with(getActivity()).load((String)msg.obj).into(picture);
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_my, container, false);
        initView(view);
        initData();
        initListener();
        return view;
    }

    public void initView(View view){
        picture=view.findViewById(R.id.civ_my_pic);
        username=view.findViewById(R.id.tv_my_loginname);
        register=view.findViewById(R.id.tv_my_register);
        pubOrders=view.findViewById(R.id.tv_my_pub_ord);
        passOrders=view.findViewById(R.id.tv_my_ords);
        setting=view.findViewById(R.id.tv_setting);
    }

    public void initListener(){
        //上传头像
        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = UserUtils.getUser(getContext());
                if (user!=null && user.getId()!=null){
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");//相片类型
                    startActivityForResult(intent, 200);
                }else {
                    Toast.makeText(getContext(),"请登录后再上传头像~~~",Toast.LENGTH_SHORT).show();
                }

            }
        });
        //登录
        username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), LoginActivity. class);
                startActivity(intent);
            }
        });
        //注册
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), RegisterActivity.class);
                startActivity(intent);
            }
        });
        //打开发布的订单
        pubOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = UserUtils.getUser(getContext());
                if (user!=null && user.getId()!=null){
                    Intent intent=new Intent(getActivity(), PubOrdersActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(getContext(),"请登录后再查看发布的预约~~~",Toast.LENGTH_SHORT).show();
                }

            }
        });
        //打开通过的订单
        passOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = UserUtils.getUser(getContext());
                if (user!=null && user.getId()!=null){
                    Intent intent=new Intent(getActivity(), PassOrdersActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(getContext(),"请登录后再查看申请的预约~~~",Toast.LENGTH_SHORT).show();
                }

            }
        });
        //打开设置
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
            }
        });
    }

    public void initData(){
        User user = UserUtils.getUser(getContext());
        //用户名和密码不为空的时候，刷新数据
        if (user!=null){
            if (user.getUserImage()!=null) {
                Glide.with(getActivity()).load(user.getUserImage()).into(picture);
            }
            if (!"".equals(user.getUserLoginname())){
                username.setText(user.getUserLoginname());
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    //打开图库后返回图片的路径,并上传图片
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 200://上传照片
                if (resultCode == RESULT_OK) {
                    try {
                        //该uri是打开图库的Activity返回的
                        uri = data.getData();
                        //以下这几步是由uri转化为图片的真实路径的
                        String[] proj = {MediaStore.Images.Media.DATA};
                        Cursor actualimagecursor = getActivity().managedQuery(uri, proj, null, null, null);
                        int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        actualimagecursor.moveToFirst();
                        final String img_path = actualimagecursor.getString(actual_image_column_index);
                        File file = new File(img_path);
                        final File compressFile = new Compressor(getActivity()).compressToFile(file);//压缩文件再上传

                        //进行文件上传操作
                        if (compressFile != null) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    //上传头像
                                    String resultStr = OkHttpUtils.uploadImage("http://47.107.126.98:8090/upload",compressFile.getPath());
                                    //更新个人信息，云端
                                    Gson gson=new Gson();
                                    User user = UserUtils.getUser(getContext());
                                    user.setUserImage(resultStr);
                                    String userJson = gson.toJson(user);
                                    OkHttpUtils.OkHttpPost("http://47.107.126.98:8090/user/change",userJson);
                                    //更新个人信息，本地
                                    SharedPreferences sp=getActivity().getSharedPreferences("admin",MODE_PRIVATE);
                                    SharedPreferences.Editor editor=sp.edit();
                                    editor.putString("image",resultStr);
                                    editor.commit();
                                    //发送消息
                                    Message message = Message.obtain();
                                    message.what=UPLOAD;
                                    message.obj=resultStr;
                                    picPath=resultStr;
                                    handler.sendMessage(message);
                                }
                            }).start();
                        } else {
                            Toast.makeText(getActivity(), "文件为空!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                    }
                }
                break;
        }
    }
}
