package counselorcompetitionsystem.xioalian.com.counselorcompetitionsystem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import counselorcompetitionsystem.xioalian.com.counselorcompetitionsystem.bean.CompetitorBeforeScore;
import counselorcompetitionsystem.xioalian.com.counselorcompetitionsystem.bean.JudgeItemScore;
import counselorcompetitionsystem.xioalian.com.counselorcompetitionsystem.bean.SubmitScore;
import counselorcompetitionsystem.xioalian.com.counselorcompetitionsystem.url.Url;
import counselorcompetitionsystem.xioalian.com.counselorcompetitionsystem.utils.LogUtil;
import counselorcompetitionsystem.xioalian.com.counselorcompetitionsystem.utils.PreferencesUtils;
import counselorcompetitionsystem.xioalian.com.counselorcompetitionsystem.utils.ToastUtils;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 竞赛
 */
public class CompetitionActivity extends BasicActivity {
    // 当前用户
    private TextView user;
    // 比赛名称
    private TextView title;
    // 项目名称以及组别
    private TextView item_name;
    // 时间
    private TextView timeTextView;
    // 选手打分列表
    private RecyclerView rv;
    // 提交分数
    private Button input_score;
    // 当前比赛项目
    private String currentItemName;

    // 当前组 耗时
    private int consumeTime = 0;

    // 当前组截止时间
    private int totalTime;

    // 第一位选手打分栏目
    private RelativeLayout ll_judge_score_1;
    // 第二位选手打分栏目
    private RelativeLayout ll_judge_score_2;
    // 第三位选手打分栏目
    private RelativeLayout ll_judge_score_3;

    // 选手出场顺序
    private ArrayList<CompetitorBeforeScore> dataList = new ArrayList<CompetitorBeforeScore>();

    // 第一个选手的得分、备注、保存
    private EditText score1;
    private EditText remark1;
    private Button save1;
    private TextView tips1;
    private TextView order1;

    // 第二个选手的得分、备注、保存
    private EditText score2;
    private EditText remark2;
    private Button save2;
    private TextView tips2;
    private TextView order2;

    // 第三个选手的得分、备注、保存
    private EditText score3;
    private EditText remark3;
    private Button save3;
    private TextView tips3;
    private TextView order3;

    // 总共有几位选手
    private int totalCompetitiors;
    // 当前批次
    private int currentBatch = 1;
    // 共有多少批
    private int totalBatch;
    // 剩下多少
    private int remainCompetitors;
    // 计时任务
    Timer timer = new Timer(true);
    TimerTask task = new TimerTask(){
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    LogUtil.LogUtil("time:", ""+consumeTime);
                    consumeTime += 1;
                    timeTextView.setText("已进行：" + consumeTime / 60 + "分" + consumeTime % 60 + "秒");
                }
            });
        }
    };
    // 返回主界面
    class MyTimerTask extends TimerTask{
        @Override
        public void run() {
            isBack = false;
        }
    }

    // 判断三个分数是不是都提交成功了
    /**
     * 三个图片
     */
    private ImageView gender1;
    private ImageView gender2;
    private ImageView gender3;

    /**
     * 提交成功了吗
     */
    private boolean submit1 = false;
    private boolean submit2 = false;
    private boolean submit3 = false;

    private ProgressDialog progressDialog;

    // 返回按钮
    private ImageView back;

    // 是否确定要返回主界面
    private boolean isBack = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competition);

        // 获取控件
        getViews();

        // 获取数据
        getData();

        // 添加监听
        addListener();

        timer.schedule(task, 1000, 1000);
    }

    // 返回按钮
    @Override
    public void onBackPressed() {
        if(!isBack){
            isBack = true;
            ToastUtils.show(CompetitionActivity.this, "再按一次返回主界面");
            PreferencesUtils.putString(CompetitionActivity.this, currentItemName + "consumetime", consumeTime + "");
            PreferencesUtils.putInt(CompetitionActivity.this, currentItemName + "i", currentBatch);
            timer.schedule(new MyTimerTask(), 2000);
            return ;
        }
        super.onBackPressed();
    }

    /**
     *
     * @param data
     * @return
     */
    private boolean isValid(String data){
        // Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");|| !pattern.matcher(data).matches()
        if(data.equals("") || Double.parseDouble(data) < 0 || Double.parseDouble(data) > 100){
            return false;
        }
        return true;
    }

    // 判断输入的数字是否合法
    private boolean isScoreValidAll(){

        String score_1 = score1.getText().toString().trim();
        String score_2 = score2.getText().toString().trim();
        String score_3 = score3.getText().toString().trim();

        // 检查 分数有没有填错
        // 判断是否为空
        if (!isValid(score_1)) {
            tips1.setVisibility(View.VISIBLE);
            closeProgress();
            return false;
        } else {
            tips1.setVisibility(View.GONE);
        }

        if (!isValid(score_2)) {
            tips2.setVisibility(View.VISIBLE);
            closeProgress();
            return false;
        } else {
            tips2.setVisibility(View.GONE);
        }
        if (!isValid(score_3)) {
            tips3.setVisibility(View.VISIBLE);
            closeProgress();
            return false;
        } else {
            tips3.setVisibility(View.GONE);
        }
        return true;
    }


    // 只判断第一个输入的数字是否合法
    private boolean isScoreValidOne(){
        String score_1 = score1.getText().toString().trim();

        // 检查 分数有没有填错
        // 判断是否为空
        if (!isValid(score_1)) {
            tips1.setVisibility(View.VISIBLE);
            closeProgress();
            return false;
        } else {
            tips1.setVisibility(View.GONE);
        }

        return true;
    }

    // 只判断第一个和第二个输入的数字是否合法
    private boolean isScoreValidTwo(){
        String score_1 = score1.getText().toString().trim();
        String score_2 = score2.getText().toString().trim();

        // 检查 分数有没有填错
        // 判断是否为空
        if (!isValid(score_1)) {
            tips1.setVisibility(View.VISIBLE);
            closeProgress();
            return false;
        } else {
            tips1.setVisibility(View.GONE);
        }

        if (!isValid(score_2)) {
            tips2.setVisibility(View.VISIBLE);
            closeProgress();
            return false;
        } else {
            tips2.setVisibility(View.GONE);
        }
        return true;
    }

    // 清除数据
    private void removedata(){

        PreferencesUtils.remove(CompetitionActivity.this, currentItemName+"score1");
        PreferencesUtils.remove(CompetitionActivity.this, currentItemName+"score2");
        PreferencesUtils.remove(CompetitionActivity.this, currentItemName+"score3");

        PreferencesUtils.remove(CompetitionActivity.this, currentItemName+"remark1");
        PreferencesUtils.remove(CompetitionActivity.this, currentItemName+"remark2");
        PreferencesUtils.remove(CompetitionActivity.this, currentItemName+"remark3");

        PreferencesUtils.remove(CompetitionActivity.this, currentItemName + "consumetime");
    }

    private void addListener() {

        // 返回按钮
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CompetitionActivity.this.onBackPressed();
            }
        });


        // 提交分数
        input_score.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 显示进度条

                if (currentBatch < totalBatch) {

                    // 判断是否合法
                    if (!isScoreValidAll()) {
                        ToastUtils.show(CompetitionActivity.this, "请认真检查输入的数字合法后再上传，谢谢！", 1);
                        return;
                    }

                    // 隐藏全部tips
                    tips1.setVisibility(View.GONE);
                    tips2.setVisibility(View.GONE);
                    tips3.setVisibility(View.GONE);

                    // 上传数
                    String score_1 = score1.getText().toString().trim();
                    String score_2 = score2.getText().toString().trim();
                    String score_3 = score3.getText().toString().trim();

                    String remark_1 = remark1.getText().toString().trim();
                    String remark_2 = remark2.getText().toString().trim();
                    String remark_3 = remark3.getText().toString().trim();

                    String[] score = {score_1, score_2, score_3};
                    String[] remark = {remark_1, remark_2, remark_3};

                    for (int j = 0; j < 3; ++j) {
                        RequestBody formBody = new FormBody.Builder()
                                .add("userName", MyApplication.currentId)
                                .add("activeName", currentItemName)
                                .add("score", score[j])
                                .add("description", remark[j])
                                .add("competitorID", dataList.get((currentBatch - 1)* 3 + j).getId() + "")
                                .build();
                        if (j == 0) {
                            sendRequestWithOkHttpJudgeSubmitScore1(Url.URL_BASIC + Url.URL_SUBMIT, formBody);
                        }
                        if (j == 1) {
                            sendRequestWithOkHttpJudgeSubmitScore2(Url.URL_BASIC + Url.URL_SUBMIT, formBody);
                        }
                        if (j == 2) {
                            sendRequestWithOkHttpJudgeSubmitScore3(Url.URL_BASIC + Url.URL_SUBMIT, formBody);
                        }
                    }

                    // 判断三个分数是不是都提交了
                    for (int i = 0; i < 50; ++i) {
                        if (submit1 && submit2 && submit3) {
                            handleAfterSubmit1();
                            break;
                        } else {
                           // showProgress();

                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }


                } else if (currentBatch == totalBatch) {

                    // 判断是否合法
                    if (!isScoreValidAll()) {
                        ToastUtils.show(CompetitionActivity.this, "请认真检查输入的数字合法后再上传，谢谢！", 1);
                        return;
                    }

                    // 隐藏全部tips
                    tips1.setVisibility(View.GONE);
                    tips2.setVisibility(View.GONE);
                    tips3.setVisibility(View.GONE);

                    String score_1 = score1.getText().toString().trim();
                    String score_2 = score2.getText().toString().trim();
                    String score_3 = score3.getText().toString().trim();

                    String remark_1 = remark1.getText().toString().trim();
                    String remark_2 = remark2.getText().toString().trim();
                    String remark_3 = remark3.getText().toString().trim();

                    String[] score = {score_1, score_2, score_3};
                    String[] remark = {remark_1, remark_2, remark_3};

                    // 提交成功之后
                    for (int j = 0; j < 3; ++j) {
                        RequestBody formBody = new FormBody.Builder()
                                .add("userName", MyApplication.currentId)
                                .add("activeName", currentItemName)
                                .add("score", score[j])
                                .add("description", remark[j])
                                .add("competitorID", dataList.get((currentBatch - 1) * 3 + j).getId() + "")
                                .build();
                        if (j == 0) {
                            sendRequestWithOkHttpJudgeSubmitScore1(Url.URL_BASIC + Url.URL_SUBMIT, formBody);
                        }
                        if (j == 1) {
                            sendRequestWithOkHttpJudgeSubmitScore2(Url.URL_BASIC + Url.URL_SUBMIT, formBody);
                        }
                        if (j == 2) {
                            sendRequestWithOkHttpJudgeSubmitScore3(Url.URL_BASIC + Url.URL_SUBMIT, formBody);
                        }
                    }

                    for (int i = 0; i < 50; ++i) {
                        if (submit1 && submit2 && submit3) {
                            handleAfterSubmit2();
                            break;
                        } else {
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            // showProgress();
                        }

                    }

                } else if (currentBatch > totalBatch) {

                    // 只剩下一位需要评分的选手
                    if (1 == remainCompetitors) {

                        // 检查不通过
                        if (!isScoreValidOne()) {
                            ToastUtils.show(CompetitionActivity.this, "请认真检查输入的数字合法后再上传，谢谢！", 1);
                            return;
                        }

                        tips1.setVisibility(View.GONE);

                        // 检查通过
                        // 提交成功

                        String score_1 = score1.getText().toString().trim();

                        String remark_1 = remark1.getText().toString().trim();


                        // 提交成功之后
                        RequestBody formBody = new FormBody.Builder()
                                .add("userName", MyApplication.currentId)
                                .add("activeName", currentItemName)
                                .add("score", score_1)
                                .add("description", remark_1)
                                .add("competitorID", dataList.get((currentBatch - 1) * 3).getId() + "")
                                .build();
                        sendRequestWithOkHttpJudgeSubmitScore1(Url.URL_BASIC + Url.URL_SUBMIT, formBody);

                        for(int i = 0; i < 10; i++){
                            if(submit1){
                                // 清除数据
                                removedata();
                                submit1 = false;

                                PreferencesUtils.remove(CompetitionActivity.this, MyApplication.currentUser + currentItemName + "currentBatch");
                                PreferencesUtils.putBoolean(CompetitionActivity.this, MyApplication.competitionNameID + MyApplication.currentId + currentItemName, true);
                                closeProgress();

                                // 跳转到项目选择界面
                                // Intent intent = new Intent(CompetitionActivity.this, ItemSelectActivity.class);
                                // startActivity(intent);
                                finish();
                                break;
                            }else{
                                try {
                                    Thread.sleep(100);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                // showProgress();
                            }

                        }
                        // 剩下两位需要评分的选手
                    } else if (2 == remainCompetitors) {
                        // 检查不通过
                        if (!isScoreValidTwo()) {
                            ToastUtils.show(CompetitionActivity.this, "请认真检查输入的数字合法后再上传，谢谢！", 1);
                            return;
                        }

                        tips1.setVisibility(View.GONE);
                        tips2.setVisibility(View.GONE);

                        // 检查通过
                        // 提交成功

                        String score_1 = score1.getText().toString().trim();
                        String score_2 = score2.getText().toString().trim();

                        String remark_1 = remark1.getText().toString().trim();
                        String remark_2 = remark2.getText().toString().trim();

                        String[] score = {score_1, score_2};
                        String[] remark = {remark_1, remark_2};

                        // 提交成功之后
                        for (int j = 0; j < 2; ++j) {
                            RequestBody formBody = new FormBody.Builder()
                                    .add("userName", MyApplication.currentId)
                                    .add("activeName", currentItemName)
                                    .add("score", score[j])
                                    .add("description", remark[j])
                                    .add("competitorID", dataList.get((currentBatch - 1) * 3 + j).getId() + "")
                                    .build();
                            if (j == 0) {
                                sendRequestWithOkHttpJudgeSubmitScore1(Url.URL_BASIC + Url.URL_SUBMIT, formBody);
                            }
                            if (j == 1) {
                                sendRequestWithOkHttpJudgeSubmitScore2(Url.URL_BASIC + Url.URL_SUBMIT, formBody);
                            }
                        }

                        for(int i = 0; i < 10; ++i){
                            if(submit1 && submit2){
                                // 清除数据
                                removedata();
                                submit1 = false;
                                submit2 = false;

                                PreferencesUtils.putBoolean(CompetitionActivity.this, MyApplication.competitionNameID + MyApplication.currentId + currentItemName, true);
                                PreferencesUtils.remove(CompetitionActivity.this, MyApplication.currentUser + currentItemName + "currentBatch");
                                closeProgress();

                                // 跳转到项目选择界面
                                // Intent intent = new Intent(CompetitionActivity.this, ItemSelectActivity.class);
                                // startActivity(intent);
                                finish();

                                break;
                            }else{

                                try {
                                    Thread.sleep(100);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                // showProgress();
                            }
                        }
                    }
                }
            }

        });

        save1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = score1.getText().toString();
                String r = remark1.getText().toString();
                if(isValid(s)){
                    PreferencesUtils.putString(CompetitionActivity.this, currentItemName + "score1", s);
                    PreferencesUtils.putString(CompetitionActivity.this, currentItemName + "remark1", r);
                    ToastUtils.show(CompetitionActivity.this, "保存成功！", 1);
                }else{
                    ToastUtils.show(CompetitionActivity.this, "请先保证输入的数字合法后再保存！", 1);
                }
            }
        });

        save2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = score2.getText().toString();
                String r = remark2.getText().toString();
                if(isValid(s)){
                    PreferencesUtils.putString(CompetitionActivity.this, currentItemName + "score2", s);
                    PreferencesUtils.putString(CompetitionActivity.this, currentItemName + "remark2", r);
                    ToastUtils.show(CompetitionActivity.this, "保存成功！", 1);
                }else{
                    ToastUtils.show(CompetitionActivity.this, "请先保证输入的数字合法后再保存！", 1);
                }
            }
        });

        save3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = score3.getText().toString();
                String r = remark3.getText().toString();
                if(isValid(s)){
                    PreferencesUtils.putString(CompetitionActivity.this, currentItemName + "score3", s);
                    PreferencesUtils.putString(CompetitionActivity.this, currentItemName + "remark3", r);
                    ToastUtils.show(CompetitionActivity.this, "保存成功！", 1);
                }else{
                    ToastUtils.show(CompetitionActivity.this, "请先保证输入的数字合法后再保存！", 1);
                }
            }
        });
    }

    /**
     * 获得数据
     */
    private void getData() {
        Intent intent = getIntent();
        currentItemName = intent.getStringExtra("itemName");
        dataList = (ArrayList<CompetitorBeforeScore>) intent.getSerializableExtra("data");
        totalCompetitiors = dataList.size();
        totalBatch = totalCompetitiors / 3;
        remainCompetitors = totalCompetitiors % 3;

        user.setText("当前评委编号：" + MyApplication.currentId);
        title.setText(MyApplication.competitionName);
        item_name.setText(currentItemName + "( 第" + dataList.get(0).getGroup() + "组 )");

        setValues();
    }

    /**
     * 出现进度条
     */
    private void showProgress(){
        progressDialog = new ProgressDialog(CompetitionActivity.this);
        progressDialog.setTitle("亲，系统正在提交哦");
        progressDialog.setMessage("loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    /**
     * 关闭进度条
     */
    private void closeProgress(){
        if(progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    /**
     * 评委提交成绩1
     * */
    private void sendRequestWithOkHttpJudgeSubmitScore1(final String url, final RequestBody formBody) {
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
                    if(response.isSuccessful()){
                        Log.i("JudgeSubmitScore1", response.toString());
                        submit1 =true;
                    }else{

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 评委提交成绩2
     * */
    private void sendRequestWithOkHttpJudgeSubmitScore2(final String url, final RequestBody formBody) {
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

                    if(response.isSuccessful()){
                        Log.i("JudgeSubmitScore2", response.toString());
                        submit2 =true;
                    }else{

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 评委提交成绩3
     * */
    private void sendRequestWithOkHttpJudgeSubmitScore3(final String url, final RequestBody formBody) {
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
                    if(response.isSuccessful()){
                        Log.i("JudgeSubmitScore3", response.toString());
                        submit3 =true;
                    }else{
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    private void setValues(){

        String score_1 = PreferencesUtils.getString(CompetitionActivity.this, currentItemName + "score1", "");
        String score_2 = PreferencesUtils.getString(CompetitionActivity.this, currentItemName + "score2", "");
        String score_3 = PreferencesUtils.getString(CompetitionActivity.this, currentItemName + "score3", "");

        String remark_1 = PreferencesUtils.getString(CompetitionActivity.this, currentItemName + "remark1", "");
        String remark_2 = PreferencesUtils.getString(CompetitionActivity.this, currentItemName + "remark2", "");
        String remark_3 = PreferencesUtils.getString(CompetitionActivity.this, currentItemName + "remark3", "");

        score1.setText(score_1);
        score2.setText(score_2);
        score3.setText(score_3);

        remark1.setText(remark_1);
        remark2.setText(remark_2);
        remark3.setText(remark_3);

        // 获取轮数
        currentBatch = PreferencesUtils.getInt(CompetitionActivity.this, MyApplication.currentUser + currentItemName + "currentBatch", 1);

        order1.setText("" + ((currentBatch - 1)*3 + 1) );
        order2.setText("" + ((currentBatch - 1)*3 + 2) );
        order3.setText("" + ((currentBatch - 1)*3 + 3) );

        consumeTime = Integer.parseInt(PreferencesUtils.getString(CompetitionActivity.this, currentItemName + "consumetime", "0") );



        if(currentBatch <= totalBatch) {
            String genderJudge1 = dataList.get((currentBatch - 1) * 3).getGender();
            String genderJudge2 = dataList.get((currentBatch - 1) * 3 + 1).getGender();
            String genderJudge3 = dataList.get((currentBatch - 1) * 3 + 2).getGender();
            if ( genderJudge1 != null && genderJudge1.equals("男")) {
                gender1.setImageResource(R.drawable.icon_mail);
            } else if (genderJudge1 != null && genderJudge1.equals("女")) {
                gender1.setImageResource(R.drawable.icon_femail);
            }

            if (genderJudge2 != null && genderJudge2.equals("男")) {
                gender2.setImageResource(R.drawable.icon_mail);
            } else if (genderJudge2 != null && genderJudge2.equals("女")) {
                gender2.setImageResource(R.drawable.icon_femail);
            }

            if (genderJudge3 != null && genderJudge3.equals("男")) {
                gender3.setImageResource(R.drawable.icon_mail);
            } else if (genderJudge3 != null && genderJudge3.equals("女")) {
                gender3.setImageResource(R.drawable.icon_femail);
            }
        }else{
            if(remainCompetitors == 1){
                ll_judge_score_2.setVisibility(View.GONE);
                ll_judge_score_3.setVisibility(View.GONE);
                String genderJudge1 = dataList.get((currentBatch - 1) * 3).getGender();
                if ( genderJudge1 != null && genderJudge1.equals("男")) {
                    gender1.setImageResource(R.drawable.icon_mail);
                } else if (genderJudge1 != null && genderJudge1.equals("女")) {
                    gender1.setImageResource(R.drawable.icon_femail);
                }
            }else if(remainCompetitors == 2){
                ll_judge_score_3.setVisibility(View.GONE);
                String genderJudge1 = dataList.get((currentBatch - 1) * 3).getGender();
                String genderJudge2 = dataList.get((currentBatch - 1) * 3 + 1).getGender();

                if ( genderJudge1 != null && genderJudge1.equals("男")) {
                    gender1.setImageResource(R.drawable.icon_mail);
                } else if (genderJudge1 != null && genderJudge1.equals("女")) {
                    gender1.setImageResource(R.drawable.icon_femail);
                }
                if (genderJudge2 != null && genderJudge2.equals("男")) {
                    gender2.setImageResource(R.drawable.icon_mail);
                } else if (genderJudge2 != null && genderJudge2.equals("女")) {
                    gender2.setImageResource(R.drawable.icon_femail);
                }
            }
        }
    }

    private void getViews() {
        user = (TextView)findViewById(R.id.tv_competition_commission_id);
        title = (TextView)findViewById(R.id.tv_competition_titlt);
        item_name = (TextView)findViewById(R.id.tv_item_name_and_team_number);
        timeTextView = (TextView)findViewById(R.id.tv_competition_time);
        input_score = (Button)findViewById(R.id.btn_competition_input_score);
        back = (ImageView)findViewById(R.id.img_back_competition_activity);

        // 第一个选手
        order1 = (TextView)findViewById(R.id.tv_game_item_order1);
        score1 = (EditText)findViewById(R.id.et_game_competition_score1);
        remark1 = (EditText)findViewById(R.id.et_game_competition_remark1);
        save1 = (Button)findViewById(R.id.btn_game_competition_save1);
        tips1 = (TextView)findViewById(R.id.tv_game_competition_score_input_tips1);

        // 第二个选手
        order2 = (TextView)findViewById(R.id.tv_game_item_order2);
        score2 = (EditText)findViewById(R.id.et_game_competition_score2);
        remark2 = (EditText)findViewById(R.id.et_game_competition_remark2);
        save2 = (Button)findViewById(R.id.btn_game_competition_save2);
        tips2 = (TextView)findViewById(R.id.tv_game_competition_score_input_tips2);

        // 第三个选手
        order3 = (TextView)findViewById(R.id.tv_game_item_order3);
        score3 = (EditText)findViewById(R.id.et_game_competition_score3);
        remark3 = (EditText)findViewById(R.id.et_game_competition_remark3);
        save3 = (Button)findViewById(R.id.btn_game_competition_save3);
        tips3 = (TextView)findViewById(R.id.tv_game_competition_score_input_tips3);

        // 三个栏目
        ll_judge_score_1 = (RelativeLayout)findViewById(R.id.ll_judge_score1);
        ll_judge_score_2 = (RelativeLayout)findViewById(R.id.ll_judge_score2);
        ll_judge_score_3 = (RelativeLayout)findViewById(R.id.ll_judge_score3);

        // 三个性别
        gender1 = (ImageView)findViewById(R.id.iv_gender_competition1);
        gender2 = (ImageView)findViewById(R.id.iv_gender_competition2);
        gender3 = (ImageView)findViewById(R.id.iv_gender_competition3);
    }

    /**
     * 提交之后的处理
     */
    private void handleAfterSubmit1(){
        // 更新布局
        ++currentBatch;

        PreferencesUtils.putInt(CompetitionActivity.this, MyApplication.currentUser + currentItemName + "currentBatch", currentBatch);

        order1.setText("" + ((currentBatch - 1) * 3 + 1));
        order2.setText("" + ((currentBatch - 1) * 3 + 2));
        order3.setText("" + ((currentBatch - 1) * 3 + 3));

        //
        String genderJudge1 = dataList.get((currentBatch - 1) * 3).getGender();
        String genderJudge2 = dataList.get((currentBatch - 1) * 3 + 1).getGender();
        String genderJudge3 = dataList.get((currentBatch - 1) * 3 + 2).getGender();

        if (genderJudge1 != null && genderJudge1.equals("男")) {
            gender1.setImageResource(R.drawable.icon_mail);
        } else if (genderJudge1 != null && genderJudge1.equals("女")) {
            gender1.setImageResource(R.drawable.icon_femail);
        }

        if (genderJudge2 != null && genderJudge2.equals("男")) {
            gender2.setImageResource(R.drawable.icon_mail);
        } else if (genderJudge2 != null && genderJudge2.equals("女")) {
            gender2.setImageResource(R.drawable.icon_femail);
        }

        if (genderJudge3 != null && genderJudge3.equals("男")) {
            gender3.setImageResource(R.drawable.icon_mail);
        } else if (genderJudge3 != null && genderJudge3.equals("女")) {
            gender3.setImageResource(R.drawable.icon_femail);
        }

        score1.setText("");
        remark1.setText("");

        score2.setText("");
        remark2.setText("");

        score3.setText("");
        remark3.setText("");

        consumeTime = 0;

        removedata();

        submit1 = false;
        submit2 = false;
        submit3 = false;

        closeProgress();


    }

    /**
     * 提交之后的处理
     */
    private void handleAfterSubmit2(){

        if(remainCompetitors == 0){
            removedata();

            PreferencesUtils.remove(CompetitionActivity.this, MyApplication.currentUser + currentItemName + "currentBatch");
            PreferencesUtils.putBoolean(CompetitionActivity.this, MyApplication.competitionNameID + MyApplication.currentId + currentItemName, true);
            closeProgress();

            // 跳转到项目选择界面
            // Intent intent = new Intent(CompetitionActivity.this, ItemSelectActivity.class);
            // startActivity(intent);
            finish();

            return ;
        }

        ++ currentBatch;
        PreferencesUtils.putInt(CompetitionActivity.this, MyApplication.currentUser + currentItemName + "currentBatch", currentBatch);

        // 更新界面

        if (remainCompetitors > 0) {

            if (1 == remainCompetitors) {

                ll_judge_score_2.setVisibility(View.GONE);
                ll_judge_score_3.setVisibility(View.GONE);

                score1.setText("");
                remark1.setText("");

                String genderJudge1 = dataList.get((currentBatch - 1) * 3).getGender();

                if (genderJudge1 != null && genderJudge1.equals("男")) {
                    gender1.setImageResource(R.drawable.icon_mail);
                } else if (genderJudge1 != null && genderJudge1.equals("女")) {
                    gender1.setImageResource(R.drawable.icon_femail);
                }

                order1.setText("" + ((currentBatch - 1) * 3 + 1));

            } else if (2 == remainCompetitors) {

                ll_judge_score_3.setVisibility(View.GONE);

                String genderJudge1 = dataList.get((currentBatch - 1) * 3).getGender();
                String genderJudge2 = dataList.get((currentBatch - 1) * 3 + 1).getGender();

                if (genderJudge1 != null && genderJudge1.equals("男")) {
                    gender1.setImageResource(R.drawable.icon_mail);
                } else if (genderJudge1 != null && genderJudge1.equals("女")) {
                    gender1.setImageResource(R.drawable.icon_femail);
                }

                if (genderJudge2 != null && genderJudge2.equals("男")) {
                    gender2.setImageResource(R.drawable.icon_mail);
                } else if (genderJudge2 != null && genderJudge2.equals("女")) {
                    gender2.setImageResource(R.drawable.icon_femail);
                }

                score1.setText("");
                remark1.setText("");

                score2.setText("");
                remark2.setText("");

                order1.setText("" + ((currentBatch - 1) * 3 + 1));
                order2.setText("" + ((currentBatch - 1) * 3 + 2));
            }

            consumeTime = 0;
            removedata();

            submit1 = false;
            submit2 = false;
            submit3 = false;

            closeProgress();
        }

    }

}
