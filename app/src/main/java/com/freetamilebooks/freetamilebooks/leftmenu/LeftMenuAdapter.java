package com.freetamilebooks.freetamilebooks.leftmenu;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.freetamilebooks.freetamilebooks.R;
import com.freetamilebooks.freetamilebooks.utils.DeviceUtils;
import com.freetamilebooks.freetamilebooks.utils.TypefaceUtils;
import com.freetamilebooks.freetamilebooks.views.AnimatedExpandableListView;

import java.util.ArrayList;

public class LeftMenuAdapter extends AnimatedExpandableListView.AnimatedExpandableListAdapter {

    private LayoutInflater mInflater;
    private Activity mActivity;
    private int menuType;
    private ArrayList<LeftMenuFragment.LeftMenuItem> menuList;

    public LeftMenuAdapter(Activity activity, int type) {
        this.mActivity = activity;
        mInflater = LayoutInflater.from(activity.getBaseContext());
        this.menuType = type;
        menuList = new ArrayList<>();

            menuList = getLeftMenuListItem();
    }

    @Override
    public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.home_left_menu_item, null);
        }

        LeftMenuFragment.LeftMenuItem item = getChild(groupPosition, childPosition);
        TextView txtName = (TextView) convertView.findViewById(R.id.left_menu_item);
        txtName.setText(item.name);
        txtName.setTextColor(mActivity.getResources().getColor(R.color.text_dark_color));
        txtName.setPadding(DeviceUtils.getPixelFromDp(mActivity, 50), DeviceUtils.getPixelFromDp(mActivity, 12), 0, DeviceUtils.getPixelFromDp(mActivity, 12));

        View section = convertView.findViewById(R.id.left_menu_section);
        section.setVisibility(item.showSection ? View.VISIBLE : View.GONE);
        return convertView;
    }

    @Override
    public int getRealChildrenCount(int groupPosition) {
        return menuList.get(groupPosition).subMenuItems.size();
    }

    @Override
    public int getGroupCount() {
        return menuList.size();
    }

    @Override
    public LeftMenuFragment.LeftMenuItem getGroup(int groupPosition) {
        return menuList.get(groupPosition);
    }

    @Override
    public LeftMenuFragment.LeftMenuItem getChild(int groupPosition, int childPosition) {
        return menuList.get(groupPosition).subMenuItems.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.home_left_menu_item, null);
        }

        LeftMenuFragment.LeftMenuItem item = getGroup(groupPosition);
        TextView txtName = (TextView) convertView.findViewById(R.id.left_menu_item);
        View section = convertView.findViewById(R.id.left_menu_section);
        txtName.setText(item.name);
        txtName.setTextColor(mActivity.getResources().getColor(R.color.text_dark_color));
        txtName.setTypeface(TypefaceUtils.getTypeFace(mActivity, TypefaceUtils.LIGHT));
        section.setVisibility(View.VISIBLE);

        txtName.setPadding(DeviceUtils.getPixelFromDp(mActivity, 22), DeviceUtils.getPixelFromDp(mActivity, 12), DeviceUtils.getPixelFromDp(mActivity, 20), DeviceUtils.getPixelFromDp(mActivity, 12));
        txtName.setTypeface(isExpanded ? TypefaceUtils.getTypeFace(mActivity, TypefaceUtils.MEDIUM) : TypefaceUtils.getTypeFace(mActivity, TypefaceUtils.LIGHT));

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private ArrayList<LeftMenuFragment.LeftMenuItem> getLeftMenuListItem() {
        ArrayList<LeftMenuFragment.LeftMenuItem> items = new ArrayList<>();
        ArrayList<LeftMenuFragment.LeftMenuItem> subItems = new ArrayList<>();
        items.add(new LeftMenuFragment.LeftMenuItem(mActivity.getString(R.string.home), subItems, false));
        items.add(new LeftMenuFragment.LeftMenuItem(mActivity.getString(R.string.aboutus), subItems, false));
        items.add(new LeftMenuFragment.LeftMenuItem(mActivity.getString(R.string.our_team), subItems, false));
        items.add(new LeftMenuFragment.LeftMenuItem(mActivity.getString(R.string.help_us), subItems, false));
        items.add(new LeftMenuFragment.LeftMenuItem(mActivity.getString(R.string.comments), subItems, false));
        items.add(new LeftMenuFragment.LeftMenuItem(mActivity.getString(R.string.downloads), subItems, false));
//        items.add(new LeftMenuFragment.LeftMenuItem(R.drawable.settings, mActivity.getString(R.string.settings), subItems, false));
        return items;
    }
}
