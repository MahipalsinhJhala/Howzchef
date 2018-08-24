package com.houz.chef.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.houz.chef.R;

public class RatingDialog extends Dialog implements View.OnClickListener {
    private Button btnSend,btnCancel;
    private RatingListener ratingListener;

    public RatingDialog(@NonNull Context context, RatingListener ratingListener) {
        super(context);
        this.ratingListener = ratingListener;
    }

    public RatingDialog(@NonNull Context context, @StyleRes int themeResId, RatingListener ratingListener) {
        super(context, themeResId);
        this.ratingListener = ratingListener;
    }

    protected RatingDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener, RatingListener ratingListener) {
        super(context, cancelable, cancelListener);
        this.ratingListener = ratingListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        View contentView = View.inflate(getContext(), R.layout.dialog_rating, null);
        setContentView(contentView);

        final Window window = getWindow();
        if(window != null) {
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            //window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            //window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        btnSend = (Button) contentView.findViewById(R.id.btn_send);
        btnSend.setOnClickListener(this);
        btnCancel = (Button) contentView.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_send:
                dismiss();
                if(ratingListener != null)
                    ratingListener.onContinueClick();
                break;
            case R.id.btn_cancel:
                dismiss();
                break;
        }
    }

    public interface RatingListener {
        void onContinueClick();
    }
}
