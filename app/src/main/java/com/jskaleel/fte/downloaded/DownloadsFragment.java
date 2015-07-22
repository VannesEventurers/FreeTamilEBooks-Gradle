package com.jskaleel.fte.downloaded;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jskaleel.fte.R;
import com.jskaleel.fte.base.BaseFragment;
import com.jskaleel.fte.utils.AlertUtils;

import java.io.File;
import java.util.ArrayList;

public class DownloadsFragment extends BaseFragment {
    private ArrayList<String> item = null;
    private ArrayList<String> path = null;
    private String root;
    private TextView myPath;
    private ListView downloadsList;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setPageTitle(getString(R.string.downloads));
        setLeftDrawable(R.drawable.left_menu_white);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_downloads, null);

        init(view);
        setupDefaults();
        setupEvents();

        return view;
    }

    private void init(View view) {
        myPath = (TextView) view.findViewById(R.id.path);
        downloadsList = (ListView) view.findViewById(R.id.download_list);
        downloadsList.setEmptyView(view.findViewById(R.id.empty));
    }

    private void setupDefaults() {
        root = Environment.getExternalStorageDirectory().getPath() + "/Free_Tamil_Ebooks/";
        getDir(root);
    }

    private void setupEvents() {
        downloadsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OpenBook(path.get(position));
            }
        });
    }

    private void getDir(String dirPath) {

        myPath.setText("Location: " + dirPath);
        item = new ArrayList<>();
        path = new ArrayList<>();
        File f = new File(dirPath);
        File[] files = f.listFiles();

        if (!dirPath.equals(root)) {
            item.add(root);
            path.add(root);
            item.add("../");
            path.add(f.getParent());
        }

        for (int i = 0; i < files.length; i++) {
            File file = files[i];

            if (!file.isHidden() && file.canRead()) {
                path.add(file.getPath());
                if (!file.isDirectory()) {
                    item.add(file.getName());
                }
            }
        }
        ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, item);
        downloadsList.setAdapter(adapter);

    }

    public void OpenBook(String path) {
        // TODO Auto-generated method stub
        File file = new File(root + path);

        if (file.isDirectory()) {
            if (file.canRead()) {
                getDir(file.toString());
            } else {
                new AlertDialog.Builder(getActivity()).setIcon(R.drawable.ic_launcher).setTitle("[" + file.getName() + "] folder can't be read!")
                        .setPositiveButton("Ok", null).show();
            }
        } else {
            String extension = (file.toString()).substring(((file.toString()).lastIndexOf(".") + 1), (file.toString()).length());

            if (extension.equals("epub")) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setDataAndType(Uri.fromFile(file), "application/epub");
                ComponentName cn = new ComponentName("org.geometerplus.zlibrary.ui.android", "org.geometerplus.android.fbreader.FBReader");
                intent.setComponent(cn);
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    AlertUtils.showAlertWithYesNo(getActivity(), "", getString(R.string.down_epub), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            downloadIt("org.geometerplus.zlibrary.ui.android");
                        }
                    }, false);
                }
            } else {
                AlertUtils.showAlert(getActivity(), getString(R.string.wrong_format));
            }
        }
    }

    private void downloadIt(String packageName) {
        Uri uri = Uri.parse("market://search?q=" + packageName + ".FBReader");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);

        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            String url = "https://play.google.com/store/apps/details?id=" + packageName + ".FBReader";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        }
    }
}
