package counselorcompetitionsystem.xioalian.com.counselorcompetitionsystem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.TimeUnit;

import counselorcompetitionsystem.xioalian.com.counselorcompetitionsystem.adapter.TotalScoreAdapter;
import counselorcompetitionsystem.xioalian.com.counselorcompetitionsystem.bean.ItemScore;
import counselorcompetitionsystem.xioalian.com.counselorcompetitionsystem.bean.TotalScore;
import counselorcompetitionsystem.xioalian.com.counselorcompetitionsystem.testdata.TotalScoreTestdata;
import counselorcompetitionsystem.xioalian.com.counselorcompetitionsystem.url.Url;
import counselorcompetitionsystem.xioalian.com.counselorcompetitionsystem.utils.ToastUtils;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TotalScoreActivity extends BasicActivity {

    private RecyclerView rv;
    // 标题
    private TextView title;
    // 返回按钮
    private ImageView img_back;
    // 左移按钮
    private ImageView img_left;
    // 右移按钮
    private ImageView img_right;
    // 1
    private Button btn1;
    // 2
    private Button btn2;
    // 3
    private Button btn3;
    // 4
    private Button btn4;
    // 5
    private Button btn5;

    // 数据
    private ArrayList<TotalScore> dataList = new ArrayList<>();
    // 更新数据集
    private ArrayList<TotalScore> newDataList = new ArrayList<>();

    //
    private int totalTeams = 7;
    //
    private static int btnInt1 = 0;
    private static int btnInt2 = 1;
    private static int btnInt3 = 2;
    private static int btnInt4 = 3;
    private static int btnInt5 = 4;
    // 第一个按钮的数字 表示第几组
    private static int i = 1;

    // 当前显示第几组 默认第一组
    private static int currentTeam = 0;

    // 进度条
    private ProgressDialog progressDialog;
    private ImageView img_search;
    private EditText et_search;
    // 适配器
    private TotalScoreAdapter adapter;
    // 序号
    private TextView tv_id;
    // 汇总
    private TextView tv_total;
    // 组别
    private TextView tv_team;
    // 项目分析
    private TextView tv_item_analysis;
    // 主题演讲
    private TextView tv_topic_speech;
    // 主题班会
    private TextView tv_topic_meeting;
    // 笔试
    private TextView tv_written_exam;
    // 学号
    private TextView tv_sno;
    // 谈心谈话
    private TextView tv_concact;
    /**
     * 出场顺序
     */
    private TextView tv_appear;
    /**
     * 刷新按钮
     */
    private ImageView iv_refresh;
    private  Button btn_refresh;
    // 刷新动画
    private Animation circle_anim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_score);

        // 获得控件
        getViews();

        // 获得数据
        getData();

        // 放置数据
        setData();

        // 添加监听
        addListener();

    }

    // 判断左移是否合法
    private boolean isLeftMoveValid(){
        if(btnInt2 > 1){
            return true;
        }
        return false;
    }

    // 判断右移是否合法
    private boolean isRightMoveValid(){
        if(btnInt5 < totalTeams){
            return true;
        }
        return false;

    }
    // 左移加
    private void btnLeftMove(){
        btn2.setText("" + (i + 0));
        btn3.setText("" + (i + 1));
        btn4.setText("" + (i + 2));
        btn5.setText("" + (i + 3));

        btnInt2 = i+0;
        btnInt3 = i+1;
        btnInt4 = i+2;
        btnInt5 = i+3;
    }

    // 右移减
    private void btnRightMove(){
        btn2.setText("" + (i + 0));
        btn3.setText("" + (i + 1));
        btn4.setText("" + (i + 2));
        btn5.setText("" + (i + 3));

        btnInt2 = i+0;
        btnInt3 = i+1;
        btnInt4 = i+2;
        btnInt5 = i+3;
    }

    // 设置第几个控件为红色 然后其他按钮的背景为白色
    private void btnBackgroundAdaptive(Button setRed, Button setWhite1, Button setWhite2, Button setWhite3, Button setWhite4){
        setRed.setBackgroundColor(Color.RED);
        setWhite1.setBackgroundColor(Color.WHITE);
        setWhite2.setBackgroundColor(Color.WHITE);
        setWhite3.setBackgroundColor(Color.WHITE);
        setWhite4.setBackgroundColor(Color.WHITE);
    }

    // 清楚按钮背景
    private void clearBtnBg(){
        btn1.setBackgroundColor(Color.WHITE);
        btn2.setBackgroundColor(Color.WHITE);
        btn3.setBackgroundColor(Color.WHITE);
        btn4.setBackgroundColor(Color.WHITE);
        btn5.setBackgroundColor(Color.WHITE);
    }

    // 出现进度条
    private void showProgress(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("数据正在加载...请耐心等待...");
        progressDialog.setMessage("loading...");
        progressDialog.show();
    }

    // 关闭进度条
    private void closeProgress(){
        if(progressDialog != null && progressDialog.isShowing()){
            progressDialog.cancel();
        }
    }

    /**
     * 需排序的按钮字体颜色变成黑色
     */
    private void setTextColorBlack(){
        tv_sno.setTextColor(Color.BLACK);
        tv_id.setTextColor(Color.BLACK);
        tv_team.setTextColor(Color.BLACK);
        tv_written_exam.setTextColor(Color.BLACK);
        tv_topic_meeting.setTextColor(Color.BLACK);
        tv_item_analysis.setTextColor(Color.BLACK);
        tv_concact.setTextColor(Color.BLACK);
        tv_topic_speech.setTextColor(Color.BLACK);
        tv_total.setTextColor(Color.BLACK);
    }

    /**
     * 改变按钮的字体颜色为红色
     * @param view 需要改变颜色的TextView
     */
    private void setTextColorRed(TextView view){
        view.setTextColor(Color.RED);
    }

    /**
     *
     * @param view
     */
    private void changeTextColor(TextView view){
        setTextColorBlack();
        setTextColorRed(view);
    }

    /**
     * 根据更新的数据 按id排序 后显示
     */
    private void sortDataById(){
        newDataList.clear();
        for(int i = 0; i<dataList.size(); ++i){
            if(currentTeam == dataList.get(i).getGroup()){
                newDataList.add(dataList.get(i));
            }
        }

        /**
         * 根据id排序
         */

        /**
        Collections.sort(newDataList, new Comparator<TotalScore>() {
            @Override
            public int compare(TotalScore o1, TotalScore o2) {
                Integer id1 = o1.getOrder();
                Integer id2 = o2.getOrder();
                return id1.compareTo(id2);
            }
        });

         */
        adapter.setNewData(newDataList);
    }


    private void addListener() {
        // 返回
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TotalScoreActivity.this.finish();
            }
        }
        );


        // 左移
        img_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 先判断能不能移动 不能直接返回
                if(!isLeftMoveValid()){
                    ToastUtils.show(TotalScoreActivity.this, "亲 不能再向左移了哦！");
                    return ;
                }
                i--;

                // 可以移动
                // 显示进度条
                showProgress();

                // 改变按钮数字
                btnLeftMove();

                // 加载数据
                // 加载全部
                if(currentTeam == 0){
                    adapter.setNewData(dataList);
                    // 关进度条
                    closeProgress();
                }else{
                    currentTeam--;

                    sortDataById();

                    // 关进度条
                    closeProgress();
            }
        }});

        // 右移
        img_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 先判断能不能移动 不能直接返回
                if(!isRightMoveValid()){
                    ToastUtils.show(TotalScoreActivity.this, "亲 不能再向右移了哦！");
                    return ;
                }

                i++;
                // 可以移动
                // 显示进度条
                showProgress();

                // 加载数据
                btnRightMove();

                // 加载数据
                // 加载全部
                if(currentTeam == 0){
                    adapter.setNewData(dataList);
                    // 关进度条
                    closeProgress();
                }else {
                    currentTeam++;

                    sortDataById();

                   // Log.d("currentteam", currentTeam + "");
                    // Log.d("new data size", newDataList.size() + "");
                    // Log.d("dataList size", dataList.size() + "");



                    // 关进度条
                    closeProgress();
                }
            }
        });

        // 1
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 显示进度条
                showProgress();
                // 加载数据
                btnBackgroundAdaptive(btn1, btn2, btn3, btn4, btn5);

                // 更新数据
                currentTeam = 0;

                adapter.setNewData(dataList);
                setTextColorRed(tv_id);
                // 关进度条
                closeProgress();
            }
        });

        // 2
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 显示进度条
                showProgress();
                // 加载数据
                btnBackgroundAdaptive(btn2, btn1, btn3, btn4, btn5);


                currentTeam = btnInt2;

                sortDataById();
                setTextColorRed(tv_id);


                // 关进度条
                closeProgress();
            }
        });

        // 3
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 显示进度条
                showProgress();
                // 加载数据
                btnBackgroundAdaptive(btn3, btn2, btn1, btn4, btn5);

                currentTeam = btnInt3;

                sortDataById();
                setTextColorRed(tv_id);

                // 关进度条
                closeProgress();
            }
        });

        // 4
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 显示进度条
                showProgress();
                // 加载数据
                btnBackgroundAdaptive(btn4, btn2, btn3, btn1, btn5);

                currentTeam = btnInt4;

                sortDataById();
                setTextColorRed(tv_id);

                // 关进度条
                closeProgress();
            }
        });

        // 5
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 显示进度条
                showProgress();
                // 加载数据
                btnBackgroundAdaptive(btn5, btn2, btn3, btn4, btn1);

                currentTeam = btnInt5;

                sortDataById();
                setTextColorRed(tv_id);

                // 关进度条
                closeProgress();
            }
        });

        // 搜索
        img_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 显示进度条
                showProgress();

                // 获取搜索框的信息
                String info = et_search.getText().toString().trim();
                if (info.equals("")){
                    closeProgress();
                    return ;
                }
                newDataList.clear();

                Log.d("info", info);
                // 请求信息
                for(int i = 0; i < dataList.size(); ++i){
                    if(dataList.get(i).getSno().equals(info) || dataList.get(i).getName().equals(info)){
                        newDataList.add(dataList.get(i));
                    }
                }


                if(newDataList.size() == 0){
                    ToastUtils.show(TotalScoreActivity.this, "没有这样的选手");
                }

                adapter.setNewData(newDataList);

                et_search.setText("");

                setTextColorBlack();
                // 关进度条
                closeProgress();
                // 不成功则吐司提示没有这样的选手
                // ToastUtils.show(ItemTotalScoreActivity.this, "没有这样的选手，请重新检查后再查找！");

            }
        });

        /**
        // 序号
        tv_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentTeam == 0){
                    Collections.sort(dataList, new Comparator<TotalScore>() {
                        @Override
                        public int compare(TotalScore o1, TotalScore o2) {
                            Integer id1 = o1.getOrder();
                            Integer id2 = o2.getOrder();
                            return id1.compareTo(id2);
                        }
                    });

                    adapter.setNewData(dataList);
                }else{
                    Collections.sort(newDataList, new Comparator<TotalScore>() {
                        @Override
                        public int compare(TotalScore o1, TotalScore o2) {
                            Integer id1 = o1.getOrder();
                            Integer id2 = o2.getOrder();
                            return id1.compareTo(id2);
                        }
                    });
                    adapter.setNewData(newDataList);
                }
                changeTextColor(tv_id);
            }
        });
         */

        // 学号
        tv_sno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentTeam == 0){
                    Collections.sort(dataList, new Comparator<TotalScore>() {
                        @Override
                        public int compare(TotalScore o1, TotalScore o2) {
                            String id1 = o1.getSno();
                            String id2 = o2.getSno();
                            return id1.compareTo(id2);
                        }
                    });
                    adapter.setNewData(dataList);
                }else{
                    Collections.sort(newDataList, new Comparator<TotalScore>() {
                        @Override
                        public int compare(TotalScore o1, TotalScore o2) {
                            String id1 = o1.getSno();
                            String id2 = o2.getSno();
                            return id1.compareTo(id2);
                        }
                    });
                    adapter.setNewData(newDataList);
                }
                changeTextColor(tv_sno);
            }
        });

        // 组别
        tv_team.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentTeam == 0){
                    Collections.sort(dataList, new Comparator<TotalScore>() {
                        @Override
                        public int compare(TotalScore o1, TotalScore o2) {
                            Integer id1 = o1.getGroup();
                            Integer id2 = o2.getGroup();
                            return id1.compareTo(id2);
                        }
                    });
                    adapter.setNewData(dataList);

                }else{
                    Collections.sort(newDataList, new Comparator<TotalScore>() {
                        @Override
                        public int compare(TotalScore o1, TotalScore o2) {
                            Integer id1 = o1.getGroup();
                            Integer id2 = o2.getGroup();
                            return id1.compareTo(id2);
                        }
                    });
                    adapter.setNewData(newDataList);
                }
                changeTextColor(tv_team);
            }
        });

        // 笔试
        tv_written_exam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentTeam == 0){
                    Collections.sort(dataList, new Comparator<TotalScore>() {
                        @Override
                        public int compare(TotalScore o1, TotalScore o2) {
                            Double id1 = o1.getWritten_exam();
                            Double id2 = o2.getWritten_exam();
                            return id1.compareTo(id2);
                        }
                    });
                    adapter.setNewData(dataList);
                }else{
                    Collections.sort(newDataList, new Comparator<TotalScore>() {
                        @Override
                        public int compare(TotalScore o1, TotalScore o2) {
                            Double id1 = o1.getWritten_exam();
                            Double id2 = o2.getWritten_exam();
                            return id1.compareTo(id2);
                        }
                    });
                    adapter.setNewData(newDataList);
                }
                changeTextColor(tv_written_exam);
            }
        });

        // 主题班会
        tv_topic_meeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentTeam == 0){
                    Collections.sort(dataList, new Comparator<TotalScore>() {
                        @Override
                        public int compare(TotalScore o1, TotalScore o2) {
                            Double id1 = o1.getTopic_meeting();
                            Double id2 = o2.getTopic_meeting();
                            return id1.compareTo(id2);
                        }
                    });

                    adapter.setNewData(dataList);
                }else{
                    Collections.sort(newDataList, new Comparator<TotalScore>() {
                        @Override
                        public int compare(TotalScore o1, TotalScore o2) {
                            Double id1 = o1.getTopic_meeting();
                            Double id2 = o2.getTopic_meeting();
                            return id1.compareTo(id2);
                        }
                    });
                    adapter.setNewData(newDataList);
                }
                changeTextColor(tv_topic_meeting);
            }
        });

        // 案例分析
        tv_item_analysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentTeam == 0){
                    Collections.sort(dataList, new Comparator<TotalScore>() {
                        @Override
                        public int compare(TotalScore o1, TotalScore o2) {
                            Double id1 = o1.getItem_analysis();
                            Double id2 = o2.getItem_analysis();
                            return id1.compareTo(id2);
                        }
                    });
                    adapter.setNewData(dataList);

                }else{
                    Collections.sort(newDataList, new Comparator<TotalScore>() {
                        @Override
                        public int compare(TotalScore o1, TotalScore o2) {
                            Double id1 = o1.getItem_analysis();
                            Double id2 = o2.getItem_analysis();
                            return id1.compareTo(id2);
                        }
                    });
                    adapter.setNewData(newDataList);
                }
                changeTextColor(tv_item_analysis);
            }
        });

        // 谈心谈话
        tv_concact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentTeam == 0){
                    Collections.sort(dataList, new Comparator<TotalScore>() {
                        @Override
                        public int compare(TotalScore o1, TotalScore o2) {
                            Double id1 = o1.getConcact();
                            Double id2 = o2.getConcact();
                            return id1.compareTo(id2);
                        }
                    });
                    adapter.setNewData(dataList);

                }else{
                    Collections.sort(newDataList, new Comparator<TotalScore>() {
                        @Override
                        public int compare(TotalScore o1, TotalScore o2) {
                            Double id1 = o1.getConcact();
                            Double id2 = o2.getConcact();
                            return id1.compareTo(id2);
                        }
                    });
                    adapter.setNewData(newDataList);
                }
                changeTextColor(tv_concact);
            }
        });

        // 主题演讲
        tv_topic_speech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentTeam == 0){
                    Collections.sort(dataList, new Comparator<TotalScore>() {
                        @Override
                        public int compare(TotalScore o1, TotalScore o2) {
                            Double id1 = o1.getTopic_speech();
                            Double id2 = o2.getTopic_speech();
                            return id1.compareTo(id2);
                        }
                    });
                    adapter.setNewData(dataList);

                }else{
                    Collections.sort(newDataList, new Comparator<TotalScore>() {
                        @Override
                        public int compare(TotalScore o1, TotalScore o2) {
                            Double id1 = o1.getTopic_speech();
                            Double id2 = o2.getTopic_speech();
                            return id1.compareTo(id2);
                        }
                    });

                    adapter.setNewData(newDataList);
                }
                changeTextColor(tv_topic_speech);
            }
        });

        // 汇总
        tv_total.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentTeam == 0){
                    Collections.sort(dataList, new Comparator<TotalScore>() {
                        @Override
                        public int compare(TotalScore o1, TotalScore o2) {
                            Double id1 = o1.getTotal_score();
                            Double id2 = o2.getTotal_score();
                            return id1.compareTo(id2);
                        }
                    });

                    adapter.setNewData(dataList);
                }else{
                    Collections.sort(newDataList, new Comparator<TotalScore>() {
                        @Override
                        public int compare(TotalScore o1, TotalScore o2) {
                            Double id1 = o1.getTotal_score();
                            Double id2 = o2.getTotal_score();
                            return id1.compareTo(id2);
                        }
                    });
                    adapter.setNewData(newDataList);
                }

                changeTextColor(tv_total);
            }
        });

        /**
        //
        tv_appear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentTeam == 0){
                    Collections.sort(dataList, new Comparator<TotalScore>() {
                        @Override
                        public int compare(TotalScore o1, TotalScore o2) {
                            Integer id1 = o1.getAppear();
                            Integer id2 = o2.getAppear();
                            return id1.compareTo(id2);
                        }
                    });

                    adapter.setNewData(dataList);
                }else{
                    Collections.sort(newDataList, new Comparator<TotalScore>() {
                        @Override
                        public int compare(TotalScore o1, TotalScore o2) {
                            Integer id1 = o1.getAppear();
                            Integer id2 = o2.getAppear();
                            return id1.compareTo(id2);
                        }
                    });
                    adapter.setNewData(newDataList);
                }

                changeTextColor(tv_appear);
            }
        });
         */


        btn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendRequestWithOkHttpTotalScore(Url.URL_BASIC + Url.URL_TOTAL_SCORE);
                refreshButtonRotate();
            }
        });

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
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.setNewData(dataList);
                stopRefreshButtonRotate();
                btnBackgroundAdaptive(btn1, btn2, btn3, btn4, btn5);
            }
        });
    }

    /**
     * 刷新按钮旋转
     */
    private void refreshButtonRotate(){
        circle_anim = AnimationUtils.loadAnimation(TotalScoreActivity.this, R.anim.tip);
        LinearInterpolator linearInterpolator = new LinearInterpolator();
        AccelerateInterpolator accelerateInterpolator = new AccelerateInterpolator();
        circle_anim.setInterpolator(linearInterpolator);
        if(circle_anim != null){
            iv_refresh.startAnimation(circle_anim);
        }
    }

    /**
     * 按钮停止
     */
    private void stopRefreshButtonRotate(){
        iv_refresh.clearAnimation();
    }

    /**
     *
     */
    private void setData() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(linearLayoutManager);
        adapter = new TotalScoreAdapter(dataList);
        rv.setAdapter(adapter);

        title.setText(MyApplication.competitionName);

        // 加载数据
        btnBackgroundAdaptive(btn1, btn2, btn3, btn4, btn5);

        // 将 序号按钮字体颜色设成 红色
        changeTextColor(tv_id);
    }

    private void getData() {
        // TotalScoreTestdata.getData(dataList);
        Intent intent = getIntent();
        dataList = (ArrayList<TotalScore>) intent.getSerializableExtra("data");
    }

    private void getViews() {
        rv = (RecyclerView)findViewById(R.id.rv_total_score);
        title = (TextView)findViewById(R.id.tv_title_total_score);
        img_back = (ImageView)findViewById(R.id.img_back_total_score);

        img_left = (ImageView)findViewById(R.id.img_left_total_score);
        img_right = (ImageView)findViewById(R.id.img_right_total_score);

        btn1  = (Button)findViewById(R.id.btn1_total_score);
        btn2  = (Button)findViewById(R.id.btn2_total_score);
        btn3  = (Button)findViewById(R.id.btn3_total_score);
        btn4  = (Button)findViewById(R.id.btn4_total_score);
        btn5  = (Button)findViewById(R.id.btn5_total_score);

        et_search = (EditText)findViewById(R.id.et_total_score);
        img_search = (ImageView)findViewById(R.id.img_search_total_score);

        // 按钮
        tv_id = (TextView)findViewById(R.id.tv_sort_id_total_score);
        tv_sno = (TextView)findViewById(R.id.tv_sort_sno_total_score);
        tv_team = (TextView)findViewById(R.id.tv_sort_team_total_score);
        tv_written_exam = (TextView)findViewById(R.id.tv_sort_written_examization_total_score);
        tv_topic_meeting = (TextView)findViewById(R.id.tv_sort_topic_meeting_total_score);
        tv_item_analysis = (TextView)findViewById(R.id.tv_sort_item_analysis_total_score);
        tv_concact = (TextView)findViewById(R.id.tv_sort_concact_total_score);
        tv_topic_speech = (TextView)findViewById(R.id.tv_topic_speech_total_score);
        tv_total = (TextView)findViewById(R.id.tv_sort_total_total_score);
        // tv_appear = (TextView)findViewById(R.id.tv_appear_team_total_score);

        /**
         * 刷新按钮
         */
        iv_refresh = (ImageView)findViewById(R.id.iv_refresh_total_score);
        btn_refresh = (Button)findViewById(R.id.btn_refresh_total_score);
    }
}
