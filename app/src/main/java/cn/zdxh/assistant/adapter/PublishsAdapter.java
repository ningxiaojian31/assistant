package cn.zdxh.assistant.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.List;

import cn.zdxh.assistant.R;
import cn.zdxh.assistant.bean.Invitation;
import cn.zdxh.assistant.bean.Order;
import cn.zdxh.assistant.bean.Publish;
import cn.zdxh.assistant.bean.User;
import cn.zdxh.assistant.utils.OkHttpUtils;
import cn.zdxh.assistant.utils.RandomNameUtils;
import cn.zdxh.assistant.utils.UserUtils;
import de.hdodenhof.circleimageview.CircleImageView;

public class PublishsAdapter extends BaseAdapter {

    private List<Publish> publishes;
    private Context mContext;

    public PublishsAdapter(List<Publish> publishes, Context mContext) {
        this.publishes = publishes;
        this.mContext=mContext;
    }

    @Override

    public int getCount() {
        return publishes.size();
    }

    @Override
    public Object getItem(int position) {
        return publishes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view=null;
        if (convertView==null){
            //复用item对象
           view= LayoutInflater.from(mContext).inflate(R.layout.fragment_publish_item,parent,false);
        }else {
            view=convertView;
        }

        ImageView imageContent;
        TextView textContent;
        Button btnAddOrder;

        imageContent=view.findViewById(R.id.iv_pubs_pic);
        textContent=view.findViewById(R.id.tv_pubs_content);
        btnAddOrder=view.findViewById(R.id.btn_add_ord);

        if (publishes.get(position).getPubImage()!=null){
            Glide.with(mContext).load(publishes.get(position).getPubImage()).into(imageContent);
        }
        textContent.setText(publishes.get(position).getPubContent());

        btnAddOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = UserUtils.getUser(mContext);
                if (user!=null && user.getId() != null){
                    Order order=new Order();
                    order.setUser(user);
                    order.setPublish(publishes.get(position));
                    Gson gson=new Gson();
                    final String json = gson.toJson(order);
                    new Thread(new Runnable(){
                        @Override
                        public void run() {
                            OkHttpUtils.OkHttpPost("http://47.107.126.98:8090/ord",json);
                        }
                    }).start();
                    Toast.makeText(mContext,"预定成功！",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(mContext,"请登录后再进行预约！",Toast.LENGTH_SHORT).show();
                }

            }
        });

        return view;
    }


}
