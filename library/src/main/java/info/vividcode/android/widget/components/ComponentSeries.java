package info.vividcode.android.widget.components;

import info.vividcode.android.widget.Component;
import info.vividcode.android.widget.ComponentAndPosition;
import info.vividcode.android.widget.ComponentsRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 複数の {@link Component} を縦列に繋げて管理する {@link Component} のサブクラス。
 * データのセクション分けなどに利用できる。
 */
public class ComponentSeries implements Component<Object>, Component.Container {

    private final ComponentsRecyclerAdapter.ComponentObservable mObservable = new ComponentsRecyclerAdapter.ComponentObservable(this);
    private final List<Component<?>> mComponents = new ArrayList<>();

    private final ComponentsRecyclerAdapter.ComponentObserver mChildrenObserver = new OffsettingBypassObserver(mObservable) {
        @Override
        protected int calcOffsetCount(Component dc) {
            int offsetCount = 0;
            for (Component component : mComponents) {
                if (dc == component) break;
                offsetCount += component.getComponentItemCount();
            }
            return offsetCount;
        }
    };

    public int numberOfChildComponents() {
        return mComponents.size();
    }

    public void addChildComponent(Component<?> dc) {
        int insertedPosition = getComponentItemCount();
        int insertedCount = dc.getComponentItemCount();
        mComponents.add(dc);
        mObservable.notifyItemRangeInserted(insertedPosition, insertedCount);
        dc.getObservable().registerObserver(mChildrenObserver);
    }

    @Override
    public ComponentsRecyclerAdapter.ComponentObservable getObservable() {
        return mObservable;
    }

    @Override
    public int getComponentItemCount() {
        int itemCount = 0;
        for (Component<?> dc : mComponents) {
            itemCount += dc.getComponentItemCount();
        }
        return itemCount;
    }

    @Override
    public int getItemViewType(int componentPosition) {
        ComponentAndPosition dccp = getChildDataComponentAndComponentPosition(componentPosition);
        return dccp.component.getItemViewType(dccp.componentPosition);
    }

    @Override
    public Object getItem(int componentPosition) {
        ComponentAndPosition dccp = getChildDataComponentAndComponentPosition(componentPosition);
        return dccp.component.getItem(dccp.componentPosition);
    }

    @Override
    public ComponentAndPosition getComponentAndPosition(int position, ComponentAndPosition dccp) {
        ComponentAndPosition childDccp = getChildDataComponentAndComponentPosition(position, dccp);
        if (childDccp.component instanceof Container) {
            Container child = (Container) childDccp.component;
            return child.getComponentAndPosition(childDccp.componentPosition, dccp);
        } else {
            return childDccp;
        }
    }

    public ComponentAndPosition getChildDataComponentAndComponentPosition(int position, ComponentAndPosition dccp) {
        int filteredPos = position;
        for (Component dc : mComponents) {
            int count = dc.getComponentItemCount();
            if (filteredPos < count) {
                dccp.component = dc;
                dccp.componentPosition = filteredPos;
                return dccp;
            }
            filteredPos -= count;
        }
        throw new RuntimeException();
    }

    private ComponentAndPosition getChildDataComponentAndComponentPosition(int position) {
        return getChildDataComponentAndComponentPosition(position, new ComponentAndPosition());
    }

}
