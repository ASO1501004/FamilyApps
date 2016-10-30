package jp.ac.asojuku.st.familyapps;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class AnbayasiViewHolder extends RecyclerView.ViewHolder{
    View base;
    TextView textViewComment;

    public AnbayasiViewHolder(View v){
        super(v);
        this.base = v;
        this.textViewComment = (TextView) v.findViewById(R.id.comment);
    }
}
