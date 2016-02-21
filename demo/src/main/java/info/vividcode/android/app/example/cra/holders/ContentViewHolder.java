package info.vividcode.android.app.example.cra.holders;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import info.vividcode.android.app.example.cra.R;
import info.vividcode.android.cra.ViewHolderFactory;

public class ContentViewHolder extends RecyclerView.ViewHolder {

    public static final ViewHolderFactory<ContentViewHolder> FACTORY =
            new ViewHolderFactory<ContentViewHolder>() {
                @Override
                public ContentViewHolder createViewHolder(ViewGroup parent) {
                    TextView v = (TextView) LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.item_content, parent, false);
                    return new ContentViewHolder(v);
                }
            };

    private final TextView mTextView;

    public ContentViewHolder(TextView itemView) {
        super(itemView);
        mTextView = itemView;
    }

    public void updateText(String text) {
        mTextView.setText(text);
    }

}
