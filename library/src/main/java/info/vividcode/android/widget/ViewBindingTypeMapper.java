package info.vividcode.android.widget;

/**
 * 項目を受け取ってそれに応じた view type を返すメソッドを持つインターフェイス。
 * {@link Component} を実装する際に使用される。
 * @param <T> 項目の型。
 */
public interface ViewBindingTypeMapper<T> {
    ComponentsRecyclerAdapter.ViewBindingType<?, T> getViewType(T item, int componentPosition);
}
