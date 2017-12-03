package counselorcompetitionsystem.xioalian.com.counselorcompetitionsystem;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.TimeUnit;

import counselorcompetitionsystem.xioalian.com.counselorcompetitionsystem.adapter.ItemScoreAdapter;
import counselorcompetitionsystem.xioalian.com.counselorcompetitionsystem.bean.ItemScore;
import counselorcompetitionsystem.xioalian.com.counselorcompetitionsystem.url.Url;
import counselorcompetitionsystem.xioalian.com.counselorcompetitionsystem.utils.ToastUtils;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ItemTotalScoreActivity extends BasicActivity{
    // 显示数据列表
    private RecyclerView rv;
    // 排序数据
    private ArrayList<ItemScore> newDataList = new ArrayList<>();
    // 当前项目名称
    private String currentItemName ;
    // 返回按钮
    private ImageView img_back_item_total_score;
    // 比赛标题
    private TextView tv_title_item_total_score;
    // 项目标题
    private TextView tv_item_name_item_total_score;
    // 左移按钮
    private ImageView left_move;
    // 右移按钮
    private ImageView right_move;
    // 查看 第1组
    private Button btn1;
    // 查看 第2组
    private Button btn2;
    // 查看 第3组
    private Button btn3;
    // 查看 第4组
    private Button btn4;
    // 查看 第5组
    private Button btn5;
    // 总的队伍数
    private int totalTeams = 7;

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
    // 搜索内容
    private EditText et_search;
    // 搜索按钮
    private ImageView img_search;
    // 列表显示适配器
    private ItemScoreAdapter adapter;
    // 序号
    private TextView tv_id;
    // 学号
    private TextView tv_sno;
    // 队伍
    private TextView tv_team;
    // 出场
    private TextView tv_appear;
    // A1 分数
    private TextView tv_A1;
    // A2 分数
    private TextView tv_A2;
    // A3 分数
    private TextView tv_A3;
    // 平均分
    private TextView tv_average;
    // 标准分
    private TextView tv_standard;
    /**
     * 刷新
     */
    private Button btn_refresh;
    private ImageView iv_refresh;

    private ArrayList<ItemScore> dataList = new ArrayList<>();
    // 刷新动画
    private Animation circle_anim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_total_score);

        // 获得控件
        getViews();

        // 获取数据
        getdata();

        // 设置数据
        setData();

        // 加监听
        addListener();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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

    // 弹框显示备注
    public void showRemark(int position){

        ItemScore itemScore = dataList.get(position);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(itemScore.getSno() + " 的备注：");
        alert.setMessage(itemScore.getDescription());

        alert.setCancelable(false).create();
        alert.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alert.show();

    }

    /**
     *  出现进度条
     */
    private void showProgress(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("数据正在加载...请耐心等待...");
        progressDialog.setMessage("loading...");
        progressDialog.show();
    }

    /**
     *  关闭进度条
     */

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
        tv_appear.setTextColor(Color.BLACK);
        tv_A1.setTextColor(Color.BLACK);
        tv_A2.setTextColor(Color.BLACK);
        tv_A3.setTextColor(Color.BLACK);
        tv_average.setTextColor(Color.BLACK);
        tv_standard.setTextColor(Color.BLACK);
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
        Collections.sort(newDataList, new Comparator<ItemScore>() {
            @Override
            public int compare(ItemScore o1, ItemScore o2) {
                Integer id1 = o1.getOrder();
                Integer id2 = o2.getOrder();
                return id1.compareTo(id2);
            }
        });
         */
        adapter.setNewData(newDataList);

    }

    /**
     * 添加监听
     */
    private void addListener() {

        // 返回
        img_back_item_total_score.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemTotalScoreActivity.this.finish();
            }
        });

        // 左移按钮
        left_move.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 先判断能不能移动 不能直接返回
                if(!isLeftMoveValid()){
                    ToastUtils.show(ItemTotalScoreActivity.this, "亲 不能再向左移了哦");
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
                    changeTextColor(tv_id);

                    // 关进度条
                    closeProgress();
                }
            }
        });

        // 右移按钮
        right_move.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 先判断能不能移动 不能直接返回
                if(!isRightMoveValid()){
                    ToastUtils.show(ItemTotalScoreActivity.this, "亲 不能再向右移了哦");
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
                }else{
                    currentTeam++;
                    sortDataById();
                    changeTextColor(tv_id);
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
                // 调整界面
                btnBackgroundAdaptive(btn1, btn2, btn3, btn4, btn5);
                // 更新数据
                currentTeam = 0;

                /**
                Collections.sort(dataList, new Comparator<ItemScore>() {
                    @Override
                    public int compare(ItemScore o1, ItemScore o2) {
                        Integer id1 = o1.getOrder();
                        Integer id2 = o2.getOrder();
                        return id1.compareTo(id2);
                    }
                });
                 */


                adapter.setNewData(dataList);
                changeTextColor(tv_id);

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
                // 调整界面
                btnBackgroundAdaptive(btn2, btn1, btn3, btn4, btn5);
                // 更新数据


                currentTeam = btnInt2;

                sortDataById();
                changeTextColor(tv_id);
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
                // 调整界面
                btnBackgroundAdaptive(btn3, btn2, btn1, btn4, btn5);
                //

                currentTeam = btnInt3;

                sortDataById();
                changeTextColor(tv_id);
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
                // 调整界面
                btnBackgroundAdaptive(btn4, btn2, btn3, btn1, btn5);
                //

                currentTeam = btnInt4;

                sortDataById();
                changeTextColor(tv_id);
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
                // 调整界面
                btnBackgroundAdaptive(btn5, btn2, btn3, btn4, btn1);

                currentTeam = btnInt5;

                sortDataById();
                changeTextColor(tv_id);
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
                    ToastUtils.show(ItemTotalScoreActivity.this, "没有这样的选手");
                }

                adapter.setNewData(newDataList);

                et_search.setText("");

                /**
                 * 所有的排序按钮颜色都设置成黑色
                 */
                setTextColorBlack();

                // 关进度条
                closeProgress();
                // 不成功则吐司提示没有这样的选手
                // ToastUtils.show(ItemTotalScoreActivity.this, "没有这样的选手，请重新检查后再查找！");

            }
        });



        // 学号排名
        tv_sno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 更换排序后的数据
                 */
                if(currentTeam == 0) {
                    Collections.sort(dataList, new Comparator<ItemScore>() {
                        @Override
                        public int compare(ItemScore o1, ItemScore o2) {
                            String s1 = o1.getSno();
                            String s2 = o2.getSno();
                            return s1.compareTo(s2);
                        }
                    });

                    adapter.setNewData(dataList);
                }else{
                    Collections.sort(newDataList, new Comparator<ItemScore>() {
                        @Override
                        public int compare(ItemScore o1, ItemScore o2) {
                            String s1 = o1.getSno();
                            String s2 = o2.getSno();
                            return s1.compareTo(s2);
                        }
                    });

                    adapter.setNewData(newDataList);
                }

                /**
                 *  将相应排序项的字体颜色变为红色
                 */
                changeTextColor(tv_sno);
            }
        });

        // 组别排名
        tv_team.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 更换排序后的数据
                 */
                if(currentTeam == 0) {

                    Collections.sort(dataList, new Comparator<ItemScore>() {
                        @Override
                        public int compare(ItemScore o1, ItemScore o2) {
                            Integer team_1 = o1.getGroup();
                            Integer team_2 = o2.getGroup();
                            return team_1.compareTo(team_2);
                        }
                    });


                    adapter.setNewData(dataList);
                }else{

                    Collections.sort(newDataList, new Comparator<ItemScore>() {
                        @Override
                        public int compare(ItemScore o1, ItemScore o2) {
                            Integer team_1 = o1.getGroup();
                            Integer team_2 = o2.getGroup();
                            return team_1.compareTo(team_2);
                        }
                    });
                    adapter.setNewData(newDataList);
                }
                /**
                 *  将相应排序项的字体颜色变为红色
                 */
                changeTextColor(tv_team);

            }
        });



        // A1 排名
        tv_A1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /**
                 * 更换排序后的数据
                 */
                if(currentTeam == 0) {

                    Collections.sort(dataList, new Comparator<ItemScore>() {
                        @Override
                        public int compare(ItemScore o1, ItemScore o2) {
                            Double f1 = o1.getA1();
                            Double f2 = o2.getA1();
                            return f1.compareTo(f2);
                        }
                    });

                    adapter.setNewData(dataList);
                }else{

                    Collections.sort(newDataList, new Comparator<ItemScore>() {
                        @Override
                        public int compare(ItemScore o1, ItemScore o2) {
                            Double f1 = o1.getA1();
                            Double f2 = o2.getA1();
                            return f1.compareTo(f2);
                        }
                    });

                    adapter.setNewData(newDataList);
                }

                /**
                 *  将相应排序项的字体颜色变为红色
                 */
                changeTextColor(tv_A1);

            }
        });

        // A2 排名
        tv_A2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /**
                 * 更换排序后的数据
                 */

                if(currentTeam == 0) {


                    Collections.sort(dataList, new Comparator<ItemScore>() {
                        @Override
                        public int compare(ItemScore o1, ItemScore o2) {
                            Double f1 = o1.getA2();
                            Double f2 = o2.getA2();
                            return f1.compareTo(f2);
                        }
                    });


                    adapter.setNewData(dataList);
                }else{

                    Collections.sort(newDataList, new Comparator<ItemScore>() {
                        @Override
                        public int compare(ItemScore o1, ItemScore o2) {
                            Double f1 = o1.getA2();
                            Double f2 = o2.getA2();
                            return f1.compareTo(f2);
                        }
                    });


                    adapter.setNewData(newDataList);
                }

                /**
                 *  将相应排序项的字体颜色变为红色
                 */
                changeTextColor(tv_A2);
            }
        });

        // A3 排名
        tv_A3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 更换排序后的数据
                 */
                if(currentTeam == 0) {

                    Collections.sort(dataList, new Comparator<ItemScore>() {
                        @Override
                        public int compare(ItemScore o1, ItemScore o2) {
                            Double f1 = o1.getA3();
                            Double f2 = o2.getA3();
                            return f1.compareTo(f2);
                        }
                    });

                    adapter.setNewData(dataList);
                }else{

                    Collections.sort(newDataList, new Comparator<ItemScore>() {
                        @Override
                        public int compare(ItemScore o1, ItemScore o2) {
                            Double f1 = o1.getA3();
                            Double f2 = o2.getA3();
                            return f1.compareTo(f2);
                        }
                    });

                    adapter.setNewData(newDataList);
                }

                /**
                 *  将相应排序项的字体颜色变为红色
                 */
                changeTextColor(tv_A3);

            }
        });

        // 平均分 排名
        tv_average.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 更换排序后的数据
                 */

                if(currentTeam == 0) {
                    Collections.sort(dataList, new Comparator<ItemScore>() {
                        @Override
                        public int compare(ItemScore o1, ItemScore o2) {
                            Double a1 = o1.getAvg_score();
                            Double a2 = o2.getAvg_score();
                            return a1.compareTo(a2);
                        }
                    });

                    adapter.setNewData(dataList);
                }else{

                    Collections.sort(newDataList, new Comparator<ItemScore>() {
                        @Override
                        public int compare(ItemScore o1, ItemScore o2) {
                            Double a1 = o1.getAvg_score();
                            Double a2 = o2.getAvg_score();
                            return a1.compareTo(a2);
                        }
                    });

                    adapter.setNewData(newDataList);
                }

                /**
                 *  将相应排序项的字体颜色变为红色
                 */
                changeTextColor(tv_average);
            }
        });

        // 标准分 排名
        tv_standard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 更换排序后的数据
                 */
                if(currentTeam == 0) {

                    Collections.sort(dataList, new Comparator<ItemScore>() {
                        @Override
                        public int compare(ItemScore o1, ItemScore o2) {
                            Double s1 = o1.getStandard_acore();
                            Double s2 = o2.getStandard_acore();
                            return s1.compareTo(s2);
                        }
                    });

                    adapter.setNewData(dataList);
                }else{

                    Collections.sort(newDataList, new Comparator<ItemScore>() {
                        @Override
                        public int compare(ItemScore o1, ItemScore o2) {
                            Double s1 = o1.getStandard_acore();
                            Double s2 = o2.getStandard_acore();
                            return s1.compareTo(s2);
                        }
                    });

                    adapter.setNewData(newDataList);
                }

                /**
                 *  将相应排序项的字体颜色变为红色
                 */

                changeTextColor(tv_standard);


            }
        });

        // 刷新
        btn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                RequestBody formBody = new FormBody.Builder()
                        .add("activeName", currentItemName)
                        .build();
                sendRequestWithOkHttpItemTotalScore(Url.URL_BASIC + Url.URL_ITEM_TOTAL_SCORE, formBody);
                startRefreshAnim();
            }
        });




    }

    /**
     * 开始动画
     */
    private void startRefreshAnim(){
        circle_anim = AnimationUtils.loadAnimation(ItemTotalScoreActivity.this, R.anim.tip);
        LinearInterpolator linearInterpolator = new LinearInterpolator();
        circle_anim.setInterpolator(linearInterpolator);
        if(circle_anim != null){
            iv_refresh.startAnimation(circle_anim);
        }
    }

    /**
     * 结束动画
     */
    private void endRefreshAnim(){
        if(circle_anim != null){
            iv_refresh.clearAnimation();
        }
    }

    private void setData() {

        tv_title_item_total_score.setText(MyApplication.competitionName);
        tv_item_name_item_total_score.setText(currentItemName);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(linearLayoutManager);
        adapter = new ItemScoreAdapter(dataList, this);
        rv.setAdapter(adapter);

        btnBackgroundAdaptive(btn1, btn2, btn3, btn4, btn5);

        changeTextColor(tv_id);


    }

    /**
     * 仲裁查看分项目成绩
     */
    private void sendRequestWithOkHttpItemTotalScore(final String url, final RequestBody formBody) {
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
                        if(responseData.equals("") || responseData.isEmpty()){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtils.show(ItemTotalScoreActivity.this, "刷新失败,请重新刷新!", Toast.LENGTH_SHORT);
                                }
                            });
                            return ;
                        }


                        Log.i("tag", responseData);
                        // 解析数据
                        parseJsonWithJSONObjectItemTotalScore(responseData);
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

    private void parseJsonWithJSONObjectItemTotalScore(String responseData){
        Gson gson = new Gson();
        dataList = gson.fromJson(responseData, new TypeToken<ArrayList<ItemScore>>(){}.getType());

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.setNewData(dataList);
                endRefreshAnim();
                btnBackgroundAdaptive(btn1, btn2, btn3, btn4, btn5);
            }
        });


    }

    private void getdata() {
        Intent intent = getIntent();
        currentItemName = intent.getStringExtra("itemName");
        dataList = (ArrayList<ItemScore>) intent.getSerializableExtra("data");
    }

    private void getViews() {
        rv = (RecyclerView) findViewById(R.id.rv_item_total_score);
        img_back_item_total_score = (ImageView) findViewById(R.id.img_back_item_total_score);
        tv_title_item_total_score = (TextView)findViewById(R.id.tv_title_item_total_score);
        tv_item_name_item_total_score = (TextView)findViewById(R.id.tv_item_name_item_total_score);

        left_move = (ImageView)findViewById(R.id.iv_left_move_item_total_score);
        right_move = (ImageView)findViewById(R.id.iv_right_move_item_total_score);
        btn1 = (Button)findViewById(R.id.btn1_item_total_score);
        btn2 = (Button)findViewById(R.id.btn2_item_total_score);
        btn3 = (Button)findViewById(R.id.btn3_item_total_score);
        btn4 = (Button)findViewById(R.id.btn4_item_total_score);
        btn5 = (Button)findViewById(R.id.btn5_item_total_score);

        et_search = (EditText)findViewById(R.id.et_search_item_total_score);
        img_search = (ImageView)findViewById(R.id.img_search_item_total_score);


        // 按钮
        tv_id = (TextView)findViewById(R.id.tv_sort_id_item_total_score);
        tv_sno = (TextView)findViewById(R.id.tv_sort_sno_total_score);
        tv_team = (TextView)findViewById(R.id.tv_sort_team_total_score);
        tv_appear = (TextView)findViewById(R.id.tv_sort_appear_item_total_score);
        tv_A1 = (TextView)findViewById(R.id.tv_sort_A1_item_total_score);
        tv_A2 = (TextView)findViewById(R.id.tv_sort_A2_item_total_score);
        tv_A3 = (TextView)findViewById(R.id.tv_sort_A3_item_total_score);
        tv_average = (TextView)findViewById(R.id.tv_sort_average_item_total_score);
        tv_standard = (TextView)findViewById(R.id.tv_sort_standard_item_total_score);

        /**
         * 刷新
         */
        btn_refresh = (Button)findViewById(R.id.btn_refresh_item_total_score);
        iv_refresh = (ImageView)findViewById(R.id.iv_refresh_item_total_score);
    }


}
