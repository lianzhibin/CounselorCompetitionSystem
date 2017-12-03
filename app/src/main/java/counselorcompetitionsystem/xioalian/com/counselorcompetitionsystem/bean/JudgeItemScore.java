package counselorcompetitionsystem.xioalian.com.counselorcompetitionsystem.bean;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by xiaolian on 04/11/2017.
 */

public class JudgeItemScore implements Serializable{

    // 出场顺序
    public int appear;
    // 分数
    public double score;
    // 备注
    public String remark;
    // 性别
    public String gender;
    // 组别
    public int group;


    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }


    public int getAppear() {
        return appear;
    }

    public void setAppear(int appear) {
        this.appear = appear;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
