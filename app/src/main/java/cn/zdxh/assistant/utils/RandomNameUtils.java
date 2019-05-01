package cn.zdxh.assistant.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomNameUtils {
    public static String createName(){
        Random random=new Random();
        List<String> titles=new ArrayList<String>();
        titles.add("美国队长");
        titles.add("浩克");
        titles.add("蜘蛛侠");
        titles.add("雷神");
        titles.add("钢铁侠");
        titles.add("超胆侠");
        titles.add("惩罚者");
        titles.add("金刚狼");
        titles.add("死侍");
        titles.add("惊奇女士");
        titles.add("恶灵骑士");
        titles.add("刀锋战士");
        return titles.get(random.nextInt(titles.size()));
    }
}
