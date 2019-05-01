package cn.zdxh.assistant.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.zdxh.assistant.R;
import cn.zdxh.assistant.adapter.CommentDetailAdapter;
import cn.zdxh.assistant.adapter.InvitationDetailAdapter;
import cn.zdxh.assistant.base.BaseActivity;
import cn.zdxh.assistant.bean.Comment;
import cn.zdxh.assistant.bean.Invitation;
import cn.zdxh.assistant.bean.Reply;
import cn.zdxh.assistant.bean.User;
import cn.zdxh.assistant.utils.OkHttpUtils;
import cn.zdxh.assistant.utils.UserUtils;
import de.hdodenhof.circleimageview.CircleImageView;

public class CommentDetailActivity extends BaseActivity {

    private int COMMENT=1;
    private int REPLY=2;
    private Comment comment;
    private Comment commentIntent;
    private ImageView comBack;
    private CircleImageView userImage;
    private TextView userContent;
    private ListView listView;
    private TextView comContent;
    private EditText comEdit;
    private Button comSend;
    private int userIdTo;

    @SuppressLint("HandlerLeak")
    Handler handler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==COMMENT){
                //部分值传到listView上
                CommentDetailAdapter commentDetailAdapter=new CommentDetailAdapter(comment.getReplys(),getApplicationContext());
                listView.setAdapter(commentDetailAdapter);
            } else if (msg.what==REPLY){
                Toast.makeText(getApplicationContext(),(String)msg.obj,Toast.LENGTH_SHORT).show();

            }
        }
    };


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_detail);
        initView();
        initData();
        initListener();
    }

    public void initData(){
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        //获取上一个activity过来的对象
        commentIntent=(Comment) bundle.getSerializable("comment");
        //部分值直接更新UI
        comContent.setText(commentIntent.getComContent());//加载评论详情
        if (commentIntent.getUser()!=null && !"".equals(commentIntent.getUser().getUserImage())){
            Glide.with(getApplicationContext()).load(commentIntent.getUser().getUserImage()).into(userImage);//加载评论的人头像
        }
        userContent.setText(commentIntent.getUser().getUserLoginname());//加载评论的人登录名
        //初始化userIdTo
        userIdTo=commentIntent.getUserId();
        //请求数据
        queryCommentDetail(commentIntent.getId());
    }

    public void initView() {
        comBack=findViewById(R.id.iv_com_back);
        userImage=findViewById(R.id.iv_com);
        userContent=findViewById(R.id.tv_title_com);
        comContent = findViewById(R.id.tv_com);
        listView = findViewById(R.id.ll_com);
        comEdit=findViewById(R.id.et_com);
        comSend=findViewById(R.id.bt_com);
    }

    public void initListener(){
        //返回键
        comBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //监听是否点击了评论回复
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //更新userIdTo
                userIdTo=comment.getReplys().get(position).getUserFrom().getId();//评论的回复,列表的中的评论人id
                comEdit.setHint("回复： "+comment.getReplys().get(position).getUserFrom().getUserLoginname());
            }
        });
        //发表评论
        comSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userIdTo==commentIntent.getUserId()){
                    joinReply(1,userIdTo);//评论的评论
                }else {
                    joinReply(2,userIdTo);//评论的回复
                }
                //清空EditText
                comEdit.setText("");
                //更新数据
                queryCommentDetail(commentIntent.getId());
            }
        });
    }

    //组拼评论回复
    public void joinReply(int replyType,int userIdTo){
        User user = UserUtils.getUser(getApplicationContext());
        if (user!=null && user.getId()!=null){
            //获取到Edit框内输入的值
            String sendContent = comEdit.getText().toString().trim();
            //组拼要发送的json对象
            Gson gson = new Gson();
            Map<Object,Object> map = new HashMap<>();
            map.put("reType",replyType);//1为评论中评论，2为评论中回复
            map.put("reContent",sendContent);
            map.put("reLaud",0);//默认0个赞
            map.put("comId",commentIntent.getId());
            map.put("userIdFrom",user.getId());//要动态获取登录用户，评论的发表人
            map.put("userIdTo",userIdTo);//评论的对象
            final String replyStr = gson.toJson(map);
            if (!"".equals(sendContent)){
                //得到请求结果，需要开启子线程
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String reply = OkHttpUtils.OkHttpPost("http://47.107.126.98:8090/rep", replyStr);
                        Message message=Message.obtain();
                        message.obj=reply;
                        message.what=REPLY;
                        handler.sendMessage(message);
                    }
                }).start();
            }else {
                Toast.makeText(getApplicationContext(),"回复评论不能为空！",Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(getApplicationContext(),"亲！登录后才能进行评论哦~~~",Toast.LENGTH_SHORT).show();
        }


    }

    public void queryCommentDetail(final Integer cId){
        new Thread(new Runnable() {
            @Override public void run() {
                //请求数据
                String json= OkHttpUtils.OkHttpGet("http://47.107.126.98:8090/com/"+cId);
                //Gson解析json数据
                Gson gson=new Gson();
                comment = gson.fromJson(json, Comment.class);
                //发送消息，不能在子线程更新UI
                Message message=Message.obtain();
                message.obj=comment;
                message.what=COMMENT;
                handler.sendMessage(message);
            }
        }).start();//不要忘记start线程
    }

}
