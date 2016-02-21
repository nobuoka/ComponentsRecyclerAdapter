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
 * This class represents a view type of an item displayed in a {@link RecyclerView}.
 * To obtain a instance of this class, you need to use
 * {@link ViewHolderFactoryRegistry#register(ViewHolderFactory)} method.
 * @param <VH> View holder's type corresponding to a view type. This parameter is used only for type checking.
 */
public class ViewType<VH extends RecyclerView.ViewHolder> {

    /** Raw view type. */
    public final int value;

    /**
     * Library users should not call this constructor directory.
     * Use {@link ViewHolderFactoryRegistry#register(ViewHolderFactory)} method to get an instance
     * of this class.
     * @param value The raw value of view type.
     */
    ViewType(int value) {
        this.value = value;
    }

}
