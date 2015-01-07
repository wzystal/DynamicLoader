package com.wzystal.dynamicloader;

import java.io.File;
import java.util.ArrayList;

import com.ryg.dynamicload.DLBasePluginActivity;
import com.ryg.dynamicload.DLClassLoader;
import com.ryg.dynamicload.DLProxyActivity;
import com.ryg.dynamicload.DLProxyFragmentActivity;
import com.ryg.utils.DLConstants;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileObserver;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnItemClickListener {
	private static final String DIR_PLUGIN = Environment
			.getExternalStorageDirectory() + "/plugins/";
	private PluginObserver pluginObserver;
	private GridView gridview_plugins;
	private ArrayList<Plugin> pluginList = new ArrayList<Plugin>();
	private PluginAdapter pluginAdapter;
	public Handler mHandler = new Handler() {
		@Override
		public void dispatchMessage(Message msg) {
			if (null == msg || null == pluginAdapter)
				return;
			String pluginPath = msg.getData().getString("pluginPath");
			Plugin plugin = new Plugin(MainActivity.this, DIR_PLUGIN
					+ pluginPath);
			switch (msg.what) {
			case FileObserver.CREATE:
				Toast.makeText(MainActivity.this, "CREATE: " + pluginPath,
						Toast.LENGTH_LONG).show();
				pluginList.add(plugin);
				pluginAdapter.notifyDataSetChanged();
				break;
			case FileObserver.DELETE:
				Toast.makeText(MainActivity.this, "DELETE: " + pluginPath,
						Toast.LENGTH_LONG).show();
				pluginList.remove(plugin);
				pluginAdapter.notifyDataSetChanged();
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		gridview_plugins = (GridView) findViewById(R.id.gridview_plugins);
		new PluginsInitTask().execute();
		pluginObserver = new PluginObserver(mHandler, DIR_PLUGIN);
	}

	@Override
	protected void onResume() {
		super.onResume();
		pluginObserver.startWatching();
	}

	@Override
	protected void onStop() {
		super.onStop();
		pluginObserver.stopWatching();
	}

	class PluginsInitTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			File dir = new File(DIR_PLUGIN);
			File[] plugins = dir.listFiles();
			if (plugins == null || plugins.length == 0) {
				return null;
			}
			for (File file : plugins) {
				if (file.getName().endsWith(".apk")) {
					Plugin plugin = new Plugin(MainActivity.this,
							file.getAbsolutePath());
					pluginList.add(plugin);
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			pluginAdapter = new PluginAdapter(MainActivity.this, pluginList);
			gridview_plugins.setAdapter(pluginAdapter);
			gridview_plugins.setOnItemClickListener(MainActivity.this);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Plugin item = pluginList.get(position);
		Toast.makeText(this, "selected apk: " + item.getPluginPath(),
				Toast.LENGTH_LONG).show();
		Class<?> proxyCls = null;
		try {
			Class<?> cls = Class.forName(item.getLauncherActivity(), false,
					DLClassLoader.getClassLoader(item.getPluginPath(),
							getApplicationContext(), getClassLoader()));
			if (cls.asSubclass(DLBasePluginActivity.class) != null) {
				proxyCls = DLProxyActivity.class;
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			Toast.makeText(
					this,
					"load plugin apk failed, load class "
							+ item.getLauncherActivity() + " failed.",
					Toast.LENGTH_SHORT).show();
		} catch (ClassCastException e) {
			e.printStackTrace();
		} finally {
			if (proxyCls == null) {
				proxyCls = DLProxyFragmentActivity.class;
			}
			Intent intent = new Intent(this, proxyCls);
			intent.putExtra(DLConstants.EXTRA_DEX_PATH, pluginList
					.get(position).getPluginPath());
			startActivity(intent);
		}
	}

}
