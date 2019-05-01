package cn.zdxh.assistant.adapter;

import android.content.Context;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import cn.zdxh.assistant.R;
import cn.zdxh.assistant.bean.Comment;
import cn.zdxh.assistant.bean.Invitation;
import cn.zdxh.assistant.bean.Reply;
import cn.zdxh.assistant.utils.OkHttpUtils;
import de.hdodenhof.circleimageview.CircleImageView;

public class CommentDetailAdapter extends BaseAdapter {

    private List<Reply> replies;
    private Context mContext;

    public CommentDetailAdapter(List<Reply> replies, Context mContext) {
        this.replies = replies;
        this.mContext=mContext;
    }

    @Override

    public int getCount() {
        return replies.size();
    }

    @Override
    public Object getItem(int position) {
        return replies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=null;
        if (convertView==null){
            //复用view,不会一直创建item,保持高性能
           view= LayoutInflater.from(mContext).inflate(R.layout.activity_comment_detail_item,parent,false);
        }else {
            view=convertView;
        }
        CircleImageView userImage=view.findViewById(R.id.iv_com_detail);
        TextView userContent=view.findViewById(R.id.tv_com_title_detail);
        TextView textView=view.findViewById(R.id.tv_com_item);
        Glide.with(mContext).load(replies.get(position).getUserFrom().getUserImage()).into(userImage);//加载二级评论的人头像
        //评论中的评论
        if (replies.get(position).getReType()==1){
            userContent.setText(replies.get(position).getUserFrom().getUserLoginname());//加载二级评论的人登录名
            textView.setText(replies.get(position).getReContent());//加载评论详情
        }else if(replies.get(position).getReType()==2){
            //评论中的回复
            userContent.setText(replies.get(position).getUserFrom().getUserLoginname());//加载二级评论的人登录名
            textView.setText("回复   "+replies.get(position).getUserTo().getUserLoginname()+"   "+replies.get(position).getReContent());//加载评论详情
        }

        return view;
    }


}
