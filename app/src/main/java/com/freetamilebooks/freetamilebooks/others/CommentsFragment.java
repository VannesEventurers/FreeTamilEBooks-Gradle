package com.freetamilebooks.freetamilebooks.others;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.freetamilebooks.freetamilebooks.R;
import com.freetamilebooks.freetamilebooks.base.BaseFragment;
import com.freetamilebooks.freetamilebooks.utils.AlertUtils;
import com.freetamilebooks.freetamilebooks.utils.TextUtils;

public class CommentsFragment extends BaseFragment {

    private EditText edtFName, edtLName, edtMail, edtMessage;
    private Button btnSubmit;
    private static final int FROM_EMAIL_CLIENT = 121;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setPageTitle(getString(R.string.comments));
        setLeftDrawable(R.drawable.left_menu_white);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact, null);

        init(view);
        setupDefaults();
        setupEvents();

        return view;
    }

    private void init(View view) {
        edtFName = (EditText) view.findViewById(R.id.edtFName);
        edtLName = (EditText) view.findViewById(R.id.edtLName);
        edtMail = (EditText) view.findViewById(R.id.edtMail);
        edtMessage = (EditText) view.findViewById(R.id.edtMessage);

        btnSubmit = (Button) view.findViewById(R.id.btnSubmit);
    }

    private void setupDefaults() {
        edtFName.setText("");
        edtLName.setText("");
        edtMail.setText("");
        edtMessage.setText("");
    }

    private void setupEvents() {
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateFields();
            }
        });
    }

    protected void validateFields() {
        String FName = edtFName.getText().toString();
        String LName = edtLName.getText().toString();
        String EMail = edtMail.getText().toString();
        String Message = edtMessage.getText().toString();

        if(TextUtils.isNullOrEmpty(FName)) {
            AlertUtils.showAlert(getActivity(), getString(R.string.firstname_alert));
            return;
        }

        if(TextUtils.isNullOrEmpty(LName)) {
            AlertUtils.showAlert(getActivity(), getString(R.string.lastname_alert));
            return;
        }

        if(TextUtils.isNullOrEmpty(EMail)) {
            AlertUtils.showAlert(getActivity(), getString(R.string.nomail_alert));
            return;
        }

        if(TextUtils.isValidEmail(EMail)) {
            AlertUtils.showAlert(getActivity(), getString(R.string.mail_alert));
        }

        if(TextUtils.isNullOrEmpty(Message)) {
            AlertUtils.showAlert(getActivity(), getString(R.string.message_alert));
        }


        Intent i = new Intent(Intent.ACTION_SEND);
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{"freetamilebooksteam@gmail.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, "Review from User[Android-App]");
        i.putExtra(Intent.EXTRA_TEXT, "\nName : " + FName + " " + LName + "\nEmail : " + EMail + "\nMessage : " + Message);
        i.setType("message/rfc822");
        getActivity().startActivityForResult(Intent.createChooser(i, "Choose an Email Client..."), FROM_EMAIL_CLIENT);

    }
}
