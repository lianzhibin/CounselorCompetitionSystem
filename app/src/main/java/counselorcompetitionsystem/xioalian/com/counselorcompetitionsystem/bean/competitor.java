package counselorcompetitionsystem.xioalian.com.counselorcompetitionsystem.bean;

import java.io.Serializable;

/**
 * Created by xiaolian on 04/11/2017.
 */

public class competitor implements Serializable{
    // 次序
    public int order;
    // 项目
    public String item;
    // 评委编号
    public String judgeId;
    // 选手出场学号
    public int id;
    // 分数
    public double score;
    // 备注
    public String remark;

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
