package com.houz.chef.activity;

import android.app.AlertDialog;
import android.content.ContentProvider;
import android.content.DialogInterface;
import android.content.UriMatcher;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.houz.chef.R;
import com.houz.chef.apiBase.FetchServiceBase;
import com.houz.chef.databinding.ActivityEditProfileBinding;
import com.houz.chef.databinding.ActivityNotificationBinding;
import com.houz.chef.modelBean.AboutMe;
import com.houz.chef.modelBean.OrderData;
import com.houz.chef.modelBean.ProfileBean;
import com.houz.chef.modelBean.ProfileResponse;
import com.houz.chef.utils.CommonUtils;
import com.houz.chef.utils.Const;
import com.houz.chef.utils.Preferences;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.file.spi.FileSystemProvider;
import java.util.HashMap;
import java.util.Map;

import br.com.packapps.retropicker.callback.CallbackPicker;
import br.com.packapps.retropicker.config.Retropicker;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by m.j on 7/22/2018.
 */

public class ActivityEditProfile extends BaseActivity implements View.OnClickListener {

    ActivityEditProfileBinding binding;
    private ImageView ivBack;
    private TextView tvTitle, tvTitleLeft;
    private Retropicker retropicker = null;
    private Bitmap bitmap = null;
    private String newImagePath = "";
    private File file;
    @Override
    public int getLayoutResId() {
        return R.layout.activity_edit_profile;
    }

    @Override
    public void init() {
        binding = (ActivityEditProfileBinding) getBindingObj();
        tvTitle = findViewById(R.id.tv_title);
        tvTitleLeft = findViewById(R.id.tv_left_title);
        ivBack = findViewById(R.id.iv_back);
        tvTitle.setText(R.string.edit_profile);
        setListener();
        setData();
    }

    private void setListener() {
        ivBack.setOnClickListener(this);
        binding.ivChangeProfile.setOnClickListener(this);
    }

    public void onSaveProfile(final View view) {
        CommonUtils.hideKeyboard(view);
        final String fname = binding.edtFname.getText().toString().trim();
        final String lname = binding.edtLname.getText().toString().trim();
        final String email = binding.edtEmail.getText().toString().trim();
        final String phone = binding.edtPhone.getText().toString().trim();
        if (TextUtils.isEmpty(fname)) {
            CommonUtils.toast(view.getContext(), getString(R.string.pls_enter_fname));
            return;
        } else if (TextUtils.isEmpty(lname)) {
            CommonUtils.toast(view.getContext(), getString(R.string.pls_enter_last_name));
            return;
        } else if (TextUtils.isEmpty(phone)) {
            CommonUtils.toast(view.getContext(), getString(R.string.pls_enter_phone_number));
            return;
        } else if (TextUtils.isEmpty(email)) {
            CommonUtils.toast(view.getContext(), getString(R.string.pls_enter_email));
            return;
        }
        if (!CommonUtils.isInternetOn(view.getContext())) {
            CommonUtils.toast(view.getContext(), getString(R.string.snack_bar_no_internet));
        }
        if (!CommonUtils.isInternetOn(view.getContext())) {
            CommonUtils.toast(view.getContext(), R.string.snack_bar_no_internet);
            return;
        }
        binding.progress.setVisibility(View.VISIBLE);
        if (file != null && file.exists()) {
            binding.progress.setVisibility(View.INVISIBLE);
            Map param = new HashMap();
            Preferences preferences = new Preferences(view.getContext());
            AboutMe aboutMe = preferences.getUserDataPref();
            //param.put("profile", s);
            param.put("first_name", fname);
            param.put("last_name", lname);
            param.put("email", email);
            param.put("phone", phone);
            param.put("is_chef", aboutMe.getIs_chef());
            param.put("login_by", aboutMe.getLogin_by());
            param.put("is_available", "1");
            param.put("device_type", Const.DEVICE_TYPE_ANDROID);
            Log.e("filePath",file.getPath()+" is File Exist :" +file.exists());
            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("profile", file.getName(), reqFile);
            Map<String, RequestBody> bodyMap = CommonUtils.converRequestBodyFromMap(param);
            FetchServiceBase.getFetcherServiceWithToken(view.getContext())
                    .callUpdateProfile(aboutMe.getId(), bodyMap, body)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Subscriber<ProfileResponse>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            binding.progress.setVisibility(View.INVISIBLE);
                            e.printStackTrace();
                        }

                        @Override
                        public void onNext(ProfileResponse profileResponse) {
                            file.delete();
                            CommonUtils.toast(view.getContext(), "Profile Updated");
                        }
                    });
        } else {
            Map param = new HashMap();
            Preferences preferences = new Preferences(view.getContext());
            AboutMe aboutMe = preferences.getUserDataPref();
            param.put("first_name", fname);
            param.put("last_name", lname);
            param.put("email", email);
            param.put("phone", phone);
            param.put("is_chef", aboutMe.getIs_chef());
            param.put("login_by", aboutMe.getLogin_by());
            param.put("is_available", "1");
            param.put("profile", "");
            param.put("device_type", Const.DEVICE_TYPE_ANDROID);
            Map<String, RequestBody> bodyMap = CommonUtils.converRequestBodyFromMap(param);
            FetchServiceBase.getFetcherServiceWithToken(view.getContext())
                    .callUpdateProfile(aboutMe.getId(), bodyMap, null)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Subscriber<ProfileResponse>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            binding.progress.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onNext(ProfileResponse profileResponse) {
                            binding.progress.setVisibility(View.INVISIBLE);
                            CommonUtils.toast(view.getContext(), "Profile Updated");
                        }
                    });
        }
    }

    public void onProfileImageCapture(final View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setCancelable(false);
        builder.setTitle(R.string.add_profile_image);
        builder.setMessage(R.string.capture_options_lbl);
        builder.setPositiveButton(R.string.camera, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                Retropicker.Builder builder = new Retropicker.Builder(ActivityEditProfile.this)
                        .setTypeAction(Retropicker.CAMERA_PICKER) //Para abrir a galeria passe Retropicker.GALLERY_PICKER
                        .setImageName("first_image.jpg") //Opicional
                        .checkPermission(true);

                builder.enquee(new CallbackPicker() {
                    @Override
                    public void onSuccess(Bitmap bitmap, String imagePath) {
                        ActivityEditProfile.this.bitmap = bitmap;
                        newImagePath = imagePath;
                        //binding.ivUser.setImageBitmap(bitmap); //ImageView do seu aplicativo onde quer exibir a imagem final

                        file = new File(context.getCacheDir(), "profile");
                        try {
                            file.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

//Convert bitmap to byte array
                        Bitmap bmp = bitmap;
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        bmp.compress(Bitmap.CompressFormat.JPEG, 0 /*ignored for PNG*/, bos);
                        byte[] bitmapdata = bos.toByteArray();

//write the bytes in file
                        FileOutputStream fos = null;
                        try {
                            fos = new FileOutputStream(file);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        try {
                            fos.write(bitmapdata);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            fos.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        binding.ivUser.setImageURI(Uri.parse(newImagePath));
                    }

                    @Override
                    public void onFailure(Throwable error) {
                        Log.e("TAG", "error: " + error.getMessage());
                        Log.e("TAG", "error toString: " + error.toString());
                    }
                });

                retropicker = builder.create();
                retropicker.open();
            }
        });
        builder.setNegativeButton(R.string.gallery, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                Retropicker.Builder builder = new Retropicker.Builder(ActivityEditProfile.this)
                        .setTypeAction(Retropicker.GALLERY_PICKER) //Para abrir a galeria passe Retropicker.GALLERY_PICKER
                        .setImageName("first_image.jpg") //Opicional
                        .checkPermission(true);

                builder.enquee(new CallbackPicker() {
                    @Override
                    public void onSuccess(Bitmap bitmap, String imagePath) {
                        ActivityEditProfile.this.bitmap = bitmap;
                        newImagePath = imagePath;
                        //binding.ivUser.setImageBitmap(bitmap); //ImageView do seu aplicativo onde quer exibir a imagem final
                        file = new File(context.getCacheDir(), "profile");
                        try {
                            file.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

//Convert bitmap to byte array
                        Bitmap bmp = bitmap;
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        bmp.compress(Bitmap.CompressFormat.JPEG, 0 /*ignored for PNG*/, bos);
                        byte[] bitmapdata = bos.toByteArray();

//write the bytes in file
                        FileOutputStream fos = null;
                        try {
                            fos = new FileOutputStream(file);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        try {
                            fos.write(bitmapdata);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            fos.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        binding.ivUser.setImageURI(Uri.parse(newImagePath));
                    }

                    @Override
                    public void onFailure(Throwable error) {
                        Log.e("TAG", "error: " + error.getMessage());
                        Log.e("TAG", "error toString: " + error.toString());
                    }
                });

                retropicker = builder.create();
                retropicker.open();
            }
        });
        builder.setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //Call this line fir manager RetroPicker Library
        if (retropicker != null)
            retropicker.onRequesPermissionResult(requestCode, permissions, grantResults);

    }

    private void setData() {
        AboutMe aboutMe = preferences.getUserDataPref();
        binding.edtFname.setText(aboutMe.getFirst_name());
        binding.edtLname.setText(aboutMe.getLast_name());
        binding.edtEmail.setText(aboutMe.getEmail());
        binding.edtPhone.setText(aboutMe.getPhone());

        if (aboutMe.getFcm_token() != null && !aboutMe.getProfile().isEmpty()) {
            Picasso.with(this)
                    .load(aboutMe.getProfile())
                    .placeholder(R.drawable.user_placeholder)
                    .into(binding.ivUser);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;

            case R.id.iv_change_profile:
                break;
        }
    }
}
