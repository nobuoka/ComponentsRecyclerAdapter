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
import android.view.ViewGroup;

/**
 * This interface defines a method to create view holder.
 * @param <VH> Type of view holder.
 */
public interface ViewHolderFactory<VH extends RecyclerView.ViewHolder> {

    /**
     * This method creates a view holder.
     * This is usually called from the {@link ComponentsRecyclerAdapter#onCreateViewHolder(ViewGroup, int)} method.
     * @param parent The parent view into which the newly created view will be added.
     * @return Newly created view holder..
     */
    VH createViewHolder(ViewGroup parent);

}
