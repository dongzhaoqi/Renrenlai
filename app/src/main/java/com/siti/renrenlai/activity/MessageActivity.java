package com.siti.renrenlai.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ExpandableListView;

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
import com.siti.renrenlai.db.DbActivity;
import com.siti.renrenlai.db.DbProject;
import com.siti.renrenlai.db.DbReceivedComment;
import com.siti.renrenlai.db.DbReceivedLike;
import com.siti.renrenlai.db.DbSystemMessage;
import com.siti.renrenlai.util.CustomApplication;
import com.siti.renrenlai.view.AnimatedExpandableListView;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.ArrayList;
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
    private String[] users = {"张三", "李四", "小王", "张三", "李四", "小王", "张三", "李四", "小王"};
    private String[] contents = {"哈哈", "不错", "好玩", "哈哈", "不错", "好玩", "哈哈", "不错", "好玩"};
    private DbManager db;
    private List<DbSystemMessage> systemMessageList;
    private List<DbReceivedComment> receivedCommentList;
    private List<DbReceivedLike> receivedLikeList;
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
    public void initSystemMessage() {
        try {
            systemMessageList = db.selector(DbSystemMessage.class).findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }

        List<MessageGroup> systemMessageGroupList = new ArrayList<>();
        MessageGroup systemMessageGroup = new MessageGroup();
        if (systemMessageList != null && systemMessageList.size() > 0) {
            for (int i = systemMessageList.size()-1; i >= 0 ; i--) {
                DbActivity dbActivity = new DbActivity();
                MessageChild child = new MessageChild();
                child.type = systemMessageList.get(i).getType();
                child.adviceId = systemMessageList.get(i).getAdviceId();
                child.activityId = systemMessageList.get(i).getActivOrProId();
                child.message = systemMessageList.get(i).getContent();
                child.userHeadImagePath = systemMessageList.get(i).getUserHeadImagePath();
                child.alert_time = systemMessageList.get(i).getTime();
                child.handleOrNot = systemMessageList.get(i).getHandleOrNot();

                try {
                    dbActivity = db.selector(DbActivity.class).where("activityId", "=", child.activityId).findFirst();
                } catch (DbException e) {
                    e.printStackTrace();
                }
                if(dbActivity != null) {
                    child.activity_name = dbActivity.getActivityName();
                    try {
                        if (dbActivity.getActivityImages(db) != null && dbActivity.getActivityImages(db).size() > 0) {
                            Log.d(TAG, "initSystem: " + dbActivity.getActivityImages(db).get(0).getActivityImagePath());
                            child.activityImagePath = dbActivity.getActivityImages(db).get(0).getActivityImagePath();
                        }
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                }
                systemMessageGroup.systemMessageChildList.add(child);
            }
        }
        systemMessageGroupList.add(systemMessageGroup);

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
    public void initReview() {

        try {
            receivedCommentList = db.selector(DbReceivedComment.class).findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }

        List<ReviewGroup> receivedCommentGroupList = new ArrayList<>();
        ReviewGroup reviewGroup = new ReviewGroup();
        if (receivedCommentList != null && receivedCommentList.size() > 0) {
            for (int i = receivedCommentList.size()-1; i >= 0 ; i--) {
                DbActivity dbActivity = new DbActivity();
                DbProject dbProject = new DbProject();
                int type;
                ReviewChild child = new ReviewChild();
                child.adviceId = receivedCommentList.get(i).getAdviceId();
                child.username = receivedCommentList.get(i).getUserName();
                child.userHeadImagePath = receivedCommentList.get(i).getUserHeadImagePath();
                child.review = receivedCommentList.get(i).getContent();
                child.review_time = receivedCommentList.get(i).getTime();
                child.handleOrNot = receivedCommentList.get(i).getHandleOrNot();

                type = receivedCommentList.get(i).getType();
                if (type == 0 || type == 1 || type == 2) {//活动消息
                    child.activityId = receivedCommentList.get(i).getActivOrProId();
                    try {
                        dbActivity = db.selector(DbActivity.class).where("activityId", "=", child.activityId).findFirst();
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                    if (dbActivity != null) {
                        child.activity_name = dbActivity.getActivityName();
                        try {
                            //Log.d(TAG, "initReview: " + dbActivity.getActivityImages(db).get(0).getActivityImagePath());
                            Log.d(TAG, "initReview: child.adviceId--->" + child.adviceId);
                            if (dbActivity.getActivityImages(db) != null && dbActivity.getActivityImages(db).size() > 0) {
                                child.activityImagePath = dbActivity.getActivityImages(db).get(0).getActivityImagePath();
                            }
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                    }

                } else { //项目消息
                    child.projectId = receivedCommentList.get(i).getActivOrProId();

                    try {
                        Log.d(TAG, "initReview: child.adviceId--->" + child.adviceId);
                        dbProject = db.selector(DbProject.class).where("projectId", "=", child.projectId).findFirst();
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                    if (dbProject != null) {
                        child.activity_name = dbProject.getProjectName();
                        if (dbProject.getProjectImagePath() != null) {
                            child.activityImagePath = dbProject.getProjectImagePath();
                        }
                    }
                }

                reviewGroup.receivedCommentChildList.add(child);
            }
        }
        receivedCommentGroupList.add(reviewGroup);

        reviewAdapter = new ReviewExpandAdapter(this);
        reviewAdapter.setData(receivedCommentGroupList, receivedCommentList);
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
    public void initReceivedLike() {

        try {
            receivedLikeList = db.selector(DbReceivedLike.class).findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }

        List<ReceivedLikeGroup> receivedLikeGroupList = new ArrayList<>();
        ReceivedLikeGroup receivedLikeGroup = new ReceivedLikeGroup();

        if (receivedLikeList != null && receivedLikeList.size() > 0) {
            for (int i = receivedLikeList.size()-1; i >= 0; i--) {
                DbActivity dbActivity = new DbActivity();
                DbProject dbProject = new DbProject();
                int type;
                ReceivedLikeChild child = new ReceivedLikeChild();
                child.adviceId = receivedLikeList.get(i).getAdviceId();
                child.received_time = receivedLikeList.get(i).getTime();
                child.userName = receivedLikeList.get(i).getUserName();
                child.handleOrNot = receivedLikeList.get(i).getHandleOrNot();

                type = receivedLikeList.get(i).getType();
                if (type == 0 || type == 1 || type == 2) {
                    child.activityId = receivedLikeList.get(i).getActivOrProId();
                    try {
                        dbActivity = db.selector(DbActivity.class).where("activityId", "=", child.activityId).findFirst();
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                    if (dbActivity != null) {
                        child.activity_name = dbActivity.getActivityName();
                        try {
                            if (dbActivity.getActivityImages(db) != null && dbActivity.getActivityImages(db).size() > 0) {
                                Log.d(TAG, "initSystem: " + dbActivity.getActivityImages(db).get(0).getActivityImagePath());
                                child.activityImagePath = dbActivity.getActivityImages(db).get(0).getActivityImagePath();
                            }
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    child.projectId = receivedLikeList.get(i).getActivOrProId();
                    try {
                        dbProject = db.selector(DbProject.class).where("projectId", "=", child.projectId).findFirst();
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                    if (dbProject != null) {
                        child.activity_name = dbProject.getProjectName();
                        try {
                            if(dbProject.getProjectImages(db) != null && dbProject.getProjectImages(db).size() > 0) {
                                Log.d(TAG, "initLike: dbProject " + dbProject.getProjectImages(db).get(0).getProjectImagePath());
                                child.activityImagePath = dbProject.getProjectImages(db).get(0).getProjectImagePath();
                            }
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                    }
                }

                receivedLikeGroup.receivedLikeChildList.add(child);
            }
        }
        receivedLikeGroupList.add(receivedLikeGroup);

        receivedLikeAdapter = new ReceivedLikeExpandAdapter(this);
        receivedLikeAdapter.setData(receivedLikeGroupList, receivedLikeList);
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

    @Override
    protected void onResume() {
        super.onResume();
        initSystemMessage();
        initReview();
        initReceivedLike();
    }
}