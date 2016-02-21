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

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import info.vividcode.android.cra.Binder;
import info.vividcode.android.cra.Component;
import info.vividcode.android.cra.ComponentsRecyclerAdapter;
import info.vividcode.android.cra.ViewTypeBinderPair;
import info.vividcode.android.cra.ViewTypeBinderPairProvider;

public abstract class AbstractLeafComponent<T> implements Component<T> {

    private final ComponentsRecyclerAdapter.ComponentObservable mObservable = new ComponentsRecyclerAdapter.ComponentObservable(this);

    @NonNull
    public final ViewTypeBinderPairProvider<T> viewTypeBinderPairProvider;

    public AbstractLeafComponent(@NonNull ViewTypeBinderPairProvider<T> viewTypeBinderPairProvider) {
        this.viewTypeBinderPairProvider = viewTypeBinderPairProvider;
    }

    @Override
    public final ComponentsRecyclerAdapter.ComponentObservable getObservable() {
        return mObservable;
    }

    // handler が扱う ViewHolder の型について関知しないが、生成時に正しい型のオブジェクトが生成されて
    // いるはずなので、unchecked 警告を抑制する。
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int positionInThisComponent, int positionInAllItems) {
        ((Binder<RecyclerView.ViewHolder, T>) (getViewTypeBinderPair(positionInThisComponent)).binder)
                .bindViewHolder(holder, this, positionInThisComponent, positionInAllItems);
    }

    @Override
    public final int getItemViewType(int positionInThisComponent, int positionInAdapter) {
        return getViewTypeBinderPair(positionInThisComponent).viewType.value;
    }

    public final ViewTypeBinderPair<?, T>
    getViewTypeBinderPair(int positionInThisComponent) {
        return viewTypeBinderPairProvider.getViewTypeBinderPair(this, positionInThisComponent);
    }

}