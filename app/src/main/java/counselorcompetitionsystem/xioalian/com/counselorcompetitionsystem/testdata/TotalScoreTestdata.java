package counselorcompetitionsystem.xioalian.com.counselorcompetitionsystem.testdata;

import java.util.ArrayList;

import counselorcompetitionsystem.xioalian.com.counselorcompetitionsystem.bean.TotalScore;

/**
 * Created by xiaolian on 20/11/2017.
 */

public class TotalScoreTestdata {

    public static ArrayList<TotalScore> getData(ArrayList<TotalScore> dataList){

        /**
        TotalScore totalScore = new TotalScore();
        totalScore.setTeam(1);
        totalScore.setUsername("myname1");
        totalScore.setCollege("华南师范大学");
        totalScore.setConcact(32);
        totalScore.setGender("男");
        totalScore.setItemAnalysis(45);
        totalScore.setOrder(1);
        totalScore.setSno("4792749");
        totalScore.setTopicMeeting(87);
        totalScore.setTopicSpeech(78);
        totalScore.setTotal(87);
        totalScore.setWrittenExam(78);
        totalScore.setAppear(12);
        dataList.add(totalScore);
        dataList.add(totalScore);
        dataList.add(totalScore);

        TotalScore totalScore1 = new TotalScore();
        totalScore1.setTeam(2);
        totalScore1.setAppear(23);
        totalScore1.setUsername("myname2");
        totalScore1.setCollege("华中科技大学");
        totalScore1.setConcact(12);
        totalScore1.setGender("男");
        totalScore1.setItemAnalysis(35);
        totalScore1.setOrder(5);
        totalScore1.setSno("3213113");
        totalScore1.setTopicMeeting(97);
        totalScore1.setTopicSpeech(76);
        totalScore1.setTotal(97);
        totalScore1.setWrittenExam(88);
        dataList.add(totalScore1);
        dataList.add(totalScore1);
        dataList.add(totalScore1);

        TotalScore totalScore2 = new TotalScore();
        totalScore2.setTeam(3);
        totalScore2.setUsername("myname3");
        totalScore2.setCollege("岭南学院");
        totalScore2.setAppear(29);
        totalScore2.setConcact(62);
        totalScore2.setGender("男");
        totalScore2.setItemAnalysis(46);
        totalScore2.setOrder(2);
        totalScore2.setSno("39813791");
        totalScore2.setTopicMeeting(99);
        totalScore2.setTopicSpeech(79);
        totalScore2.setTotal(88);
        totalScore2.setWrittenExam(79);
        dataList.add(totalScore2);
        dataList.add(totalScore2);
        dataList.add(totalScore2);

        TotalScore totalScore3 = new TotalScore();
        totalScore3.setTeam(4);
        totalScore3.setAppear(8);
        totalScore3.setUsername("myname4");
        totalScore3.setCollege("江南大学");
        totalScore3.setConcact(87);
        totalScore3.setGender("女");
        totalScore3.setItemAnalysis(45);
        totalScore3.setOrder(1);
        totalScore3.setSno("4792749");
        totalScore3.setTopicMeeting(87);
        totalScore3.setTopicSpeech(78);
        totalScore3.setTotal(87);
        totalScore3.setWrittenExam(78);
        dataList.add(totalScore3);
        dataList.add(totalScore3);
        dataList.add(totalScore3);

        dataList.add(totalScore1);
        dataList.add(totalScore2);
        dataList.add(totalScore3);
        dataList.add(totalScore1);
        dataList.add(totalScore);
        dataList.add(totalScore);
        dataList.add(totalScore);
        dataList.add(totalScore);
        dataList.add(totalScore);
        dataList.add(totalScore);
        dataList.add(totalScore);

        TotalScore totalScore4 = new TotalScore();
        totalScore4.setTeam(5);
        totalScore4.setAppear(91);
        totalScore4.setUsername("myname5");
        totalScore4.setCollege("华南师范大学");
        totalScore4.setConcact(88);
        totalScore4.setGender("男");
        totalScore4.setItemAnalysis(25);
        totalScore4.setOrder(19);
        totalScore4.setSno("4242345");
        totalScore4.setTopicMeeting(36);
        totalScore4.setTopicSpeech(99);
        totalScore4.setTotal(78);
        totalScore4.setWrittenExam(38);
        dataList.add(totalScore4);
        dataList.add(totalScore4);
        dataList.add(totalScore4);
        dataList.add(totalScore4);
        dataList.add(totalScore4);
        dataList.add(totalScore4);

        TotalScore totalScore5 = new TotalScore();
        totalScore5.setTeam(6);
        totalScore5.setAppear(61);
        totalScore5.setUsername("myname6");
        totalScore5.setCollege("武汉大学");
        totalScore5.setConcact(88);
        totalScore5.setGender("女");
        totalScore5.setItemAnalysis(67);
        totalScore5.setOrder(49);
        totalScore5.setSno("482424");
        totalScore5.setTopicMeeting(46);
        totalScore5.setTopicSpeech(79);
        totalScore5.setTotal(88);
        totalScore5.setWrittenExam(98);
        dataList.add(totalScore5);
        dataList.add(totalScore5);
        dataList.add(totalScore5);
        dataList.add(totalScore5);

        TotalScore totalScore6 = new TotalScore();
        totalScore6.setTeam(7);
        totalScore6.setAppear(1);
        totalScore6.setUsername("myname6");
        totalScore6.setCollege("武汉大学");
        totalScore6.setConcact(88);
        totalScore6.setGender("女");
        totalScore6.setItemAnalysis(78);
        totalScore6.setOrder(59);
        totalScore6.setSno("792847");
        totalScore6.setTopicMeeting(59);
        totalScore6.setTopicSpeech(67);
        totalScore6.setTotal(58);
        totalScore6.setWrittenExam(76);

        dataList.add(totalScore6);
        dataList.add(totalScore6);

        TotalScore totalScore7 = new TotalScore();
        totalScore7.setTeam(7);
        totalScore7.setAppear(9);
        totalScore7.setUsername("myname6");
        totalScore7.setCollege("武汉大学");
        totalScore7.setConcact(88);
        totalScore7.setGender("女");
        totalScore7.setItemAnalysis(78);
        totalScore7.setOrder(59);
        totalScore7.setSno("792847");
        totalScore7.setTopicMeeting(59);
        totalScore7.setTopicSpeech(67);
        totalScore7.setTotal(58);
        totalScore7.setWrittenExam(76);
        dataList.add(totalScore7);
        dataList.add(totalScore7);
        dataList.add(totalScore7);

        TotalScore totalScore8 = new TotalScore();
        totalScore7.setTeam(3);
        totalScore7.setAppear(9);
        totalScore7.setUsername("myname7");
        totalScore7.setCollege("武汉大学");
        totalScore7.setConcact(5);
        totalScore7.setGender("女");
        totalScore7.setItemAnalysis(79);
        totalScore7.setOrder(9);
        totalScore7.setSno("792840987");
        totalScore7.setTopicMeeting(9);
        totalScore7.setTopicSpeech(7);
        totalScore7.setTotal(5);
        totalScore7.setWrittenExam(6);

        TotalScore totalScore9 = new TotalScore();
        totalScore7.setTeam(2);
        totalScore7.setAppear(9);
        totalScore7.setUsername("myname7");
        totalScore7.setCollege("武汉大学");
        totalScore7.setConcact(4);
        totalScore7.setGender("女");
        totalScore7.setItemAnalysis(79);
        totalScore7.setOrder(9);
        totalScore7.setSno("792840987");
        totalScore7.setTopicMeeting(92);
        totalScore7.setTopicSpeech(75);
        totalScore7.setTotal(58);
        totalScore7.setWrittenExam(61);

        TotalScore totalScore10 = new TotalScore();
        totalScore7.setTeam(2);
        totalScore7.setAppear(10);
        totalScore7.setUsername("myname7");
        totalScore7.setCollege("武汉大学");
        totalScore7.setConcact(89);
        totalScore7.setGender("女");
        totalScore7.setItemAnalysis(73);
        totalScore7.setOrder(29);
        totalScore7.setSno("292898987");
        totalScore7.setTopicMeeting(94);
        totalScore7.setTopicSpeech(78);
        totalScore7.setTotal(55);
        totalScore7.setWrittenExam(64);

        /**
        dataList.add(totalScore10);
        dataList.add(totalScore9);
        dataList.add(totalScore1);
        dataList.add(totalScore8);
        dataList.add(totalScore2);
        dataList.add(totalScore10);
        dataList.add(totalScore1);
        dataList.add(totalScore5);
        dataList.add(totalScore6);
        dataList.add(totalScore10);
        dataList.add(totalScore2);
        dataList.add(totalScore9);
        dataList.add(totalScore8);

        dataList.add(totalScore10);
        dataList.add(totalScore9);
        dataList.add(totalScore1);
        dataList.add(totalScore8);
        dataList.add(totalScore2);
        dataList.add(totalScore10);
        dataList.add(totalScore1);
        dataList.add(totalScore5);
        dataList.add(totalScore6);
        dataList.add(totalScore10);
        dataList.add(totalScore2);
        dataList.add(totalScore9);
        dataList.add(totalScore8);



        for (int i = 0; i < dataList.size(); ++i){
            if(dataList.get(i).getOrder() == 0 || dataList.get(i).getAppear() == 0){
                dataList.remove(i);
            }
        }

         */
        return dataList;
    }
}
