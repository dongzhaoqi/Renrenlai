package com.siti.renrenlai.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.siti.renrenlai.R;
import com.siti.renrenlai.activity.LoginActivity;
import com.siti.renrenlai.adapter.CommentAdapter;
import com.siti.renrenlai.bean.CommentContents;
import com.siti.renrenlai.util.ConstantValue;
import com.siti.renrenlai.util.CustomApplication;
import com.siti.renrenlai.util.SharedPreferencesUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class CommentDialog extends Dialog implements OnClickListener{

    private EditText etContent;
    private ImageView btnSend;
    private Activity mActivity;
    private Dialog dialog;
    private List<CommentContents> commentsList;
    private CommentAdapter mAdapter;
    int position = -1;
    int activity_id;
    int replyId;
    String url = ConstantValue.COMMENT_ACTIVITY;
    String userName, userHeadImagePath, contents, commentTime;
    private static final String TAG = "CommentDialog";

    public CommentDialog(Activity activity, int theme) {
        super(activity, theme);
        this.mActivity = activity;
        dialog = new Dialog(activity);
    }

    public CommentDialog(Activity activity, CommentAdapter mAdapter) {
        this(activity, R.style.dialog_comment);
        this.mActivity = activity;
        this.mAdapter = mAdapter;
    }

    public CommentDialog(Activity activity, CommentAdapter mAdapter, List<CommentContents> commentsList, int activity_id) {
        this(activity, R.style.dialog_comment);
        this.mActivity = activity;
        this.mAdapter = mAdapter;
        this.commentsList = commentsList;
        this.activity_id = activity_id;
        dialog = new Dialog(activity);
    }


    public void setCommentList(List<CommentContents> commentsList, int pos, int activity_id){
        this.commentsList = commentsList;
        this.position = pos;
        this.activity_id = activity_id;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_comment);
        etContent = (EditText) findViewById(R.id.et_content);
        btnSend = (ImageView) findViewById(R.id.iv_send);
        btnSend.setOnClickListener(this);
        if(position != -1) {
            CommentContents comment = commentsList.get(position);
            replyId = comment.getCommentId();
            String userName = comment.getUserName();
            etContent.setHint("回复 " + userName + ":");
        }else{
            etContent.setHint("评论 " + ":");
        }
    }

    @Override
    public void onClick(View v) {
        userName = SharedPreferencesUtil.readString(
                SharedPreferencesUtil.getSharedPreference(
                        mActivity, "login"), "userName");
        //Log.d(TAG, "onClick: " + userName);
        if("0".equals(userName)){
            mActivity.startActivity(new Intent(mActivity, LoginActivity.class));
            CommentDialog.this.dismiss();
        }else {
            userHeadImagePath = SharedPreferencesUtil.readString(SharedPreferencesUtil.getSharedPreference(mActivity, "login"), "userHeadPicImagePath");
            commentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            contents = etContent.getText().toString();
            if (position != -1) {
                contents = "回复" + commentsList.get(position).getUserName() + ": " + contents;
            }
            CommentContents comment = new CommentContents(userName, contents, userHeadImagePath, commentTime);
            commentsList.add(0, comment);
            mAdapter.notifyDataSetChanged();
            if (position == -1) {
                comment(userName, contents, activity_id);
            } else {
                comment(userName, contents, activity_id, replyId);
            }
        }
    }

    /**
     * 发表评论
     * @param userName  评论人用户名
     * @param commentContent    评论内容
     * @param activityId    评论的活动id
     */
    private void comment(String userName, String commentContent, int activityId) {
        //Log.d(TAG, "comment: 执行了--->" + "comment: " + userName + " " + commentContent + " " + activityId);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userName", userName);
            jsonObject.put("commentContent", commentContent);
            jsonObject.put("activityId", activityId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.d("response", "response:" + response.toString());
                        CommentDialog.this.dismiss();
                        Toast.makeText(mActivity, "评论成功", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error: ", "error.getMessage():" + error.getMessage());
                Toast.makeText(mActivity, "评论出错", Toast.LENGTH_SHORT).show();
            }
        });
        req.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        CustomApplication.getInstance().addToRequestQueue(req);
    }

    /**
     * 回复某人的评论
     * @param userName
     * @param commentContent
     * @param activityId
     * @param replyId
     */
    private void comment(String userName, String commentContent, int activityId, int replyId ){
        //Log.d(TAG, "comment: " + userName + " " + commentContent + " " + activityId + " " + replyId);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userName", userName);
            jsonObject.put("commentContent", commentContent);
            jsonObject.put("activityId", activityId);
            jsonObject.put("replyId", replyId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.d("response", "response:" + response.toString());
                        Toast.makeText(mActivity, "回复成功", Toast.LENGTH_SHORT).show();
                        CommentDialog.this.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error: ", "error.getMessage():" + error.getMessage());
                Toast.makeText(mActivity, "回复出错", Toast.LENGTH_SHORT).show();
            }
        });
        req.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        CustomApplication.getInstance().addToRequestQueue(req);
    }
}
