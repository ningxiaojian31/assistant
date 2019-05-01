package cn.zdxh.assistant.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.zdxh.assistant.R;
import cn.zdxh.assistant.adapter.InvitationDetailAdapter;
import cn.zdxh.assistant.base.BaseActivity;
import cn.zdxh.assistant.bean.Comment;
import cn.zdxh.assistant.bean.Invitation;
import cn.zdxh.assistant.bean.User;
import cn.zdxh.assistant.utils.OkHttpUtils;
import cn.zdxh.assistant.utils.RandomNameUtils;
import cn.zdxh.assistant.utils.UserUtils;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static cn.zdxh.assistant.utils.OkHttpUtils.JSON;

public class InvitationDetailActivity extends BaseActivity {

    private static int COMMENTS=1;
    private static int COMMENT=2;
    private List<Comment> comments;
    private Invitation invitation;
    private TextView invContent;
    private RecyclerView recyclerView;
    private TextView invTitle;
    private CircleImageView invImage;
    private ImageView invBack;
    private EditText invEdit;
    private Button invSend;

    @SuppressLint("HandlerLeak")
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //如果属于数据请求的消息，更新UI
            if (msg.what==COMMENTS){
                LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(layoutManager);
                //设置监听器
                InvitationDetailAdapter adapter = new InvitationDetailAdapter((List<Comment>)msg.obj);
                adapter.setItemClickListener(new InvitationDetailAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        // Toast.makeText(getApplicationContext(),"您点击了"+position+"行", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(getApplicationContext(),CommentDetailActivity.class);
                        Bundle bundle=new Bundle();
                        //包装对象传给第二个Activity
                        Comment commentBundle = comments.get(position);
                        bundle.putSerializable("comment",commentBundle);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });

                recyclerView.setAdapter(adapter);
            }else if (msg.what==COMMENT){
                Toast.makeText(getApplicationContext(),(String)msg.obj,Toast.LENGTH_SHORT).show();
                //清空EditText
                invEdit.setText("");
                //更新数据
                queryInvitationDetail(invitation.getId());
            }
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation_detail);
        initView();
        initData();
        initListener();
    }

    public void initView(){
        invContent=findViewById(R.id.tv_inv);
        recyclerView=findViewById(R.id.rv_inv);
        invTitle=findViewById(R.id.tv_title_inv);
        invImage=findViewById(R.id.iv_inv);
        invBack=findViewById(R.id.iv_inv_back);
        invEdit=findViewById(R.id.et_inv);
        invSend=findViewById(R.id.bt_inv);
;    }

    public void initListener(){
        //返回的点击事件
        invBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //发送的点击事件
        invSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = UserUtils.getUser(getApplicationContext());
                if (user!=null && user.getId()!=null){
                    //获取到Edit框内输入的值
                    String sendContent = invEdit.getText().toString().trim();
                    //组拼要发送的json对象
                    Gson gson = new Gson();
                    Map<Object,Object> map = new HashMap<>();
                    map.put("invId",invitation.getId());
                    // map.put("comDate",new Date());
                    map.put("comContent",sendContent);
                    map.put("userId",user.getId());//要动态获取登录用户
                    final String commentStr = gson.toJson(map);
                    if (!"".equals(sendContent)){
                        //得到请求结果，需要开启子线程
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String comment = OkHttpUtils.OkHttpPost("http://47.107.126.98:8090/com", commentStr);
                                Message message=Message.obtain();
                                message.obj=comment;
                                message.what=COMMENT;
                                handler.sendMessage(message);
                            }
                        }).start();
                    }else {
                        Toast.makeText(getApplicationContext(),"发表评论不能为空！",Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(getApplicationContext(),"亲！登陆后才能进行评论哦~~~",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void initData(){
        //假数据，匿名
        //String url="http://guolin.tech/book.png";
        invTitle.setText(RandomNameUtils.createName());
        //Glide.with(getApplicationContext()).load(url).into(invImage);
        //获取上一个页面fragment传来的对象
        Intent intent=getIntent();
        Bundle bundle = intent.getExtras();
        invitation=(Invitation) bundle.getSerializable("invitation");
        //有数据后更新UI
        invContent.setText(invitation.getInvContent());

        int invId = invitation.getId();
        comments=new ArrayList<Comment>();
        //请求树洞详情
        queryInvitationDetail(invId);
    }

    public void queryInvitationDetail(final int iId){
        new Thread(new Runnable() {
            @Override public void run() {
                //请求数据
                String json= OkHttpUtils.OkHttpGet("http://47.107.126.98:8090/inv/"+iId);
                //Gson解析json数据
                Gson gson=new Gson();
                comments = gson.fromJson(json, new TypeToken<List<Comment>>(){}.getType());
                //发送消息，不能在子线程更新UI
                Message message=Message.obtain();
                message.obj=comments;
                message.what=COMMENTS;
                handler.sendMessage(message);
            }
        }).start();//不要忘记start线程

    }
}
