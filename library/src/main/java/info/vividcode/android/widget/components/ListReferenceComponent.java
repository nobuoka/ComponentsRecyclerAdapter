package info.vividcode.android.widget.components;

import java.util.Collections;
import java.util.List;

import info.vividcode.android.widget.ComponentsRecyclerAdapter;
import info.vividcode.android.widget.FixedViewBindingTypeSupplier;
import info.vividcode.android.widget.ViewBindingTypeMapper;

public class ListReferenceComponent<T> extends AbstractComponentUnit<T> {

    private List<T> mList = Collections.emptyList();

    public static <T> ListReferenceComponent<T> create(ViewBindingTypeMapper<T> mapper) {
        return new ListReferenceComponent<>(mapper);
    }

    public static <T> ListReferenceComponent<T> create(ComponentsRecyclerAdapter.ViewBindingType<?, T> type) {
        return new ListReferenceComponent<>(new FixedViewBindingTypeSupplier<>(type));
    }

    private ListReferenceComponent(ViewBindingTypeMapper<T> mapper) {
        super(mapper);
    }

    public void setList(List<T> list) {
        mList = list;
    }

    public List<T> getList() {
        return mList;
    }

    @Override
    public int getComponentItemCount() {
        return mList.size();
    }

    @Override
    public T getItem(int componentPosition) {
        return mList.get(componentPosition);
    }

}
