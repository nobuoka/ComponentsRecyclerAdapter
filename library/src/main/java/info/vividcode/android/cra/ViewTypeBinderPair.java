package info.vividcode.android.cra;

import android.support.v7.widget.RecyclerView;

public abstract class ViewTypeBinderPair<D> {

    public abstract ViewType<?> getViewType();

    public abstract Binder<?, D> getBinder();

    public static <D> Builder<D> create() {
        return new ViewTypeBinderPairImpl<>();
    }

    private ViewTypeBinderPair() {}

    public interface Builder<D> {
        <VH extends RecyclerView.ViewHolder> ViewTypeBinderPair<D> set(ViewType<VH> viewType, Binder<VH, D> binder);
    }

    public static class ViewTypeBinderPairImpl<D> extends ViewTypeBinderPair<D> implements Builder<D> {
        private ViewType<?> mViewType;
        private Binder<?, D> mBinder;

        private ViewTypeBinderPairImpl() {}

        public <VH extends RecyclerView.ViewHolder> ViewTypeBinderPair<D> set(ViewType<VH> viewType, Binder<VH, D> binder) {
            mViewType = viewType;
            mBinder = binder;
            return this;
        }

        public ViewType<?> getViewType() {
            return mViewType;
        }

        public Binder<?, D> getBinder() {
            return mBinder;
        }
    }

}
