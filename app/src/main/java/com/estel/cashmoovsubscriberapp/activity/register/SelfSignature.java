package com.estel.cashmoovsubscriberapp.activity.register;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.estel.cashmoovsubscriberapp.MyApplication;
import com.estel.cashmoovsubscriberapp.R;
import com.estel.cashmoovsubscriberapp.apiCalls.API;
import com.estel.cashmoovsubscriberapp.apiCalls.Api_Responce_Handler;
import com.github.gcacace.signaturepad.views.SignaturePad;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;

public class SelfSignature extends AppCompatActivity implements View.OnClickListener{
    public static SelfSignature selfsignatureC;
    private SignaturePad signaturePad_electricityBill;
    InputStream imageStream, imageStreamCamera;
    Bitmap signatureBitmapBillElectricity;

    Button btnClear,btnSave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acticity_self_signature);
        selfsignatureC=this;
        getIds();

    }

    private void getIds() {
        btnClear = findViewById(R.id.btnClear);
        btnSave = findViewById(R.id.btnSave);

        signaturePad_electricityBill = findViewById(R.id.signaturePad_electricityBill);
        signaturePad_electricityBill.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {

                signatureBitmapBillElectricity = signaturePad_electricityBill.getSignatureBitmap();
                System.out.println(signatureBitmapBillElectricity);

            }

            @Override
            public void onSigned() {
                btnClear.setEnabled(true);
                btnSave.setEnabled(true);
            }

            @Override
            public void onClear() {
                btnClear.setEnabled(true);  // change all page
                btnClear.setEnabled(false);
            }
        });

        setOnCLickListener();


    }

    private void setOnCLickListener() {
        btnClear.setOnClickListener(selfsignatureC);
        btnSave.setOnClickListener(selfsignatureC);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.signaturePad_electricityBill:
                System.out.println();
                break;

            case R.id.btnClear: {
                signaturePad_electricityBill.clear();

            }
            break;

            case R.id.btnSave: {
                signatureBitmapBillElectricity = signaturePad_electricityBill.getSignatureBitmap();
                addJpgSignatureToGallery(signatureBitmapBillElectricity, "BillElectricity");

                if(photoSend!=null){
                    callupload(photoSend,"100039");
                }

            }
            break;
        }

    }

    File photoSend;
    public boolean addJpgSignatureToGallery(Bitmap signature, String selectionType) {
        boolean result = false;
        try {
            File photo = new File(getAlbumStorageDir("SignaturePad"), String.format("Signature_%d.jpg", System.currentTimeMillis()));
            saveBitmapToJPG(signature, photo);
            signSignatureHuman(photo, selectionType);
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    public File getAlbumStorageDir(String albumName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            Log.e("SignaturePad", "Directory not created");
        }
        return file;
    }

    public void saveBitmapToJPG(Bitmap bitmap, File photo) throws IOException {
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        OutputStream stream = new FileOutputStream(photo);
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        stream.close();
    }
    void signSignatureHuman(File photo, String selectionType) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(photo);


        try {
            imageStream = getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);


        ColorMatrix matrix = new ColorMatrix();   // color to black in white
        matrix.setSaturation(0);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
        //  imageview_preview_mpinPage.setColorFilter(filter);

        Bitmap resizeBmp = Bitmap.createScaledBitmap(selectedImage, 200, 200, true);
        selectedImage = resizeBmp;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
        byte[] imageBytes = baos.toByteArray();


        Uri photoSend1 = getImageUri(getApplicationContext(), selectedImage);


        photoSend = new File(getRealPathFromURI(photoSend1).toString());
        String  uploadGalleryStringElectricity = Base64.encodeToString(imageBytes, Base64.DEFAULT);

       // Toast.makeText(SelfSignature.this,uploadGalleryStringElectricity,Toast.LENGTH_LONG).show();
        //   imageview_bill_electricityBill.setImageBitmap(selectedImage);
        //  imageview_preview_drawHome.setImageDrawable(null);

        System.out.println();

        mediaScanIntent.setData(uri);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "sele"+ Calendar.getInstance().getTime(), null);
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

    private void callupload(File file,String Code) {

        MyApplication.showloader(selfsignatureC, "uploading file...");
        //idProofTypeModelList.get((Integer) spIdProof.getTag()).getCode()
        API.Upload_REQEST("ewallet/api/v1/fileUpload",file,Code,
                 new Api_Responce_Handler() {

                    @Override
                    public void success(JSONObject jsonObject) {
                        MyApplication.hideLoader();
                        if (jsonObject != null) {
                            if (jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")) {
                                //MyApplication.showToast(getString(R.string.document_upload_msg));

                                MyApplication.showToast(selfsignatureC,"upload success");
                                // callApiUpdateDataApproval();
                                Intent intent = new Intent(selfsignatureC, AccountCreatedScreen.class);
                                startActivity(intent);
                                finish();

                            } else if (jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("2001")) {
                                MyApplication.showToast(selfsignatureC,getString(R.string.technical_failure));
                            } else {
                                MyApplication.showToast(selfsignatureC,jsonObject.optString("resultDescription", "N/A"));
                            }
                        }


                    }

                    @Override
                    public void failure(String aFalse) {

                    }
                });
    }


}