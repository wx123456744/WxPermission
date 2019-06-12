package com.zfkj.gamecenter.permission;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class RationaleDialog extends Dialog {

    public RationaleDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    private static RationaleDialog mDialog;

    private final int CLOSE_ALERTDIALOG = 0;  //定义关闭对话框的动作信号标志
    private final int CLOSE_SAMPLE_VIEW = 1;  //定义关闭SampleView的动作信号标志


    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CLOSE_SAMPLE_VIEW: // 接受关闭对话框弹窗

                    break;
                case CLOSE_ALERTDIALOG:
                    if (isShowing())
                        mDialog.dismiss();
                    break;
                default:
                    break;
            }
        }
    };

    public static class Builder {
        private String message, title;
        private String confirmText;
        private String cancelText;
        private OnClickListener confirmListener;
        private OnClickListener cancelListener;
        private int cancelBgStyle, confirmBgStyle;

        private View layout;
        private boolean cancelable = true, canceledOnTouchOutside = false;

        public Builder(Context context, int layoutId) {
            // 这里传入自定义的style，直接影响此Dialog的显示效果。style具体实现见style.xml
            mDialog = new RationaleDialog(context, R.style.fillet_dialog_style);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = inflater.inflate(layoutId, null);
        }

        public Builder setTitleText(String title) {
            this.title = title;
            return this;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setDuration(int duration) {
            mDialog.mHandler.sendEmptyMessageDelayed(mDialog.CLOSE_ALERTDIALOG, duration);
            return this;
        }


        public Builder setConfirmButton(String positiveButtonText,int themeId, OnClickListener listener) {
            this.confirmText = positiveButtonText;
            this.confirmBgStyle = themeId;
            this.confirmListener = listener;
            return this;
        }

        public Builder setCancelButton(String negativeButtonText,int themeId, OnClickListener listener) {
            this.cancelText = negativeButtonText;
            this.cancelBgStyle = themeId;
            this.cancelListener = listener;
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            this.cancelable = cancelable;
            return this;
        }

        public Builder setCanceledOnTouchOutside(boolean canceledOnTouchOutside) {
            this.canceledOnTouchOutside = canceledOnTouchOutside;
            return this;
        }


        /**
         * 创建双按钮对话框
         *
         * @return
         */
        private void _create() {
            layout.findViewById(R.id.positiveButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    confirmListener.onClick(mDialog, 0);
                }
            });
            layout.findViewById(R.id.negativeButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cancelListener.onClick(mDialog, 0);
                }
            });

            if (!ConversionUtils.isEmpty(message)) {
                ((TextView) layout.findViewById(R.id.message)).setText(message);
            }

            Button positiveButtom = ((Button) layout.findViewById(R.id.positiveButton));
            // 如果传入的按钮文字为空，则使用默认的“是”和“否”
            if (confirmText != null) {
                positiveButtom.setText(confirmText);
            } else {
                positiveButtom.setText("是");
            }
            if (confirmBgStyle != 0)
                positiveButtom.setBackgroundResource(confirmBgStyle);
            Button negativeButton = ((Button) layout.findViewById(R.id.negativeButton));
            if (cancelText != null) {
                negativeButton.setText(cancelText);
            } else {
                negativeButton.setText("否");
            }
            if (cancelBgStyle != 0)
                negativeButton.setBackgroundResource(cancelBgStyle);
            if (!ConversionUtils.isEmpty(title))
                this.layout.findViewById(R.id.top_tv).setVisibility(View.GONE);
            else
                ((TextView) this.layout.findViewById(R.id.top_tv)).setText(title);
        }

        public RationaleDialog build() {
            this._create();
            mDialog.addContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            mDialog.setContentView(this.layout);
            mDialog.setCancelable(cancelable);
            mDialog.setCanceledOnTouchOutside(canceledOnTouchOutside);
            return mDialog;
        }
    }

}
