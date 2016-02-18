package info.vividcode.android.app.example.cra;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

import info.vividcode.android.app.example.cra.holders.ContentViewHolder;
import info.vividcode.android.app.example.cra.holders.SectionTitleViewHolder;

public class ExampleAdapterOldStyle extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM_VIEW_TYPE_CONTENT = 1;
    private static final int ITEM_VIEW_TYPE_SECTION_TITLE = 2;

    private List<String> mStringList;
    private List<ListItem> mItemContentList;

    public ExampleAdapterOldStyle(List<String> stringList, List<ListItem> itemContentList) {
        mStringList = stringList;
        mItemContentList = itemContentList;
    }

    @Override
    public int getItemCount() {
        return mStringList.size() + mItemContentList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ITEM_VIEW_TYPE_CONTENT:
                return ContentViewHolder.FACTORY.createViewHolder(parent);
            case ITEM_VIEW_TYPE_SECTION_TITLE:
                return SectionTitleViewHolder.FACTORY.createViewHolder(parent);
            default:
                throw new IllegalArgumentException("Invalid item view type: " + viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position < mStringList.size()) {
            String item = mStringList.get(position);
            ((ContentViewHolder) holder).updateText(item);
        } else {
            int posInStringList = position - mStringList.size();
            int type = getStringListItemViewType(posInStringList);
            ListItem item = mItemContentList.get(posInStringList);
            if (type == ITEM_VIEW_TYPE_SECTION_TITLE) {
                ((SectionTitleViewHolder) holder).updateText(((ListItem.SectionHeader) item).title);
            } else {
                ((ContentViewHolder) holder).updateText(((ListItem.Content) item).data);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position < mStringList.size()) {
            return ITEM_VIEW_TYPE_CONTENT;
        } else {
            return getStringListItemViewType(position - mStringList.size());
        }
    }

    private int getStringListItemViewType(int positionInStringList) {
        ListItem item = mItemContentList.get(positionInStringList);
        return item.getType() == ListItem.Type.SECTION_HEADER ?
                ITEM_VIEW_TYPE_SECTION_TITLE :
                ITEM_VIEW_TYPE_CONTENT;
    }

}
