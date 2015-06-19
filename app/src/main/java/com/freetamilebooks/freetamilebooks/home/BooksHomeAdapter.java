package com.freetamilebooks.freetamilebooks.home;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.freetamilebooks.freetamilebooks.R;
import com.freetamilebooks.freetamilebooks.app.FTEApp;
import com.freetamilebooks.freetamilebooks.utils.DeviceUtils;
import com.nineoldandroids.animation.ObjectAnimator;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BooksHomeAdapter extends BaseAdapter implements OnScrollListener {

    private Context context;
    private ArrayList<BooksHomeParser.Books.Book> bookList;
    private ArrayList<BooksHomeParser.Books.Book> searchListArray;
    private HomeFragment fragmentHome;
    private Typeface tf;

    private boolean fastAnim, isAnim;
    private int SwipeLength;
    private HomeItemListener homeItemListener;
    private int selectedPos = 0;

    public BooksHomeAdapter(ArrayList<BooksHomeParser.Books.Book> bookList, HomeFragment fragmentHome, Context context) {
        this.bookList = bookList;
        this.fragmentHome = fragmentHome;
        this.context = context;
        searchListArray = new ArrayList<BooksHomeParser.Books.Book>();
        this.searchListArray.addAll(bookList);
        SwipeLength = DeviceUtils.getPixelFromDp(context, 110);
    }

    @Override
    public int getCount() {
        return bookList.size();
    }

    @Override
    public Object getItem(int position) {
        return bookList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setListItemListener(HomeItemListener listener) {
        this.homeItemListener = listener;
    }

    public void setResetPos() {
        selectedPos = -1;
        fastAnim = true;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View layout = null;
        final ItemViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflator = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = (View) inflator.inflate(R.layout.books_list_item, null);
            holder = new ItemViewHolder();

            holder.txtBookTitle = (TextView) layout.findViewById(R.id.txtTitle);
            holder.txtAuthorName = (TextView) layout.findViewById(R.id.txtAuthor);
            holder.ivBookIcon = (ImageView) layout.findViewById(R.id.ivBookImage);
            holder.ivProgress = (ProgressBar) layout.findViewById(R.id.ivProgress);

            holder.baseLayout = (LinearLayout) layout.findViewById(R.id.baseLayout);
            holder.btnDownload = (TextView) layout.findViewById(R.id.btnDown);
            holder.btnOpen = (TextView) layout.findViewById(R.id.btnOpen);


            layout.setTag(holder);
        } else {
            layout = (RelativeLayout) convertView;
            holder = (ItemViewHolder) layout.getTag();
        }
        tf = Typeface.createFromAsset(context.getAssets(), "fonts/NotoSansTamil-Bold.ttf");

        final BooksHomeParser.Books.Book singleItem = bookList.get(position);

        if (singleItem.title != null && !singleItem.title.equalsIgnoreCase("")) {
            holder.txtBookTitle.setTypeface(tf);
            holder.txtBookTitle.setText(singleItem.title);
        } else {
            holder.txtBookTitle.setText("");
        }

        if (singleItem.author != null && !singleItem.author.equalsIgnoreCase("")) {
            holder.txtAuthorName.setTypeface(tf);
            holder.txtAuthorName.setText(singleItem.author);
        } else {
            holder.txtAuthorName.setText("");
        }

        if (singleItem.image != null) {
            Picasso.with(context).load(singleItem.image).into(holder.ivBookIcon);
            holder.ivProgress.setVisibility(View.GONE);
        } else {
            holder.ivProgress.setVisibility(View.GONE);
        }

        if (!FTEApp.PRE_HC_11) {
            if (selectedPos != position) {
                if (fastAnim)
                    ObjectAnimator.ofFloat(holder.baseLayout, "translationX", 0).setDuration(0).start();
                else
                    ObjectAnimator.ofFloat(holder.baseLayout, "translationX", 0).setDuration(500).start();
            } else {
                if (fastAnim)
                    ObjectAnimator.ofFloat(holder.baseLayout, "translationX", 0).setDuration(0).start();
            }
            isAnim = false;
        }

        holder.baseLayout.setOnTouchListener(new OnListItemTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return super.onTouch(view, motionEvent);
            }

            @Override
            public void onMove(float difference) {
                if (!FTEApp.PRE_HC_11) {
                    if (!isAnim) {
                        if (difference <= -7) {
                            isAnim = true;
                            ObjectAnimator.ofFloat(holder.baseLayout, "translationX", -SwipeLength).setDuration(500).start();
                            selectedPos = position;
                            fastAnim = false;
                            notifyDataSetChanged();
                        } else if (difference >= 7) {
                            isAnim = true;
                            ObjectAnimator.ofFloat(holder.baseLayout, "translationX", 0).setDuration(500).start();
                            notifyDataSetChanged();
                        }
                    }
                }
                super.onMove(difference);
            }

            @Override
            public void onUp(float difference, float touchUpX) {
                if (!FTEApp.PRE_HC_11) {
                    if (difference <= -7) {
                        ObjectAnimator.ofFloat(holder.baseLayout, "translationX", -SwipeLength).setDuration(500).start();
                        selectedPos = position;
                        fastAnim = false;
                    } else if (difference >= 7) {
                        ObjectAnimator.ofFloat(holder.baseLayout, "translationX", 0).setDuration(500).start();
                    }
                    notifyDataSetChanged();
                }
                super.onUp(difference, touchUpX);
            }

            @Override
            public void onCancel(float difference) {
                super.onCancel(difference);
            }

            @Override
            public void onSwipeRight() {
                if (!FTEApp.PRE_HC_11) {
                    ObjectAnimator.ofFloat(holder.baseLayout, "translationX", 0).setDuration(500).start();
                    notifyDataSetChanged();
                }
                super.onSwipeRight();
            }

            @Override
            public void onSwipeLeft() {
                if (!FTEApp.PRE_HC_11) {
                    ObjectAnimator.ofFloat(holder.baseLayout, "translationX", -SwipeLength).setDuration(500).start();
                    fastAnim = false;
                    selectedPos = position;
                    notifyDataSetChanged();
                }
                super.onSwipeLeft();
            }

            @Override
            public void onSwipeTop() {
                super.onSwipeTop();
            }

            @Override
            public void onSwipeBottom() {
                super.onSwipeBottom();
            }

            @Override
            public void onHover() {
                super.onHover();
            }
        });

        holder.btnDownload.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (homeItemListener != null) {
                    homeItemListener.DownloadPressed(singleItem);
                }
            }
        });

        holder.btnOpen.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (homeItemListener != null) {
                    homeItemListener.OpenPressed(singleItem);
                }
            }
        });
        return layout;
    }

    public static class ItemViewHolder {
        private TextView txtBookTitle, txtAuthorName, btnDownload, btnOpen;
        private ProgressBar ivProgress;
        private ImageView ivBookIcon;
        private LinearLayout baseLayout;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == 2 || scrollState == 1)
            fastAnim = true;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
    }

    // Filter Class
    public void filter(String charText, int type) {
        if (charText == null) {
            charText = "";
        }
        bookList.clear();
        if (charText.length() == 0) {
            bookList.addAll(searchListArray);
        } else {
            for (BooksHomeParser.Books.Book tempBooks : searchListArray) {
                if (type == 1) {
                    if (tempBooks.author.contains(charText)) {
                        bookList.add(tempBooks);
                    }
                } else if (type == 2) {
                    if (tempBooks.title != null) {
                        if (tempBooks.title.contains(charText)) {
                            bookList.add(tempBooks);
                        }
                    }
                }
            }
        }
        notifyDataSetChanged();
    }
}
