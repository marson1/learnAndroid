package com.example.he.sample;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private static final int OK = 1;


    private myListView listView;
    private ArrayAdapter<Integer> adapter;
    private int headerHeight;
    private List<Integer> list;
    private Random random = new Random(47);//随机数
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case OK:
                    adapter.notifyDataSetChanged();//刷新数据
                    listView.topPadding(-headerHeight);//隐藏header
                    break;
            }
        }
    };


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (myListView) findViewById(R.id.lv);
        headerHeight = listView.getHeaderHeight();
        list = new ArrayList<Integer>();
        initList();
        adapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);


        /**
         * listView的滑动监听
         */
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //正在刷新则刷新数据
                if (listView.getState() == State.REFRESH) {
                    onRefresh();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

    }

    private void initList() {
        for (int i = 0; i < 50; i++) {
            list.add(random.nextInt(50));
        }

    }

    /**
     * 更新数据
     */
    private void onRefresh() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = OK;
                for (int i : list) {
                    list.set(i, random.nextInt(50));
                }
                handler.sendMessageDelayed(message, 2000);//2s以后发送message
            }
        }).start();
    }

}
