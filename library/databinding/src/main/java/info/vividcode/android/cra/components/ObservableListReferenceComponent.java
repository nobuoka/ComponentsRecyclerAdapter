package info.vividcode.android.cra.components;

import android.databinding.ObservableList;
import android.support.annotation.Nullable;

import info.vividcode.android.cra.ComponentsRecyclerAdapter;
import info.vividcode.android.cra.ViewTypeBinderPairProvider;

public class ObservableListReferenceComponent<T> extends AbstractLeafComponent<T> {

    @Nullable
    private ObservableList<T> mList;

    public ObservableListReferenceComponent(ViewTypeBinderPairProvider<T> provider) {
        super(provider);
    }

    public void setList(@Nullable ObservableList<T> list) {
        if (mList != null) {
            mList.removeOnListChangedCallback(mCallback);
        }
        mList = list;
        getObservable().notifyChanged();
        if (mList != null) {
            mList.addOnListChangedCallback(mCallback);
        }
    }

    @Nullable
    public ObservableList<T> getList() {
        return mList;
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    @Override
    public T getItem(int positionInThisComponent) {
        if (mList == null)
            throw new IndexOutOfBoundsException("There is no item. (index : " + positionInThisComponent + ")");
        return mList.get(positionInThisComponent);
    }

    private final ObservableList.OnListChangedCallback<ObservableList<T>> mCallback = new ObservableList.OnListChangedCallback<ObservableList<T>>() {
        @Override
        public void onChanged(ObservableList sender) {
            getObservable().notifyChanged();
        }

        @Override
        public void onItemRangeChanged(ObservableList sender, int positionStart, int itemCount) {
            getObservable().notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeInserted(ObservableList sender, int positionStart, int itemCount) {
            getObservable().notifyItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(ObservableList sender, int fromPosition, int toPosition, int itemCount) {
            ComponentsRecyclerAdapter.ComponentObservable observable = getObservable();
            if (fromPosition < toPosition) {
                // Sequentially from behind.
                for (int i = itemCount - 1; 0 <= i; i--) {
                    observable.notifyItemMoved(fromPosition + i, toPosition + i);
                }
            } else if (toPosition < fromPosition) {
                // Sequentially from front.
                for (int i = 0; i < itemCount; i++) {
                    observable.notifyItemMoved(fromPosition + i, toPosition + i);
                }
            }
            // If `toPosition` equals to `fromPosition`, there is no need to send notifications.
        }

        @Override
        public void onItemRangeRemoved(ObservableList sender, int positionStart, int itemCount) {
            getObservable().notifyItemRangeRemoved(positionStart, itemCount);
        }
    };

}
