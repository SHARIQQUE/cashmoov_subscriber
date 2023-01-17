package com.estel.cashmoovsubscriberapp.activity.register;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.estel.cashmoovsubscriberapp.MyApplication;
import com.estel.cashmoovsubscriberapp.R;
import com.estel.cashmoovsubscriberapp.apiCalls.API;
import com.estel.cashmoovsubscriberapp.apiCalls.Api_Responce_Handler;
import com.estel.cashmoovsubscriberapp.model.IDProofTypeModel;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class RegisterStepTwo extends AppCompatActivity implements View.OnClickListener {

    public static RegisterStepTwo registersteptwoC;
    TextView spIdProof,tvNext;
    Button btnFrontUpload,btnBackUpload;
    SpinnerDialog spinnerDialogIdProofType;
    ImageButton btnFront,btnBack;
    EditText etFront,etBack,etProofNo;
    private ArrayList<String> idProofTypeList = new ArrayList<>();
    private ArrayList<IDProofTypeModel.IDProofType> idProofTypeModelList=new ArrayList<>();
    static final int REQUEST_IMAGE_CAPTURE_ONE = 1;
    static final int REQUEST_IMAGE_CAPTURE_TWO = 2;
    public static final int RESULT_CODE_FAILURE = 10;
    private Intent Data;
    Uri tempUriFront,tempUriBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_steptwo);
        registersteptwoC = this;
        getIds();
    }

        private void getIds() {
            spIdProof = findViewById(R.id.spIdProof);
            tvNext = findViewById(R.id.tvNext);
            btnFront = findViewById(R.id.btnFront);
            etFront = findViewById(R.id.etFront);
            btnBack = findViewById(R.id.btnBack);
            etBack = findViewById(R.id.etBack);
            etProofNo = findViewById(R.id.etProofNo);
            btnFrontUpload = findViewById(R.id.btnFrontUpload);
            btnBackUpload = findViewById(R.id.btnBackUpload);
            btnFrontUpload.setVisibility(View.GONE);
            btnBackUpload.setVisibility(View.GONE);

            spIdProof.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (spinnerDialogIdProofType!=null)
                        spinnerDialogIdProofType.showSpinerDialog();
                }
            });

            setOnCLickListener();

            callApiIdProofType();

    }

    private void setOnCLickListener() {
        tvNext.setOnClickListener(registersteptwoC);
        btnFront.setOnClickListener(registersteptwoC);
        btnBack.setOnClickListener(registersteptwoC);
        btnFrontUpload.setOnClickListener(registersteptwoC);
        btnBackUpload.setOnClickListener(registersteptwoC);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch(view.getId()){
            case R.id.btnFront:

                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                try {
                    startActivityForResult(intent, REQUEST_IMAGE_CAPTURE_ONE);
                } catch (ActivityNotFoundException e) {
                    // display error state to the user
                }

                break;

            case R.id.btnFrontUpload:

                filesUploadFront();
                break;

            case R.id.btnBack:

                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                try {
                    startActivityForResult(intent, REQUEST_IMAGE_CAPTURE_TWO);
                } catch (ActivityNotFoundException e) {
                    // display error state to the user
                }
                break;

            case R.id.btnBackUpload:

                filesUploadBack();
                break;

            case R.id.tvNext:
                if(spIdProof.getText().toString().equals(getString(R.string.valid_select_id_proof))) {
                    MyApplication.showErrorToast(RegisterStepTwo.this,getString(R.string.val_select_id_proof));
                    return;
                }
                if(etProofNo.getText().toString().trim().isEmpty()) {
                    MyApplication.showErrorToast(registersteptwoC,getString(R.string.val_proof_no));
                    return;
                }

                if(!isFrontUpload){
                    MyApplication.showErrorToast(registersteptwoC,getString(R.string.frontimageerror));
                    return;
                }

                if(!isBackUpload){
                    MyApplication.showErrorToast(registersteptwoC,getString(R.string.backuploafimageserror));
                    return;
                }
                 callupload(fileBack,"");
                break;
        }
    }

    File fileFront,fileBack;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE_ONE) {
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                Data = data;

                tempUriFront = getImageUri(getApplicationContext(), imageBitmap);

                etFront.setText(tempUriFront.getLastPathSegment());

                fileFront = new File(getRealPathFromURI(tempUriFront).toString());
                int file_size = Integer.parseInt(String.valueOf(fileFront.length() / 1024));     //calculate size of image in KB
                if (file_size <= 100){
                    isFrontUpload=true;
                }else {
                    MyApplication.showErrorToast(registersteptwoC,getString(R.string.fileexceed));
                }
              //  btnFrontUpload.setVisibility(View.VISIBLE);
                // CALL THIS METHOD TO GET THE ACTUAL PATH
//                File file = new File(getRealPathFromURI(tempUriFront));
//                System.out.println(file);

            } else if (resultCode == RESULT_CANCELED) {
                if( MyApplication.getSaveString("Locale", MyApplication.getInstance()).equalsIgnoreCase("fr")){
                    MyApplication.showToast(registersteptwoC,"Utilisateur annulé");

                }else{
                    MyApplication.showToast(registersteptwoC,getString(R.string.usercancel));

                }

            } else if (resultCode == RESULT_CODE_FAILURE) {
                MyApplication.showToast(registersteptwoC,getString(R.string.failed));
            }

        }
        if (requestCode == REQUEST_IMAGE_CAPTURE_TWO) {
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                Data = data;

                tempUriBack = getImageUri(getApplicationContext(), imageBitmap);

                etBack.setText(tempUriBack.getLastPathSegment());

                fileBack = new File(getRealPathFromURI(tempUriBack).toString());
                int file_size = Integer.parseInt(String.valueOf(fileBack.length() / 1024));     //calculate size of image in KB
                if (file_size <= 100){
                    isBackUpload=true;
                }else {
                    MyApplication.showErrorToast(registersteptwoC,getString(R.string.fileexceed));
                }
                //btnBackUpload.setVisibility(View.VISIBLE);
                // CALL THIS METHOD TO GET THE ACTUAL PATH
               // File file = new File(getRealPathFromURI(tempUri));

            } else if (resultCode == RESULT_CANCELED) {
                if( MyApplication.getSaveString("Locale", MyApplication.getInstance()).equalsIgnoreCase("fr")){
                    MyApplication.showToast(registersteptwoC,"Utilisateur annulé");

                }else{
                    MyApplication.showToast(registersteptwoC,getString(R.string.usercancel));

                }            } else if (resultCode == RESULT_CODE_FAILURE) {
                MyApplication.showToast(registersteptwoC,getString(R.string.failed));
            }

        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title"+ Calendar.getInstance().getTime(), null);
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

    public boolean isFrontUpload=false;
    public boolean isBackUpload=false;
    public File filesUploadFront() {
        File file = new File(getRealPathFromURI(tempUriFront).toString());
        int file_size = Integer.parseInt(String.valueOf(file.length() / 1024));     //calculate size of image in KB
        if (file_size <= 100){
            isFrontUpload=true;

            callupload(file,"100012");
        }else {
            MyApplication.showErrorToast(registersteptwoC,getString(R.string.fileexceed));
        }
        return file;
    }

    public File filesUploadBack() {
        File file = new File(getRealPathFromURI(tempUriBack).toString());
        int file_size = Integer.parseInt(String.valueOf(file.length() / 1024));     //calculate size of image in KB
        if (file_size <= 100){
           isBackUpload=true;
            callupload(file,"100013");
        }else {
            MyApplication.showErrorToast(registersteptwoC,getString(R.string.fileexceed));
        }
        return file;
    }

    private void callApiIdProofType() {
        try {
            API.GET_PUBLIC("ewallet/public/idProofType/all",
                    new Api_Responce_Handler() {
                        @Override
                        public void success(JSONObject jsonObject) {
                            MyApplication.hideLoader();

                            if (jsonObject != null) {
                                idProofTypeList.clear();
                                if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")){
                                    JSONArray walletOwnerListArr = jsonObject.optJSONArray("idProofTypeList");
                                    for (int i = 0; i < walletOwnerListArr.length(); i++) {
                                        JSONObject data = walletOwnerListArr.optJSONObject(i);
                                        idProofTypeModelList.add(new IDProofTypeModel.IDProofType(
                                                data.optInt("id"),
                                                data.optString("code"),
                                                data.optString("type"),
                                                data.optString("status"),
                                                data.optString("creationDate")

                                        ));

                                        idProofTypeList.add(data.optString("type").trim());

                                    }

                                    spinnerDialogIdProofType = new SpinnerDialog(registersteptwoC, idProofTypeList, getString(R.string.select_id_proof), R.style.DialogAnimations_SmileWindow, "CANCEL");// With 	Animation
                                    spinnerDialogIdProofType.setCancellable(true); // for cancellable
                                    spinnerDialogIdProofType.setShowKeyboard(false);// for open keyboard by default
                                    spinnerDialogIdProofType.bindOnSpinerListener(new OnSpinerItemClick() {
                                        @Override
                                        public void onClick(String item, int position) {
                                            //Toast.makeText(MainActivity.this, item + "  " + position+"", Toast.LENGTH_SHORT).show();
                                            spIdProof.setText(item);
                                            spIdProof.setTag(position);
                                        }
                                    });

                                } else {
                                    MyApplication.showToast(registersteptwoC,jsonObject.optString("resultDescription", "N/A"));
                                }
                            }
                        }

                        @Override
                        public void failure(String aFalse) {
                            MyApplication.hideLoader();

                        }
                    });


        } catch (Exception e) {

        }

    }

    JSONObject documentUploadJsonObj;
    private void callupload(File file,String Code) {

        MyApplication.showloader(registersteptwoC, "uploading file...");
        //idProofTypeModelList.get((Integer) spIdProof.getTag()).getCode()
        API.Upload_REQESTID("ewallet/api/v1/fileUpload/mobile",idProofTypeModelList.get((Integer) spIdProof.getTag()).getCode(),
                etProofNo.getText().toString().trim(),fileFront, fileBack, new Api_Responce_Handler() {

            @Override
            public void success(JSONObject jsonObject) {
                MyApplication.hideLoader();
                if (jsonObject != null) {
                    if (jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")) {
                        //MyApplication.showToast(getString(R.string.document_upload_msg));
                        documentUploadJsonObj=jsonObject;
                        MyApplication.showToast(registersteptwoC,"upload success");
                       // callApiUpdateDataApproval();
                        Intent intent = new Intent(registersteptwoC, SelfSignature.class);
                        startActivity(intent);
                        finish();

                    } else if (jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("2001")) {
                        MyApplication.showToast(registersteptwoC,getString(R.string.technical_failure));
                    } else {
                        MyApplication.showToast(registersteptwoC,jsonObject.optString("resultDescription", "N/A"));
                    }
                }


            }

            @Override
            public void failure(String aFalse) {

            }
        });
    }

//    private void callApiUpdateDataApproval() {
//        try {
//            JSONObject updatejsonObject = new JSONObject();
//            JSONArray jsonArray = new JSONArray();
//            updatejsonObject.put("actionType", "Updated");
//            updatejsonObject.put("assignTo", "");
//            updatejsonObject.put("comments", "");
//            updatejsonObject.put("entityCode",KYCDocumentActivity.walletOwner.optJSONObject("walletOwner").optString("code"));
//            updatejsonObject.put("entityName", KYCDocumentActivity.walletOwner.optJSONObject("walletOwner").optString("ownerName"));
//            updatejsonObject.put("featureCode", "100019");
//            updatejsonObject.put("status", "UP");
//
//            JSONObject updateInfojson=new JSONObject();
//            updatejsonObject.put("updatedInformation", updateInfojson);
//
//            JSONObject documentListObject = new JSONObject();
//            JSONArray documentListArray = new JSONArray();
//            documentListArray.put(documentListObject);
//
//
//            updateInfojson.put("documentList",documentListArray);
//
//
//            documentListObject.put("code",documentUploadJsonObj.optJSONObject("documentUpload").optString("code"));
//            documentListObject.put("walletOwnerCode",documentUploadJsonObj.optJSONObject("documentUpload").optString("walletOwnerCode"));
//            documentListObject.put("documentTypeCode",documentUploadJsonObj.optJSONObject("documentUpload").optString("documentTypeCode"));
//            documentListObject.put("documentTypeName",documentUploadJsonObj.optJSONObject("documentUpload").optString("documentTypeName"));
//            documentListObject.put("fileName",documentUploadJsonObj.optJSONObject("documentUpload").optString("fileName"));
//            if(documentUploadJsonObj.optJSONObject("documentUpload").optString("status").equalsIgnoreCase("Active")){
//                documentListObject.put("status",documentUploadJsonObj.optJSONObject("documentUpload").optString("Y"));
//            }
//            if(documentUploadJsonObj.optJSONObject("documentUpload").optString("status").equalsIgnoreCase("Inactive")){
//                documentListObject.put("status",documentUploadJsonObj.optJSONObject("documentUpload").optString("N"));
//            }
//            documentListObject.put("createdBy",documentUploadJsonObj.optJSONObject("documentUpload").optString("createdBy"));
//            documentListObject.put("creationDate",documentUploadJsonObj.optJSONObject("documentUpload").optString("creationDate"));
//
//
//            JSONObject entityJson=new JSONObject();
//
//            entityJson.put("documentUploadList",docUploadList);
//
//            updatejsonObject.put("entity", entityJson);
//
//            jsonArray.put(updatejsonObject);
//            JSONObject data = new JSONObject();
//            data.put("dataApprovalList", jsonArray);
//
//            System.out.println("EditKYCDoc=="+data.toString());
//
//            if(updateInfojson.length()==0){
//                MyApplication.showErrorToast(getString(R.string.no_data_updated));
//            }else {
//                MyApplication.showloader(editkycdocumentC,"Please wait!");
//                API.POST_REQEST_WH_NEW("ewallet/api/v1/dataApproval", data, new Api_Responce_Handler() {
//                    @Override
//                    public void success(JSONObject jsonObject) {
//                        MyApplication.hideLoader();
//
//                        if (jsonObject != null) {
//                            if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("0")){
//                                MyApplication.showToast(getString(R.string.subscriber_updated));
//                                Intent intent = new Intent(editkycdocumentC,KYCDocumentActivity.class);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                startActivity(intent);
//                                finish();
//                            }else if(jsonObject.optString("resultCode", "N/A").equalsIgnoreCase("2001")){
//                                MyApplication.showToast(getString(R.string.technical_failure));
//                            } else {
//                                MyApplication.showToast(jsonObject.optString("resultDescription", "N/A"));
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void failure(String aFalse) {
//
//                    }
//                });
//            }
//
//
//
//        }catch (Exception e){
//
//        }
//
//    }


}