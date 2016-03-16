package info.vividcode.android.cra.components;

import android.support.v7.widget.RecyclerView;

import info.vividcode.android.cra.Component;

public class ChildComponentItemCallbacks {

    public static final Void VOID = null;

    public static final ChildComponentItemCallback<RecyclerView.ViewHolder, Void> CC_ON_BIND_VIEW_HOLDER =
            new ChildComponentItemCallback<RecyclerView.ViewHolder, Void>() {
                @Override
                public Void onChildComponentAndPositionFound(
                        Component<?> child, int positionInChildComponent, RecyclerView.ViewHolder holder) {
                    child.onBindViewHolder(holder, positionInChildComponent); return VOID;
                }
            };

    public static final ChildComponentItemCallback<Void, Integer> CC_GET_ITEM_VIEW_TYPE =
            new ChildComponentItemCallback<Void, Integer>() {
                @Override
                public Integer onChildComponentAndPositionFound(
                        Component<?> child, int positionInChildComponent, Void v) {
                    return child.getItemViewType(positionInChildComponent);
                }
            };

    public static final ChildComponentItemCallback<Void, Object> CC_GET_ITEM =
            new ChildComponentItemCallback<Void, Object>() {
                @Override
                public Object onChildComponentAndPositionFound(Component<?> child, int positionInChildComponent, Void v) {
                    return child.getItem(positionInChildComponent);
                }
            };

}
