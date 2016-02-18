package info.vividcode.android.widget;

/**
 * {@link ComponentsRecyclerAdapter} によって扱われる項目リストを表すクラス。
 */
public interface Component<T> {

    /**
     * このオブジェクト内の項目の変更を伝える {@link ComponentsRecyclerAdapter.ComponentObservable} を返す。
     * @return 変更を伝えるための {@link ComponentsRecyclerAdapter.ComponentObservable}。
     */
    ComponentsRecyclerAdapter.ComponentObservable getObservable();

    /**
     * このオブジェクトが管理する項目の数を返す。
     * @return 項目の数。
     */
    int getComponentItemCount();

    /**
     * 指定の位置の項目の view type を返す。
     * @param componentPosition 指定の位置。
     * @return View type。
     */
    int getItemViewType(int componentPosition);

    /**
     * 指定の位置の項目を返す。
     * @param componentPosition 指定の位置。
     * @return 指定の位置の項目。
     */
    T getItem(int componentPosition);

    interface Container {
        ComponentAndPosition getComponentAndPosition(int position, ComponentAndPosition dccp);
    }

}
