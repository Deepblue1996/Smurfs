package com.prohua.smurfs.ui.other;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.prohua.smurfs.R;
import com.prohua.smurfs.listener.CircleEditActionListener;
import com.prohua.smurfs.listener.CircleTextOnClickListener;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Deep on 2017/4/28 0028.
 */

public class CircleEditDialogFragment extends DialogFragment {

    @BindView(R.id.t_edit)
    EditText editText;
    @BindView(R.id.t_send)
    TextView textView;

    public static String hint;

    CircleEditActionListener onEditorActionListener;

    CircleTextOnClickListener onClickListener;

    public void setCircleEditActionListener(CircleEditActionListener onEditorActionListener) { //接口对象构造(接口没有实例化,因为没有具体方法)
        this.onEditorActionListener = onEditorActionListener;
    }

    public void setCircleTextOnClickListener(CircleTextOnClickListener onClickListener) { //接口对象构造(接口没有实例化,因为没有具体方法)
        this.onClickListener = onClickListener;
    }

    @Override
    public void onStart() {
        super.onStart();
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        WindowManager.LayoutParams layoutParams = getDialog().getWindow().getAttributes();
        layoutParams.gravity = Gravity.BOTTOM;
        getDialog().getWindow().setAttributes(layoutParams);
        getDialog().getWindow().setLayout(dm.widthPixels, getDialog().getWindow().getAttributes().height);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0xff000000));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.circle_edit_layout, container);

        ButterKnife.bind(this, view);

        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        editText.setHint(hint);

        // 200后执行
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) editText.getContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
            }

        }, 200);

        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                switch (i) {
                    case KeyEvent.KEYCODE_BACK:
                        dismiss();
                        return true;
                    default:
                        break;
                }
                return false;
            }
        });

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                return onEditorActionListener.onEditorAction(textView, i, keyEvent);
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListener.onClick(view, editText);
            }
        });

        return view;
    }
}