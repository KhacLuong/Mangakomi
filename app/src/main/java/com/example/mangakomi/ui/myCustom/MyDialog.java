package com.example.mangakomi.ui.myCustom;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Layout;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.mangakomi.R;

public class MyDialog extends Dialog{
    private Button btnNo;
    private Button btnYes;
    private TextView tvTitle;
    private TextView tvContent;

    private YesOnClickListener okButtonClickListener;
    private NoOnClickListener noButtonClickListener;


    public MyDialog(@NonNull Context context) {
        super(context);
    }

//    type ==1, yes, no
//    type ==0, OK
    public  MyDialog(Context context ,int type,String title, String content,  int gravity){
        super(context);

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert_dialog);
        dialog.setCancelable(false);
        Window window = dialog.getWindow();

        if(window ==null)
            return;
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttribute  = window.getAttributes();
        windowAttribute.gravity = gravity;

        window.setAttributes(windowAttribute);

        btnNo = dialog.findViewById(R.id.btn_dialog_no);
        btnYes = dialog.findViewById(R.id.btn_dialog_yes);
        tvTitle = dialog.findViewById(R.id.tv_title);
        tvContent = dialog.findViewById(R.id.tv_content);
        tvTitle.setText(title);
        tvContent.setText(content);
        if (type==0){
            btnNo.setVisibility(View.GONE);
            btnYes.setText("OK");

        }

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                okButtonClickListener.onClick();
                dialog.dismiss();
            }

        });
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noButtonClickListener.onClick();
                dialog.dismiss();
            }
        });


        dialog.show();


//        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
//            @Override
//            public void onShow(DialogInterface dialogInterface) {
//                // Yêu cầu focus cho Dialog khi nó được hiển thị lên
//                dialog.getWindow().getDecorView().findViewById(android.R.id.content).requestFocus();
//            }
//        });
    }


    public void setOnButtonClickListener(YesOnClickListener listener1,NoOnClickListener listener2) {
        okButtonClickListener = listener1;
        noButtonClickListener = listener2;
    }

    public interface YesOnClickListener {
        void onClick();

    }
    public interface NoOnClickListener {
        void onClick();

    }

//    CustomDialog dialog = new CustomDialog(this);
//    dialog.setOnOKButtonClickListener(new CustomDialog.OnClickListener() {
//            @Override
//            public void onClick() {
//                // Xử lý sự kiện onclick tùy chỉnh
//            }
//        });
//    dialog.show();
}
