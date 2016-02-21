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
 * Interface for a component which provides a binding from a part of an app-specific data set to
 * views that are displayed within a {@link RecyclerView}.
 */
public interface Component<T> {

    /**
     * Returns an observable to notify a data set's changes of observers.
     * <p>
     * When a data set referred by this component is changed, the change need to be notified through
     * this observable.
     * @return An observable.
     */
    ComponentsRecyclerAdapter.ComponentObservable getObservable();

    void onBindViewHolder(RecyclerView.ViewHolder holder, int positionInThisComponent, int positionInAllItems);

    /**
     * Returns the sum of a number of items in this component and a number of items in descendant components.
     * @return A number of items.
     */
    int getItemCount();

    /**
     * Returns the view type of the item at specified position.
     * @param positionInThisComponent A position in this component to query.
     * @param positionInAdapter A position in all items to query.
     * @return Integer value identifying the type of view (= view type) representing the item at specified position.
     *
     * @see {@link android.support.v7.widget.RecyclerView.Adapter#getItemViewType(int)}
     */
    int getItemViewType(int positionInThisComponent, int positionInAdapter);

    /**
     * Returns the item at specified position.
     * @param positionInThisComponent A position in this component to query.
     * @return The item at specified position.
     */
    T getItem(int positionInThisComponent);

}
