package cn.zdxh.assistant.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import cn.zdxh.assistant.R;
import cn.zdxh.assistant.bean.Invitation;
import cn.zdxh.assistant.utils.RandomNameUtils;
import de.hdodenhof.circleimageview.CircleImageView;

public class InvitationsAdapter extends BaseAdapter {

    private List<Invitation> invitations;
    private Context mContext;

    public InvitationsAdapter(List<Invitation> invitations, Context mContext) {
        this.invitations = invitations;
        this.mContext=mContext;
    }

    @Override

    public int getCount() {
        return invitations.size();
    }

    @Override
    public Object getItem(int position) {
        return invitations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=null;
        if (convertView==null){
            //复用item对象
           view= LayoutInflater.from(mContext).inflate(R.layout.fragment_invitation_item,parent,false);
        }else {
            view=convertView;
        }

        //String url="http://guolin.tech/book.png";
        TextView invContent=view.findViewById(R.id.tv_invs);
        TextView invTitle=view.findViewById(R.id.tv_title);
        CircleImageView imageView=view.findViewById(R.id.iv_invs);
        //Glide.with(mContext).load(url).into(imageView);
        invContent.setText(invitations.get(position).getInvContent());
        //随机生成的名字
        invTitle.setText(RandomNameUtils.createName());
        return view;
    }


}
