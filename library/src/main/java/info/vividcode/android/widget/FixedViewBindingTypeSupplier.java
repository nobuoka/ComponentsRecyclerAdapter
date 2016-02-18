package info.vividcode.android.widget;

/** 常に固定の値を返す {@link ViewBindingTypeMapper} の実装。 */
public class FixedViewBindingTypeSupplier<T> implements ViewBindingTypeMapper<T> {

    private final ComponentsRecyclerAdapter.ViewBindingType<?, T> mViewType;

    public FixedViewBindingTypeSupplier(ComponentsRecyclerAdapter.ViewBindingType<?, T> vbType) {
        mViewType = vbType;
    }

    @Override
    public ComponentsRecyclerAdapter.ViewBindingType<?, T> getViewType(Object item, int componentPosition) {
        return mViewType;
    }

}
