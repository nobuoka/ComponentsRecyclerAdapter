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
import android.util.SparseArray;

/**
 * This class is thread-safe.
 */
public class ViewHolderFactoryRegistry {

    private int viewTypeCounter = 1;
    private final SparseArray<ViewHolderFactory<?>> mViewTypeToFactoryMap = new SparseArray<>();

    public synchronized <VH extends RecyclerView.ViewHolder> ViewType<VH> register(
            ViewHolderFactory<VH> factory) {
        if (factory == null)
            throw new NullPointerException("Factory to be registered cannot be null.");
        int viewType = viewTypeCounter;
        viewTypeCounter += 1;
        mViewTypeToFactoryMap.put(viewType, factory);
        return new ViewType<>(viewType);
    }

    public synchronized ViewHolderFactory<?> getFactory(int viewType) {
        ViewHolderFactory<?> factory = mViewTypeToFactoryMap.get(viewType);
        if (factory != null) {
            return factory;
        } else {
            throw new IllegalArgumentException("Not registered view type: " + viewType);
        }
    }

}
