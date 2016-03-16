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

package info.vividcode.android.cra;

import android.support.v7.widget.RecyclerView;

/**
 * This interface defines a method to update a view to reflect an item.
 * @param <VH> The type of a view holder.
 * @param <D> The type of an item.
 */
public interface Binder<VH extends RecyclerView.ViewHolder, D> {

    /**
     * This method update the passed view's contents to reflect the item at the given position in a passed component.
     * This is usually called from {@link Component#onBindViewHolder(RecyclerView.ViewHolder, int)}.
     * @param holder The view holder holding a view which is target of updating.
     * @param component The component containing the item.
     * @param positionInComponent The position of the item within the component.
     */
    void bindViewHolder(VH holder, Component<D> component, int positionInComponent);

}
