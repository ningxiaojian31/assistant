package cn.zdxh.assistant.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import cn.zdxh.assistant.R;
import cn.zdxh.assistant.bean.Comment;
import cn.zdxh.assistant.bean.Invitation;
import cn.zdxh.assistant.bean.Reply;
import de.hdodenhof.circleimageview.CircleImageView;

public class InvitationDetailAdapter extends RecyclerView.Adapter<InvitationDetailAdapter.ViewHold>{

    private List<Reply> replies;
    private List<Comment> comments;
    private int position;
    private Context context;

    //点击事件的接口
    private OnItemClickListener mItemClickListener;

    /**
     * 由于recycleView没有监听器，需要自定义监听器接口
     */
    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    //构造函数
    public InvitationDetailAdapter(List<Comment> comments) {
        this.comments = comments;
    }



    public class ViewHold extends RecyclerView.ViewHolder{
        private RecyclerView recyclerView;
        private TextView comContent;
        private CircleImageView comImage;
        private TextView comTitle;
        public ViewHold(View itemView) {
            super(itemView);
            //找到控件
            comContent=(TextView)itemView.findViewById(R.id.tv_inv_info);
            comImage=(CircleImageView)itemView.findViewById(R.id.iv_inv_item);
            comTitle=(TextView)itemView.findViewById(R.id.tv_inv_title) ;

            //嵌套RecycleView使用
            recyclerView=itemView.findViewById(R.id.rv_inv_nest);
            LinearLayoutManager layoutManager = new LinearLayoutManager(itemView.getContext());
            layoutManager.setAutoMeasureEnabled(true);
            recyclerView.setLayoutManager(layoutManager);
            //方法加载顺序的问题
            if (replies==null){
                //第一次加载的时候
                if (comments.get(position)!=null){
                    replies=comments.get(position).getReplys();
                }

            } else {
                //第二次往后的加载
                replies=comments.get(position+1).getReplys();
            }

            //设置第二个recycleView的是配置
            NestInvitationDetailAdapter adapter = new NestInvitationDetailAdapter(replies);
            recyclerView.setAdapter(adapter);


        }
    }
    @Override
    public ViewHold onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.invitation_layout,parent,false);
        context=parent.getContext();
        ViewHold viewHold = new ViewHold(view);
        if( mItemClickListener!= null){
            view.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //获取传值过来的position
                    mItemClickListener.onItemClick((int)v.getTag());

                }
            });
        }
        return viewHold;
    }

    @Override
    public void onBindViewHolder(ViewHold holder, int position) {
        Comment comment = comments.get(position);
        this.position=position;
        // Log.e("哈哈哈哈哈哈哈",position+"");//从0开始逐渐递增
        if (comment!=null){
            holder.comContent.setText(comment.getComContent());
            holder.comTitle.setText(comment.getUser().getUserLoginname());
            Glide.with(context).load(comment.getUser().getUserImage()).into(holder.comImage);
        }

         //把item的position传给onItemClick
         holder.itemView.setTag(position);
    }


    @Override
    public int getItemCount() {
        return comments.size();
    }

}
