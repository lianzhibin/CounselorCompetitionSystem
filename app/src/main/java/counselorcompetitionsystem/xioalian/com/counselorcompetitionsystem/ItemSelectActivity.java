package counselorcompetitionsystem.xioalian.com.counselorcompetitionsystem;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import counselorcompetitionsystem.xioalian.com.counselorcompetitionsystem.bean.CompetitorBeforeScore;
import counselorcompetitionsystem.xioalian.com.counselorcompetitionsystem.bean.ItemScore;
import counselorcompetitionsystem.xioalian.com.counselorcompetitionsystem.bean.JudgeItemScore;
import counselorcompetitionsystem.xioalian.com.counselorcompetitionsystem.bean.TotalScore;
import counselorcompetitionsystem.xioalian.com.counselorcompetitionsystem.url.Url;
import counselorcompetitionsystem.xioalian.com.counselorcompetitionsystem.utils.PreferencesUtils;
import counselorcompetitionsystem.xioalian.com.counselorcompetitionsystem.utils.ToastUtils;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class ItemSelectActivity extends BasicActivity {

    // 谈心谈话按钮
    private Button mbtn_concact;
    // 主题演讲按钮
    private Button mbtn_speech;
    // 案例分析按钮
    private Button mbtn_item_analysis;
    // 总成绩按钮
    private Button mbtn_total_score;
    // 编号
    private String id;
    // 评委还是仲裁
    private String user;
    // 显示用户
    private TextView is_comission_arbitrate;
    // 标题
    private TextView title_item_select;
    // 是否要返回上一页
    private boolean isBack = false;
    //
    private Timer timer = new Timer(true);
    //
    private Button mbtn_concact_score;
    //
    private Button mbtn_item_analysis_score;
    //
    private Button mbtn_speech_score;
    private LinearLayout ll_item_select;


    private ArrayList<TotalScore> dataList;
    private ArrayList<CompetitorBeforeScore> dataListCompetitorBeforeScore ;
    private ArrayList<ItemScore> dataListItemScore;
    private ArrayList<JudgeItemScore> dataListJudgeScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_select);

        Log.i("currentid", MyApplication.currentId);
        // 获取控件
        getViews();

        // 获取数据
        getData();

        Log.i("current user", MyApplication.currentUser);

        // 添加监听
        addListener();
    }

    // 返回主界面
    class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            isBack = false;
        }
    }

    // 设置返回功能
    @Override
    public void onBackPressed() {
        if(!isBack){
            ToastUtils.show(ItemSelectActivity.this, "再按一次返回登陆界面");
            isBack = true;
            timer.schedule(new MyTimerTask(), 2000);
            return ;
        }
        super.onBackPressed();
    }

    /**
     *
     */
    private void addListener() {


        // 分别判断是仲裁还是评委
        // 谈心谈话
        mbtn_concact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 评委
                if(MyApplication.currentUser.equals(MyApplication.comission)) {

                    // 判断是否打完分了
                    if(isFinishScore(MyApplication.concact)){
                        ToastUtils.show(ItemSelectActivity.this, "您已经打完分!", 1);
                        return ;
                    }

                    RequestBody formBody = new FormBody.Builder()
                            .add("userName", MyApplication.currentId)
                            .add("activeName", MyApplication.concact)
                            .build();
                    sendRequestWithOkHttpJudgeCompetitor(Url.URL_BASIC + Url.URL_COMPETITOR,formBody, MyApplication.concact);


                } else if(MyApplication.currentUser.equals(MyApplication.arbitrate)) {
                // 仲裁


                    RequestBody formBody = new FormBody.Builder()
                            .add("activeName", MyApplication.concact)
                            .build();
                    sendRequestWithOkHttpItemTotalScore(Url.URL_BASIC + Url.URL_ITEM_TOTAL_SCORE, formBody, MyApplication.concact);


                }
            }

        });

        // 主题演讲
        mbtn_speech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 评委
                if (MyApplication.currentUser.equals(MyApplication.comission)) {

                    // 判断是否打完分了
                    if(isFinishScore(MyApplication.titleSpeech)){
                        ToastUtils.show(ItemSelectActivity.this, "您已经打完分!", 1);
                        return ;
                    }

                    RequestBody formBody = new FormBody.Builder()
                            .add("userName", MyApplication.currentId)
                            .add("activeName", MyApplication.titleSpeech)
                            .build();
                    sendRequestWithOkHttpJudgeCompetitor(Url.URL_BASIC + Url.URL_COMPETITOR,formBody, MyApplication.titleSpeech);


                }else if(MyApplication.currentUser.equals(MyApplication.arbitrate)){
                // 仲裁

                    RequestBody formBody = new FormBody.Builder()
                            .add("activeName", MyApplication.titleSpeech)
                            .build();
                    sendRequestWithOkHttpItemTotalScore(Url.URL_BASIC + Url.URL_ITEM_TOTAL_SCORE, formBody, MyApplication.titleSpeech);


                }
            }
        });

        //案例分析
        mbtn_item_analysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 评委
                if(MyApplication.currentUser.equals(MyApplication.comission)) {

                    // 判断是否打完分了
                    if(isFinishScore(MyApplication.itemAnalysis)){
                        ToastUtils.show(ItemSelectActivity.this, "您已经打完分!", 1);
                        return ;
                    }

                    RequestBody formBody = new FormBody.Builder()
                            .add("userName", MyApplication.currentId)
                            .add("activeName", MyApplication.itemAnalysis)
                            .build();
                    sendRequestWithOkHttpJudgeCompetitor(Url.URL_BASIC + Url.URL_COMPETITOR,formBody, MyApplication.itemAnalysis);



                }else if(MyApplication.currentUser.equals(MyApplication.arbitrate)){

                    RequestBody formBody = new FormBody.Builder()
                            .add("activeName", MyApplication.itemAnalysis)
                            .build();
                    sendRequestWithOkHttpItemTotalScore(Url.URL_BASIC + Url.URL_ITEM_TOTAL_SCORE, formBody, MyApplication.itemAnalysis);




                }
            }
        });

        // 总成绩
        mbtn_total_score.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequestWithOkHttpTotalScore(Url.URL_BASIC + Url.URL_TOTAL_SCORE);
            }
        });

        // 谈心谈话查看成绩
        mbtn_concact_score.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 判断是否打完分了
                if(!isFinishScore(MyApplication.concact)){
                    ToastUtils.show(ItemSelectActivity.this, "您还未打完分, 不能查看成绩", 1);
                    return ;
                }

                RequestBody formBody = new FormBody.Builder()
                        .add("userName", MyApplication.currentId)
                        .add("activeName", MyApplication.concact)
                        .build();
                sendRequestWithOkHttpJudgeTotalScore(Url.URL_BASIC + Url.URL_JUDGE_SCORE, formBody, MyApplication.concact);

            }
        });

        // 主题演讲查看成绩
        mbtn_speech_score.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 判断是否打完分了
                if(!isFinishScore(MyApplication.titleSpeech)){
                    ToastUtils.show(ItemSelectActivity.this, "您还未打完分, 不能查看成绩", 1);
                    return ;
                }

                RequestBody formBody = new FormBody.Builder()
                        .add("userName", MyApplication.currentId)
                        .add("activeName", MyApplication.titleSpeech)
                        .build();
                sendRequestWithOkHttpJudgeTotalScore(Url.URL_BASIC + Url.URL_JUDGE_SCORE, formBody, MyApplication.titleSpeech);

            }
        });

        // 案例分析查看成绩
        mbtn_item_analysis_score.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 判断是否打完分了
                if(!isFinishScore(MyApplication.itemAnalysis)){
                    ToastUtils.show(ItemSelectActivity.this, "您还未打完分, 不能查看成绩", 1);
                    return ;
                }

                RequestBody formBody = new FormBody.Builder()
                        .add("userName", MyApplication.currentId)
                        .add("activeName", MyApplication.itemAnalysis)
                        .build();
                Log.i("url", Url.URL_BASIC + Url.URL_JUDGE_SCORE);
                sendRequestWithOkHttpJudgeTotalScore(Url.URL_BASIC + Url.URL_JUDGE_SCORE, formBody, MyApplication.itemAnalysis);
            }
        });

    }

    /**
     * 评委查看分项目成绩
     */
    private void sendRequestWithOkHttpJudgeTotalScore(final String url, final RequestBody formBody, final String itemName) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient okHttpClient = new OkHttpClient.Builder()
                        .connectTimeout(90, TimeUnit.SECONDS)//10秒连接超时
                        .writeTimeout(90, TimeUnit.SECONDS)//10m秒写入超时
                        .readTimeout(90, TimeUnit.SECONDS)//10秒读取超时
                        .build();

                Request request = new Request.Builder().post(formBody).url(url).build();

                try {
                    Response response = okHttpClient.newCall(request).execute();

                    /**
                     * 连接成功
                     */
                    if(response.isSuccessful()){
                        // 获取数据

                        String responseData = response.body().string();
                        if(responseData.equals("") || responseData.isEmpty() || responseData.equals("[]")){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtils.show(ItemSelectActivity.this, "比赛还未结束!", Toast.LENGTH_SHORT);
                                    return ;
                                }
                            });
                        }

                        Log.i("tag", responseData);
                        // 解析数据
                        parseJsonWithJSONObjectJudgeScore(responseData, itemName);
                        // 角色
                        // MyApplication.currentUser = user;
                    }else{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtils.show(ItemSelectActivity.this, "比赛还未结束!", Toast.LENGTH_SHORT);
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }

    private void parseJsonWithJSONObjectJudgeScore(String responseData, String itemName){
        Gson gson = new Gson();
        dataListJudgeScore = gson.fromJson(responseData, new TypeToken<ArrayList<JudgeItemScore>>(){}.getType());
        Log.i("tag", dataListJudgeScore.size() + "");

        Intent intent = new Intent(ItemSelectActivity.this, JudgeScoreActivity.class);
        intent.putExtra("data", dataListJudgeScore);
        intent.putExtra("itemName", itemName);
        startActivity(intent);
    }

    /**
     * 仲裁查看分项目成绩
     */
    private void sendRequestWithOkHttpItemTotalScore(final String url, final RequestBody formBody, final String itemName) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient okHttpClient = new OkHttpClient.Builder()
                        .connectTimeout(90, TimeUnit.SECONDS)//10秒连接超时
                        .writeTimeout(90, TimeUnit.SECONDS)//10m秒写入超时
                        .readTimeout(90, TimeUnit.SECONDS)//10秒读取超时
                        .build();

                Request request = new Request.Builder().post(formBody).url(url).build();

                try {
                    Response response = okHttpClient.newCall(request).execute();
                    /**
                     * 连接成功
                     */
                    if(response.isSuccessful()){
                        // 获取数据

                        String responseData = response.body().string();
                        if(responseData.equals("") || responseData.isEmpty() || responseData.equals("[]")){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtils.show(ItemSelectActivity.this, "比赛还未开始", Toast.LENGTH_SHORT);
                                }
                            });
                            return ;
                        }


                        // Log.i("tag", responseData);
                        // 解析数据
                        parseJsonWithJSONObjectItemTotalScore(responseData, itemName);
                        // 角色
                        // MyApplication.currentUser = user;
                    }else{

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void parseJsonWithJSONObjectItemTotalScore(String responseData, String itemName){
        Gson gson = new Gson();
        dataListItemScore = gson.fromJson(responseData, new TypeToken<ArrayList<ItemScore>>(){}.getType());
        Log.i("tag", dataListItemScore.toString());

        if(dataListItemScore != null && dataListItemScore.size() != 0) {
            Intent intent = new Intent(ItemSelectActivity.this, ItemTotalScoreActivity.class);
            intent.putExtra("data", dataListItemScore);
            intent.putExtra("itemName", itemName);
            startActivity(intent);
        }else{
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ToastUtils.show(ItemSelectActivity.this, "打分还未结束!", 1);
                }
            });

        }
    }

    /**
     * 选手出场顺序
     * @param url
     */
    private void sendRequestWithOkHttpJudgeCompetitor(final String url, final RequestBody formBody , final String itemName){
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient okHttpClient = new OkHttpClient.Builder()
                        .connectTimeout(90, TimeUnit.SECONDS)//10秒连接超时
                        .writeTimeout(90, TimeUnit.SECONDS)//10m秒写入超时
                        .readTimeout(90, TimeUnit.SECONDS)//10秒读取超时
                        .build();

                Request request = new Request.Builder().post(formBody).url(url).build();

                try {
                    Response response = okHttpClient.newCall(request).execute();

                    /**
                     * 连接成功
                     */
                    if(response.isSuccessful()){
                        // 获取数据
                        String responseData = response.body().string();
                        Log.i("tag", responseData);

                        if(responseData == null || responseData.equals("[]") ){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtils.show(ItemSelectActivity.this, "比赛还未开始!");
                                }
                            });

                        }

                        parseJsonWithJSONObjectCompetitorBeforeScore(responseData, itemName);

                        // 角色
                        // MyApplication.currentUser = user;
                    }else{

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 选手出场顺序数据解析
     * @param responseData
     */
    private void parseJsonWithJSONObjectCompetitorBeforeScore(String responseData, String itemName ){
        Gson gson = new Gson();
        dataListCompetitorBeforeScore = gson.fromJson(responseData, new TypeToken<ArrayList<CompetitorBeforeScore>>(){}.getType());
        Log.i("tag", dataListCompetitorBeforeScore.toString());

        if(dataListCompetitorBeforeScore != null && dataListCompetitorBeforeScore.size() != 0) {
            Intent intent = new Intent(ItemSelectActivity.this, CompetitionActivity.class);
            intent.putExtra("itemName", itemName);
            intent.putExtra("data", dataListCompetitorBeforeScore);
            startActivity(intent);
        }else{
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ToastUtils.show(ItemSelectActivity.this, "比赛还未开始,请耐心等待!", 1);
                }
            });

        }
    }

    /**
     *
     * @param url
     */
    private void sendRequestWithOkHttpTotalScore(final String url){
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient okHttpClient = new OkHttpClient.Builder()
                        .connectTimeout(90, TimeUnit.SECONDS)//10秒连接超时
                        .writeTimeout(90, TimeUnit.SECONDS)//10m秒写入超时
                        .readTimeout(90, TimeUnit.SECONDS)//10秒读取超时
                        .build();

                Request request = new Request.Builder().url(url).build();
                try {
                    Response response = okHttpClient.newCall(request).execute();
                    /**
                     * 登陆成功
                     */
                    if(response.isSuccessful()){
                        // 获取数据
                        String responseData = response.body().string();
                        Log.i("tag", responseData);
                        parseJsonWithJSONObjectTotalScore(responseData);
                        // 角色
                        // MyApplication.currentUser = user;
                    }else{

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void parseJsonWithJSONObjectTotalScore(String responseData) {
        Gson gson = new Gson();
        dataList = gson.fromJson(responseData, new TypeToken<ArrayList<TotalScore>>(){}.getType());

        Log.i("tag", dataList.toString());

        Intent intent = new Intent(ItemSelectActivity.this, TotalScoreActivity.class);
        intent.putExtra("itemName", MyApplication.totalScore);
        intent.putExtra("data", dataList);
        startActivity(intent);
    }


    /**
     *
     */
    private void getViews() {
        is_comission_arbitrate = (TextView)findViewById(R.id.tv_comission_or_arbitrate);
        title_item_select = (TextView)findViewById(R.id.title_item_select);

        mbtn_concact = (Button)findViewById(R.id.btn_concact);
        mbtn_speech = (Button)findViewById(R.id.btn_speech);
        mbtn_item_analysis = (Button)findViewById(R.id.btn_item_analysis);
        mbtn_total_score = (Button)findViewById(R.id.btn_total_score);

        mbtn_concact_score = (Button)findViewById(R.id.btn_concact_score);
        mbtn_item_analysis_score = (Button)findViewById(R.id.btn_item_analysis_score);
        mbtn_speech_score = (Button)findViewById(R.id.btn_speech_score);

        ll_item_select = (LinearLayout)findViewById(R.id.ll_item_select);
    }

    /**
     *
     */
    private void getData() {
        title_item_select.setText(MyApplication.competitionName);
        if(MyApplication.currentUser.equals(MyApplication.comission)) {
            is_comission_arbitrate.setText("当前" + "评委" + "编号 ：" + MyApplication.currentId);

        }else if(MyApplication.currentUser.equals(MyApplication.arbitrate)){
            is_comission_arbitrate.setText("当前" + "仲裁" + "编号 ：" + MyApplication.currentId);
        }

        // 如果是评委
        if(MyApplication.currentUser.equals(MyApplication.comission)){
            mbtn_total_score.setVisibility(View.GONE);
        }

        // 如果是仲裁
        if(MyApplication.currentUser.equals(MyApplication.arbitrate)){
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)ll_item_select.getLayoutParams();

            params.setMargins(0, 1200, 0, 0);
            ll_item_select.setLayoutParams(params);

            mbtn_total_score.setVisibility(View.VISIBLE);
            mbtn_concact_score.setVisibility(View.GONE);
            mbtn_speech_score.setVisibility(View.GONE);
            mbtn_item_analysis_score.setVisibility(View.GONE);
        }


    }

    /**
     * 判断评委是否打完分了
     * @param currentItemName
     * @return
     */
    private boolean isFinishScore(String currentItemName){
        return PreferencesUtils.getBoolean(ItemSelectActivity.this, MyApplication.competitionNameID + MyApplication.currentId + currentItemName, false);
    }



}
