ComponentsRecyclerAdapter
==============================

This library provides `ComponentsRecyclerAdapter`, the subclass of RecyclerView.Adapter.
Using it, you can easily create RecyclerView with multiple view types, or with complex data hierarchy.

## Problems resolved by this library

You may face following problems associated with `RecyclerView` when you use a simple subclass
inheriting `RecyclerView.Adapter`.

* Multiple item types are
* It's hard to handle complex data structure to display composed of multiple data sets.
    * e.g.
        * displaying items in a data set followed by items in another data set,
        * displaying a section header, a “read more” button or a progress bars,
        * displaying a item with a special type in the middle of items of other data set,
        * etc.

This library provides a solution for these problems.

## Usage

First, you need to define a subclass of the `ViewHolder` class and its factory.
Typically, it becomes as follows.

```java
public class ContentViewHolder extends RecyclerView.ViewHolder {
    public static final ViewHolderFactory<ContentViewHolder> FACTORY =
            new ViewHolderFactory<ContentViewHolder>() {
                @Override public ContentViewHolder createViewHolder(ViewGroup parent) {
                    TextView v = (TextView) LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.item_content, parent, false);
                    return new ContentViewHolder(v);
                }};

    public final TextView textView;

    public ContentViewHolder(TextView itemView) {
        super(itemView);
        textView = itemView;
    }
}
```

A subclass of the `Binder` class also need to be defined.

```java
public class StringContentBinder implements Binder<ContentViewHolder, String> {
    public static final StringContentBinder INSTANCE = new StringContentBinder();
    @Override
    public void bindViewHolder(ContentViewHolder h, Component<String> c, int posInComponent) {
        h.updateText(c.getItem(posInComponent));
    }
}
```

Then you can write an adapter class using these classes.

```java
public class MyAdapter extends ComponentsRecyclerAdapter {
    /** Registry of ViewHolderFactories. This object should be passed to a constructor of a super class. */
    public static final ViewTypes VIEW_TYPES = new ViewTypes();
    public static class ViewTypes extends ViewHolderFactoryRegistry {
        public final ViewType<ContentViewHolder> content = register(ContentViewHolder.FACTORY);
    }

    /** Components refers a list of strings. */
    private final ListReferenceComponent<String> mStringListComponent = ListReferenceComponent.create(
        // This parameter specifies a view type and a binder to an item respectively.
        // In this example, a view type of all items is a `content`, and a binder of these is a `StringContentBinder`.
        // (It's possible to designate a view type and a binder differently for each item.)
        new FixedViewTypeBinderPairProvider<>(VIEW_TYPES.content, StringContentBinder.INSTANCE)
    );
    // You can other components to represents complex data structure to display.

    public MyAdapter(List<String> list) {
        super(VIEW_TYPES);
        // Set a passed list to the component.
        mStringListComponent.setList(list);
        // Set the component as a root component.
        // (You can create a hierarchy of components here.)
        setComponent(mStringListComponent);
    }
}
```

### Demo project

Please see [the `ExampleAdapter` class](./demo/src/main/java/info/vividcode/android/app/example/cra/ExampleAdapter.java)
in the `demo` project.

The `demo` project has [the `ExampleAdapterOldStyle` class](./demo/src/main/java/info/vividcode/android/app/example/cra/ExampleAdapterOldStyle.java)
also, which provides functions equivalent to the `ExampleAdapter` class using normal `RecyclerView.Adapter` class.
Seeing it, you will contrast adapter in this library with normal one.

## License

Apache License, Version 2.0

## Similar libraries

* [RecyclerView-MultipleViewTypesAdapter](https://github.com/yqritc/RecyclerView-MultipleViewTypesAdapter) by yqritc
* [Smart Adapters Library](https://github.com/mrmans0n/smart-adapters) by Nacho López
