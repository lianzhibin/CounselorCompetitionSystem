package counselorcompetitionsystem.xioalian.com.counselorcompetitionsystem.bean;

import java.io.Serializable;

/**
 * Created by xiaolian on 04/11/2017.
 */

public class TotalScore implements Serializable{
    // 学号
    public String sno;
    // 学校
    public String college;
    // 性别
    public String gender;
    // 名字
    public String name;
    // 组别
    public int group;
    // 笔试
    public double written_exam;
    // 主题班会
    public double topic_meeting;
    // 案例分析
    public double item_analysis;
    // 谈心谈话
    public double concact;
    // 主题演讲
    public double topic_speech;
    // 汇总
    public double total_score;

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

    public double getWritten_exam() {
        return written_exam;
    }

    public void setWritten_exam(double written_exam) {
        this.written_exam = written_exam;
    }

    public double getTopic_meeting() {
        return topic_meeting;
    }

    public void setTopic_meeting(double topic_meeting) {
        this.topic_meeting = topic_meeting;
    }

    public double getItem_analysis() {
        return item_analysis;
    }

    public void setItem_analysis(double item_analysis) {
        this.item_analysis = item_analysis;
    }

    public double getConcact() {
        return concact;
    }

    public void setConcact(double concact) {
        this.concact = concact;
    }

    public double getTopic_speech() {
        return topic_speech;
    }

    public void setTopic_speech(double topic_speech) {
        this.topic_speech = topic_speech;
    }

    public double getTotal_score() {
        return total_score;
    }

    public void setTotal_score(double total_score) {
        this.total_score = total_score;
    }
}
