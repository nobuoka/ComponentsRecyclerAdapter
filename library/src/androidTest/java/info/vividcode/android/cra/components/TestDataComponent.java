package info.vividcode.android.cra.components;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import info.vividcode.android.cra.ComponentsRecyclerAdapter;

/**
 * テスト用のクラス。
 */
class TestDataComponent<T> implements info.vividcode.android.cra.Component {

    public TestDataComponent() {
    }

    public TestDataComponent(Collection<T> items) {
        this();
        mItems = new ArrayList<>(items);
    }

    public TestDataComponent(Collection<T> items, Collection<Integer> itemViewTypes) {
        this();
        mItems = new ArrayList<>(items);
        this.itemViewTypes = new ArrayList<>(itemViewTypes);
    }

    ComponentsRecyclerAdapter.ComponentObservable mObservable = new ComponentsRecyclerAdapter.ComponentObservable(this);

    @Override
    public ComponentsRecyclerAdapter.ComponentObservable getObservable() {
        return mObservable;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int positionInThisComponent) {
        // Do nothing.
    }

    public int itemViewType = 0;
    public List<Integer> itemViewTypes = null;
    private List<T> mItems;
    public void setItems(Collection<T> items) {
        setItems(items, true);
    }
    public void setItems(Collection<T> items, boolean doNotifyChanged) {
        mItems = new ArrayList<>(items);
        if (doNotifyChanged) mObservable.notifyChanged();
    }

    @Override
    public int getItemCount() {
        return (mItems != null ? mItems.size() : 0);
    }

    @Override
    public int getItemViewType(int positionInThisComponent) {
        return itemViewTypes != null && positionInThisComponent < itemViewTypes.size() ?
                itemViewTypes.get(positionInThisComponent) : itemViewType;
    }

    @Override
    public T getItem(int componentPosition) {
        return mItems.get(componentPosition);
    }

}
