package cn.zdxh.assistant.utils;

import android.content.Context;
import android.content.SharedPreferences;

import cn.zdxh.assistant.bean.User;

public class UserUtils {
    public static User getUser(Context context){
        SharedPreferences sp=context.getSharedPreferences("admin", Context.MODE_PRIVATE);
        String id = sp.getString("id", "");
        String username = sp.getString("username", "");
        String password = sp.getString("password", "");
        String name = sp.getString("name", "");
        String image = sp.getString("image", "");
        String studentID = sp.getString("studentID", "");
        String wxNumber = sp.getString("wxNumber", "");
        User user=new User();
        if (!"".equals(id)){
           user.setId(Integer.parseInt(id));
        }
        user.setUserLoginname(username);
        user.setUserPassword(password);
        user.setUserName(name);
        if (!"".equals(image)){
            user.setUserImage(image);
        }
        user.setUserStudentID(studentID);
        user.setUserWxNumber(wxNumber);
        return user;
    }
}
