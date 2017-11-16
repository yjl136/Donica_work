package cn.donica.slcd.settings.bite;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.MenuItem;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.donica.slcd.settings.R;

/**
 * Project Name:   Donica
 * Create By:      aLinGe
 * Create Time:    2017-09-22 14:39
 * Describe:
 */

public class BiteActivity extends Activity {
    @BindView(R.id.biteRv)
    public RecyclerView biteRV;
    private List<BiteEntity> bites;
    private Map<String, String> biteMap;
    private String[] biteKeys = new String[]{
            "temperature",
            "cpu",
            "memory_all",
            "memory_used",
            "memory_free",
            "ssd_all",
            "ssd_free",
            "ssd_used"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_bite);
        initToolBar();
        ButterKnife.bind(this);
        initBites();
        BiteAdapter adapter = new BiteAdapter(this, bites);
        biteRV.setLayoutManager(new LinearLayoutManager(this));
        biteRV.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initToolBar() {
        String title = "<h5>" + getString(R.string.system_bite) + "</h5>";
        ActionBar actionBar = this.getActionBar();
        actionBar.setTitle(Html.fromHtml(title));
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.show();
    }

    private void initBites() {
        if (biteMap == null) {
            biteMap = new HashMap<String, String>();
        }
        biteMap.put("temperature", getString(R.string.cpu_temperature));
        biteMap.put("cpu", getString(R.string.cpu_usage));
        biteMap.put("memory_all", getString(R.string.memory_all));
        biteMap.put("memory_used", getString(R.string.memory_used));
        biteMap.put("memory_free", getString(R.string.memory_free));
        biteMap.put("ssd_all", getString(R.string.ssd_all));
        biteMap.put("ssd_free", getString(R.string.ssd_free));
        biteMap.put("ssd_used", getString(R.string.ssd_used));

        if (bites == null) {
            bites = new ArrayList<BiteEntity>();
        }
        for (int i = 0; i < biteKeys.length; i++) {
            bites.add(getBiteEntity(biteKeys[i]));
        }
    }

    /**
     * 获取自检实体
     *
     * @param key
     * @return
     */
    private BiteEntity getBiteEntity(String key) {
        ContentResolver resolver = getContentResolver();
        Cursor cursor = null;
        String value = "N/A";
        try {
            cursor = resolver.query(Uri.parse("content://cn.donica.slcd.provider/bite"), null, "name=?", new String[]{key}, null, null);
            if (cursor.moveToFirst()) {
                int valueIndex = cursor.getColumnIndex("value");
                value = cursor.getString(valueIndex);
            }
        } catch (Exception e) {
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (key.equals("memory_all") || key.equals("memory_used") || key.equals("memory_free") || key.equals("ssd_all") || key.equals("ssd_free") || key.equals("ssd_used")) {
                return new BiteEntity(biteMap.get(key), BiteHelper.kb2mb(value));
            } else if (key.equals("temperature")) {
                return new BiteEntity(biteMap.get(key), BiteHelper.formatTemperature(value));
            } else if (key.equals("cpu")) {
                return new BiteEntity(biteMap.get(key), BiteHelper.formatCPU(value));
            } else {
                return new BiteEntity(biteMap.get(key), value);
            }

        }
    }
}
