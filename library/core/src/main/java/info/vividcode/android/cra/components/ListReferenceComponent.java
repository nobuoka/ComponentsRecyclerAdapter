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

import java.util.Collections;
import java.util.List;

import info.vividcode.android.cra.ViewTypeBinderPairProvider;

public class ListReferenceComponent<T> extends AbstractLeafComponent<T> {

    private List<T> mList = Collections.emptyList();

    public static <T> ListReferenceComponent<T> create(ViewTypeBinderPairProvider<T> provider) {
        return new ListReferenceComponent<>(provider);
    }

    private ListReferenceComponent(ViewTypeBinderPairProvider<T> provider) {
        super(provider);
    }

    public void setList(List<T> list) {
        mList = list;
        getObservable().notifyChanged();
    }

    public List<T> getList() {
        return mList;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public T getItem(int positionInThisComponent) {
        return mList.get(positionInThisComponent);
    }

}
