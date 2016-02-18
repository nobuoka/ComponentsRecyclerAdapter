package info.vividcode.android.widget.components;

import android.support.annotation.NonNull;

import info.vividcode.android.widget.Component;
import info.vividcode.android.widget.ComponentsRecyclerAdapter;
import info.vividcode.android.widget.ViewBindingTypeMapper;

public abstract class AbstractComponentUnit<T> implements Component<T> {

    private final ComponentsRecyclerAdapter.ComponentObservable mObservable = new ComponentsRecyclerAdapter.ComponentObservable(this);

    @NonNull
    private final ViewBindingTypeMapper<? super T> mItemToViewTypeMapper;

    public AbstractComponentUnit(@NonNull ViewBindingTypeMapper<? super T> itemToViewTypeMapper) {
        mItemToViewTypeMapper = itemToViewTypeMapper;
    }

    @Override
    public final ComponentsRecyclerAdapter.ComponentObservable getObservable() {
        return mObservable;
    }

    @Override
    public final int getItemViewType(int componentPosition) {
        return mItemToViewTypeMapper.getViewType(getItem(componentPosition), componentPosition).viewType;
    }

}
