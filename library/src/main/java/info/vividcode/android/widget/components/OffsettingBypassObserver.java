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

package info.vividcode.android.widget.components;

import info.vividcode.android.widget.Component;
import info.vividcode.android.widget.ComponentsRecyclerAdapter;

/**
 * {@link ComponentsRecyclerAdapter.ComponentObservable} で発生したイベントを監視し、
 * オフセットを付けて別の {@link ComponentsRecyclerAdapter.ComponentObservable} に流すクラス。
 */
public abstract class OffsettingBypassObserver implements ComponentsRecyclerAdapter.ComponentObserver {

    private final ComponentsRecyclerAdapter.ComponentObservable mObservable;

    public OffsettingBypassObserver(ComponentsRecyclerAdapter.ComponentObservable observable) {
        mObservable = observable;
    }

    @Override
    public void onChanged(Component dc) {
        mObservable.notifyChanged();
    }

    protected abstract int calcOffsetCount(Component dc);

    @Override
    public void onItemRangeChanged(Component dc, int positionStart, int itemCount) {
        int offsetCount = calcOffsetCount(dc);
        mObservable.notifyItemRangeChanged(offsetCount + positionStart, itemCount);
    }

    @Override
    public void onItemRangeInserted(Component dc, int positionStart, int itemCount) {
        int offsetCount = calcOffsetCount(dc);
        mObservable.notifyItemRangeInserted(offsetCount + positionStart, itemCount);
    }

    @Override
    public void onItemRangeRemoved(Component dc, int positionStart, int itemCount) {
        int offsetCount = calcOffsetCount(dc);
        mObservable.notifyItemRangeRemoved(offsetCount + positionStart, itemCount);
    }

    @Override
    public void onItemMoved(Component dc, int fromPosition, int toPosition) {
        int offsetCount = calcOffsetCount(dc);
        mObservable.notifyItemMoved(offsetCount + fromPosition, offsetCount + toPosition);
    }

}
