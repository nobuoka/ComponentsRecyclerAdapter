package info.vividcode.android.widget.components;

import info.vividcode.android.widget.Component;
import info.vividcode.android.widget.ComponentsRecyclerAdapter;

/**
 * {@link ComponentsRecyclerAdapter.ComponentObservable} で発生したイベントを監視し、
 * オフセットを付けて別の {@link ComponentsRecyclerAdapter.ComponentObservable} に流すクラス。
 */
public abstract class OffsettingBypassObserver implements ComponentsRecyclerAdapter.ComponentObserver {

    private final ComponentsRecyclerAdapter.ComponentObservable mObservable;

    public OffsettingBypassObserver(ComponentsRecyclerAdapter.ComponentObservable observable) {
        mObservable = observable;
    }

    @Override
    public void onChanged(Component dc) {
        mObservable.notifyChanged();
    }

    protected abstract int calcOffsetCount(Component dc);

    @Override
    public void onItemRangeChanged(Component dc, int positionStart, int itemCount) {
        int offsetCount = calcOffsetCount(dc);
        mObservable.notifyItemRangeChanged(offsetCount + positionStart, itemCount);
    }

    @Override
    public void onItemRangeInserted(Component dc, int positionStart, int itemCount) {
        int offsetCount = calcOffsetCount(dc);
        mObservable.notifyItemRangeInserted(offsetCount + positionStart, itemCount);
    }

    @Override
    public void onItemRangeRemoved(Component dc, int positionStart, int itemCount) {
        int offsetCount = calcOffsetCount(dc);
        mObservable.notifyItemRangeRemoved(offsetCount + positionStart, itemCount);
    }

    @Override
    public void onItemMoved(Component dc, int fromPosition, int toPosition) {
        int offsetCount = calcOffsetCount(dc);
        mObservable.notifyItemMoved(offsetCount + fromPosition, offsetCount + toPosition);
    }

}
