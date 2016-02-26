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

import android.database.Observable;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

public class ComponentsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface ComponentObserver {
        void onChanged(Component dc);
        void onItemRangeChanged(Component dc, int positionStart, int itemCount);
        void onItemRangeInserted(Component dc, int positionStart, int itemCount);
        void onItemRangeRemoved(Component dc, int positionStart, int itemCount);
        void onItemMoved(Component dc, int fromPosition, int toPosition);
    }

    public static class ComponentObservable extends Observable<ComponentObserver> {
        private final Component mOwner;
        public ComponentObservable(Component owner) {
            mOwner = owner;
        }

        public void notifyChanged() {
            for (int i = mObservers.size() - 1; i >= 0; --i)
                mObservers.get(i).onChanged(mOwner);
        }

        public void notifyItemRangeChanged(int positionStart, int itemCount) {
            for (int i = mObservers.size() - 1; i >= 0; --i)
                mObservers.get(i).onItemRangeChanged(mOwner, positionStart, itemCount);
        }

        public void notifyItemRangeInserted(int positionStart, int itemCount) {
            for (int i = mObservers.size() - 1; i >= 0; --i)
                mObservers.get(i).onItemRangeInserted(mOwner, positionStart, itemCount);
        }

        public void notifyItemRangeRemoved(int positionStart, int itemCount) {
            for (int i = mObservers.size() - 1; i >= 0; --i)
                mObservers.get(i).onItemRangeRemoved(mOwner, positionStart, itemCount);
        }

        public void notifyItemMoved(int fromPosition, int toPosition) {
            for (int i = mObservers.size() - 1; i >= 0; --i)
                mObservers.get(i).onItemMoved(mOwner, fromPosition, toPosition);
        }
    }

    private final ViewHolderFactoryRegistry mViewHolderFactoryRegistry;

    private Component<?> mComponent;

    public ComponentsRecyclerAdapter(ViewHolderFactoryRegistry registry) {
        if (registry == null)
            throw new NullPointerException("ViewHolderFactoryRegistry must not be null.");
        mViewHolderFactoryRegistry = registry;
    }

    public Component<?> getComponent() {
        return mComponent;
    }

    public void setComponent(Component<?> dc) {
        if (mComponent != null) {
            int oldCount = mComponent.getItemCount();
            mComponent = null;
            notifyItemRangeRemoved(0, oldCount);
        }

        mComponent = dc;
        notifyItemRangeInserted(0, mComponent.getItemCount());
        mComponent.getObservable().registerObserver(new ComponentObserver() {
            @Override
            public void onChanged(Component dc) {
                notifyDataSetChanged();
            }

            @Override
            public void onItemRangeChanged(Component dc, int positionStart, int itemCount) {
                notifyItemRangeChanged(positionStart, itemCount);
            }

            @Override
            public void onItemRangeInserted(Component dc, int positionStart, int itemCount) {
                notifyItemRangeInserted(positionStart, itemCount);
            }

            @Override
            public void onItemRangeRemoved(Component dc, int positionStart, int itemCount) {
                notifyItemRangeRemoved(positionStart, itemCount);
            }

            @Override
            public void onItemMoved(Component dc, int fromPosition, int toPosition) {
                notifyItemMoved(fromPosition, toPosition);
            }
        });
    }

    /** {@inheritDoc} */
    @Override
    public final RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolderFactory<?> factory = mViewHolderFactoryRegistry.getFactory(viewType);
        return factory.createViewHolder(parent);
    }

    /** {@inheritDoc} */
    @Override
    public final void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (mComponent == null)
            throw new RuntimeException("`onBindViewHolder` cannot be called when there is no component.");
        mComponent.onBindViewHolder(holder, position);
    }

    /** {@inheritDoc} */
    @Override
    public final int getItemCount() {
        return (mComponent != null ? mComponent.getItemCount() : 0);
    }

    /** {@inheritDoc} */
    @Override
    public final int getItemViewType(int position) {
        if (mComponent == null)
            throw new RuntimeException("`getItemViewType` cannot be called when there is no component.");
        return mComponent.getItemViewType(position);
    }

}
