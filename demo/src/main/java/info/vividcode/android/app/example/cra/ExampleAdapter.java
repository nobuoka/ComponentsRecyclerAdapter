package info.vividcode.android.app.example.cra;

import info.vividcode.android.app.example.cra.holders.ContentViewHolder;
import info.vividcode.android.app.example.cra.holders.SectionTitleViewHolder;
import info.vividcode.android.widget.Component;
import info.vividcode.android.widget.ComponentsRecyclerAdapter;
import info.vividcode.android.widget.ViewBindingTypeMapper;
import info.vividcode.android.widget.components.ComponentSeries;
import info.vividcode.android.widget.components.ListReferenceComponent;

public class ExampleAdapter extends ComponentsRecyclerAdapter {

    // `onCreateViewHolder` 相当の処理の登録。
    public final ViewType<ContentViewHolder> viewTypeContent =
            addViewHolderFactory(ContentViewHolder.FACTORY);
    public final ViewType<SectionTitleViewHolder> viewTypeSectionTitle =
            addViewHolderFactory(SectionTitleViewHolder.FACTORY);

    // `onBindViewHolder` 相当の処理の登録。
    private final ViewBindingType<ContentViewHolder, String> mVBTypeContentString =
            addBinder(viewTypeContent, new ContentStringBinder());
    private final ViewBindingType<ContentViewHolder, ListItem> mVBTypeContentItem =
            addBinder(viewTypeContent, new ContentItemBinder());
    private final ViewBindingType<SectionTitleViewHolder, ListItem> mVBTypeSectionTitleItem =
            addBinder(viewTypeSectionTitle, new SectionTitleItemBinder());

    // 表示のためのデータ構造の定義 (item view type の指定も)。
    private final ListReferenceComponent<String> mStringListComponent =
            ListReferenceComponent.create(mVBTypeContentString);
    private final ListReferenceComponent<ListItem> mItemListComponent =
            ListReferenceComponent.create(new ViewBindingTypeMapper<ListItem>() {
                @Override
                public ViewBindingType<?, ListItem> getViewType(ListItem item, int componentPosition) {
                    return (item.getType() == ListItem.Type.SECTION_HEADER ? mVBTypeSectionTitleItem : mVBTypeContentItem);
                }
            });
    private final ComponentSeries mRootComponent = new ComponentSeries();
    {
        mRootComponent.addChildComponent(mStringListComponent);
        mRootComponent.addChildComponent(mItemListComponent);
        setComponent(mRootComponent);
    }

    public ListReferenceComponent<String> getStringComponent() {
        return mStringListComponent;
    }
    public ListReferenceComponent<ListItem> getItemComponent() {
        return mItemListComponent;
    }

    public static class ContentItemBinder implements Binder<ContentViewHolder, ListItem> {
        @Override
        public void bindViewHolder(ContentViewHolder holder, Component<ListItem> item, int positionInDataComponent, int positionInAllItems) {
            holder.updateText(((ListItem.Content) (item.getItem(positionInDataComponent))).data);
        }
    }

    public static class ContentStringBinder implements Binder<ContentViewHolder, String> {
        @Override
        public void bindViewHolder(ContentViewHolder holder, Component<String> item, int positionInDataComponent, int positionInAllItems) {
            holder.updateText(item.getItem(positionInDataComponent));
        }
    }

    public static class SectionTitleItemBinder implements Binder<SectionTitleViewHolder, ListItem> {
        @Override
        public void bindViewHolder(SectionTitleViewHolder holder, Component<ListItem> item, int positionInDataComponent, int positionInAllItems) {
            holder.updateText(((ListItem.SectionHeader) (item.getItem(positionInDataComponent))).title);
        }
    }
}
