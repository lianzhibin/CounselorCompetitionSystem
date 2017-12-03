package counselorcompetitionsystem.xioalian.com.counselorcompetitionsystem.bean;

import java.io.Serializable;

/**
 * Created by xiaolian on 04/11/2017.
 */

public class ItemScore implements Serializable{
    // 学号
    public String sno;
    // 学校
    public String college;
    // 名字
    public String name;
    // 性别
    public String gender;
    // 组别
    public int group;
    // 评委1 打分
    public double a1;
    // 评委2 打分
    public double a2;
    // 评委3 打分
    public double a3;
    // 平均分
    public double avg_score;
    // 标准分
    public double standard_acore;
    // 备注
    public String description;
    // 出场号
    public String appear;

    public String getAppear() {
        return appear;
    }

    public void setAppear(String appear) {
        this.appear = appear;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }


    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }



    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public double getA1() {
        return a1;
    }

    public void setA1(double a1) {
        this.a1 = a1;
    }

    public double getA2() {
        return a2;
    }

    public void setA2(double a2) {
        this.a2 = a2;
    }

    public double getA3() {
        return a3;
    }

    public void setA3(double a3) {
        this.a3 = a3;
    }

    public double getAvg_score() {
        return avg_score;
    }

    public void setAvg_score(double avg_score) {
        this.avg_score = avg_score;
    }

    public double getStandard_acore() {
        return standard_acore;
    }

    public void setStandard_acore(double standard_acore) {
        this.standard_acore = standard_acore;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
