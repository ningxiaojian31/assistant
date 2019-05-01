package cn.zdxh.assistant.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.zdxh.assistant.R;
import cn.zdxh.assistant.bean.Publish;
import cn.zdxh.assistant.bean.User;
import cn.zdxh.assistant.bean.UserAndPublish;

public class PassOrdersAdapter extends BaseAdapter {

    private List<UserAndPublish> userAndPublishes;
    private Context mContext;

    public PassOrdersAdapter(List<UserAndPublish> userAndPublishes, Context mContext) {
        this.userAndPublishes = userAndPublishes;
        this.mContext=mContext;
    }

    @Override

    public int getCount() {
        return userAndPublishes.size();
    }

    @Override
    public Object getItem(int position) {
        return userAndPublishes.get(position);
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
           view= LayoutInflater.from(mContext).inflate(R.layout.activity_pub_orders_item,parent,false);
        }else {
            view=convertView;
        }
        //找到控件
        TextView infoTip=view.findViewById(R.id.tv_pub_ord_info);
        TextView userTip=view.findViewById(R.id.tv_pub_ord_user);
        TextView orderContent=view.findViewById(R.id.tv_pub_ord_content);
        TextView loginName=view.findViewById(R.id.tv_pub_ord_loginname);
        TextView reallyName=view.findViewById(R.id.tv_pub_ord_reallyname);
        TextView wxNumber=view.findViewById(R.id.tv_pub_ord_wxnumber);
        //给控件填充内容
        Publish publish = userAndPublishes.get(position).getPublish();
        infoTip.setText("发布的信息：");
        orderContent.setText(publish.getPubContent());
        User user = userAndPublishes.get(position).getUser();
        if (user!=null){
            userTip.setText("发布人的信息：");
            loginName.setText("登录名:"+user.getUserLoginname());
            reallyName.setText("真实名:"+user.getUserName());
        }else {
            userTip.setText("发布人的信息：");
            loginName.setText("登录名:"+"暂无");
            reallyName.setText("真实名:"+"暂无");
        }
        wxNumber.setText("微信号:"+"暂无权限查看~~~");

        return view;
    }


}
