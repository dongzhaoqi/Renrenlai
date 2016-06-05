package com.siti.renrenlai.dialog;

import android.app.Activity;
import android.app.Dialog;
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
import com.siti.renrenlai.adapter.CommentAdapter;
import com.siti.renrenlai.bean.CommentContents;
import com.siti.renrenlai.util.ConstantValue;
import com.siti.renrenlai.util.CustomApplication;
import com.siti.renrenlai.util.SharedPreferencesUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


public class CommentDialog extends Dialog implements OnClickListener{

    private EditText etContent;
    private ImageView btnSend;

    private Activity mActivity;
    private Dialog dialog;
    private List<CommentContents> commentsList;
    private CommentAdapter mAdapter;
    private int position;
    private int activity_id;

    public CommentDialog(Activity activity, int theme) {
        super(activity, theme);
        this.mActivity = activity;
    }

    public CommentDialog(Activity activity, CommentAdapter mAdapter) {
        super(activity);
        this.mActivity = activity;
        this.mAdapter = mAdapter;
    }

    public CommentDialog(Activity activity, CommentAdapter mAdapter, int activity_id) {
        super(activity);
        this.mActivity = activity;
        this.mAdapter = mAdapter;
        this.activity_id = activity_id;

    }

    public CommentDialog(Activity activity) {
        this(activity, R.style.dialog_comment);
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
        String userName = SharedPreferencesUtil.readString(
                SharedPreferencesUtil.getSharedPreference(
                        mActivity, "login"), "userName");
        String contents = etContent.getText().toString();
        comment(userName, contents, activity_id);
        System.out.println("userName:" + userName + " contents:" + contents);
        CommentContents comment = new CommentContents(userName, contents);
        //commentsList.add(0, comment);
        mAdapter.notifyDataSetChanged();
    }

    private void comment(String userName, String commentContent, int activityId) {
        System.out.println("userName:" + userName + " commentContent:" + commentContent + " activityId:" + activityId);
        String api = "/insertCommentForApp";
        String url = ConstantValue.urlRoot + api;
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
                        Log.d("response", "response:" + response.toString());
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
}
