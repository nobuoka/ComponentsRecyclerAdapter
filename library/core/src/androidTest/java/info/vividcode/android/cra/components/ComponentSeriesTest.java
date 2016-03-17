package info.vividcode.android.cra.components;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class ComponentSeriesTest {

    private ComponentSeries componentSeries;

    @Before
    public void setupComponentSeries() {
        componentSeries = new ComponentSeries();
    }

    @Test
    public void getItemCount_withNoChildComponent() {
        assertEquals("Zero because there is no child component.",
                0, componentSeries.getItemCount());
    }

    @Test
    public void getItemCount_withChildComponents() {
        componentSeries.addChildComponent(new TestComponent<>(Arrays.asList(0, 1, 2)));
        assertEquals("Three because the only child component has three items.",
                3, componentSeries.getItemCount());

        componentSeries.addChildComponent(new TestComponent<>(Arrays.asList(10, 11)));
        componentSeries.addChildComponent(new TestComponent<>(Arrays.asList(20, 21, 22, 23)));
        assertEquals("Sum of item count of child components (3 + 2 + 4 = 9).",
                9, componentSeries.getItemCount());
    }

    @Test
    public void getItemViewType() {
        componentSeries.addChildComponent(new TestComponent<>(Arrays.asList(0, 1, 2), Arrays.asList(100, 101, 102)));
        assertEquals("Returns view type at specified position in child component.",
                102, componentSeries.getItemViewType(2));

        componentSeries.addChildComponent(new TestComponent<>(Arrays.asList(10, 11), Arrays.asList(110, 111)));
        componentSeries.addChildComponent(new TestComponent<>(Arrays.asList(20, 21, 22, 23), Arrays.asList(120, 121, 122, 123)));
        assertEquals("Returns view type at specified position in items of concatenated child components, " +
                        "in case that there are multiple child components.",
                120, componentSeries.getItemViewType(5));
        assertEquals("View type at last position.",
                123, componentSeries.getItemViewType(8));
    }

    @Test
    public void getItem() {
        componentSeries.addChildComponent(new TestComponent<>(Arrays.asList(0, 1, 2), Arrays.asList(100, 101, 102)));
        assertEquals("Returns an item at specified position in child component.",
                2, componentSeries.getItem(2));

        componentSeries.addChildComponent(new TestComponent<>(Arrays.asList(10, 11), Arrays.asList(110, 111)));
        componentSeries.addChildComponent(new TestComponent<>(Arrays.asList(20, 21, 22, 23), Arrays.asList(120, 121, 122, 123)));
        assertEquals("Returns an item at specified position in items of concatenated child components, " +
                        "in case that there are multiple child components.",
                20, componentSeries.getItem(5));
        assertEquals("An item at last position",
                23, componentSeries.getItem(8));
    }

    private static class TestCallback implements ChildComponentItemCallback<String, Integer> {
        private final int mResVal;
        TestCallback(int resVal) {
            mResVal = resVal;
        }
        public final List<Object> args = new ArrayList<>();
        @Override
        public Integer onChildComponentAndPositionFound(info.vividcode.android.cra.Component child, int posInChildComponent, String arg1) {
            args.addAll(Arrays.asList(child, posInChildComponent, arg1));
            return mResVal;
        }
    }

    @Test
    public void callBackForChildComponentItem_usual() {
        TestComponent child1 = new TestComponent<>(Arrays.asList(10, 11));
        ComponentSeries child2 = new ComponentSeries();
        TestComponent child2child1 = new TestComponent<>(Arrays.asList(0, 1));
        TestComponent child2child2 = new TestComponent<>(Arrays.asList("a", "b"));
        TestComponent child3 = new TestComponent<>(Arrays.asList("A", "B"));

        componentSeries.addChildComponent(child1);
        componentSeries.addChildComponent(child2);
        componentSeries.addChildComponent(child3);
        child2.addChildComponent(child2child1);
        child2.addChildComponent(child2child2);

        // [[10, 11], [[0, 1], ["a", "b"]], ["A", "B"]]

        {
            TestCallback testCallback = new TestCallback(100);
            int res = this.componentSeries.callBackForChildComponentItem(0, testCallback, "This is test arg");
            assertEquals("Position 0 in this component corresponds to position 0 in first child component",
                    Arrays.asList(child1, 0, "This is test arg"),
                    testCallback.args);
            assertEquals(100, res);
        }

        {
            TestCallback testCallback = new TestCallback(211);
            int res = this.componentSeries.callBackForChildComponentItem(5, testCallback, "Test arg 2");
            assertEquals("Position 5 in this component corresponds to position 3 in second child component",
                    Arrays.asList(child2, 3, "Test arg 2"),
                    testCallback.args);
            assertEquals(211, res);
        }
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void callBackForChildComponentItem_indexOutOfBoundsWithNoChildComponent() {
        componentSeries.callBackForChildComponentItem(0, new TestCallback(1), "This is test arg");
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void callBackForChildComponentItem_indexOutOfBoundsWithMinusPosition() {
        componentSeries.addChildComponent(new TestComponent<>(Arrays.asList(1, 2)));
        componentSeries.callBackForChildComponentItem(-1, new TestCallback(1), "This is test arg");
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void callBackForChildComponentItem_indexOutOfBoundsWithOverPosition() {
        componentSeries.addChildComponent(new TestComponent<>(Arrays.asList(1, 2)));
        componentSeries.callBackForChildComponentItem(2, new TestCallback(1), "This is test arg");
    }

    @Test
    public void observable_notify() {
        TestComponent<Integer> child1 = new TestComponent<>(Arrays.asList(0, 1));
        TestComponent<Integer> child2 = new TestComponent<>(Collections.<Integer>emptyList());
        TestComponent<String> child3 = new TestComponent<>(Arrays.asList("a", "b", "c"));

        TestObserver testObserver = new TestObserver();
        componentSeries.getObservable().registerObserver(testObserver);

        List<TestObserver.Event> expectedEvents;

        testObserver.resetObservedEvents();
        componentSeries.addChildComponent(child1);
        expectedEvents = Collections.singletonList(
                new TestObserver.Event(TestObserver.EventType.ITEM_RANGE_INSERTED, Arrays.asList(componentSeries, 0, 2))
        );
        assertEquals("Publishes `inserted` event when child component is added.",
                expectedEvents, testObserver.getObservedEvents());

        testObserver.resetObservedEvents();
        componentSeries.addChildComponent(child2);
        expectedEvents = Collections.singletonList(
                new TestObserver.Event(TestObserver.EventType.ITEM_RANGE_INSERTED, Arrays.asList(componentSeries, 2, 0))
        );
        assertEquals("Publishes `inserted` event with length 0 when empty child component is added.",
                expectedEvents, testObserver.getObservedEvents());

        testObserver.resetObservedEvents();
        componentSeries.addChildComponent(child3);
        expectedEvents = Collections.singletonList(
                new TestObserver.Event(TestObserver.EventType.ITEM_RANGE_INSERTED, Arrays.asList(componentSeries, 2, 3))
        );
        assertTrue("In case that it has a child component already, the inserted position in event is the last position.",
                expectedEvents.equals(testObserver.getObservedEvents()));

        testObserver.resetObservedEvents();
        child2.setItems(Arrays.asList(100, 200), false);
        child2.getObservable().notifyChanged();
        expectedEvents = Collections.singletonList(
                new TestObserver.Event(TestObserver.EventType.CHANGED, Collections.<Object>singletonList(componentSeries))
        );
        assertTrue("Publishes `changed` event when child component is changed.",
                expectedEvents.equals(testObserver.getObservedEvents()));

        testObserver.resetObservedEvents();
        child2.setItems(Arrays.asList(0, 1, 2, 3), false);
        child2.getObservable().notifyItemRangeInserted(0, 4);
        expectedEvents = Collections.singletonList(
                new TestObserver.Event(TestObserver.EventType.ITEM_RANGE_INSERTED, Arrays.asList(componentSeries, 2, 4))
        );
        assertEquals("Publishes `inserted` event with position offset when child component publishes `inserted` event.",
                expectedEvents, testObserver.getObservedEvents());

        testObserver.resetObservedEvents();
        child2.setItems(Arrays.asList(0, 1, 2), false);
        child2.getObservable().notifyItemRangeRemoved(3, 1);
        expectedEvents = Collections.singletonList(
                new TestObserver.Event(TestObserver.EventType.ITEM_RANGE_REMOVED, Arrays.asList(componentSeries, 5, 1))
        );
        assertTrue("Publishes `removed` event with position offset when child component publishes `removed` event.",
                expectedEvents.equals(testObserver.getObservedEvents()));

        testObserver.resetObservedEvents();
        child2.setItems(Arrays.asList(0, 2, 1), false);
        child2.getObservable().notifyItemMoved(1, 2);
        expectedEvents = Collections.singletonList(
                new TestObserver.Event(TestObserver.EventType.ITEM_MOVED, Arrays.asList(componentSeries, 3, 4))
        );
        assertTrue("Publishes `moved` event with position offset when child component publishes `moved` event.",
                expectedEvents.equals(testObserver.getObservedEvents()));

        testObserver.resetObservedEvents();
        child2.setItems(Arrays.asList(100, 102, 1), false);
        child2.getObservable().notifyItemRangeChanged(0, 2);
        expectedEvents = Collections.singletonList(
                new TestObserver.Event(TestObserver.EventType.ITEM_RANGE_CHANGED, Arrays.asList(componentSeries, 2, 2))
        );
        assertTrue("Publishes `changed` event with position offset when child component publishes `changed` event.",
                expectedEvents.equals(testObserver.getObservedEvents()));
    }

}
