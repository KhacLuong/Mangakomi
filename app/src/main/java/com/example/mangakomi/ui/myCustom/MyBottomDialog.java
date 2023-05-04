package com.example.mangakomi.ui.myCustom;

import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.google.android.material.R;
import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialog;

public class MyBottomDialog extends BottomSheetDialog {


        public MyBottomDialog(@NonNull Context context) {
            super(context);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Thiết lập margin cho BottomSheetDialog
            int screenHeight = getContext().getResources().getDisplayMetrics().heightPixels;
            int peekHeight = (int) (screenHeight * 0.8); // Thiết lập chiều cao khi hiển thị
            int margin = screenHeight - peekHeight;
            FrameLayout bottomSheet = findViewById(R.id.design_bottom_sheet);
            ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();
//            layoutParams.height = peekHeight;
            bottomSheet.setLayoutParams(layoutParams);
            bottomSheet.setPadding(0, 0, 0, 180);
            bottomSheet.setBackgroundColor(0);
        }

}
