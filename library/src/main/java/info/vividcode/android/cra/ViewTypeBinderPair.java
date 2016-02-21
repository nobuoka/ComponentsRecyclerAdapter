package info.vividcode.android.cra;

import android.support.v7.widget.RecyclerView;

public class ViewTypeBinderPair<VH extends RecyclerView.ViewHolder, D> {

    public final ViewType<VH> viewType;
    public final Binder<VH, D> binder;

    public static <VH extends RecyclerView.ViewHolder, D> ViewTypeBinderPair<VH, D> create(
            ViewType<VH> viewType, Binder<VH, D> binder) {
        return new ViewTypeBinderPair<>(viewType, binder);
    }

    ViewTypeBinderPair(ViewType<VH> viewType, Binder<VH, D> binder) {
        this.viewType = viewType;
        this.binder = binder;
    }

}
