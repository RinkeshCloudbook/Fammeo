package com.fammeo.app.fragment.FammeoFragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.fammeo.app.R;
import com.fammeo.app.adapter.fammeoAdapter.UserSpinnerAdapter;
import com.fammeo.app.animation.ViewAnimation;
import com.fammeo.app.util.Tools;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConversationsFragment extends Fragment {

    View mView;
    private ImageButton bt_toggle_input;
    private LinearLayout lyt_expand_input;
    private LinearLayout lyt_all,lyt_newCon,lyt_favorite,lyt_cat;
    private TextView txt_user;
    public ConversationsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_conversations, container, false);

        bt_toggle_input = mView.findViewById(R.id.bt_toggle_input);
        lyt_expand_input = mView.findViewById(R.id.lyt_expand_input);
        lyt_all = mView.findViewById(R.id.lyt_all);
        lyt_newCon = mView.findViewById(R.id.lyt_newCon);
        lyt_favorite = mView.findViewById(R.id.lyt_favorite);
        lyt_cat = mView.findViewById(R.id.lyt_cat);
        txt_user = mView.findViewById(R.id.txt_user);

        lyt_expand_input.setVisibility(View.GONE);
        ((LinearLayout) mView.findViewById(R.id.lin_user)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSectionInput(bt_toggle_input);
            }
        });
        bt_toggle_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSectionInput(bt_toggle_input);
            }
        });

        lyt_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("TEST","All");
                lyt_expand_input.setVisibility(View.GONE);
                txt_user.setText("All");
            }
        });
        lyt_newCon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("TEST","Conversation");
                lyt_expand_input.setVisibility(View.GONE);
                txt_user.setText("New Conversation");
            }
        });
        lyt_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("TEST","Favorite");
                lyt_expand_input.setVisibility(View.GONE);
                txt_user.setText("Favorite");
                ((LinearLayout) mView.findViewById(R.id.lin_allList)).setVisibility(View.GONE);
                ((LinearLayout) mView.findViewById(R.id.lin_catList)).setVisibility(View.GONE);
                ((TextView) mView.findViewById(R.id.txt_converContent)).setVisibility(View.VISIBLE);
                ((TextView) mView.findViewById(R.id.txt_converContent)).setText("You have no Conversations");
            }
        });
        lyt_cat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("TEST","Category");
                lyt_expand_input.setVisibility(View.GONE);
                txt_user.setText("Category");
                ((LinearLayout) mView.findViewById(R.id.lin_allList)).setVisibility(View.GONE);
                ((LinearLayout) mView.findViewById(R.id.lin_catList)).setVisibility(View.VISIBLE);
                ((TextView) mView.findViewById(R.id.txt_converContent)).setVisibility(View.GONE);
            }
        });


        return mView;
    }

    private void toggleSectionInput(ImageButton bt_toggle_input) {
        boolean show = toggleArrow(bt_toggle_input);
        if (show) {
            ViewAnimation.expand(lyt_expand_input, new ViewAnimation.AnimListener() {
                @Override
                public void onFinish() {
                    //Tools.nestedScrollTo(nested_scroll_view, lyt_expand_input);
                }
            });
        } else {
            ViewAnimation.collapse(lyt_expand_input);
        }
    }

    private boolean toggleArrow(ImageButton bt_toggle_input) {
        if (bt_toggle_input.getRotation() == 0) {
            bt_toggle_input.animate().setDuration(200).rotation(180);
            return true;
        } else {
            bt_toggle_input.animate().setDuration(200).rotation(0);
            return false;
        }
    }
}
