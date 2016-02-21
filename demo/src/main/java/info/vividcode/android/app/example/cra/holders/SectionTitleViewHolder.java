package info.vividcode.android.app.example.cra.holders;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import info.vividcode.android.app.example.cra.R;
import info.vividcode.android.cra.ViewHolderFactory;

public class SectionTitleViewHolder extends RecyclerView.ViewHolder {

    public static final ViewHolderFactory<SectionTitleViewHolder> FACTORY =
            new ViewHolderFactory<SectionTitleViewHolder>() {
                @Override
                public SectionTitleViewHolder createViewHolder(ViewGroup parent) {
                    TextView v = (TextView) LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.item_section_title, parent, false);
                    return new SectionTitleViewHolder(v);
                }
            };

    private final TextView mTextView;

    public SectionTitleViewHolder(TextView itemView) {
        super(itemView);
        mTextView = itemView;
    }

    public void updateSectionTitle(String text) {
        mTextView.setText(text);
    }

}
