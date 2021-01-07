package com.example.digibook.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.digibook.LoginActivity;
import com.example.digibook.MainNavActivity;
import com.example.digibook.Networking.APIclient;
import com.example.digibook.R;
import com.example.digibook.SettingsActivity;
import com.example.digibook.utilities.CurrentSession;
import com.example.digibook.utilities.RealPathUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    ImageView image;
    Button upload;
    Uri imageurl;
    Button settings;
    TextView name,email;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int READ_STORAGE_PERMISSION_REQUEST_CODE = 1 ;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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



        View viewroot = inflater.inflate(R.layout.fragment_profile, container, false);

        BottomNavigationView mainBotNav = viewroot.findViewById(R.id.profile_navig);
        View host = viewroot.findViewById(R.id.profile_host);
        NavController navController = Navigation.findNavController(host);
        NavigationUI.setupWithNavController(mainBotNav, navController);

        image = viewroot.findViewById(R.id.profile_image);
        upload = viewroot.findViewById(R.id.profile_upload_image);

        name = viewroot.findViewById(R.id.profilename);
        email = viewroot.findViewById(R.id.profileemail);
        settings = viewroot.findViewById(R.id.profilesettings);

        name.setText(CurrentSession.CurrentUser.getName());
        email.setText(CurrentSession.CurrentUser.getEmail());
        Log.d("lol", CurrentSession.CurrentUser.getEmail() + " " + CurrentSession.CurrentUser.getName());

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("settingsNet", CurrentSession.CurrentUser.getPassword().toString());
                Intent goMainNavAct = new Intent(getContext(), SettingsActivity.class);
                startActivity(goMainNavAct);
            }
        });

        //image code : thama // zeyda I guess ba3ed base url
        Log.d("testtt", APIclient.base_url + CurrentSession.CurrentUser.getPicurl().toString());
        Glide.with(getContext())
                .load(APIclient.base_url + CurrentSession.CurrentUser.getPicurl())
                .into(image);

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("testtt", APIclient.base_url + CurrentSession.CurrentUser.getPicurl().toString());

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

        // Inflate the layout for this fragment
        return viewroot;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK){
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
            Log.d("FILEPATHBRO", "File Path: " + path);
            // Get the file instance

//            Glide.with(getContext())
//                    .load(APIclient.base_url + CurrentSession.CurrentUser.getPicurl())
//    .into(image);
            CurrentSession.uploadProfilePicture(path, getContext());
        }

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
            ActivityCompat.requestPermissions((Activity) getContext(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    READ_STORAGE_PERMISSION_REQUEST_CODE);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}