package com.jskaleel.fte.home;

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
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.jskaleel.fte.R;
import com.jskaleel.fte.app.ConnectionDetector;
import com.jskaleel.fte.base.BaseFragment;
import com.jskaleel.fte.leftmenu.HomeActivity;
import com.jskaleel.fte.preferences.UserPreference;
import com.jskaleel.fte.search.BookSearchActivity;
import com.jskaleel.fte.utils.AlertUtils;
import com.jskaleel.fte.utils.FTELog;
import com.jskaleel.fte.utils.TextUtils;
import com.jskaleel.fte.webservice.FTEHttpClient;
import com.jskaleel.fte.webservice.FTERequestHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class HomeFragment extends BaseFragment implements HomeItemListener, SwipeRefreshLayout.OnRefreshListener {
    private ListView listView;
    private BooksHomeParser booksHomeListParser;
    private BooksHomeAdapter booksHomeAdapter;
    private static final String xmlUrl = "https://raw.githubusercontent.com/kishorek/Free-Tamil-Ebooks/master/booksdb.xml";
    private Boolean isInternetAvailable = false;
    private ConnectionDetector connectionDetector;
    private String savedfilePath;
    private HomeActivity mainActivity;
    private SwipeRefreshLayout swipeRefreshLayout;
    private UserPreference mPreference;

    @Override
    public void onRightActionClick(View v) {
        super.onRightActionClick(v);
        startActivity(new Intent(getActivity(), BookSearchActivity.class));
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setLeftDrawable(R.drawable.left_menu_white);
        setRightDrawable(R.drawable.search_icon);
        setPageTitle(R.string.home);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, null);

        connectionDetector = new ConnectionDetector(getActivity());
        isInternetAvailable = connectionDetector.isConnectingToInternet();

        init(rootView);
        setupDefaults();
        setupEvents();

        return rootView;
    }

    private void init(View view) {

        mPreference = getApp().getUserPreference();

        listView = (ListView) view.findViewById(R.id.books_list);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.swipe_color1, R.color.swipe_color2, R.color.swipe_color3, R.color.swipe_color4);

        booksHomeAdapter = new BooksHomeAdapter(new ArrayList<BooksHomeParser.Books.Book>(), getActivity());
        booksHomeAdapter.setListItemListener(HomeFragment.this);
        listView.setAdapter(booksHomeAdapter);
        listView.setEmptyView(view.findViewById(R.id.empty));

        mainActivity = (HomeActivity) getActivity();
    }

    private void setupDefaults() {

            if (TextUtils.isNullOrEmpty(mPreference.getBookResponse())) {
                if (isInternetAvailable) {
                    getBookList(true);
                }else {
                    AlertUtils.showAlert(getActivity(), getResources().getString(R.string.check_connection));
                }
            } else {
                showBookList(mPreference.getBookResponse());
            }
    }

    private void showBookList(String bookResponse) {
        Gson gson = new Gson();
        booksHomeListParser = gson.fromJson(bookResponse, BooksHomeParser.class);
        booksHomeAdapter = new BooksHomeAdapter(booksHomeListParser.books.book, getActivity());
        booksHomeAdapter.setListItemListener(HomeFragment.this);

        listView.setAdapter(booksHomeAdapter);
        booksHomeAdapter.setResetPos();
    }

    private void setupEvents() {
        swipeRefreshLayout.setOnRefreshListener(this);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                boolean enable = false;
                if (listView != null && listView.getChildCount() > 0) {
                    boolean firstItemVisible = listView.getFirstVisiblePosition() == 0;
                    boolean topOfFirstItemVisible = listView.getChildAt(0).getTop() == 0;
                    enable = firstItemVisible && topOfFirstItemVisible;
                }
                swipeRefreshLayout.setEnabled(enable);
            }
        });
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        getBookList(false);
    }

    private void getBookList(boolean showProgDiag) {
        final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
        if (showProgDiag) {
            mProgressDialog.setMessage(getString(R.string.loading_));
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        getHttpClient().performRequest(xmlUrl, FTEHttpClient.HttpMethod.GET, null, new FTERequestHandler() {
            @Override
            public void onSuccess(String content) {
                if (getActivity() != null) {
                    FTELog.print(content);
                    JSONObject jsonObj = null;
                    try {
                        jsonObj = XML.toJSONObject(content);
                        if (jsonObj.toString() != null) {
                            mPreference.setBookResponse(jsonObj.toString());
                            showBookList(jsonObj.toString());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (mProgressDialog != null && mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();
                    }
                    stopSwipeRefresh();
                }
            }

            @Override
            public void onFailure(int statusCode, Throwable error, String message) {
                if (getActivity() != null) {
                    if (mProgressDialog != null && mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();
                    }
                    AlertUtils.showAlert(getActivity(), message);
                    stopSwipeRefresh();
                }
            }

            @Override
            public void onProgressUpdate(long percent) {
            }
        });
    }

    private void stopSwipeRefresh() {
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    public void downloadEpub(BooksHomeParser.Books.Book singleItem) {
        final String url = Uri.parse(singleItem.epub).toString();
        new TaskDownloadEpub(singleItem).execute(url);
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
            downDialog = new ProgressDialog(getActivity());
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

    @Override
    public void DownloadPressed(BooksHomeParser.Books.Book singleItem) {
        final String url = Uri.parse(singleItem.epub).toString();
        new TaskDownloadEpub(singleItem).execute(url);
    }

    public void methodNotify(String string) {
        Notification noti = new NotificationCompat.Builder(getActivity())
                .setContentTitle(getString(R.string.app_name))
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentText(getActivity().getString(R.string.file_saved)).build();
        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify((int) System.currentTimeMillis(), noti);
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

    public void showOkCancel(final String filePath, final int from, final BooksHomeParser.Books.Book singleItem) {    // from = 1 --> request to OpenBook
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

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
}
