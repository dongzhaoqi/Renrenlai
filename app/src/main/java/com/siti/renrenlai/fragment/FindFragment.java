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
import com.siti.renrenlai.adapter.ActivityAdapter;
import com.siti.renrenlai.bean.Activity;
import com.siti.renrenlai.bean.ActivityImage;
import com.siti.renrenlai.bean.CommentContents;
import com.siti.renrenlai.bean.LovedUsers;
import com.siti.renrenlai.util.ConstantValue;
import com.siti.renrenlai.util.CustomApplication;
import com.siti.renrenlai.view.HeaderLayout.onLeftTextClickListener;
import com.siti.renrenlai.view.HeaderLayout.onRightImageButtonClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    private List<LovedUsers> lovedUsersList;
    private List<CommentContents> commentsList;
    private List<ActivityImage> imageList;
    private SearchBox search;
    private FloatingSearchView mSearchView;
    String api = "/getActivityListForApp";
    String url = ConstantValue.urlRoot + api;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_find, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();

        cache();

        initEvent();
    }

    /**
     * 判断缓存中是否已经有请求的数据，若已有直接从缓存中取，若没有，发起网络请求
     */
    private void cache() {
        Cache cache = CustomApplication.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(url);
        if(entry != null){              // Cache is available
            String data = null;
            try {
                data = new String(entry.data, "UTF-8");
                JSONObject jsonObject = new JSONObject(data);
                System.out.println("data:"+jsonObject);
                getData(jsonObject);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            // Cache data
            System.out.println("initData");
            initData();
        }
    }

    private void initView() {
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
                        Log.d("response", "jsonobject:" + response.toString());
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

        JSONArray result = response.optJSONArray("result");
        activityList = new ArrayList<>();

        for (int i = 0; i < result.length(); i++) {
            lovedUsersList = new ArrayList<>();
            commentsList = new ArrayList<>();
            imageList = new ArrayList<>();
            Activity activity = new Activity();
            try {
                activity.setActivityId(result.getJSONObject(i).getInt("activityId"));
                activity.setActivityName(result.getJSONObject(i).optString("activityName"));
                activity.setActivityStartTime(result.getJSONObject(i).optString("activityStartTime"));
                activity.setActivityEndTime(result.getJSONObject(i).optString("activityEndTime"));
                activity.setactivityReleaserTel(result.getJSONObject(i).optString("activityReleaserTel"));
                activity.setActivityAddress(result.getJSONObject(i).optString("activityAddress"));
                activity.setactivityDetailDescrip(result.getJSONObject(i).optString("activityDetailDescrip"));

                JSONArray imagesArray = result.getJSONObject(i).optJSONArray("activityImages");

                if (imagesArray != null && imagesArray.length() > 0) {
                    for (int m = 0; m < imagesArray.length(); m++) {
                        ActivityImage image = new ActivityImage();
                        image.setActivityImageId(imagesArray.optJSONObject(m).optInt("activityImageId"));
                        image.setActivityImageName(imagesArray.optJSONObject(m).optString("activityImageName"));
                        image.setActivityImagePath(ConstantValue.urlRoot + imagesArray.optJSONObject(m).optString("activityImagePath"));
                        imageList.add(image);
                        System.out.println("getActivityImagePath " + ConstantValue.urlRoot + imagesArray.optJSONObject(m).optString("activityImagePath"));
                    }
                }
                activity.setActivityImages(imageList);

                JSONArray lovedusersArray = result.getJSONObject(i).optJSONArray("lovedUsers");
                for (int j = 0; j < lovedusersArray.length(); j++) {
                    LovedUsers lovedUsers = new LovedUsers();
                    lovedUsers.setUserHeadPicImagePath(lovedusersArray.optJSONObject(j).optString("userHeadPicImagePath"));
                    lovedUsers.setUserId(lovedusersArray.optJSONObject(j).optString("userId"));
                    lovedUsersList.add(lovedUsers);
                }
                activity.setLovedUsers(lovedUsersList);

                JSONArray commentsArray = result.getJSONObject(i).optJSONArray("commentContents");
                for (int k = 0; k < commentsArray.length(); k++) {
                    CommentContents comment = new CommentContents();
                    comment.setUserName(commentsArray.optJSONObject(k).optString("userName"));
                    comment.setCommentContent(commentsArray.optJSONObject(k).optString("commentContent"));
                    commentsList.add(comment);
                }
                activity.setComments(commentsList);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            activityList.add(activity);
        }

        adapter = new ActivityAdapter(getActivity(), activityList);
        adapter.setOnItemClickListener(new ActivityAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, Object data) {
                int pos = Integer.parseInt(data.toString());
                Activity activity = activityList.get(pos);
                Intent intent = new Intent(getActivity(), ActivityInfo.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("activity", activity);
                intent.putExtras(bundle);
                startAnimActivity(intent);
            }
        });
        mXRecyclerView.setAdapter(adapter);
        mXRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mXRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);
    }

    private void refreshData(){
        initData();
    }
    private void loadData() {
        initData();
    }

    private void initEvent() {
        iv_fund.setOnClickListener(this);

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
