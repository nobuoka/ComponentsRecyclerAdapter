package info.vividcode.android.cra.components;

import info.vividcode.android.cra.Component;

public interface ChildComponentItemCallback<A, R> {

    R onChildComponentAndPositionFound(Component<?> child, int positionInChildComponent, A arg1);

}
