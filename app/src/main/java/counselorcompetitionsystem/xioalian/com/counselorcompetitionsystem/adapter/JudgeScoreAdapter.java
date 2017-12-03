package counselorcompetitionsystem.xioalian.com.counselorcompetitionsystem.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import counselorcompetitionsystem.xioalian.com.counselorcompetitionsystem.JudgeScoreActivity;
import counselorcompetitionsystem.xioalian.com.counselorcompetitionsystem.R;
import counselorcompetitionsystem.xioalian.com.counselorcompetitionsystem.bean.JudgeItemScore;
import counselorcompetitionsystem.xioalian.com.counselorcompetitionsystem.utils.ToastUtils;


/**
 * Created by xiaolian on 04/11/2017.
 */

/**
 *
 *
 *
 */
public class JudgeScoreAdapter extends RecyclerView.Adapter<JudgeScoreAdapter.ViewHolder> {

    private List<JudgeItemScore> mDataList;


    public JudgeScoreAdapter(List<JudgeItemScore> dataList){
        this.mDataList = dataList;
    }

    @Override
    public JudgeScoreAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_judge_score, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(JudgeScoreAdapter.ViewHolder holder, int position) {
        DecimalFormat df = new DecimalFormat("#.00");
        JudgeItemScore judgeItemScore = mDataList.get(position);
        holder.id.setText(""+ judgeItemScore.getAppear());
        holder.myScore.setText("" + df.format(judgeItemScore.getScore()));
        if(judgeItemScore.getRemark() == null) {
            holder.remark.setText("");
        }else{
            holder.remark.setText("" + judgeItemScore.getRemark());
        }
        holder.gender.setText("" + judgeItemScore.getGender());
        holder.team.setText("" + judgeItemScore.getGroup());
        Log.d("judgescore", "isoutput");
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView team;
        TextView gender;
        TextView myScore;
        TextView remark;
        TextView id;

        public ViewHolder(View itemView) {
            super(itemView);
            id = (TextView) itemView.findViewById(R.id.tv_item_rv_judge_score_id);
            myScore = (TextView) itemView.findViewById(R.id.tv_item_rv_judge_score_myscore);
            remark = (TextView) itemView.findViewById(R.id.tv_item_rv_judge_score_remark);
            team = (TextView)itemView.findViewById(R.id.tv_item_rv_judge_score_group);
            gender = (TextView)itemView.findViewById(R.id.tv_item_rv_judge_score_gender);
        }
    }

    public void setNewData(ArrayList<JudgeItemScore> dataList){
        //
        mDataList = dataList;
        //mDataList.clear();
        //mDataList.addAll(dataList);
        notifyDataSetChanged();

        Log.i("adapter", dataList.size()+"   " );

        for(int i = 0; i< dataList.size(); ++i ){
            Log.i("tag", dataList.get(i).getAppear() + "");
        }
    }
}
