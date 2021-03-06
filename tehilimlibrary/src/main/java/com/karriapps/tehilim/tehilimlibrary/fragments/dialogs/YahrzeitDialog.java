package com.karriapps.tehilim.tehilimlibrary.fragments.dialogs;

import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.karriapps.tehilim.tehilimlibrary.R;
import com.karriapps.tehilim.tehilimlibrary.fragments.NavigationDrawerFragment.NavigationDrawerCallbacks;
import com.karriapps.tehilim.tehilimlibrary.generators.YahrzeitGenerator;
import com.karriapps.tehilim.tehilimlibrary.utils.Tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class YahrzeitDialog extends DialogFragment {

    private TextView mTextView;
    private EditText mEditText;
    private Button mButton;

    private NavigationDrawerCallbacks mCallback;

    private List<Integer> mChapters;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.yahrzeit_dialog, container, false);

        mTextView = view.findViewById(R.id.yahrzeit_dialog_text);
        mEditText = view.findViewById(R.id.yahrzeit_dialog_edit_text);
        mButton = view.findViewById(R.id.yahrzeit_dialog_button);
        mEditText.addTextChangedListener(mTextWatcher);

        getDialog().setTitle(R.string.yahrzeitPopupTitle);

        mButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mCallback != null) {
                    YahrzeitGenerator generator = new YahrzeitGenerator(mChapters);
                    mCallback.onNavigationDrawerItemSelected(generator, getString(R.string.yahrzeitTitle));
                }
                dismiss();
            }
        });

        return view;
    }

    public void setCallback(NavigationDrawerCallbacks callback) {
        mCallback = callback;
    }

    TextWatcher mTextWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            try {
                String str = s.toString().trim();
                List<String> items = Arrays.asList(str.split("\\s*,\\s*"));

                mChapters = new ArrayList<>();
                for (int i = 0; i < items.size(); i++) {
                    if (TextUtils.isDigitsOnly(items.get(i))) {
                        mChapters.add(Integer.parseInt(items.get(i)));
                    } else {
                        mChapters.add(Tools.getCharValOrdinal(items.get(i).charAt(0)));
                    }
                }
                String name = "";
                for (int i = 0; i < mChapters.size(); i++) {
                    char letter = Tools.getGimatriya(mChapters.get(i), false);
                    mChapters.set(i, Tools.getCharValOrdinal(letter));
                    name += letter;
                }

                mTextView.setText(name);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
}
