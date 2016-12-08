package cn.appleye.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import cn.appleye.timelineview.TimeLineView;

import java.util.ArrayList;

/**
 * @author appleye<liuliaopu>
 * @date 2016-12-08
 */

public class TimeLineDemo extends Activity{
    private static final String TAG = "TimeLineDemo";

    private ListView mListView;
    private ListAdapter mAdapter;

    private ArrayList<String> mData = new ArrayList<>();
    private static final int COUNT = 10;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_demo);

        mListView = (ListView) findViewById(R.id.list_view);

        initData();

        setupListView();
    }

    private void initData() {
        for(int i=1; i<= COUNT; i++) {
            mData.add("this is " + i + " item");
        }
    }

    private void setupListView() {
        if(mAdapter == null) {
            mAdapter = new ListAdapter();
            mListView.setAdapter(mAdapter);
            mListView.setDividerHeight(0);
            mListView.setDivider(null);
        }
    }

    private class ListAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.list_item, null);
            }

            TimeLineView timeLineView = (TimeLineView) convertView.findViewById(R.id.time_line);
            TextView textView = (TextView) convertView.findViewById(R.id.text_view);
            textView.setText(mData.get(position));

            timeLineView.setTopLineVisible(false);
            timeLineView.setBottomBallVisible(false);

            if(position != 0) {
                timeLineView.setTopLineVisible(true);
            }

            if(getCount() - 1 == position) {
                timeLineView.setBottomBallVisible(true);
            }

            switch (position % 3) {
                case 0 :{
                    timeLineView.setTopTimeBallDrawable(getResources().getDrawable(R.drawable.green_ball));
                    break;
                }

                case 1 :{
                    timeLineView.setTopTimeBallDrawable(getResources().getDrawable(R.drawable.yellow_ball));
                    break;
                }

                case 2 :{
                    timeLineView.setTopTimeBallDrawable(getResources().getDrawable(R.drawable.pink_ball));
                    break;
                }
            }

            /*调用下面强制绘制，否则会出现显示错位问题*/
            timeLineView.invalidate();

            return convertView;
        }
    }
}
