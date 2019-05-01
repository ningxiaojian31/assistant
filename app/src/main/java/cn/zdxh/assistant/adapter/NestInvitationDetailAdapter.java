package cn.zdxh.assistant.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cn.zdxh.assistant.R;
import cn.zdxh.assistant.bean.Comment;
import cn.zdxh.assistant.bean.Reply;

public class NestInvitationDetailAdapter extends RecyclerView.Adapter<NestInvitationDetailAdapter.ViewHold> {

    private List<Reply> replies;

    public NestInvitationDetailAdapter(List<Reply> replies) {
        this.replies = replies;
    }

    public static class ViewHold extends RecyclerView.ViewHolder{
        private TextView replyCount;
        private TextView replyUser;
        private TextView replyUser2;
        private TextView replyContent;
        private TextView replyContent2;
        public ViewHold(View itemView) {
            super(itemView);
            replyUser=(TextView)itemView.findViewById(R.id.tv_title_nest);
            replyUser2=(TextView)itemView.findViewById(R.id.tv_title_nest2);
            replyContent=(TextView)itemView.findViewById(R.id.tv_inv_info_nest);
            replyContent2=(TextView)itemView.findViewById(R.id.tv_inv_info_nest2);
            replyCount=(TextView)itemView.findViewById(R.id.tv_inv_info_count);
        }
    }
    @Override
    public ViewHold onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.invitation_layout_nest,parent,false);
        ViewHold viewHold = new ViewHold(view);
        return viewHold;
    }

    @Override
    public void onBindViewHolder(ViewHold holder, int position) {
        //Log.e("呵呵呵呵呵呵呵",position+"");
        if (position==0){
            //代表下拉
            if (replies.size()>=2){
                //回复多于两条的时候
                //第一位回复者
                holder.replyContent.setText(replies.get(position).getReContent());
                holder.replyUser.setText(replies.get(position).getUserFrom().getUserLoginname()+":");

                //第二位回复者
                holder.replyContent2.setText(replies.get(position+1).getReContent());
                holder.replyUser2.setText(replies.get(position+1).getUserFrom().getUserLoginname()+":");


                holder.replyCount.setText("查看"+replies.size()+"条评论");
            }else if(replies.size()==1){
                //只有一条回复
                holder.replyContent.setText(replies.get(position).getReContent());
                holder.replyCount.setText("查看"+replies.size()+"条评论");
            }
        }else if (position==1){
            //代表上划
            if (replies.size()>2){
                //第一位回复者
                holder.replyContent.setText(replies.get(position-1).getReContent());
                holder.replyUser.setText(replies.get(position-1).getUserFrom().getUserLoginname()+":");

                //第二位回复者
                holder.replyContent2.setText(replies.get(position).getReContent());
                holder.replyUser2.setText(replies.get(position).getUserFrom().getUserLoginname()+":");


                holder.replyCount.setText("查看"+replies.size()+"条评论");
            }else if(replies.size()==1){
                holder.replyContent.setText(replies.get(position-1).getReContent());
            }
        }

    }


    @Override
    public int getItemCount() {
        if (replies==null)
            return 0;
        return replies.size();
    }
}
