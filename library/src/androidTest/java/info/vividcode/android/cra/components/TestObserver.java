package info.vividcode.android.cra.components;

import com.google.common.base.Objects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import info.vividcode.android.cra.Component;
import info.vividcode.android.cra.ComponentsRecyclerAdapter;

class TestObserver implements ComponentsRecyclerAdapter.ComponentObserver {
    public enum EventType {
        CHANGED, ITEM_RANGE_CHANGED, ITEM_RANGE_INSERTED, ITEM_RANGE_REMOVED, ITEM_MOVED
    }

    public static class Event {
        public final EventType eventType;
        public final List<Object> args;

        public Event(EventType type, Collection<Object> args) {
            this.eventType = type;
            this.args = new ArrayList<>(args);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Event) {
                Event evt = (Event) obj;
                return evt.eventType == this.eventType && this.args.equals(evt.args);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(eventType, args);
        }
    }

    private List<Event> mObservedEvents = new ArrayList<>();

    public List<Event> getObservedEvents() {
        return mObservedEvents;
    }

    public void resetObservedEvents() {
        mObservedEvents.clear();
    }

    @Override
    public void onChanged(Component dc) {
        mObservedEvents.add(new Event(EventType.CHANGED, Collections.<Object>singletonList(dc)));
    }

    @Override
    public void onItemRangeChanged(Component dc, int positionStart, int itemCount) {
        mObservedEvents.add(new Event(EventType.ITEM_RANGE_CHANGED, Arrays.asList(dc, positionStart, itemCount)));
    }

    @Override
    public void onItemRangeInserted(Component dc, int positionStart, int itemCount) {
        mObservedEvents.add(new Event(EventType.ITEM_RANGE_INSERTED, Arrays.asList(dc, positionStart, itemCount)));
    }

    @Override
    public void onItemRangeRemoved(Component dc, int positionStart, int itemCount) {
        mObservedEvents.add(new Event(EventType.ITEM_RANGE_REMOVED, Arrays.asList(dc, positionStart, itemCount)));
    }

    @Override
    public void onItemMoved(Component dc, int fromPosition, int toPosition) {
        mObservedEvents.add(new Event(EventType.ITEM_MOVED, Arrays.asList(dc, fromPosition, toPosition)));
    }
}
