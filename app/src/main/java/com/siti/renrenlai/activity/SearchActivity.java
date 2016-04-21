package com.siti.renrenlai.activity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.Toolbar.OnMenuItemClickListener;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.quinny898.library.persistentsearch.SearchBox;
import com.quinny898.library.persistentsearch.SearchResult;
import com.siti.renrenlai.R;

import java.util.ArrayList;

/**
 * Created by Dong on 2016/3/22.
 */

public class SearchActivity extends AppCompatActivity {

    private SearchBox search;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        search = (SearchBox) findViewById(R.id.searchbox);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                openSearch();
                return true;
            }
        });
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void openSearch() {
        //toolbar.setTitle("");
        search.revealFromMenuItem(R.id.action_search, this);
        for (int x = 0; x < 10; x++) {
            SearchResult option = new SearchResult("Result "
                    + Integer.toString(x), getResources().getDrawable(
                    R.drawable.ic_history));
            search.addSearchable(option);
        }
        search.setMenuListener(new SearchBox.MenuListener() {

            @Override
            public void onMenuClick() {
                Toast.makeText(SearchActivity.this, "Menu click",
                        Toast.LENGTH_LONG).show();
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
                Toast.makeText(SearchActivity.this, searchTerm + " Searched",
                        Toast.LENGTH_LONG).show();
                toolbar.setTitle(searchTerm);

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1234 && resultCode == RESULT_OK) {
            ArrayList<String> matches = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            search.populateEditText(matches.get(0));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    protected void closeSearch() {
        search.hideCircularly(this);
        //if(search.getSearchText().isEmpty())toolbar.setTitle("");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


}