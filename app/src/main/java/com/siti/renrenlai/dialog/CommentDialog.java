package com.siti.renrenlai.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.siti.renrenlai.R;
import com.siti.renrenlai.adapter.CommentAdapter;
import com.siti.renrenlai.bean.CommentContents;
import com.siti.renrenlai.util.SharedPreferencesUtil;

import java.util.List;


public class CommentDialog extends Dialog implements OnClickListener{

    private EditText etContent;
    private ImageView btnSend;

    private Activity mActivity;
    private Dialog dialog;
    private List<CommentContents> commentsList;
    private CommentAdapter mAdapter;
    private int position;

    public CommentDialog(Activity activity, int theme) {
        super(activity, theme);
        this.mActivity = activity;
    }

    public CommentDialog(Activity activity, CommentAdapter mAdapter) {
        super(activity);
        this.mActivity = activity;
        this.mAdapter = mAdapter;
    }

    public CommentDialog(Activity activity) {
        this(activity, R.style.dialog_comment);
        dialog = new Dialog(activity);
    }

    public void setCommentList(List<CommentContents> commentsList, int pos){
        this.commentsList = commentsList;
        this.position = pos;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_comment);
        etContent = (EditText) findViewById(R.id.et_content);
        btnSend = (ImageView) findViewById(R.id.iv_send);
        btnSend.setOnClickListener(this);
        if(commentsList != null) {
            CommentContents comment = commentsList.get(position);
            String userName = comment.getUserName();
            etContent.setHint("回复 " + userName + ":");
        }else{
            etContent.setHint("评论 " + ":");
        }
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(mActivity, "发送", Toast.LENGTH_SHORT).show();
        String userName = SharedPreferencesUtil.readString(
                SharedPreferencesUtil.getSharedPreference(
                        mActivity, "login"), "userName");
        String contents = etContent.getText().toString();
        System.out.println("userName:" + userName + " contents:" + contents);
        CommentContents comment = new CommentContents(userName, contents);
        //commentsList.add(0, comment);
        mAdapter.notifyDataSetChanged();
    }
}
