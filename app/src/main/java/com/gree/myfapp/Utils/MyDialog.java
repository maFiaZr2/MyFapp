package com.gree.myfapp.Utils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.Toast;


/**
 * Created by asus on 2016/11/1.
 */

public class MyDialog {
    private static MyDialog myDialog;
    private static Context mContext;
    private static ProgressDialog dialog;
    private String info;

    private static MyDialog getInstance() {
        if (myDialog == null) {
            myDialog = new MyDialog();
        }
        return myDialog;
    }

    public static MyDialog with(Context context) {
        mContext = context;
        return getInstance();
    }

    public void dismiss() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    public void progressDialog(String s) {
        info = s;
        dialog = new ProgressDialog(mContext);
        dialog.setMessage(info);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    public void toast(String s) {
        info = s;
        Toast toast = Toast.makeText(mContext, info, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public alertDialog alertDialog() {
        return new alertDialog();
    }

    public class alertDialog {
        AlertDialog alertDialog;

        public alertDialog() {
            alertDialog = new AlertDialog.Builder(mContext).create();

        }

        public alertDialog set(String s, boolean b) {
            if (!TextUtils.isEmpty(s)) {
                alertDialog.setMessage(s);
            }
            alertDialog.setCanceledOnTouchOutside(b);
            alertDialog.show();
            return this;
        }

        public alertDialog btn(int i, String s, DialogInterface.OnClickListener listener) {
            alertDialog.setButton(i, s, listener);
            return this;
        }
    }


}
