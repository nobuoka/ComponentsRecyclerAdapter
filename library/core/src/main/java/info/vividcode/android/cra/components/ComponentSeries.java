/*
 * Copyright 2016 NOBUOKA Yu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package info.vividcode.android.cra.components;

import android.support.v7.widget.RecyclerView;

import info.vividcode.android.cra.Component;
import info.vividcode.android.cra.ComponentsRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 複数の {@link Component} を縦列に繋げて管理する {@link Component} のサブクラス。
 * データのセクション分けなどに利用できる。
 */
public class ComponentSeries implements Component {

    private final ComponentsRecyclerAdapter.ComponentObservable mObservable =
            new ComponentsRecyclerAdapter.ComponentObservable(this);
    private final List<Component<?>> mComponents = new ArrayList<>();

    private final ComponentsRecyclerAdapter.ComponentObserver mChildComponentsObserver =
            new OffsettingBypassObserver(mObservable) {
                @Override
                protected int calcOffsetCount(Component dc) {
                    int offsetCount = 0;
                    for (Component component : mComponents) {
                        if (dc == component) break;
                        offsetCount += component.getItemCount();
                    }
                    return offsetCount;
                }
            };

    public static ComponentSeries createWithChildComponents(List<? extends Component<?>> components) {
        ComponentSeries cs = new ComponentSeries();
        for (Component<?> c : components) cs.addChildComponent(c);
        return cs;
    }

    public int numberOfChildComponents() {
        return mComponents.size();
    }

    public void addChildComponent(Component<?> dc) {
        int insertedPosition = getItemCount();
        int insertedCount = dc.getItemCount();
        mComponents.add(dc);
        mObservable.notifyItemRangeInserted(insertedPosition, insertedCount);
        dc.getObservable().registerObserver(mChildComponentsObserver);
    }

    @Override
    public ComponentsRecyclerAdapter.ComponentObservable getObservable() {
        return mObservable;
    }

    <A, R> R callBackForChildComponentItem(
            int positionInThisComponent, ChildComponentItemCallback<A, R> c, A arg) {
        if (positionInThisComponent < 0)
            throw new IndexOutOfBoundsException(
                    "Position must be greater than or equal to zero. (Specified position: " +
                    positionInThisComponent + ")");
        int filteredPos = positionInThisComponent;
        for (Component dc : mComponents) {
            int count = dc.getItemCount();
            if (filteredPos < count) {
                return (R) c.onChildComponentAndPositionFound(dc, filteredPos, arg);
            }
            filteredPos -= count;
        }
        throw new IndexOutOfBoundsException(
                "Position must be smaller than " + getItemCount() + ". " +
                "(Specified position: " + positionInThisComponent + ")");
    }

    @Override
    public int getItemCount() {
        int itemCount = 0;
        for (Component<?> dc : mComponents) {
            itemCount += dc.getItemCount();
        }
        return itemCount;
    }

    @Override
    public void onBindViewHolder(
            RecyclerView.ViewHolder holder, int positionInThisComponent) {
        callBackForChildComponentItem(
                positionInThisComponent, ChildComponentItemCallbacks.CC_ON_BIND_VIEW_HOLDER, holder);
    }

    @Override
    public int getItemViewType(int positionInThisComponent) {
        return callBackForChildComponentItem(
                positionInThisComponent, ChildComponentItemCallbacks.CC_GET_ITEM_VIEW_TYPE,
                ChildComponentItemCallbacks.VOID);
    }

    @Override
    public Object getItem(int positionInThisComponent) {
        return callBackForChildComponentItem(
                positionInThisComponent, ChildComponentItemCallbacks.CC_GET_ITEM,
                ChildComponentItemCallbacks.VOID);
    }

}
