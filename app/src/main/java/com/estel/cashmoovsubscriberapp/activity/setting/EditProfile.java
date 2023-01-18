package com.estel.cashmoovsubscriberapp.activity.setting;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.estel.cashmoovsubscriberapp.MainActivity;
import com.estel.cashmoovsubscriberapp.MyApplication;
import com.estel.cashmoovsubscriberapp.R;
import com.estel.cashmoovsubscriberapp.apiCalls.API;
import com.estel.cashmoovsubscriberapp.apiCalls.Api_Responce_Handler;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.security.SecureRandom;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfile extends AppCompatActivity implements View.OnClickListener, IPickResult {
    public static EditProfile editprofileC;
    Button btnCancel,btnConfirm;
    ImageButton btnChoose;
    ImageView imgBack,imgHome;
    CircleImageView profile_img;
    TextView profilname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        editprofileC=this;
        setBackMenu();
        getIds();
        isSelect=false;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setBackMenu() {
        imgBack = findViewById(R.id.imgBack);
        imgHome = findViewById(R.id.imgHome);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.hideKeyboard(editprofileC);
                onSupportNavigateUp();
            }
        });

        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.isFirstTime=false;
                MyApplication.hideKeyboard(editprofileC);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }


    private void getIds() {
        btnChoose = findViewById(R.id.btnChoose);
        btnCancel = findViewById(R.id.btnCancel);
        btnConfirm = findViewById(R.id.btnConfirm);
        profile_img=findViewById(R.id.profile_img);
        profilname=findViewById(R.id.profilname);

        String name=MyApplication.getSaveString("firstName",editprofileC)+" "+
        MyApplication.getSaveString("lastName",editprofileC);
        profilname.setText(name);

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.profil)
                .error(R.drawable.profil);

        String ImageName=MyApplication.getSaveString("ImageName", editprofileC);
        if(ImageName!=null&&ImageName.length()>1) {
            //String image_url = MyApplication.ImageURL + ImageName;
            Glide.with(this).load(ImageName).apply(options).into(profile_img);
        }

        setOnCLickListener();

    }

    private void setOnCLickListener() {
        btnChoose.setOnClickListener(editprofileC);
        btnCancel.setOnClickListener(editprofileC);
        btnConfirm.setOnClickListener(editprofileC);
    }
    static final int REQUEST_IMAGE_CAPTURE_ONE = 1;
    static final int REQUEST_IMAGE_CAPTURE_GALARY = 3;
    static final int REQUEST_IMAGE_CAPTURE_TWO = 2;
    public static final int RESULT_CODE_FAILURE = 10;
    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnChoose:
                PickImageDialog.build(new PickSetup()
                        .setTitle(getString(R.string.choose_operation))
                        .setCancelText(getString(R.string.cancel))
                        .setCameraButtonText(getString(R.string.take_photo))
                        .setGalleryButtonText(getString(R.string.choose_image))
                        .setSystemDialog(false)
                        .setGalleryIcon(R.drawable.ic_baseline_gallery_24)
                        .setCameraIcon(R.drawable.ic_baseline_camera_alt_24)
                ).show(this);

//                PickSetup setup=new PickSetup();
//                PickImageDialog dialog = PickImageDialog.build(setup);
//                dialog.setOnClick(new IPickClick() {
//                            @Override
//                            public void onGalleryClick() {
//                                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
//                                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                                try {
//                                    startActivityForResult(pickPhoto , REQUEST_IMAGE_CAPTURE_GALARY);//one can be replaced with any action code
//                                } catch (ActivityNotFoundException e) {
//                                    // display error state to the user
//                                }
//                                Toast.makeText(editprofileC, "Gallery Click!", Toast.LENGTH_LONG).show();
//                                dialog.dismiss();
//                            }
//
//                            @Override
//                            public void onCameraClick() {
//                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                                try {
//                                    startActivityForResult(intent, REQUEST_IMAGE_CAPTURE_ONE);
//                                } catch (ActivityNotFoundException e) {
//                                    // display error state to the user
//                                }
//                                Toast.makeText(editprofileC, "Camera Click!", Toast.LENGTH_LONG).show();
//                                dialog.dismiss();
//                            }
//                        }).show(this);
                break;
            case R.id.btnCancel:
                Intent intent = new Intent(getApplicationContext(), Profile.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.btnConfirm:
                if(isSelect){
                    callupload(file);
                }else{
                    Toast.makeText(editprofileC,getString(R.string.selectimage), Toast.LENGTH_SHORT).show();
                }

                break;

        }
    }

    Boolean isSelect=false;
    File file;
    private Intent Data;

    @Override
    public void onPickResult(PickResult r) {
        if (r.getError() == null) {
            //If you want the Uri.
            //Mandatory to refresh image from Uri.
            //getImageView().setImageURI(null);

            //Setting the real returned image.
            //getImageView().setImageURI(r.getUri());

            //If you want the Bitmap.
           // getImageView().setImageBitmap(r.getBitmap());
            Uri selectedImage = getImageUri(getApplicationContext(),r.getBitmap());
            Glide.with(this).load(selectedImage).into(profile_img);
            //file = new File(getPathFromURI(selectedImage).toString());
            file = new File(getRealPathFromURI(selectedImage).toString());
            isSelect=true;
            int file_size = Integer.parseInt(String.valueOf(file.length() / 1024));
            //Image path
            //r.getPath();
        } else {
            //Handle possible errors
            //TODO: do what you have to do with r.getError();
            Toast.makeText(this, r.getError().getMessage(), Toast.LENGTH_LONG).show();
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == REQUEST_IMAGE_CAPTURE_ONE) {
//            if (resultCode == RESULT_OK) {
//                Bundle extras = data.getExtras();
//                Bitmap imageBitmap = (Bitmap) extras.get("data");
//                Data = data;
//
//
//                Uri cameraImage = getImageUri(getApplicationContext(), imageBitmap);
//                //profile_img.setImageURI(cameraImage);
//                Glide.with(this).load(cameraImage).into(profile_img);
//
//                file = new File(getRealPathFromURI(cameraImage).toString());
//                isSelect=true;
//                int file_size = Integer.parseInt(String.valueOf(file.length() / 1024));     //calculate size of image in KB
//
//                //  btnFrontUpload.setVisibility(View.VISIBLE);
//                // CALL THIS METHOD TO GET THE ACTUAL PATH
////                File file = new File(getRealPathFromURI(tempUriFront));
////                System.out.println(file);
//
//            } else if (resultCode == RESULT_CANCELED) {
//                MyApplication.showToast(editprofileC,"User Canceled");
//            } else if (resultCode == RESULT_CODE_FAILURE) {
//                MyApplication.showToast(editprofileC,"Failed");
//            }
//
//        }
//        if (requestCode == REQUEST_IMAGE_CAPTURE_GALARY) {
//            if (resultCode == RESULT_OK) {
//
//
//                Uri selectedImage = data.getData();
//                Glide.with(this).load(selectedImage).into(profile_img);
//                file = new File(getPathFromURI(selectedImage));
//                isSelect=true;
//
//               /* Bundle extras = data.getExtras();
//                Bitmap imageBitmap = (Bitmap) extras.get("data");
//                Data = data;
//                profile_img.setImageBitmap(imageBitmap);
//                Uri cameraImage = getImageUri(getApplicationContext(), imageBitmap);
//
//
//
//                File CImage = new File(getRealPathFromURI(cameraImage).toString());
//                int file_size = Integer.parseInt(String.valueOf(CImage.length() / 1024));     //calculate size of image in KB
//*/
//                //btnBackUpload.setVisibility(View.VISIBLE);
//                // CALL THIS METHOD TO GET THE ACTUAL PATH
//                // File file = new File(getRealPathFromURI(tempUri));
//
//            } else if (resultCode == RESULT_CANCELED) {
//                MyApplication.showToast(editprofileC,"User Canceled");
//            } else if (resultCode == RESULT_CODE_FAILURE) {
//                MyApplication.showToast(editprofileC,"Failed");
//            }
//
//        }
//    }

    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        SecureRandom random = new SecureRandom();
        int num = random.nextInt(100000);
        String formatted = String.format("%05d", num);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "profile"+formatted, null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        String path = "";
        if (getContentResolver() != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }

    private void callupload(File file) {

        MyApplication.showloader(editprofileC, "uploading file...");
        //idProofTypeModelList.get((Integer) spIdProof.getTag()).getCode()
        API.Upload_REQEST("ewallet/api/v1/fileUpload",file,"100040",
                 new Api_Responce_Handler() {

                    @Override
                    public void success(JSONObject jsonObject) {
                        MyApplication.hideLoader();
                        System.out.println("Json Object Req"+file);
                        System.out.println("Json Object"+jsonObject);
                        if (jsonObject != null) {
                            if (jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")) {
                                //MyApplication.showToast(getString(R.string.document_upload_msg));

                                if(jsonObject.has("documentUpload")) {
                                    JSONObject jsonObject1 = jsonObject.optJSONObject("documentUpload");

                                    MyApplication.saveString("ImageCode", jsonObject1.optString("code"), editprofileC);


                                    MyApplication.saveString("ImageName", API.BASEURL+"ewallet/api/v1/fileUpload/download/" +
                                            MyApplication.getSaveString("walletOwnerCode", editprofileC)+"/"+
                                            jsonObject1.optString("fileName"),editprofileC);
                                    String ImageName = MyApplication.getSaveString("ImageName", editprofileC);
                                    RequestOptions options = new RequestOptions()
                                            .centerCrop()
                                            .placeholder(R.drawable.profil)
                                            .error(R.drawable.profil);


                                    if (ImageName != null && ImageName.length() > 1) {
                                        //String image_url = MyApplication.ImageURL + ImageName;
                                        Glide.with(editprofileC).load(ImageName).apply(options).into(profile_img);
                                    }

                                    MyApplication.showToast(editprofileC,getString(R.string.upload_success));
                                    isSelect=false;
                                }else {
                                    MyApplication.showToast(editprofileC,getString(R.string.technical_failure));
                                }
                                // callApiUpdateDataApproval();


                            } else if (jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("2001")) {
                                MyApplication.showToast(editprofileC,getString(R.string.technical_failure));
                            } else {
                                MyApplication.showToast(editprofileC,jsonObject.optString("resultDescription", "N/A"));
                            }
                        }


                    }

                    @Override
                    public void failure(String aFalse) {

                    }
                });
    }


}