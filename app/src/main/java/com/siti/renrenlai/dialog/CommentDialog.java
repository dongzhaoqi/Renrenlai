package com.siti.renrenlai.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.siti.renrenlai.R;
import com.siti.renrenlai.bean.CommentContents;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class CommentDialog extends Dialog{

    private EditText etContent;
    private Button btnSend;

    private Activity mActivity;
    private Dialog dialog;
    private List<CommentContents> commentsList;
    private int position;

    public CommentDialog(Activity activity, int theme) {
        super(activity, theme);
        this.mActivity = activity;
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
        btnSend = (Button) findViewById(R.id.btn_send);

        CommentContents comment = commentsList.get(position);
        String userName = comment.getUserName();
        etContent.setHint("回复 " + userName + ":");
    }


    @OnClick(R.id.btn_send)
    public void onClick() {

    }
}
