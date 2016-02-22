package info.vividcode.android.app.example.cra;

import java.util.Arrays;

import info.vividcode.android.app.example.cra.holders.ContentViewHolder;
import info.vividcode.android.app.example.cra.holders.SectionTitleViewHolder;
import info.vividcode.android.cra.Binder;
import info.vividcode.android.cra.Component;
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

    // サブデータセットを扱うコンポーネントを定義。
    private final ListReferenceComponent<String> mStringListComponent = ListReferenceComponent.create(
            // このコンポーネントが扱う項目の view type は全て content で、StringContentBinder を使って bind される。
            new FixedViewTypeBinderPairProvider<>(VIEW_TYPES.content, StringContentBinder.INSTANCE)
    );

    private final ListReferenceComponent<ListItem> mItemListComponent = ListReferenceComponent.create(
            new ViewTypeBinderPairProvider<ListItem>() {
                @Override
                public ViewTypeBinderPair<ListItem> getViewTypeBinderPair(
                        ViewTypeBinderPair.Builder<ListItem> out, Component<ListItem> c, int posInComponent) {
                    // 項目の内容に応じて view type を変化させる。
                    return (c.getItem(posInComponent).getType() == ListItem.Type.SECTION_HEADER ?
                            out.set(VIEW_TYPES.sectionTitle, ListItemSectionTitleBinder.INSTANCE) :
                            out.set(VIEW_TYPES.content, ListItemContentBinder.INSTANCE));
                }
            });

    public ExampleAdapter() {
        super(VIEW_TYPES);
        // データセット全体の構造を表す Component を生成し、Adapter にそれを設定する。
        setComponent(ComponentSeries.createWithChildComponents(
                Arrays.asList(mStringListComponent, mItemListComponent)));
    }

    public ListReferenceComponent<String> getStringComponent() {
        return mStringListComponent;
    }
    public ListReferenceComponent<ListItem> getItemComponent() {
        return mItemListComponent;
    }

    public static class StringContentBinder implements Binder<ContentViewHolder, String> {
        public static final StringContentBinder INSTANCE = new StringContentBinder();
        @Override
        public void bindViewHolder(ContentViewHolder h, Component<String> c, int posInComponent) {
            h.updateText(c.getItem(posInComponent));
        }
    }

    public static class ListItemContentBinder implements Binder<ContentViewHolder, ListItem> {
        public static final ListItemContentBinder INSTANCE = new ListItemContentBinder();
        @Override
        public void bindViewHolder(ContentViewHolder h, Component<ListItem> c, int posInComponent) {
            h.updateText(((ListItem.Content) (c.getItem(posInComponent))).data);
        }
    }

    public static class ListItemSectionTitleBinder implements Binder<SectionTitleViewHolder, ListItem> {
        public static final ListItemSectionTitleBinder INSTANCE = new ListItemSectionTitleBinder();
        @Override
        public void bindViewHolder(SectionTitleViewHolder h, Component<ListItem> c, int posInComponent) {
            h.updateSectionTitle(((ListItem.SectionHeader) (c.getItem(posInComponent))).title);
        }
    }

}
