package com.siti.renrenlai.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.CardProvider;
import com.dexafree.materialList.listeners.RecyclerItemClickListener;
import com.dexafree.materialList.view.MaterialListView;
import com.siti.renrenlai.R;
import com.siti.renrenlai.ui.SearchActivity;
import com.siti.renrenlai.view.FragmentBase;
import com.siti.renrenlai.view.HeaderLayout.onRightImageButtonClickListener;
import com.squareup.picasso.RequestCreator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dong on 3/22/2016.
 */
public class FindFragment extends FragmentBase {

    private View view;
    private MaterialListView mListView;
    private Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_find,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();
    }

    private void initView() {

        initTopBarForRight("发现", R.drawable.ic_action_action_search, new onRightImageButtonClickListener() {
            @Override
            public void onClick() {
                startAnimActivity(SearchActivity.class);
            }
        });

        mContext = getActivity();
        mListView = (MaterialListView) findViewById(R.id.material_listview);

        fillArray();

        // Add the ItemTouchListener
        mListView.addOnItemTouchListener(new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull Card card, int position) {
                Log.d("CARD_TYPE", "" + card.getTag());
            }

            @Override
            public void onItemLongClick(@NonNull Card card, int position) {
                Log.d("LONG_CLICK", "" + card.getTag());
            }
        });
    }

    private void fillArray() {
        List<Card> cards = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            cards.add(getRandomCard(i));
        }
        mListView.getAdapter().addAll(cards);
    }


    private Card getRandomCard(final int position) {
        String title = "Card number " + (position + 1);
        String description = "Lorem ipsum dolor sit amet";

                return new Card.Builder(mContext)
                .setTag("BASIC_IMAGE_BUTTON_CARD")
                .withProvider(new CardProvider<>())
                .setLayout(R.layout.material_basic_image_buttons_card_layout)
                .setTitle(title)
                .setTitleGravity(Gravity.END)
                .setDescription(description)
                .setDescriptionGravity(Gravity.END)
                .setDrawable(R.drawable.dog)
                .setDrawableConfiguration(new CardProvider.OnImageConfigListener() {
                    @Override
                    public void onImageConfigure(@NonNull RequestCreator requestCreator) {
                        requestCreator.fit();
                    }
                })
                .endConfig()
                .build();

    }

}
