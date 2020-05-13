package com.fammeo.app.adapter.Company;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.fammeo.app.R;
import com.fammeo.app.common.SweetAlertCustom;
import com.fammeo.app.fragment.CompanyFragment;
import com.fammeo.app.model.CompanyRelationSubServiceJ;
import com.fammeo.app.util.UIUtils;

import java.util.ArrayList;
import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class CompanyServiceListAdapter extends RecyclerView.Adapter<CompanyServiceListAdapter.ViewHolder> {
    public static String TAG = CompanyServiceListAdapter.class.getSimpleName();
    private List<CompanyRelationSubServiceJ> mDataSet = new ArrayList<>();
    private Context mContext;
    private CompanyServiceListAdapter.AdapterListener listener;
    private boolean reverseAllAnimations = false;
    private static int currentSelectedIndex = -1;
    private CompanyFragment _thisFragment;

    public CompanyServiceListAdapter(Context mContext, List<CompanyRelationSubServiceJ> messages,
                                     CompanyServiceListAdapter.AdapterListener listener) {
        this.mDataSet = messages;
        this.mContext = mContext;
        initpDialog();
        this.listener = listener;
        setHasStableIds(true);
    }
    private SweetAlertCustom swc;

    protected void initpDialog() {
        if (swc == null)
            swc = new SweetAlertCustom(mContext);
        swc.CreatingLoadingDialog("Loading");
    }

    protected void showpDialog() {
        loading = true;
        swc.ShowLoading();
    }

    protected void hidepDialog() {
        loading = false;
        swc.HideLoading();
    }
    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    @Override
    public long getItemId(int position) {
        return this.getItem(position).Id;
    }



    public interface OnItemClickListener {
        void onClick(CompanyRelationSubServiceJ mMessage);
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {

        public TextView txtServiceName;
        public ViewHolder(View view) {
            super(view);
            txtServiceName = (TextView) view.findViewById(R.id.service_title);

        }

    }

    public void Add(CompanyRelationSubServiceJ mNewData, boolean NotifyChange) {
        mDataSet.add(mNewData);
        if (NotifyChange)
            notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.companyservice_row_list, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final CompanyRelationSubServiceJ message = mDataSet.get(position);
        // displaying text view data
        holder.txtServiceName.setText(message.N);


    }



    boolean loading = false;





    public CompanyRelationSubServiceJ getItem(int position) {
        return mDataSet.get(position);
    }

    public void set(int position, CompanyRelationSubServiceJ nMessage) {
        mDataSet.set(position, nMessage);
    }



    public void removeData(int position) {
        mDataSet.remove(position);
        //listener.onContactDeleted(position);
        resetCurrentIndex();
    }

    private void resetCurrentIndex() {
        currentSelectedIndex = -1;
    }

    public interface AdapterListener {

        void onMessageRowClicked(int position, ViewHolder holder);

        //void onContactDeleted(int position);
    }

    public void ClearData() {
        mDataSet.clear();
    }


    private void animateItem(View view) {
        view.setTranslationY(UIUtils.getScreenHeight((Activity) view.getContext()));
        view.animate()
                .translationY(0)
                .setInterpolator(new DecelerateInterpolator(3.f))
                .setDuration(700)
                .start();

            /*ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            anim.setDuration(500);
            view.startAnimation(anim);*/
    }

}
