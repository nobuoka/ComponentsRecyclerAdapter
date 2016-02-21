package info.vividcode.android.app.example.cra;

import java.util.Arrays;

import info.vividcode.android.app.example.cra.holders.ContentViewHolder;
import info.vividcode.android.app.example.cra.holders.SectionTitleViewHolder;
import info.vividcode.android.cra.ComponentsRecyclerAdapter;
import info.vividcode.android.cra.FixedViewTypeBinderPairProvider;
import info.vividcode.android.cra.ViewHolderFactoryRegistry;
import info.vividcode.android.cra.ViewType;
import info.vividcode.android.cra.ViewTypeBinderPair;
import info.vividcode.android.cra.ViewTypeBinderPairProvider;
import info.vividcode.android.cra.components.ComponentSeries;
import info.vividcode.android.cra.components.ListReferenceComponent;

public class ExampleAdapter extends ComponentsRecyclerAdapter {

    // `onCreateViewHolder` 相当の処理の登録。
    public static final ViewTypes VIEW_TYPES = new ViewTypes();
    public static class ViewTypes extends ViewHolderFactoryRegistry {
        public final ViewType<ContentViewHolder> content = register(ContentViewHolder.FACTORY);
        public final ViewType<SectionTitleViewHolder> sectionTitle = register(SectionTitleViewHolder.FACTORY);
    }

    // Item view type と Binder のペアを生成。
    private static final
    ViewTypeBinderPair<ContentViewHolder, String> VTB_CONTENT_STRING = ViewTypeBinderPair.create(
            VIEW_TYPES.content,
            (holder, component, positionInComponent, positionInAllItems) ->
                    holder.updateText(component.getItem(positionInComponent))
    );
    private static final
    ViewTypeBinderPair<ContentViewHolder, ListItem> VTB_CONTENT_LIST_ITEM = ViewTypeBinderPair.create(
            VIEW_TYPES.content,
            (holder, component, positionInComponent, positionInAllItems) ->
                    holder.updateText(((ListItem.Content) (component.getItem(positionInComponent))).data)
    );
    private static final
    ViewTypeBinderPair<SectionTitleViewHolder, ListItem> VTB_SECTION_TITLE_LIST_ITEM = ViewTypeBinderPair.create(
            VIEW_TYPES.sectionTitle,
            (holder, component, positionInComponent, positionInAllItems) ->
                    holder.updateSectionTitle(((ListItem.SectionHeader) (component.getItem(positionInComponent))).title)
    );

    private static final ViewTypeBinderPairProvider<ListItem> LIST_ITEM_TYPE_DISCRIMINATOR =
            (c, posInComponent) -> (c.getItem(posInComponent).getType() == ListItem.Type.SECTION_HEADER ?
                    VTB_SECTION_TITLE_LIST_ITEM :
                    VTB_CONTENT_LIST_ITEM);

    public ExampleAdapter() {
        super(VIEW_TYPES);
    }

    // 表示のためのデータ構造の定義 (item view type の指定も)。
    private final ListReferenceComponent<String> mStringListComponent =
            ListReferenceComponent.create(new FixedViewTypeBinderPairProvider<>(VTB_CONTENT_STRING));
    private final ListReferenceComponent<ListItem> mItemListComponent =
            ListReferenceComponent.create(LIST_ITEM_TYPE_DISCRIMINATOR);
    private final ComponentSeries mRootComponent =
            ComponentSeries.createWithChildComponents(Arrays.asList(mStringListComponent, mItemListComponent));
    {
        setComponent(mRootComponent);
    }

    public ListReferenceComponent<String> getStringComponent() {
        return mStringListComponent;
    }
    public ListReferenceComponent<ListItem> getItemComponent() {
        return mItemListComponent;
    }

}
