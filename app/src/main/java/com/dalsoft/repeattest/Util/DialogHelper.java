package com.dalsoft.repeattest.Util;

import android.content.Context;
import android.content.DialogInterface;

import com.dalsoft.repeattest.R;

import androidx.appcompat.app.AlertDialog;

public class DialogHelper {
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;
    private DialogInterface.OnClickListener mNegativeClickListener;
    private DialogInterface.OnClickListener mPositiveClickListener;

    public void dialogShow(Context context, String title, String message, DialogInterface.OnClickListener mPositiveClickListener, DialogInterface.OnClickListener mNegativeClickListener) {
        alertDialogBuilder = new AlertDialog.Builder(context);

        alertDialogBuilder.setCancelable(false); // Dialog 배경 클릭 시 종료 여부
        alertDialogBuilder.setTitle(title);      // 제목
        alertDialogBuilder.setMessage(message);  // 내용
        alertDialogBuilder.setPositiveButton(context.getResources().getString(R.string.ok), mPositiveClickListener); // 확인 버튼
        alertDialogBuilder.setNegativeButton(context.getResources().getString(R.string.cancel), mNegativeClickListener); // 취소 버튼

        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void dialogShow(Context context, String title, String message, DialogInterface.OnClickListener mPositiveClickListener) {
        alertDialogBuilder = new AlertDialog.Builder(context);

        alertDialogBuilder.setCancelable(false); // Dialog 배경 클릭 시 종료 여부
        alertDialogBuilder.setTitle(title);      // 제목
        alertDialogBuilder.setMessage(message);  // 내용
        alertDialogBuilder.setPositiveButton(context.getResources().getString(R.string.ok), mPositiveClickListener); // 확인 버튼
        alertDialogBuilder.setNegativeButton(context.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogDismiss();
            }
        });

        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void dialogDismiss() {
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }
}
