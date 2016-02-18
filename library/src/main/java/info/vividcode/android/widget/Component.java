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

/**
 * {@link ComponentsRecyclerAdapter} によって扱われる項目リストを表すクラス。
 */
public interface Component<T> {

    /**
     * このオブジェクト内の項目の変更を伝える {@link ComponentsRecyclerAdapter.ComponentObservable} を返す。
     * @return 変更を伝えるための {@link ComponentsRecyclerAdapter.ComponentObservable}。
     */
    ComponentsRecyclerAdapter.ComponentObservable getObservable();

    /**
     * このオブジェクトが管理する項目の数を返す。
     * @return 項目の数。
     */
    int getComponentItemCount();

    /**
     * 指定の位置の項目の view type を返す。
     * @param componentPosition 指定の位置。
     * @return View type。
     */
    int getItemViewType(int componentPosition);

    /**
     * 指定の位置の項目を返す。
     * @param componentPosition 指定の位置。
     * @return 指定の位置の項目。
     */
    T getItem(int componentPosition);

    interface Container {
        ComponentAndPosition getComponentAndPosition(int position, ComponentAndPosition dccp);
    }

}
