package counselorcompetitionsystem.xioalian.com.counselorcompetitionsystem.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;

import counselorcompetitionsystem.xioalian.com.counselorcompetitionsystem.ItemTotalScoreActivity;
import counselorcompetitionsystem.xioalian.com.counselorcompetitionsystem.R;
import counselorcompetitionsystem.xioalian.com.counselorcompetitionsystem.bean.ItemScore;

/**
 * Created by xiaolian on 05/11/2017.
 */

public class ItemScoreAdapter extends RecyclerView.Adapter<ItemScoreAdapter.ViewHolder> {

    private ArrayList<ItemScore> mDataList;
    private ItemTotalScoreActivity context;

    public ItemScoreAdapter(ArrayList<ItemScore> dataList, ItemTotalScoreActivity context) {
        mDataList = dataList;
        this.context = context;
    }

    @Override
    public ItemScoreAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_total_score_layout, parent, false);
        ItemScoreAdapter.ViewHolder holder = new ItemScoreAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ItemScoreAdapter.ViewHolder holder, final int position) {
        DecimalFormat df = new DecimalFormat("#.00");
        final ItemScore itemScore = mDataList.get(position);
        holder.order.setText("" + (position + 1) );
        holder.sno.setText("" + itemScore.getSno());
        holder.team.setText("" + itemScore.getGroup());
        holder.username.setText("" + itemScore.getName());
        holder.appear.setText("" + itemScore.getAppear());

        holder.judge1Score.setText("" + df.format(itemScore.getA1()));


        holder.judge2Score.setText("" + df.format(itemScore.getA2()));
        holder.judge3Score.setText("" + df.format(itemScore.getA3()));
        holder.average.setText("" + df.format(itemScore.getAvg_score()));
        holder.standardScore.setText("" + df.format(itemScore.getStandard_acore()));
        holder.gender.setText("" + itemScore.getGender());
        holder.school.setText("" + itemScore.getCollege());
        holder.remark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 提示界面弹窗
                    context.showRemark(position);
                }
        });
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // 性别
        public TextView gender;
        // 学校
        public TextView school;
        // 序号
        public TextView order;
        // 学号
        public TextView sno;
        // 组别
        public TextView team;
        // 名字
        public TextView username;
        // 出场号
        public TextView appear;
        // 评委1 打分
        public TextView judge1Score;
        // 评委2 打分
        public TextView judge2Score;
        // 评委3 打分
        public TextView judge3Score;
        // 平均分
        public TextView average;
        // 标准分
        public TextView standardScore;
        // 排名
        public TextView ranking;
        // 备注
        public Button remark;

        public ViewHolder(View itemView) {
            super(itemView);
            order = (TextView) itemView.findViewById(R.id.tv_order_item_total_score);
            sno = (TextView) itemView.findViewById(R.id.tv_sno_item_total_score);
            team = (TextView) itemView.findViewById(R.id.tv_team_item_total_score);
            username = (TextView) itemView.findViewById(R.id.tv_name_item_total_score);
            appear = (TextView) itemView.findViewById(R.id.tv_appear_item_total_score);
            judge1Score = (TextView) itemView.findViewById(R.id.tv_A1_item_total_score);
            judge2Score = (TextView) itemView.findViewById(R.id.tv_A2_item_total_score);
            judge3Score = (TextView) itemView.findViewById(R.id.tv_A3_item_total_score);
            average = (TextView) itemView.findViewById(R.id.tv_average_item_total_score);
            standardScore = (TextView) itemView.findViewById(R.id.tv_criterion_item_total_score);
            remark = (Button) itemView.findViewById(R.id.btn_remark_item_total_score);
            school = (TextView) itemView.findViewById(R.id.tv_school_item_total_score);
            gender = (TextView) itemView.findViewById(R.id.tv_gender_item_total_score);

        }

    }
        /**
         * change the data
         *
         * @param dataList
         */
        public void setNewData(ArrayList<ItemScore> dataList) {
            mDataList = dataList;
            notifyDataSetChanged();
        }
    }

