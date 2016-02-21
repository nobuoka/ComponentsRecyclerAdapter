package info.vividcode.android.cra.components;

import info.vividcode.android.cra.Component;

public interface ChildComponentItemCallback<A1, A2, R> {

    R onChildComponentAndPositionFound(Component<?> child, int positionInChildComponent, A1 arg1, A2 arg2);

}
