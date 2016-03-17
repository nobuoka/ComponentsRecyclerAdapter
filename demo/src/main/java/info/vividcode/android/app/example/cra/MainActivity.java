package info.vividcode.android.app.example.cra;

import android.databinding.ObservableArrayList;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final ObservableArrayList<String> mStringList = new ObservableArrayList<>();
    {
        mStringList.addAll(Arrays.asList(
                "こんにちは。",
                "サンプルです。"
        ));
    }
    private final List<ListItem> mItemList = Arrays.asList(
            new ListItem.SectionHeader("タイトル 1"),
            new ListItem.Content("内容 1"),
            new ListItem.Content("内容 2"),
            new ListItem.SectionHeader("タイトル 2"),
            new ListItem.Content("内容 3")
    );

    private ExampleAdapterOldStyle mOldAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.item_adding_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int insertedPos = mStringList.size();
                mStringList.add("Date : " + new Date().toString());
                // In case that you use a simple adapter for RecyclerView, you need to notify the adapter of the change of the list.
                mOldAdapter.notifyItemRangeInserted(insertedPos, 1);
                // In contrast, using ObservableListReferenceComponent, you need not notify the adapter of it.
            }
        });

        { // 新版。
            ExampleAdapter adapter = new ExampleAdapter();
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view_new_style);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            adapter.getItemComponent().setList(mItemList);
            adapter.getStringComponent().setList(mStringList);
            recyclerView.setAdapter(adapter);
        }

        { // 旧版。
            ExampleAdapterOldStyle adapter = mOldAdapter = new ExampleAdapterOldStyle(mStringList, mItemList);
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view_old_style);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);
        }
    }

}
