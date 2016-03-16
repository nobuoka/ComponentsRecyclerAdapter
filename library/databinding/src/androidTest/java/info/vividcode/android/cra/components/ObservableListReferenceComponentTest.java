package info.vividcode.android.cra.components;

import android.databinding.ListChangeRegistry;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import org.junit.Test;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import info.vividcode.android.cra.Binder;
import info.vividcode.android.cra.Component;
import info.vividcode.android.cra.ComponentsRecyclerAdapter;
import info.vividcode.android.cra.FixedViewTypeBinderPairProvider;
import info.vividcode.android.cra.ViewHolderFactory;
import info.vividcode.android.cra.ViewHolderFactoryRegistry;
import info.vividcode.android.cra.ViewType;

import static org.junit.Assert.assertEquals;

public class ObservableListReferenceComponentTest {

    /** Simple {@link ViewHolderFactory} for tests. */
    private static ViewHolderFactory<RecyclerView.ViewHolder> sTestViewHolderFactory =
            new ViewHolderFactory<RecyclerView.ViewHolder>() {
                @Override
                public RecyclerView.ViewHolder createViewHolder(ViewGroup parent) {
                    return new RecyclerView.ViewHolder(new View(parent.getContext())) {};
                }
            };

    /** {@link Binder} for tests, which do nothing. */
    private static class NopBinder implements Binder<RecyclerView.ViewHolder, Object> {
        @Override
        public void bindViewHolder(RecyclerView.ViewHolder holder, Component<Object> component, int positionInComponent) {
            // Do nothing.
        }
    }

    /** Simple {@link ViewHolderFactoryRegistry} for tests. */
    private static class ViewTypes extends ViewHolderFactoryRegistry {
        public final ViewType<RecyclerView.ViewHolder> testViewType = register(sTestViewHolderFactory);
    }
    private static final ViewTypes VIEW_TYPES = new ViewTypes();

    /**
     * Test of the {@link ObservableListReferenceComponent#getItemCount()} method.
     */
    @Test
    public void getItemCount_usual() {
        ObservableListReferenceComponent<Object> component = new ObservableListReferenceComponent<>(
                new FixedViewTypeBinderPairProvider<>(VIEW_TYPES.testViewType, new NopBinder()));

        { // Just after construction.
            assertEquals("Zero because there is no referenced list.", 0, component.getItemCount());
        }

        { // Set a referenced list.
            ObservableArrayList<Object> list = new ObservableArrayList<>();
            list.addAll(Arrays.asList("item 1", "item 2", "item 3"));
            component.setList(list);
            assertEquals("Returns the number of items of a referenced list.",
                    3, component.getItemCount());
            list.addAll(Arrays.asList("item 4", "item 5"));
            assertEquals("Follows the change of the list.",
                    5, component.getItemCount());
        }

        { // Set another list.
            ObservableArrayList<Object> list = new ObservableArrayList<>();
            list.addAll(Arrays.asList("item 1", "item 2"));
            component.setList(list);
            assertEquals("Returns the number of items of a newly referenced list.",
                    2, component.getItemCount());
        }

        { // Set null as referenced list.
            component.setList(null);
            assertEquals("Zero because there is no referenced list.", 0, component.getItemCount());
        }
    }

    @Test
    public void notifications_observingObservableArrayList() {
        ObservableListReferenceComponent<Object> component = new ObservableListReferenceComponent<>(
                new FixedViewTypeBinderPairProvider<>(VIEW_TYPES.testViewType, new NopBinder()));
        // Observer to check the received notifications.
        TestObserver observer = new TestObserver();
        component.getObservable().registerObserver(observer);

        ObservableArrayList<Object> list = new ObservableArrayList<>();
        component.setList(list);

        String item1 = "item 1";
        String item2 = "item 1";
        String item3 = "item 1";

        observer.resetObservedEvents();
        list.addAll(Arrays.asList(item1, item2));
        assertEquals("Receives an insertion event: inserted position is 0 and item count is 2.",
                Collections.singletonList(
                        new TestObserver.Event(TestObserver.EventType.ITEM_RANGE_INSERTED, Arrays.asList(component, 0, 2))
                ),
                observer.getObservedEvents());

        observer.resetObservedEvents();
        list.add(item3);
        assertEquals("Receives an insertion event: inserted position is 2 and item count is 1.",
                Collections.singletonList(
                        new TestObserver.Event(TestObserver.EventType.ITEM_RANGE_INSERTED, Arrays.asList(component, 2, 1))
                ),
                observer.getObservedEvents());

        observer.resetObservedEvents();
        list.remove(item1);
        assertEquals("Receives a remove event: removed position is 0 and item count is 1.",
                Collections.singletonList(
                        new TestObserver.Event(TestObserver.EventType.ITEM_RANGE_REMOVED, Arrays.asList(component, 0, 1))
                ),
                observer.getObservedEvents());

        component.setList(null);
        observer.resetObservedEvents();
        list.add("item 3");
        assertEquals("A component doesn't send events when list is changed after a component removes a reference to a list.",
                Collections.emptyList(),
                observer.getObservedEvents());
    }

    private static class MovingItemsObservableList extends AbstractList<Object> implements ObservableList<Object> {
        private ArrayList<Character> mImpl = new ArrayList<>(Arrays.asList(
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'
        ));

        public void moveItems(int from, int to, int count) {
            ArrayList<Character> buf = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                buf.add(mImpl.remove(from));
            }
            mImpl.addAll(to, buf);
            mListChangeRegistry.notifyMoved(this, from, to, count);
        }

        @Override
        public Character get(int location) {
            return mImpl.get(location);
        }
        @Override
        public int size() {
            return mImpl.size();
        }
        private ListChangeRegistry mListChangeRegistry = new ListChangeRegistry();
        @Override
        public void addOnListChangedCallback(OnListChangedCallback<? extends ObservableList<Object>> callback) {
            mListChangeRegistry.add(callback);
        }
        @Override
        public void removeOnListChangedCallback(OnListChangedCallback<? extends ObservableList<Object>> callback) {
            mListChangeRegistry.remove(callback);
        }
    }

    private static class MoveHandler implements ComponentsRecyclerAdapter.ComponentObserver {
        public final List<Object> list;
        public MoveHandler(List<Object> list) {
            this.list = list;
        }
        @Override
        public void onChanged(Component dc) {}
        @Override
        public void onItemRangeChanged(Component dc, int positionStart, int itemCount) {}
        @Override
        public void onItemRangeInserted(Component dc, int positionStart, int itemCount) {}
        @Override
        public void onItemRangeRemoved(Component dc, int positionStart, int itemCount) {}
        @Override
        public void onItemMoved(Component dc, int fromPosition, int toPosition) {
            this.list.add(toPosition, this.list.remove(fromPosition));
        }
    };

    @Test
    public void notifications_forObservableList_movingItems() {
        ObservableListReferenceComponent<Object> component = new ObservableListReferenceComponent<>(
                new FixedViewTypeBinderPairProvider<>(VIEW_TYPES.testViewType, new NopBinder()));

        MovingItemsObservableList list = new MovingItemsObservableList();
        component.setList(list);

        // Observer to check the received notifications.
        ArrayList<Object> mirroredList = new ArrayList<>(list);
        component.getObservable().registerObserver(new MoveHandler(mirroredList));

        { // In case that range of items before moving and range of items after moving overlap.
            { // Move from the front to the back.
                list.moveItems(0, 3, 5);
                assertEquals("First 5 items moved to position 3.",
                        Arrays.<Object>asList('F', 'G', 'H', 'A', 'B', 'C', 'D', 'E', 'I', 'J'), list);
                assertEquals("Receives moving event.",
                        list, mirroredList);
            }
            { // Move from the back to the front.
                list.moveItems(3, 0, 5);
                assertEquals("Moved 5 items went back.",
                        Arrays.<Object>asList('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'), list);
                assertEquals("Receives moving event.",
                        list, mirroredList);
            }
        }
        { // In case that range of items before moving and range of items after moving don't overlap.
            { // Move from the front to the back.
                list.moveItems(0, 5, 3);
                assertEquals("First 3 items moved to position 5.",
                        Arrays.<Object>asList('D', 'E', 'F', 'G', 'H', 'A', 'B', 'C', 'I', 'J'), list);
                assertEquals("Receives moving event.",
                        list, mirroredList);
            }
            { // Move from the back to the front.
                list.moveItems(5, 0, 3);
                assertEquals("Moved 3 items went back.",
                        Arrays.<Object>asList('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'), list);
                assertEquals("Receives moving event.",
                        list, mirroredList);
            }
        }
    }

}
