package info.vividcode.android.widget;

import android.database.Observable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class ComponentsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface ComponentObserver {
        void onChanged(Component dc);
        void onItemRangeChanged(Component dc, int positionStart, int itemCount);
        void onItemRangeInserted(Component dc, int positionStart, int itemCount);
        void onItemRangeRemoved(Component dc, int positionStart, int itemCount);
        void onItemMoved(Component dc, int fromPosition, int toPosition);
    }

    public static class ComponentObservable extends Observable<ComponentObserver> {
        private final Component mOwner;
        public ComponentObservable(Component owner) {
            mOwner = owner;
        }

        public void notifyChanged() {
            for (int i = mObservers.size() - 1; i >= 0; --i)
                mObservers.get(i).onChanged(mOwner);
        }

        public void notifyItemRangeChanged(int positionStart, int itemCount) {
            for (int i = mObservers.size() - 1; i >= 0; --i)
                mObservers.get(i).onItemRangeChanged(mOwner, positionStart, itemCount);
        }

        public void notifyItemRangeInserted(int positionStart, int itemCount) {
            for (int i = mObservers.size() - 1; i >= 0; --i)
                mObservers.get(i).onItemRangeInserted(mOwner, positionStart, itemCount);
        }

        public void notifyItemRangeRemoved(int positionStart, int itemCount) {
            for (int i = mObservers.size() - 1; i >= 0; --i)
                mObservers.get(i).onItemRangeRemoved(mOwner, positionStart, itemCount);
        }

        public void notifyItemMoved(int fromPosition, int toPosition) {
            for (int i = mObservers.size() - 1; i >= 0; --i)
                mObservers.get(i).onItemMoved(mOwner, fromPosition, toPosition);
        }
    }

    public interface ViewHolderFactory<VH extends RecyclerView.ViewHolder> {
        VH createViewHolder(ViewGroup parent);
    }

    /**
     * {@link RecyclerView.Adapter} の中で指定の view type のデータを View に反映する。
     * View の種類ごとにこのインターフェイスを実装したオブジェクトが用意される。
     * @param <VH> {@link android.support.v7.widget.RecyclerView.ViewHolder} の具象クラス。
     */
    public interface Binder<VH extends RecyclerView.ViewHolder, D> {
        void bindViewHolder(VH holder, Component<D> item, int positionInDataComponent, int positionInAllItems);
    }

    public static class ViewType<VH extends RecyclerView.ViewHolder> {
        public final int value;
        private ViewType(int value) {
            this.value = value;
        }
    }

    public static class ViewBindingType<VH extends RecyclerView.ViewHolder, D> {
        public final int viewType;
        private ViewBindingType(int viewType) {
            this.viewType = viewType;
        }
    }

    private final Map<Integer, ViewHolderFactory<?>> mViewTypeToFactoryMap = new HashMap<>();
    private final Map<Integer, Map<Class<?>, Binder<? extends RecyclerView.ViewHolder, ?>>> mViewTypeToBinderMap = new HashMap<>();
    private Component<?> mComponent;

    public Component<?> getComponent() {
        return mComponent;
    }

    public void setComponent(Component<?> dc) {
        if (mComponent != null) {
            int oldCount = mComponent.getComponentItemCount();
            mComponent = null;
            notifyItemRangeRemoved(0, oldCount);
        }

        mComponent = dc;
        notifyItemRangeInserted(0, mComponent.getComponentItemCount());
        mComponent.getObservable().registerObserver(new ComponentObserver() {
            @Override
            public void onChanged(Component dc) {
                notifyDataSetChanged();
            }

            @Override
            public void onItemRangeChanged(Component dc, int positionStart, int itemCount) {
                notifyItemRangeChanged(positionStart, itemCount);
            }

            @Override
            public void onItemRangeInserted(Component dc, int positionStart, int itemCount) {
                notifyItemRangeInserted(positionStart, itemCount);
            }

            @Override
            public void onItemRangeRemoved(Component dc, int positionStart, int itemCount) {
                notifyItemRangeRemoved(positionStart, itemCount);
            }

            @Override
            public void onItemMoved(Component dc, int fromPosition, int toPosition) {
                notifyItemMoved(fromPosition, toPosition);
            }
        });
    }

    private int viewTypeCounter = 1;

    public <VH extends RecyclerView.ViewHolder> ViewType<VH> addViewHolderFactory(
            ViewHolderFactory<VH> factory) {
        int viewType = viewTypeCounter;
        viewTypeCounter += 1;
        mViewTypeToFactoryMap.put(viewType, factory);
        return new ViewType<>(viewType);
    }

    private static Class<?> getClassFromType(Type type) {
        if (type instanceof Class) {
            return (Class) type;
        } else if (type instanceof ParameterizedType) {
            return getClassFromType(((ParameterizedType) type).getRawType());
        } else {
            throw new IllegalArgumentException("Unknown type : " + type.getClass());
        }
    }

    public <VH extends RecyclerView.ViewHolder, D> ViewBindingType<VH, D> addBinder(
            ViewType<VH> viewType, Binder<VH, D> binder) {
        Type binderDataType = null;
        Type[] binderSuperInterfaces = binder.getClass().getGenericInterfaces();
        for (Type t : binderSuperInterfaces) {
            if (t instanceof ParameterizedType) {
                ParameterizedType pt = (ParameterizedType) t;
                if (Binder.class.equals(pt.getRawType())) {
                    binderDataType = pt.getActualTypeArguments()[1];
                    break;
                }
            }
        }
        Class<?> bindingDataClass = getClassFromType(binderDataType);

        Map<Class<?>, Binder<? extends RecyclerView.ViewHolder, ?>> classBinderMap;
        if (mViewTypeToBinderMap.containsKey(viewType.value)) {
            classBinderMap = mViewTypeToBinderMap.get(viewType.value);
        } else {
            classBinderMap = new HashMap<>();
            mViewTypeToBinderMap.put(viewType.value, classBinderMap);
        }
        classBinderMap.put(bindingDataClass, binder);
        return new ViewBindingType<>(viewType.value);
    }

    @Override
    public final RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (!mViewTypeToFactoryMap.containsKey(viewType)) {
            throw new IllegalArgumentException("Not handled view type: " + viewType);
        }
        ViewHolderFactory<?> factory = mViewTypeToFactoryMap.get(viewType);
        return factory.createViewHolder(parent);
    }

    @Override
    // handler が扱う ViewHolder の型について関知しないが、生成時に正しい型のオブジェクトが生成されて
    // いるはずなので、unchecked 警告を抑制する。
    @SuppressWarnings("unchecked")
    public final void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = holder.getItemViewType();
        if (!mViewTypeToBinderMap.containsKey(viewType)) {
            throw new IllegalArgumentException("Not handled view type: " + viewType);
        }
        Map<Class<?>, Binder<? extends RecyclerView.ViewHolder, ?>> classBinderMap = mViewTypeToBinderMap.get(viewType);

        ComponentAndPosition cap = ComponentAndPosition.getFromComponent(mComponent, position, new ComponentAndPosition());
        Object item = cap.component.getItem(cap.componentPosition);

        Binder<? extends RecyclerView.ViewHolder, ?> binder = null;
        for (Class<?> c : classBinderMap.keySet()) {
            if (c.isInstance(item)) {
                binder = classBinderMap.get(c);
                break;
            }
        }
        if (binder == null) {
            throw new IllegalStateException();
        }
        ((Binder<RecyclerView.ViewHolder, ?>) binder).bindViewHolder(holder, cap.component, cap.componentPosition, position);
    }

    @Override
    public final int getItemCount() {
        return (mComponent != null ? mComponent.getComponentItemCount() : 0);
    }

    @Override
    public final int getItemViewType(int position) {
        return mComponent.getItemViewType(position);
    }

    /**
     * {@link OnItemClickListener} をラップして {@link View.OnClickListener} として扱えるオブジェクトを生成する。
     */
    protected <T> View.OnClickListener wrapOnItemClickListener(
            Class<T> targetItemClass, OnItemClickListener<T> listener) {
        return new OnItemClickListenerAdapter<>(targetItemClass, listener);
    }

    /**
     * {@link OnCreateItemContextMenuListener} をラップして {@link View.OnCreateContextMenuListener}
     * として扱えるオブジェクトを生成する。
     */
    protected <T> View.OnCreateContextMenuListener wrapOnCreateItemContextMenuListener(
            Class<T> targetItemClass, OnCreateItemContextMenuListener<T> listener) {
        return new OnCreateItemContextMenuListenerAdapter<>(targetItemClass, listener);
    }

    /**
     * 項目クリック時のイベントリスナ。
     */
    public interface OnItemClickListener<T> {
        /**
         * {@link RecyclerView} の各項目がタップされたときに呼ばれる。
         * @param item タップされた項目のデータ。 データの型が期待するものであることは呼び出し側が保証する。
         * @param adapterPosition タップされた項目の Adapter 上の位置。
         *         {@link RecyclerView#NO_POSITION} でないことは呼び出し側が保証する。
         * @param view タップされた View。
         */
        void onItemClick(T item, int adapterPosition, View view);
    }

    /**
     * 項目のコンテキストメニュー作成のイベントリスナ。
     */
    public interface OnCreateItemContextMenuListener<T> {
        /**
         * {@link RecyclerView} の各項目に対してコンテキストメニューの作成が行われようとしたときに呼ばれる。
         * @param item タップされた項目のデータ。 データの型が期待するものであることは呼び出し側が保証する。
         * @param adapterPosition タップされた項目の Adapter 上の位置。
         *         {@link RecyclerView#NO_POSITION} でないことは呼び出し側が保証する。
         * @param menu 作成対象のコンテキストメニュー。
         * @param v コンテキストメニュー作成対象の View。
         * @param menuInfo コンテキストメニューの追加情報。
         */
        void onCreateItemContextMenu(
                T item, int adapterPosition, ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo);
    }

    /**
     * {@link OnItemClickListener} をラップして {@link View.OnClickListener} として使えるようにするクラス。
     */
    private class OnItemClickListenerAdapter<T> implements View.OnClickListener {
        private final Class<T> mTargetItemClass;
        private final OnItemClickListener<T> mListener;
        public OnItemClickListenerAdapter(Class<T> targetItemClass, OnItemClickListener<T> listener) {
            mTargetItemClass = targetItemClass;
            mListener = listener;
        }
        @Override
        public void onClick(View v) {
            View recyclerViewChild = findRecyclerViewChildInAncestors(v);
            if (recyclerViewChild == null) return;

            RecyclerView recyclerView = (RecyclerView) recyclerViewChild.getParent();
            int pos = recyclerView.getChildAdapterPosition(recyclerViewChild);
            if (pos == RecyclerView.NO_POSITION) return;

            Object item = getComponent().getItem(pos);
            if (mTargetItemClass.isInstance(item)) {
                mListener.onItemClick(mTargetItemClass.cast(item), pos, v);
            }
        }
    }

    /**
     * {@link OnCreateItemContextMenuListener} をラップして {@link View.OnCreateContextMenuListener} として使えるようにするクラス。
     */
    private class OnCreateItemContextMenuListenerAdapter<T> implements View.OnCreateContextMenuListener {
        private final Class<T> mTargetItemClass;
        private final OnCreateItemContextMenuListener<T> mListener;
        public OnCreateItemContextMenuListenerAdapter(Class<T> targetItemClass, OnCreateItemContextMenuListener<T> listener) {
            mTargetItemClass = targetItemClass;
            mListener = listener;
        }
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            View recyclerViewChild = findRecyclerViewChildInAncestors(v);
            if (recyclerViewChild == null) return;

            RecyclerView recyclerView = (RecyclerView) recyclerViewChild.getParent();
            int pos = recyclerView.getChildAdapterPosition(recyclerViewChild);
            if (pos == RecyclerView.NO_POSITION) return;

            Object item = getComponent().getItem(pos);
            if (mTargetItemClass.isInstance(item)) {
                mListener.onCreateItemContextMenu(mTargetItemClass.cast(item), pos, menu, v, menuInfo);
            }
        }
    }

    /**
     * View の先祖をたどって {@link RecyclerView} を探す。
     * @param v {@link RecyclerView} の子孫であろう View。
     * @return 渡された view の先祖に {@link RecyclerView} がある場合は、その子。 なければ {@code null}。
     */
    @Nullable
    private static View findRecyclerViewChildInAncestors(View v) {
        View c = v;
        while (true) {
            ViewParent p = c.getParent();
            if (p instanceof RecyclerView) {
                // RecyclerView を発見したので終了。
                return c;
            } else if (p instanceof View) {
                // さらに 1 個上の親を調査。
                c = (View) p;
            } else {
                // これ以上親をたどってもしょうがないので終了。
                return null;
            }
        }
    }

}
