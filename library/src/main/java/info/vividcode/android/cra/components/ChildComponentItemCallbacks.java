package info.vividcode.android.cra.components;

import android.support.v7.widget.RecyclerView;

import info.vividcode.android.cra.Component;

public class ChildComponentItemCallbacks {

    public static final Void VOID = null;

    public static final ChildComponentItemCallback<RecyclerView.ViewHolder, Integer, Void> CC_ON_BIND_VIEW_HOLDER =
            new ChildComponentItemCallback<RecyclerView.ViewHolder, Integer, Void>() {
                @Override
                public Void onChildComponentAndPositionFound(
                        Component<?> child, int positionInChildComponent, RecyclerView.ViewHolder holder, Integer positionInAllItems) {
                    child.onBindViewHolder(holder, positionInChildComponent, positionInAllItems); return VOID;
                }
            };

    public static final ChildComponentItemCallback<Integer, Void, Integer> CC_GET_ITEM_VIEW_TYPE =
            new ChildComponentItemCallback<Integer, Void, Integer>() {
                @Override
                public Integer onChildComponentAndPositionFound(
                        Component<?> child, int positionInChildComponent, Integer positionInAdapter, Void v) {
                    return child.getItemViewType(positionInChildComponent, positionInAdapter);
                }
            };

    public static final ChildComponentItemCallback<Void, Void, Object> CC_GET_ITEM =
            new ChildComponentItemCallback<Void, Void, Object>() {
                @Override
                public Object onChildComponentAndPositionFound(Component<?> child, int positionInChildComponent, Void v1, Void v2) {
                    return child.getItem(positionInChildComponent);
                }
            };

}
