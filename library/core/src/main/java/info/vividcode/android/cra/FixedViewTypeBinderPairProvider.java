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

/** 常に固定の値を返す {@link ViewTypeBinderPairProvider} の実装。 */
public class FixedViewTypeBinderPairProvider<VH extends RecyclerView.ViewHolder, T> implements ViewTypeBinderPairProvider<T> {

    private final ViewType<VH> mViewType;
    private final Binder<VH, T> mBinder;

    public FixedViewTypeBinderPairProvider(ViewType<VH> viewType, Binder<VH, T> binder) {
        mViewType = viewType;
        mBinder = binder;
    }

    @Override
    public ViewTypeBinderPair<T> getViewTypeBinderPair(
            ViewTypeBinderPair.Builder<T> out, Component<T> component, int posInComponent) {
        return out.set(mViewType, mBinder);
    }

}
