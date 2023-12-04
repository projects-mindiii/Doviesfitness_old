package com.doviesfitness.utils;

import android.app.Dialog;
import android.content.Context;
import androidx.annotation.NonNull;
import android.view.*;
import android.widget.TextView;
import com.doviesfitness.R;

/**
 * Created by mindiii on 21/2/18.
 */

public abstract class Pop_Up_Option extends Dialog {

    private Object object;

    public Pop_Up_Option(@NonNull Context context) {
        super(context, R.style.CustomBottomSheetDialogTheme);
        View pop_up_view = LayoutInflater.from(context).inflate(R.layout.custom_view_for_picture, null);  //popup_custom_menu layout

        TextView tv_cancel = pop_up_view.findViewById(R.id.tv_cancel);
        TextView tv_gallery = pop_up_view.findViewById(R.id.tv_gallery);
        TextView tv_camera = pop_up_view.findViewById(R.id.tv_camera);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(pop_up_view);
        this.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationBottTop;
        this.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        this.setCanceledOnTouchOutside(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(this.getWindow().getAttributes());
        lp.width    = (int) context.getResources().getDimension(R.dimen._280sdp);
        lp.height   = (int) context.getResources().getDimension(R.dimen._150sdp);
        lp.gravity  = Gravity.BOTTOM;

        this.getWindow().setAttributes(lp);

        tv_gallery.setOnClickListener(v -> onGalleryClick(this));
        tv_camera.setOnClickListener(v -> onCameraClick(this));
        tv_cancel.setOnClickListener(v -> dismiss());
    }

    public abstract void onGalleryClick(Pop_Up_Option dialog);
    public abstract void onCameraClick(Pop_Up_Option dialog);

  /*  public void setObject(Object object){
        this.object = object;
    }*/

}
