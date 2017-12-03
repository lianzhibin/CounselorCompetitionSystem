package counselorcompetitionsystem.xioalian.com.counselorcompetitionsystem.bean;

import java.io.Serializable;

/**
 * Created by xiaolian on 21/11/2017.
 */

public class SubmitScore implements Serializable{
    /**
     * 评委编号
     */
    public String userName;
    /**
     * 项目名称
     */
    public String activeName;
    /**
     * 出场顺序
     */
    public int competitorID;
    /**
     * 分数
     */
    public double score;
    /**
     * 备注
     */
    public String description;

    /**
     *
     * @return
     */

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getActiveName() {
        return activeName;
    }

    public void setActiveName(String activeName) {
        this.activeName = activeName;
    }

    public int getCompetitorID() {
        return competitorID;
    }

    public void setCompetitorID(int competitorID) {
        this.competitorID = competitorID;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
