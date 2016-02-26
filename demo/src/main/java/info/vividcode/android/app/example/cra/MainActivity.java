package info.vividcode.android.app.example.cra;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final List<String> mStringList = Arrays.asList(
            "こんにちは。",
            "サンプルです。"
    );
    private final List<ListItem> mItemList = Arrays.asList(
            new ListItem.SectionHeader("タイトル 1"),
            new ListItem.Content("内容 1"),
            new ListItem.Content("内容 2"),
            new ListItem.SectionHeader("タイトル 2"),
            new ListItem.Content("内容 3")
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        { // 新版。
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view_new_style);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            final ExampleAdapter adapter = new ExampleAdapter();
            adapter.getItemComponent().setList(mItemList);
            adapter.getStringComponent().setList(mStringList);
            recyclerView.setAdapter(adapter);
        }

        { // 旧版。
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view_old_style);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            final ExampleAdapterOldStyle adapter = new ExampleAdapterOldStyle(mStringList, mItemList);
            recyclerView.setAdapter(adapter);
        }
    }

}
