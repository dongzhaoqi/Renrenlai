package com.siti.renrenlai.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.SearchSuggestionsAdapter;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.arlib.floatingsearchview.util.view.BodyTextView;
import com.arlib.floatingsearchview.util.view.IconImageView;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnBackPressListener;
import com.orhanobut.dialogplus.OnCancelListener;
import com.orhanobut.dialogplus.OnDismissListener;
import com.orhanobut.dialogplus.OnItemClickListener;
import com.quinny898.library.persistentsearch.SearchBox;
import com.quinny898.library.persistentsearch.SearchResult;
import com.siti.renrenlai.R;
import com.siti.renrenlai.activity.ActivityInfo;
import com.siti.renrenlai.activity.FundIntroActivity;
import com.siti.renrenlai.activity.MainActivity;
import com.siti.renrenlai.adapter.ActivityAdapter;
import com.siti.renrenlai.bean.ActivityImage;
import com.siti.renrenlai.bean.CommentContents;
import com.siti.renrenlai.bean.LovedUsers;
import com.siti.renrenlai.bean.Activity;
import com.siti.renrenlai.db.DbActivity;
import com.siti.renrenlai.db.DbActivityImage;
import com.siti.renrenlai.db.DbReceivedComment;
import com.siti.renrenlai.db.DbReceivedLike;
import com.siti.renrenlai.db.DbSystemMessage;
import com.siti.renrenlai.util.ConstantValue;
import com.siti.renrenlai.util.CustomApplication;
import com.siti.renrenlai.util.SharedPreferencesUtil;
import com.siti.renrenlai.view.HeaderLayout.onLeftTextClickListener;
import com.siti.renrenlai.view.HeaderLayout.onRightImageButtonClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dong on 3/22/2016.
 */
public class FindFragment extends BaseFragment implements View.OnClickListener {

    private View view;
    private Context mContext;
    private XRecyclerView mXRecyclerView;
    private List<Activity> activityList;
    private ActivityAdapter adapter;
    private ImageView iv_fund;
    private List<LovedUsers> lovedUsersList = new ArrayList<>();
    private List<CommentContents> commentsList = new ArrayList<>();
    private List<ActivityImage> imageList;
    private SearchBox search;
    private FloatingSearchView mSearchView;
    private static final String TAG = "FindFragment";
    String url = ConstantValue.GET_ACTIVITY_LIST;
    String userName, url1, url2, url3;
    int activityId;
    private DbManager db;
    private List<DbSystemMessage> systemMessageList;
    private List<DbReceivedComment> receivedCommentList;
    private List<DbReceivedLike> receivedLikeList;
    int systemMessageSize, receivedReviewSize, receivedLikeSize;
    int count;          //未读消息条数

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_find, container, false);
        userName = SharedPreferencesUtil.readString(SharedPreferencesUtil.getSharedPreference(getActivity(), "login"), "userName");
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();

        cache();

        initEvent();

        initMessage();
    }

    /**
     * 判断缓存中是否已经有请求的数据，若已有直接从缓存中取，若没有，发起网络请求
     */
    private void cache() {
        Cache cache = CustomApplication.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(url);
        if (entry != null) {              // Cache is available
            String data = null;
            try {
                data = new String(entry.data, "UTF-8");
                JSONObject jsonObject = new JSONObject(data);
                Log.d(TAG, jsonObject.toString());
                getData(jsonObject);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            // Cache
            System.out.println("initData");
            initData();
        }
    }

    private void initView() {

        db = x.getDb(CustomApplication.getInstance().getDaoConfig());

        initTopBarForLeftTextBoth("发现", "阳光小区", new onLeftTextClickListener() {
            @Override
            public void onClick() {
                DialogPlus dialogPlus = DialogPlus.newDialog(getActivity())
                        .setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, new String[]{"上海", "北京", "广州", "深圳"}))
                        .setCancelable(true)
                        .setGravity(Gravity.TOP)
                        .setOnDismissListener(new OnDismissListener() {
                            @Override
                            public void onDismiss(DialogPlus dialog) {

                            }
                        })
                        .setOnItemClickListener(new OnItemClickListener() {
                            @Override
                            public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                                initLeftText(item.toString());
                                dialog.dismiss();
                            }
                        })
                        .setOnCancelListener(new OnCancelListener() {
                            @Override
                            public void onCancel(DialogPlus dialog) {

                            }
                        })
                        .setOnBackPressListener(new OnBackPressListener() {
                            @Override
                            public void onBackPressed(DialogPlus dialogPlus) {

                            }
                        })
                        .create();

                dialogPlus.show();
            }
        }, R.drawable.ic_action_search, new onRightImageButtonClickListener() {

            @Override
            public void onClick() {
                //startAnimActivity(SearchActivity.class);
                //openSearch();
                mSearchView.setVisibility(View.VISIBLE);
                mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
                    @Override
                    public void onSearchTextChanged(String oldQuery, final String newQuery) {

                        Log.d("search", "onSearchTextChanged()");
                    }
                });

                mSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
                    @Override
                    public void onSuggestionClicked(SearchSuggestion searchSuggestion) {
                        Log.d("TAG", "onSuggestionClicked()");
                    }

                    @Override
                    public void onSearchAction() {
                        Log.d("TAG", "onSearchAction()");
                    }
                });

                mSearchView.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
                    @Override
                    public void onFocus() {
                        Log.d("TAG", "onFocus()");
                    }

                    @Override
                    public void onFocusCleared() {
                        Log.d("TAG", "onFocusCleared()");
                    }
                });

                mSearchView.setOnHomeActionClickListener(new FloatingSearchView.OnHomeActionClickListener() {
                    @Override
                    public void onHomeClicked() {
                        mSearchView.setVisibility(View.GONE);
                    }
                });

                mSearchView.setOnBindSuggestionCallback(new SearchSuggestionsAdapter.OnBindSuggestionCallback() {
                    @Override
                    public void onBindSuggestion(IconImageView leftIcon, BodyTextView bodyText, SearchSuggestion item, int itemPosition) {
                    }
                });
            }
        });

        search = (SearchBox) findViewById(R.id.searchbox);
        mSearchView = (FloatingSearchView) findViewById(R.id.floating_search_view);
        mXRecyclerView = (XRecyclerView) findViewById(R.id.list);
        // 设置LinearLayoutManager
        mXRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        View header = LayoutInflater.from(getActivity()).inflate(R.layout.recyclerview_header, (ViewGroup) findViewById(android.R.id.content), false);
        iv_fund = (ImageView) header.findViewById(R.id.iv_fund);
        mXRecyclerView.addHeaderView(header);

        mXRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        refreshData();
                        mXRecyclerView.refreshComplete();
                    }
                }, 1000);
            }

            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        loadData();
                        mXRecyclerView.loadMoreComplete();
                    }
                }, 1000);
            }
        });
    }


    private void initData() {
        showProcessDialog();
        Log.d("FindFragment", "url:" + url);

        JsonObjectRequest request = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        getData(response);
                        dismissProcessDialog();
                        Log.d(TAG, response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismissProcessDialog();
            }
        });

        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        CustomApplication.getInstance().addToRequestQueue(request);      //加入请求队列
    }

    private void getData(JSONObject response) {
        int activityImageSize ;
        try {
            db.delete(DbActivity.class);
            db.delete(DbActivityImage.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
        JSONArray result = response.optJSONArray("result");
        activityList = new ArrayList<>();
        activityList = com.alibaba.fastjson.JSONArray.parseArray(result.toString(), Activity.class);
        for(Activity activity : activityList){
            DbActivity dbActivity = new DbActivity();
            DbActivityImage dbActivityImage = new DbActivityImage();
            dbActivity.setActivityId(activity.getActivityId());
            dbActivity.setActivityAddress(activity.getActivityAddress());
            dbActivity.setActivityDetailDescrip(activity.getActivityDetailDescrip());
            dbActivity.setActivityName(activity.getActivityName());
            dbActivity.setActivityEndTime(activity.getActivityEndTime());
            dbActivity.setActivityStartTime(activity.getActivityStartTime());
            dbActivity.setActivityReleaserTel(activity.getActivityReleaserTel());
            dbActivity.setActivityStatus(activity.getActivityStatus());

            dbActivityImage.setActivityId(activity.getActivityId());
            if(activity.getActivityImages() != null) {
                activityImageSize = activity.getActivityImages().size();
                for (int i = 0; i < activityImageSize; i++) {
                    dbActivityImage.setActivityImagePath(activity.getActivityImages().get(i).getActivityImagePath());
                    try {
                        db.save(dbActivityImage);
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                }
            }
            try {
                db.save(dbActivity);
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
        adapter = new ActivityAdapter(getActivity(), activityList);
        adapter.setOnItemClickListener(new ActivityAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, Object data) {

                int pos = Integer.parseInt(data.toString());
                activityId = activityList.get(pos).getActivityId();
                getActivityInfo(pos, activityId);
            }
        });
        mXRecyclerView.setAdapter(adapter);
        mXRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mXRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);
    }

    private void refreshData() {
        initData();
    }

    private void loadData() {
        initData();
    }

    private void initEvent() {
        iv_fund.setOnClickListener(this);

    }

    public void getActivityInfo(final int pos, final int activityId) {
        String url = ConstantValue.GET_ACTIVITY_INFO;

        JSONObject json = new JSONObject();
        try {
            json.put("userName", userName);
            json.put("activityId", activityId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest req = new JsonObjectRequest(url, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        getActivityNewData(pos, response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                showToast("出错了!");
            }
        });

        req.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        CustomApplication.getInstance().addToRequestQueue(req);
    }


    public void getActivityNewData(int pos, JSONObject response) {
        Log.d(TAG, "getActivityNewData: " + response);
        JSONObject result = response.optJSONObject("result");
        commentsList = com.alibaba.fastjson.JSONArray.parseArray(result.optJSONArray("commentUserInfoList").toString(), CommentContents.class);
        lovedUsersList =  com.alibaba.fastjson.JSONArray.parseArray(result.optJSONArray("lovedUserList").toString(), LovedUsers.class);
        com.siti.renrenlai.bean.Activity activity = activityList.get(pos);
        activity.setLovedIs(result.optBoolean("lovedIs"));
        activity.setSignUpIs(result.optBoolean("signUpIs"));
        activity.setCommentContents(commentsList);
        activity.setLovedUsers(lovedUsersList);
        Intent intent = new Intent(getActivity(), ActivityInfo.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("activity", activity);
        intent.putExtras(bundle);
        startAnimActivity(intent);
    }

    private void initMessage(){
        url1 = ConstantValue.urlRoot + ConstantValue.GET_SYSTEM_MESSAGE;
        Log.d(TAG, "initMessage2: " + url1 + " userName " + userName);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userName", userName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request1 = new JsonObjectRequest(url1, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "onResponse: " + response);
                        JSONArray result = response.optJSONArray("result");
                        if(result != null && result.length() > 0){
                            systemMessageList = com.alibaba.fastjson.JSONArray.parseArray(result.toString(), DbSystemMessage.class);
                            try {
                                db.delete(DbSystemMessage.class);
                            } catch (DbException e) {
                                e.printStackTrace();
                            }
                            for(DbSystemMessage systemMessage : systemMessageList){
                                try {
                                    db.save(systemMessage);
                                } catch (DbException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        request1.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        CustomApplication.getInstance().addToRequestQueue(request1);      //加入请求队列


        url2 = ConstantValue.urlRoot + ConstantValue.GET_COMMENT_MESSAGE;
        Log.d(TAG, "initMessage2: " + url2 + " userName " + userName);

        JsonObjectRequest request2 = new JsonObjectRequest(url2, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "onResponse: " + response);
                        JSONArray result = response.optJSONArray("result");
                        if(result != null && result.length() > 0){
                            receivedCommentList = com.alibaba.fastjson.JSONArray.parseArray(result.toString(), DbReceivedComment.class);
                            try {
                                db.delete(DbReceivedComment.class);
                            } catch (DbException e) {
                                e.printStackTrace();
                            }
                            for(DbReceivedComment receivedComment : receivedCommentList){
                                try {
                                    db.save(receivedComment);
                                } catch (DbException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        request2.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        CustomApplication.getInstance().addToRequestQueue(request2);      //加入请求队列


        url3 = ConstantValue.urlRoot + ConstantValue.GET_LIKE_MESSAGE;
        Log.d(TAG, "initMessage2: " + url3 + " userName " + userName);

        JsonObjectRequest request3 = new JsonObjectRequest(url3, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "onResponse: " + response);
                        JSONArray result = response.optJSONArray("result");
                        if(result != null && result.length() > 0){
                            receivedLikeList = com.alibaba.fastjson.JSONArray.parseArray(result.toString(), DbReceivedLike.class);
                            try {
                                db.delete(DbReceivedLike.class);
                            } catch (DbException e) {
                                e.printStackTrace();
                            }
                            for(DbReceivedLike receivedLike : receivedLikeList){
                                try {
                                    db.save(receivedLike);
                                } catch (DbException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        request3.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        CustomApplication.getInstance().addToRequestQueue(request3);      //加入请求队列

        try {
            systemMessageList = db.selector(DbSystemMessage.class).where("handleOrNot", "=", "0").findAll();
            receivedCommentList = db.selector(DbReceivedComment.class).where("handleOrNot", "=", "0").findAll();
            receivedLikeList = db.selector(DbReceivedLike.class).where("handleOrNot", "=", "0").findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        systemMessageSize = systemMessageList == null ?  0 : systemMessageList.size();
        receivedReviewSize = receivedCommentList == null ? 0 : receivedCommentList.size();
        receivedLikeSize = receivedLikeList == null ? 0 : receivedLikeList.size();
        count = systemMessageSize + receivedReviewSize + receivedLikeSize;

        ((MainActivity)getActivity()).setIconInvisible(count);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.iv_fund:
                startAnimActivity(FundIntroActivity.class);
                break;
        }
    }

    public void openSearch() {
        search.revealFromMenuItem(R.id.action_search, getActivity());
        for (int x = 0; x < 10; x++) {
            SearchResult option = new SearchResult("Result "
                    + Integer.toString(x), getResources().getDrawable(
                    R.drawable.ic_history));
            search.addSearchable(option);
        }
        search.setMenuListener(new SearchBox.MenuListener() {

            @Override
            public void onMenuClick() {
                //Toast.makeText(getActivity(), "Menu click", Toast.LENGTH_LONG).show();
                closeSearch();
            }

        });
        search.setSearchListener(new SearchBox.SearchListener() {

            @Override
            public void onSearchOpened() {
                // Use this to tint the screen

            }

            @Override
            public void onSearchClosed() {
                // Use this to un-tint the screen
                closeSearch();
            }

            @Override
            public void onSearchTermChanged(String term) {
                // React to the search term changing
                // Called after it has updated results
            }

            @Override
            public void onSearch(String searchTerm) {
                Toast.makeText(getActivity(), searchTerm + " Searched",
                        Toast.LENGTH_LONG).show();
                closeSearch();
            }

            @Override
            public void onResultClick(SearchResult result) {
                //React to result being clicked
            }

            @Override
            public void onSearchCleared() {

            }

        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1234) {
            ArrayList<String> matches = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            search.populateEditText(matches.get(0));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    protected void closeSearch() {
        search.hideCircularly(getActivity());
    }
}
