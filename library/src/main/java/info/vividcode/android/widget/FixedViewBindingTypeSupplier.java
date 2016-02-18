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

package info.vividcode.android.widget;

/** 常に固定の値を返す {@link ViewBindingTypeMapper} の実装。 */
public class FixedViewBindingTypeSupplier<T> implements ViewBindingTypeMapper<T> {

    private final ComponentsRecyclerAdapter.ViewBindingType<?, T> mViewType;

    public FixedViewBindingTypeSupplier(ComponentsRecyclerAdapter.ViewBindingType<?, T> vbType) {
        mViewType = vbType;
    }

    @Override
    public ComponentsRecyclerAdapter.ViewBindingType<?, T> getViewType(Object item, int componentPosition) {
        return mViewType;
    }

}
