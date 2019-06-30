package tech.shmy.dd_app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import tech.shmy.dd_app.R;
import tech.shmy.dd_app.adapter.SearchItemAdapter;
import tech.shmy.dd_app.defs.AfterResponse;
import tech.shmy.dd_app.defs.BaseActivity;
import tech.shmy.dd_app.entity.VideoEntity;
import tech.shmy.dd_app.util.HttpClient;

public class SearchActivity extends BaseActivity {
    private ListView listView;
    private SearchItemAdapter searchItemAdapter;
    private List<VideoEntity> videoEntities = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        EditText editText = findViewById(R.id.keyword);
        listView = findViewById(R.id.result);
        editText.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                Intent intent = new Intent(SearchActivity.this, SearchResultActivity.class);
                intent.putExtra("keyword", editText.getText().toString());
                pushActivity(intent);
            }
            return false;
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String keyword = charSequence.toString();
                if (keyword.length() == 0) {
                    searchItemAdapter.clearVideoEntities();
                    searchItemAdapter.notifyDataSetChanged();
                }
                if (keyword.length() > 1) {
                    handleFetch(keyword);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        searchItemAdapter = new SearchItemAdapter(this);
        listView.setAdapter(searchItemAdapter);
        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            VideoEntity videoEntity = searchItemAdapter.getItem(i);
            Intent intent = new Intent(this, VideoActivity.class);
            intent.putExtra("id", videoEntity.id);
            intent.putExtra("pic", videoEntity.pic);
            pushActivity(intent);
        });
    }

    private void handleFetch(String keyword) {
       new Thread(() -> {
           AfterResponse<List<VideoEntity>> afterResponse = HttpClient.getSearchList(keyword, 1, 15);
           SearchActivity.this.runOnUiThread(() -> {
           if (afterResponse.error != null) {
               return;
           }
               searchItemAdapter.setVideoEntities(afterResponse.data);
               searchItemAdapter.notifyDataSetChanged();
           });
       }).start();
    }
}
