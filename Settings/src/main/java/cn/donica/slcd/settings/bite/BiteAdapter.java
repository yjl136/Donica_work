package cn.donica.slcd.settings.bite;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.donica.slcd.settings.R;

/**
 * Project Name:   Donica
 * Create By:      aLinGe
 * Create Time:    2017-09-22 15:07
 * Describe:
 */

public class BiteAdapter extends RecyclerView.Adapter<ViewHolder> {
    private Context context;
    private List<BiteEntity> bites;

    public BiteAdapter(Context context, List<BiteEntity> bites) {
        this.context = context;
        this.bites = bites;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.bite_item, parent, false);
        BiteViewHolder vh = new BiteViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        BiteViewHolder vh = (BiteViewHolder) holder;
        BiteEntity entity = bites.get(position);
        vh.nameTv.setText(entity.getName());
        vh.valueTv.setText(entity.getValue());
    }

    @Override
    public int getItemCount() {
        return bites.size();
    }

    public static class BiteViewHolder extends ViewHolder {
        @BindView(R.id.nameTv)
        public TextView nameTv;
        @BindView(R.id.valueTv)
        public TextView valueTv;

        public BiteViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
