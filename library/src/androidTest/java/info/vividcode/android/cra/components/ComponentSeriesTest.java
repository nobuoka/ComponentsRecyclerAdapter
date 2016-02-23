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
        assertEquals("子が存在しないので 0。", 0, componentSeries.getItemCount());
    }

    @Test
    public void getItemCount_withChildComponents() {
        componentSeries.addChildComponent(new TestDataComponent<>(Arrays.asList(0, 1, 2)));
        assertEquals("子のコンポーネントの項目数が 3 なので 3。", 3, componentSeries.getItemCount());

        componentSeries.addChildComponent(new TestDataComponent<>(Arrays.asList(10, 11)));
        componentSeries.addChildComponent(new TestDataComponent<>(Arrays.asList(20, 21, 22, 23)));
        assertEquals("子のコンポーネントの項目数の合計が 9 なので 9。", 9, componentSeries.getItemCount());
    }

    @Test
    public void getItemViewType() {
        componentSeries.addChildComponent(new TestDataComponent<>(Arrays.asList(0, 1, 2), Arrays.asList(100, 101, 102)));
        assertEquals("指定の位置の項目の view type が返ってくる。", 102, componentSeries.getItemViewType(2));

        componentSeries.addChildComponent(new TestDataComponent<>(Arrays.asList(10, 11), Arrays.asList(110, 111)));
        componentSeries.addChildComponent(new TestDataComponent<>(Arrays.asList(20, 21, 22, 23), Arrays.asList(120, 121, 122, 123)));
        assertEquals("複数の子がある場合は連結後の位置の項目の view type が返ってくる。", 120, componentSeries.getItemViewType(5));
        assertEquals("最後の項目。", 123, componentSeries.getItemViewType(8));
    }

    @Test
    public void getItem() {
        componentSeries.addChildComponent(new TestDataComponent<>(Arrays.asList(0, 1, 2), Arrays.asList(100, 101, 102)));
        assertEquals("指定の位置の項目が返ってくる。", 2, componentSeries.getItem(2));

        componentSeries.addChildComponent(new TestDataComponent<>(Arrays.asList(10, 11), Arrays.asList(110, 111)));
        componentSeries.addChildComponent(new TestDataComponent<>(Arrays.asList(20, 21, 22, 23), Arrays.asList(120, 121, 122, 123)));
        assertEquals("複数の子がある場合は連結後の位置の項目が返ってくる。", 20, componentSeries.getItem(5));
        assertEquals("最後の項目。", 23, componentSeries.getItem(8));
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
        TestDataComponent child1 = new TestDataComponent<>(Arrays.asList(10, 11));
        ComponentSeries child2 = new ComponentSeries();
        TestDataComponent child2child1 = new TestDataComponent<>(Arrays.asList(0, 1));
        TestDataComponent child2child2 = new TestDataComponent<>(Arrays.asList("a", "b"));
        TestDataComponent child3 = new TestDataComponent<>(Arrays.asList("A", "B"));

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
        componentSeries.addChildComponent(new TestDataComponent<>(Arrays.asList(1, 2)));
        componentSeries.callBackForChildComponentItem(-1, new TestCallback(1), "This is test arg");
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void callBackForChildComponentItem_indexOutOfBoundsWithOverPosition() {
        componentSeries.addChildComponent(new TestDataComponent<>(Arrays.asList(1, 2)));
        componentSeries.callBackForChildComponentItem(2, new TestCallback(1), "This is test arg");
    }

    @Test
    public void observable_notify() {
        TestDataComponent<Integer> child1 = new TestDataComponent<>(Arrays.asList(0, 1));
        TestDataComponent<Integer> child2 = new TestDataComponent<>(Collections.<Integer>emptyList());
        TestDataComponent<String> child3 = new TestDataComponent<>(Arrays.asList("a", "b", "c"));

        TestObserver testObserver = new TestObserver();
        componentSeries.getObservable().registerObserver(testObserver);

        List<TestObserver.Event> expectedEvents;

        testObserver.resetObservedEvents();
        componentSeries.addChildComponent(child1);
        expectedEvents = Collections.singletonList(
                new TestObserver.Event(TestObserver.EventType.ITEM_RANGE_INSERTED, Arrays.asList(componentSeries, 0, 2))
        );
        assertEquals("データコンポーネントを追加すると、項目の挿入イベントが発生。",
                expectedEvents, testObserver.getObservedEvents());

        testObserver.resetObservedEvents();
        componentSeries.addChildComponent(child2);
        expectedEvents = Collections.singletonList(
                new TestObserver.Event(TestObserver.EventType.ITEM_RANGE_INSERTED, Arrays.asList(componentSeries, 2, 0))
        );
        assertEquals("空のデータコンポーネントを追加すると、項目数 0 の挿入イベントが発生。",
                expectedEvents, testObserver.getObservedEvents());

        testObserver.resetObservedEvents();
        componentSeries.addChildComponent(child3);
        expectedEvents = Collections.singletonList(
                new TestObserver.Event(TestObserver.EventType.ITEM_RANGE_INSERTED, Arrays.asList(componentSeries, 2, 3))
        );
        assertTrue("既に子がある場合にさらにコンポーネントを追加すると、挿入位置が末尾になる。", expectedEvents.equals(testObserver.getObservedEvents()));

        testObserver.resetObservedEvents();
        child2.setItems(Arrays.asList(100, 200), false);
        child2.getObservable().notifyChanged();
        expectedEvents = Collections.singletonList(
                new TestObserver.Event(TestObserver.EventType.CHANGED, Collections.<Object>singletonList(componentSeries))
        );
        assertTrue("子のコンポーネントの全変更イベントが伝わる。", expectedEvents.equals(testObserver.getObservedEvents()));

        testObserver.resetObservedEvents();
        child2.setItems(Arrays.asList(0, 1, 2, 3), false);
        child2.getObservable().notifyItemRangeInserted(0, 4);
        expectedEvents = Collections.singletonList(
                new TestObserver.Event(TestObserver.EventType.ITEM_RANGE_INSERTED, Arrays.asList(componentSeries, 2, 4))
        );
        assertTrue("子のコンポーネントの挿入イベントが伝わる。 (位置のオフセット付きで。)", expectedEvents.equals(testObserver.getObservedEvents()));

        testObserver.resetObservedEvents();
        child2.setItems(Arrays.asList(0, 1, 2), false);
        child2.getObservable().notifyItemRangeRemoved(3, 1);
        expectedEvents = Collections.singletonList(
                new TestObserver.Event(TestObserver.EventType.ITEM_RANGE_REMOVED, Arrays.asList(componentSeries, 5, 1))
        );
        assertTrue("子のコンポーネントの削除イベントが伝わる。 (位置のオフセット付きで。)", expectedEvents.equals(testObserver.getObservedEvents()));

        testObserver.resetObservedEvents();
        child2.setItems(Arrays.asList(0, 2, 1), false);
        child2.getObservable().notifyItemMoved(1, 2);
        expectedEvents = Collections.singletonList(
                new TestObserver.Event(TestObserver.EventType.ITEM_MOVED, Arrays.asList(componentSeries, 3, 4))
        );
        assertTrue("子のコンポーネントの移動イベントが伝わる。 (位置のオフセット付きで。)", expectedEvents.equals(testObserver.getObservedEvents()));

        testObserver.resetObservedEvents();
        child2.setItems(Arrays.asList(100, 102, 1), false);
        child2.getObservable().notifyItemRangeChanged(0, 2);
        expectedEvents = Collections.singletonList(
                new TestObserver.Event(TestObserver.EventType.ITEM_RANGE_CHANGED, Arrays.asList(componentSeries, 2, 2))
        );
        assertTrue("子のコンポーネントの変更イベントが伝わる。 (位置のオフセット付きで。)", expectedEvents.equals(testObserver.getObservedEvents()));
    }

}
