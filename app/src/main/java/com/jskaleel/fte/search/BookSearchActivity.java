package com.jskaleel.fte.search;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jskaleel.fte.R;
import com.jskaleel.fte.app.ConnectionDetector;
import com.jskaleel.fte.base.BaseActivity;
import com.jskaleel.fte.home.BooksHomeAdapter;
import com.jskaleel.fte.home.BooksHomeParser;
import com.jskaleel.fte.home.HomeItemListener;
import com.jskaleel.fte.preferences.UserPreference;
import com.jskaleel.fte.utils.AlertUtils;
import com.jskaleel.fte.utils.DeviceUtils;
import com.jskaleel.fte.utils.TextUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class BookSearchActivity extends BaseActivity implements HomeItemListener {

    private EditText edtSearchText;
    private BooksHomeAdapter booksAdapter;
    private BooksHomeParser booksHomeListParser;
    private String savedfilePath;
    private UserPreference mPreference;
    private Boolean isInternetAvailable = false;
    private ConnectionDetector connectionDetector;
    private ListView listView;
    private RadioButton rdAuthor, rdTitle;
    private RadioGroup searchRadioGroup;

    @Override
    public void onLeftActionClick(View v) {
        super.onLeftActionClick(v);
        forceFinish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        init();
        setupDefaults();
        setupEvents();
    }

    private void init() {
        mPreference = getApp().getUserPreference();

        connectionDetector = new ConnectionDetector(getApplicationContext());
        isInternetAvailable = connectionDetector.isConnectingToInternet();

        setLeftDrawable(R.drawable.back_icon_white);

        searchRadioGroup = (RadioGroup) findViewById(R.id.search_radio_group);
        rdAuthor = (RadioButton) findViewById(R.id.rd_author);
        rdTitle = (RadioButton) findViewById(R.id.rd_title);

        edtSearchText = (EditText) findViewById(R.id.search_text);
        listView = (ListView) findViewById(R.id.books_list);
        booksAdapter = new BooksHomeAdapter(new ArrayList<BooksHomeParser.Books.Book>(), BookSearchActivity.this);
        booksAdapter.setListItemListener(BookSearchActivity.this);
        listView.setAdapter(booksAdapter);
        listView.setEmptyView(findViewById(R.id.empty));
    }

    private void setupDefaults() {
        edtSearchText.setVisibility(View.VISIBLE);

        if (!TextUtils.isNullOrEmpty(mPreference.getBookResponse())) {
            showBookList(mPreference.getBookResponse());
        }
    }

    private void showBookList(String bookResponse) {
        Gson gson = new Gson();
        booksHomeListParser = gson.fromJson(bookResponse, BooksHomeParser.class);
        booksAdapter = new BooksHomeAdapter(booksHomeListParser.books.book, BookSearchActivity.this);
        booksAdapter.setListItemListener(BookSearchActivity.this);

        listView.setAdapter(booksAdapter);
        booksAdapter.setResetPos();
    }

    private void setupEvents() {

        searchRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == rdAuthor.getId()) {
                    String text = edtSearchText.getText().toString();
                    booksAdapter.filter(text, 1);
                } else if (checkedId == rdTitle.getId()) {
                    String text = edtSearchText.getText().toString();
                    booksAdapter.filter(text, 2);
                }
            }
        });

        edtSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    DeviceUtils.hideSoftKeyboard(BookSearchActivity.this, edtSearchText);
                    return true;
                }
                return false;
            }
        });

        edtSearchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String key = s.toString().trim();
                int checkedId = searchRadioGroup.getCheckedRadioButtonId();
                if (key.length() != 0) {
                    if (checkedId == rdAuthor.getId()) {
                        booksAdapter.filter(key, 1);
                    } else if (checkedId == rdTitle.getId()) {
                        booksAdapter.filter(key, 2);
                    }
                } else if (key.length() == 0) {
                    if (checkedId == rdAuthor.getId()) {
                        booksAdapter.filter(key, 1);
                    } else if (checkedId == rdTitle.getId()) {
                        booksAdapter.filter(key, 2);
                    }
                }
            }
        });
    }

    @Override
    public void DownloadPressed(BooksHomeParser.Books.Book singleItem) {
        final String url = Uri.parse(singleItem.epub).toString();
        DeviceUtils.hideSoftKeyboard(BookSearchActivity.this, edtSearchText);
        if (isInternetAvailable) {
            new TaskDownloadEpub(singleItem).execute(url);
        } else {
            AlertUtils.showAlert(BookSearchActivity.this, getResources().getString(R.string.check_connection));
        }
    }

    class TaskDownloadEpub extends AsyncTask<String, String, String> {
        BooksHomeParser.Books.Book singleItem;
        ProgressDialog downDialog;

        private boolean isCancelled = false;

        public TaskDownloadEpub(BooksHomeParser.Books.Book singleItem) {
            this.singleItem = singleItem;
        }

        @SuppressWarnings("static-access")
        protected void onPreExecute() {
            super.onPreExecute();
            downDialog = new ProgressDialog(BookSearchActivity.this);
            downDialog.setMessage(getResources().getString(R.string.downloading));
            downDialog.setCancelable(false);
            downDialog.setProgress(0);
            downDialog.setProgressStyle(downDialog.STYLE_HORIZONTAL);
            downDialog.setMax(100);
            downDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    isCancelled = true;
                    dialog.dismiss();
                }
            });
            downDialog.show();
        }

        @Override
        protected String doInBackground(String... f_url) {
            int count;
            while (!isCancelled) {
                try {
                    URL url = new URL(f_url[0]);
                    URLConnection conection = url.openConnection();
                    conection.connect();

                    int lenghtOfFile = conection.getContentLength();
                    InputStream input = new BufferedInputStream(url.openStream(), 1024 * 10);
                    File path = new File(Environment.getExternalStorageDirectory(), "/Free_Tamil_Ebooks");

                    if (!(path.exists()))
                        path.mkdir();

                    savedfilePath = path + "/" + singleItem.title + ".epub";
                    OutputStream output = new FileOutputStream(savedfilePath);
                    byte data[] = new byte[1024];

                    long total = 0;

                    while ((count = input.read(data)) != -1) {
                        if (!isCancelled) {
                            total += count;
                            publishProgress("" + (int) ((total * 100) / lenghtOfFile));
                            output.write(data, 0, count);
                        } else {
                            File file = new File(savedfilePath);
                            file.delete();
                            return null;
                        }
                    }
                    output.flush();
                    output.close();
                    input.close();

                } catch (Exception e) {
                    Log.e("Error: ", e.getMessage());
                }

                return savedfilePath;
            }
            return null;
        }

        protected void onProgressUpdate(String... progress) {
            downDialog.setProgress(Integer.parseInt(progress[0]));
        }

        protected void onPostExecute(String filePath) {
            super.onPostExecute(filePath);
            if (filePath != null) {
                downDialog.dismiss();
                methodNotify(savedfilePath);
                showOkCancel(savedfilePath, 1, singleItem);
            }
        }
    }

    public void methodNotify(String string) {
        Notification noti = new NotificationCompat.Builder(getApplicationContext())
                .setContentTitle(getString(R.string.app_name))
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentText(getApplicationContext().getString(R.string.file_saved)).build();
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify((int) System.currentTimeMillis(), noti);
    }

    public void showOkCancel(final String filePath, final int from, final BooksHomeParser.Books.Book singleItem) {    // from = 1 --> request to OpenBook
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(BookSearchActivity.this);

        alertDialog.setTitle(getResources().getString(R.string.app_name));

        if (from == 1) {
            alertDialog.setMessage(getResources().getString(R.string.open_dialog));
        } else if (from == 2) {
            alertDialog.setMessage(getResources().getString(R.string.book_not_found));
        } else if (from == 3) {
            alertDialog.setMessage(getResources().getString(R.string.down_epub));
        }

        alertDialog.setIcon(R.drawable.ic_launcher);

        alertDialog.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (from == 1) {
                    openBook(filePath);
                } else if (from == 2) {
                    DownloadPressed(singleItem);
                } else if (from == 3) {
                    downloadIt("org.geometerplus.zlibrary.ui.android");
                }
            }
        });

        alertDialog.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }

    protected void openBook(String filePath) {
        File file = new File(filePath);
        String extension = filePath.substring((filePath.lastIndexOf(".") + 1), filePath.length());

        if (extension.equals("epub")) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(file), "application/epub");
            ComponentName cn = new ComponentName("org.geometerplus.zlibrary.ui.android", "org.geometerplus.android.fbreader.FBReader");
            intent.setComponent(cn);
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                showOkCancel("", 3, null);
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

    @Override
    public void OpenPressed(BooksHomeParser.Books.Book singleItem) {
        File path = new File(Environment.getExternalStorageDirectory(), "/Free_Tamil_Ebooks/" + singleItem.title + ".epub");
        if (path.exists()) {
            openBook(path.toString());
        } else {
            showOkCancel(path.toString(), 2, singleItem);
        }
    }
}
