package counselorcompetitionsystem.xioalian.com.counselorcompetitionsystem.bean;

import java.io.Serializable;

/**
 * Created by xiaolian on 30/11/2017.
 */

public class CompetitorBeforeScore implements Serializable{
    private int id;
    private String gender;
    private String sno;
    private int group;

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
