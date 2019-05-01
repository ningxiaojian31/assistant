package cn.zdxh.assistant.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.io.File;

import cn.zdxh.assistant.R;
import cn.zdxh.assistant.base.BaseActivity;
import cn.zdxh.assistant.bean.Invitation;
import cn.zdxh.assistant.bean.Publish;
import cn.zdxh.assistant.bean.User;
import cn.zdxh.assistant.utils.OkHttpUtils;
import cn.zdxh.assistant.utils.UserUtils;
import id.zelory.compressor.Compressor;

public class AddPublishActivity extends BaseActivity {

    private int UPLOAD=1;
    private int SEND=2;
    private ImageView imageBack;
    private ImageView uploadPic;
    private EditText editContent;
    private Button btnSend;
    private Uri uri;//选择后的图片路径
    private String picPath="";

    @SuppressLint("HandlerLeak")
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==UPLOAD){
                //请求数据成功后更新UI
                 Glide.with(getApplicationContext()).load((String)msg.obj).into(uploadPic);
            }
            if (msg.what==SEND){
                Toast.makeText(getApplicationContext(),(String)msg.obj,Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_publish);
        initView();
        initListener();
    }

    public void initView(){
        imageBack=findViewById(R.id.iv_pub_add_back);
        uploadPic=findViewById(R.id.iv_pub_pic_add);
        editContent=findViewById(R.id.et_pub_add);
        btnSend=findViewById(R.id.btn_pub_add);

    }

    public void initListener(){
        //返回键
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //上传图片
        uploadPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");//相片类型
                startActivityForResult(intent, 100);
            }
        });
        //点击发送
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = UserUtils.getUser(getApplicationContext());
                if (user!=null && user.getId()!=null){
                    String editStr = editContent.getText().toString().trim();
                    Gson gson=new Gson();
                    //封装要传输的json对象
                    Publish publish=new Publish();
                    publish.setUserId(user.getId());//动态获取登录的用户
                    if (!"".equals(editStr)) {
                        publish.setPubContent(editStr);
                    }
                    if (!"".equals(picPath)){
                        publish.setPubImage(picPath);
                    }
                    publish.setPubSign("1");//标记是拼伞还是拼饭
                    final String json = gson.toJson(publish);//对象转json
                    if (!"".equals(editStr)){
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String resultStr = OkHttpUtils.OkHttpPost("http://47.107.126.98:8090/pub", json);
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
                    Toast.makeText(getApplicationContext(),"亲！登录后才能发布拼伞/饭哦~~~",Toast.LENGTH_SHORT).show();
                }


            }
        });
    }


    //打开图库后返回图片的路径,并上传图片
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 100://上传照片
                if (resultCode == RESULT_OK) {
                    try {
                        //该uri是打开图库的Activity返回的
                        uri = data.getData();
                        //以下这几步是由uri转化为图片的真实路径的
                        String[] proj = {MediaStore.Images.Media.DATA};
                        Cursor actualimagecursor = managedQuery(uri, proj, null, null, null);
                        int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        actualimagecursor.moveToFirst();
                        final String img_path = actualimagecursor.getString(actual_image_column_index);
                        File file = new File(img_path);
                        final File compressFile = new Compressor(this).compressToFile(file);//压缩文件再上传

                        //进行文件上传操作
                        if (compressFile != null) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    String resultStr = OkHttpUtils.uploadImage("http://47.107.126.98:8090/upload",compressFile.getPath());
                                    Message message = Message.obtain();
                                    message.what=UPLOAD;
                                    message.obj=resultStr;
                                    picPath=resultStr;
                                    handler.sendMessage(message);
                                }
                            }).start();
                        } else {
                            Toast.makeText(getApplicationContext(), "文件为空!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                    }
                }
                break;
        }
    }

}
