package counselorcompetitionsystem.xioalian.com.counselorcompetitionsystem;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import counselorcompetitionsystem.xioalian.com.counselorcompetitionsystem.adapter.JudgeScoreAdapter;
import counselorcompetitionsystem.xioalian.com.counselorcompetitionsystem.bean.JudgeItemScore;
import counselorcompetitionsystem.xioalian.com.counselorcompetitionsystem.testdata.JudgeScoreTestData;
import counselorcompetitionsystem.xioalian.com.counselorcompetitionsystem.utils.ToastUtils;

public class JudgeScoreActivity extends AppCompatActivity {
    // 返回
    private ImageView img_back;
    // 比赛名称
    private TextView tv_title;
    // 项目名称
    private TextView tv_item_name;
    // 显示列表
    private RecyclerView rv;

    // 数据
    private ArrayList<JudgeItemScore> dataList = new ArrayList<>();
    // 当前项目名称
    private String currentItemName;
    // 备注
    private TextView tv_remark;
    // 编号排序按钮
    private Button btn_id_sort;
    // 我的打分排序按钮
    private Button btn_myscore_sort;
    // 适配器
    private JudgeScoreAdapter adapter;

    // 排序编号
    public final static int ID_SORT = 100;
    public final static int SCORE_SORT = 101;
    public static int current_sort = ID_SORT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_judge_score);

        // 获取控件
        getViews();

        // 获取数据
        getdata();

        // 放入数据
        setData();

        // 添加监听
        addListener();

    }



    private void addListener() {
        // 返回
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JudgeScoreActivity.this.finish();
            }
        });

        // 编号排序
        btn_id_sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 调整数据
                Collections.sort(dataList, new Comparator<JudgeItemScore>() {
                    @Override
                    public int compare(JudgeItemScore o1, JudgeItemScore o2) {
                        Integer id1 = o1.getAppear();
                        Integer id2 = o2.getAppear();
                        return id1.compareTo(id2);
                    }

                });

                for(int i = 0; i< dataList.size(); ++i ){
                    Log.i("tag", dataList.get(i).getAppear() + "");
                }


                // newData1 = new ArrayList<>();
                // newData1.addAll(dataList);
                adapter.setNewData(dataList);

                setTextColorBlack();
                setIDTextColorRed();

            }
        });

        // 我的打分排序
        btn_myscore_sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Collections.sort(dataList, new Comparator<JudgeItemScore>() {
                   @Override
                   public int compare(JudgeItemScore o1, JudgeItemScore o2) {
                       Double score1 = o1.getScore();
                       Double score2 = o2.getScore();
                       return score1.compareTo(score2);
                   }
               });

                for(int i = 0; i< dataList.size(); ++i ){
                    Log.i("tag", dataList.get(i).getAppear() + "");
                }

                adapter.setNewData(dataList);

                setTextColorBlack();
                setScoreTextColorRed();

            }
        });

    }

    public void setTextColorBlack(){
        btn_id_sort.setTextColor(Color.BLACK);
        btn_myscore_sort.setTextColor(Color.BLACK);
    }

    public void setIDTextColorRed(){
        btn_id_sort.setTextColor(Color.RED);
    }

    /**
     * 将打分按钮设置成红色
     */
    public void setScoreTextColorRed(){
        btn_myscore_sort.setTextColor(Color.RED);
    }

    private void setData() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rv.setLayoutManager(manager);
        adapter = new JudgeScoreAdapter(dataList);
        rv.setAdapter(adapter);

        tv_title.setText(MyApplication.competitionName);
        tv_item_name.setText(currentItemName + "比赛");

        setIDTextColorRed();
    }

    /**
     * 获取数据
     */
    private void getdata() {
        // JudgeScoreTestData.getTestData(dataList);

        Intent intent = getIntent();
        currentItemName = intent.getStringExtra("itemName");
        dataList = (ArrayList<JudgeItemScore>) intent.getSerializableExtra("data");
    }

    private void getViews() {
        img_back = (ImageView)findViewById(R.id.img_back_item_judge_score);
        tv_title = (TextView)findViewById(R.id.tv_judge_score_title);
        tv_remark = (TextView)findViewById(R.id.tv_item_rv_judge_score_remark);
        rv = (RecyclerView)findViewById(R.id.rv_judge_score);
        tv_item_name = (TextView)findViewById(R.id.tv_judge_score_item_name);

        //
        btn_id_sort = (Button)findViewById(R.id.btn_id_judge_score_activity);

        //
        btn_myscore_sort = (Button)findViewById(R.id.btn_myscore_judge_score_activity);


    }


}
