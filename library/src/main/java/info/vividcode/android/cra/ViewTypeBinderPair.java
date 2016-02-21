package info.vividcode.android.cra;

import android.support.v7.widget.RecyclerView;

public class ViewTypeBinderPair<D> {

    private ViewType<?> mViewType;
    private Binder<?, D> mBinder;

    public <VH extends RecyclerView.ViewHolder> void set(ViewType<VH> viewType, Binder<VH, D> binder) {
        mViewType = viewType;
        mBinder = binder;
    }

    public ViewType<?> getViewType() {
        return mViewType;
    }

    public Binder<?, D> getBinder() {
        return mBinder;
    }

}
