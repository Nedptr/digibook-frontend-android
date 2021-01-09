package com.example.digibook.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.CursorIndexOutOfBoundsException;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.digibook.MainNavActivity;
import com.example.digibook.R;
import com.example.digibook.RegisterActivity;
import com.example.digibook.utilities.CurrentSession;
import com.example.digibook.utilities.RealPathUtils;

import java.io.IOException;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int READ_STORAGE_PERMISSION_REQUEST_CODE = 1 ;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 1;
    private static final int WRITE_STORAGE_PERMISSION_REQUEST_CODE = 1;
    private static final int IMAGE_CAPTURE_CODE = 1;


    private Uri image_uri;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View viewroot = inflater.inflate(R.layout.fragment_search, container, false);
        Button uploadButton = (Button) viewroot.findViewById(R.id.UploadImage);
        Button takeimage = (Button) viewroot.findViewById(R.id.TakeImage);

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // PERMISSION
                if(checkPermissionForReadExtertalStorage() == false) {
                    try {
                        requestPermissionForReadExtertalStorage();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                Intent gallery = new Intent();
                gallery.setType("image/*");
                gallery.setAction(Intent.ACTION_GET_CONTENT);
                // PICK_IMAGE = 1
                startActivityForResult(Intent.createChooser(gallery, "Select Image"), 1);


            }
        });

        takeimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checkPermissionForReadExtertalStorage() == false) {
                    try {
                        requestPermissionForReadExtertalStorage();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, "New Picture");
                values.put(MediaStore.Images.Media.DESCRIPTION,"From the Cam");
                image_uri = viewroot.getContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                // Cam intent
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
                startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE);

            }
        });

        return viewroot;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Log.d("FILEPATHBRO", "File uri: " + data.getData().toString());

/*        if(requestCode == 1 && resultCode == RESULT_OK){
            Uri uri = data.getData();
            String path = null;
            if (Build.VERSION.SDK_INT < 11)
                path = RealPathUtils.getRealPathFromURI_BelowAPI11(getContext(), uri);

                // SDK >= 11 && SDK < 19
            else if (Build.VERSION.SDK_INT < 19)
                path = RealPathUtils.getRealPathFromURI_API11to18(getContext(), uri);

                // SDK > 19 (Android 4.4)
            else
                path = RealPathUtils.getRealPathFromURI_API19(getContext(), uri);
            Log.d("FILEPATHBRO", "File Path: " + path);*/

        if(requestCode == 1 && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            try {
                CurrentSession.uploadImageSearch(uri, getContext());
            } catch (IOException e) {
                e.printStackTrace();
            }
            Toast.makeText(getContext(),"Searching.. Please Wait..",Toast.LENGTH_LONG).show();

        }else{
            Toast.makeText(getContext(),"Please Choose a Picture!",Toast.LENGTH_SHORT).show();

            return;
        }

/*        if(requestCode == IMAGE_CAPTURE_CODE && resultCode == RESULT_OK){
            Uri uri = image_uri;
            String path = null;
            if (Build.VERSION.SDK_INT < 11)
                path = RealPathUtils.getRealPathFromURI_BelowAPI11(getContext(), uri);

                // SDK >= 11 && SDK < 19
            else if (Build.VERSION.SDK_INT < 19)
                path = RealPathUtils.getRealPathFromURI_API11to18(getContext(), uri);

                // SDK > 19 (Android 4.4)
            else
                path = RealPathUtils.getRealPathFromURI_API19(getContext(), uri);
            Log.d("FILEPATHBRO", "File Path: " + path);
            // Get the file instance
            CurrentSession.uploadImageSearch(path, getContext());
        }*/
                //  content://com.android.providers.media.documents/document/image%3A36754
    }

    public boolean checkPermissionForReadExtertalStorage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = getContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }

    public void requestPermissionForReadExtertalStorage() throws Exception {
        try {
            ActivityCompat.requestPermissions((Activity) getContext(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                    READ_STORAGE_PERMISSION_REQUEST_CODE);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

/*    //
    public boolean checkPermissionForWriteExtertalStorage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = getContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }

    public void requestPermissionForWriteExtertalStorage() throws Exception {
        try {
            ActivityCompat.requestPermissions((Activity) getContext(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    WRITE_STORAGE_PERMISSION_REQUEST_CODE);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    
    public boolean checkPermissionForCamera() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = getContext().checkSelfPermission(Manifest.permission.CAMERA);
            return result == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }

    public void requestPermissionForCamera() throws Exception {
        try {
            ActivityCompat.requestPermissions((Activity) getContext(), new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_REQUEST_CODE);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }*/
}