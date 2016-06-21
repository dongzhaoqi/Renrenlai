package com.siti.renrenlai.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.siti.renrenlai.R;
import com.siti.renrenlai.adapter.ReceivedLikeExpandAdapter;
import com.siti.renrenlai.adapter.ReceivedLikeExpandAdapter.ReceivedLikeChild;
import com.siti.renrenlai.adapter.ReceivedLikeExpandAdapter.ReceivedLikeGroup;
import com.siti.renrenlai.adapter.ReviewExpandAdapter;
import com.siti.renrenlai.adapter.ReviewExpandAdapter.ReviewChild;
import com.siti.renrenlai.adapter.ReviewExpandAdapter.ReviewGroup;
import com.siti.renrenlai.adapter.SystemMessageExpandAdapter;
import com.siti.renrenlai.adapter.SystemMessageExpandAdapter.MessageChild;
import com.siti.renrenlai.adapter.SystemMessageExpandAdapter.MessageGroup;
import com.siti.renrenlai.db.SystemMessage;
import com.siti.renrenlai.util.CustomApplication;
import com.siti.renrenlai.view.AnimatedExpandableListView;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Dong on 2016/3/29.
 */

public class MessageActivity extends BaseActivity {

    @Bind(R.id.list_system)
    AnimatedExpandableListView listSystem;
    @Bind(R.id.list_review)
    AnimatedExpandableListView listReview;
    @Bind(R.id.list_received_like)
    AnimatedExpandableListView listReceivedLike;

    private SystemMessageExpandAdapter systemAdapter;
    private ReviewExpandAdapter reviewAdapter;
    private ReceivedLikeExpandAdapter receivedLikeAdapter;
    private String[] str_message = {"本活动将于两天后举行aaaaa啊啊啊啊啊!", "本活动取消了!"};
    private String[] str_name = {"环小区挑战赛!", "越野跑", "夜跑", "环小区挑战赛!", "越野跑", "夜跑", "环小区挑战赛!", "越野跑", "夜跑"};
    private String[] users = {"张三", "李四", "小王","张三", "李四", "小王", "张三", "李四", "小王"};
    private String[] contents = {"哈哈", "不错", "好玩","哈哈", "不错", "好玩","哈哈", "不错", "好玩"};
    private DbManager db;
    private List<SystemMessage> systemMessageList;
    private static final String TAG = "MessageActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ButterKnife.bind(this);
        initViews();

    }

    private void initViews() {
        initTopBarForLeft("消息");
        db = x.getDb(CustomApplication.getInstance().getDaoConfig());
        initSystemMessage();
        initReview();
        initReceivedLike();

        /*listSystem.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if (listSystem.isGroupExpanded(groupPosition)) {
                    listSystem.collapseGroupWithAnimation(groupPosition);
                } else {
                    listSystem.expandGroupWithAnimation(groupPosition);
                }
                return true;
            }

        });*/
    }

    /**
     * 初始化"系统消息"
     */
    public void initSystemMessage(){
        try {
            systemMessageList = db.selector(SystemMessage.class).findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        if(systemMessageList != null){
            for(SystemMessage systemMessage : systemMessageList){
                Log.d(TAG, "onCreate: " + systemMessage.getMsgId() + " " + systemMessage.getMsgTitle() + " " + systemMessage.getMsgContent());
            }
        }

        List<MessageGroup> systemMessageGroupList = new ArrayList<>();
        MessageGroup systemMessageGroup = new MessageGroup();
        if(systemMessageList != null && systemMessageList.size() > 0) {
            for (int i = 0; i < systemMessageList.size(); i++) {
                MessageChild child = new MessageChild();
                child.message = systemMessageList.get(i).getMsgContent();
                child.activity_name = systemMessageList.get(i).getMsgTitle();
                systemMessageGroup.systemMessageChildList.add(child);
            }
            systemMessageGroupList.add(systemMessageGroup);
        }

        systemAdapter = new SystemMessageExpandAdapter(this);
        systemAdapter.setData(systemMessageGroupList, systemMessageList);
        // 去掉默认的箭头
        listSystem.setGroupIndicator(null);
        listSystem.setAdapter(systemAdapter);
        listSystem.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                for (int i = 0; i < systemAdapter.getGroupCount(); i++) {
                    if (groupPosition != i) {
                        listSystem.collapseGroup(i);
                    }
                }
            }
        });

    }

    /**
     * 初始化"评论"
     */
    public void initReview(){
        List<ReviewGroup> items = new ArrayList<>();
        ReviewGroup item = new ReviewGroup();
        for(int i = 0; i < users.length; i++){
            ReviewChild child = new ReviewChild();
            child.username = users[i];
            child.review = contents[i];
            child.review_time = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            child.activity_name = str_name[i];
            item.items.add(child);
        }
        items.add(item);

        reviewAdapter = new ReviewExpandAdapter(this);
        reviewAdapter.setData(items);
        // 去掉默认的箭头
        listReview.setGroupIndicator(null);
        listReview.setAdapter(reviewAdapter);
        listReview.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                for (int i = 0; i < reviewAdapter.getGroupCount(); i++) {
                    if (groupPosition != i) {
                        listReview.collapseGroup(i);
                    }
                }
            }
        });
    }

    /**
     * 初始化"收到的喜欢"
     */
    public void initReceivedLike(){
        List<ReceivedLikeGroup> items = new ArrayList<>();
        ReceivedLikeGroup item = new ReceivedLikeGroup();
        for(int i = 0; i < users.length; i++){
            ReceivedLikeChild child = new ReceivedLikeChild();
            child.received_time = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            child.activity_name = str_name[i];
            item.items.add(child);
        }
        items.add(item);

        receivedLikeAdapter = new ReceivedLikeExpandAdapter(this);
        receivedLikeAdapter.setData(items);
        // 去掉默认的箭头
        listReceivedLike.setGroupIndicator(null);
        listReceivedLike.setAdapter(receivedLikeAdapter);
        listReceivedLike.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                for (int i = 0; i < receivedLikeAdapter.getGroupCount(); i++) {
                    if (groupPosition != i) {
                        listReceivedLike.collapseGroup(i);
                    }
                }
            }
        });
    }
}