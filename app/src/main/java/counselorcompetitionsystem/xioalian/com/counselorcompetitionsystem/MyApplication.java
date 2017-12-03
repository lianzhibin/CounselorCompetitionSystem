package counselorcompetitionsystem.xioalian.com.counselorcompetitionsystem;


import android.app.Application;

/**
 * Created by xiaolian on 04/11/2017.
 */

public class MyApplication extends Application {

    // 比赛名称
    public static String competitionName = "第五届高校辅导员职业能力培训大赛暨竞赛";
    public static String competitionNameID = "1";

    // 评委还是仲裁
    public final static String comission = "评委";
    public final static String arbitrate = "仲裁";

    // 当前用户角色
    public static String currentUser ;

    // 当前用户编号
     public static String currentId  = "A4";

    // 组别
    public static int teamNumber  = 1;

    // 项目选择
    public final static String concact = "谈话谈心";
    public final static String titleSpeech = "主题演讲";
    public final static String itemAnalysis = "案例分析";
    public final static String totalScore = "总成绩";


    @Override
    public void onCreate() {
        super.onCreate();

    }


}
