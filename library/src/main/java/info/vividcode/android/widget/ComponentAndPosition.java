package info.vividcode.android.widget;

/**
 * {@link Component} と {@link Component} 内の位置をセットにしたクラス。
 */
public class ComponentAndPosition {

    public Component component;
    public int componentPosition;

    public static ComponentAndPosition getFromComponent(Component dc, int positionInBase, ComponentAndPosition dccp) {
        if (dc instanceof Component.Container) {
            return ((Component.Container) dc).getComponentAndPosition(positionInBase, dccp);
        } else {
            dccp.component = dc;
            dccp.componentPosition = positionInBase;
            return dccp;
        }
    }

}
