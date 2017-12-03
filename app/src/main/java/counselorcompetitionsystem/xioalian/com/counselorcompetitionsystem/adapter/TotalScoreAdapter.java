package counselorcompetitionsystem.xioalian.com.counselorcompetitionsystem.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import counselorcompetitionsystem.xioalian.com.counselorcompetitionsystem.R;
import counselorcompetitionsystem.xioalian.com.counselorcompetitionsystem.bean.TotalScore;

/**
 * Created by xiaolian on 04/11/2017.
 */

public class TotalScoreAdapter extends RecyclerView.Adapter<TotalScoreAdapter.ViewHolder>{

    private List<TotalScore> mDatalist;

    public TotalScoreAdapter(List<TotalScore> dataList){
        mDatalist = dataList;
    }

    @Override
    public TotalScoreAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.total_score_layout, parent, false);
        TotalScoreAdapter.ViewHolder holder = new TotalScoreAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DecimalFormat df = new DecimalFormat("#.00");
        TotalScore totalScore = mDatalist.get(position);
        holder.order.setText("" + (position + 1));
        holder.sno.setText("" + totalScore.getSno());
        holder.college.setText("" + totalScore.getCollege());
        holder.gender.setText("" + totalScore.getGender());
        holder.username.setText("" + totalScore.getName());
        holder.team.setText("" + totalScore.getGroup());
        holder.writtenExam.setText("" + df.format(totalScore.getWritten_exam()));
        holder.topicMeeting.setText("" + df.format(totalScore.getTopic_meeting()));
        holder.itemAnalysis.setText("" + df.format(totalScore.getItem_analysis()));
        holder.concact.setText("" + df.format(totalScore.getConcact()));
        holder.topicSpeech.setText("" + df.format(totalScore.getTopic_speech()));
        holder.total.setText("" + df.format(totalScore.getTotal_score()));
    }

    @Override
    public int getItemCount() {
        return mDatalist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView appear;
        // 序号
        private TextView order;
        // 学号
        private TextView sno;
        // 学校
        private TextView college;
        // 性别
        private TextView gender;
        // 名字
        private TextView username;
        // 组别
        private TextView team;
        // 笔试
        private TextView writtenExam;
        // 主题班会
        private TextView topicMeeting;
        // 案例分析
        private TextView itemAnalysis;
        // 谈心谈话
        private TextView concact;
        // 主题演讲
        private TextView topicSpeech;
        // 汇总
        private TextView total;

        public ViewHolder(View itemView) {
            super(itemView);
            order = (TextView) itemView.findViewById(R.id.tv_id);
            sno = (TextView) itemView.findViewById(R.id.tv_sno);
            college = (TextView)itemView.findViewById(R.id.tv_college);
            gender = (TextView)itemView.findViewById(R.id.tv_gender);
            username = (TextView)itemView.findViewById(R.id.tv_name);
            team = (TextView)itemView.findViewById(R.id.tv_team);
            writtenExam = (TextView)itemView.findViewById(R.id.tv_written_examization);
            topicMeeting = (TextView)itemView.findViewById(R.id.tv_topic_meeting);
            itemAnalysis = (TextView)itemView.findViewById(R.id.tv_item_analysis);
            concact = (TextView)itemView.findViewById(R.id.tv_concact);
            topicSpeech = (TextView)itemView.findViewById(R.id.tv_speech);
            total = (TextView)itemView.findViewById(R.id.tv_total);
        }
    }

    public void setNewData(ArrayList<TotalScore> dataList){
        mDatalist = dataList;
        notifyDataSetChanged();
    }
}
