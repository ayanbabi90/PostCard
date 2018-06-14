package postcard.card.post;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.Manifest.*;
import static android.Manifest.permission.*;

public class SetupActivity extends AppCompatActivity {

    CircleImageView userImage;
    EditText userName;
    Button saveSetting, deleteUserProfile;
    private Uri mainImageUri = null;
    private StorageReference mStorageRef;
    private FirebaseAuth mAuthUser;
    private FirebaseFirestore firebaseFirestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        buttonMethod();

        Toolbar setupToolbar = findViewById(R.id.activity_setup);
        setSupportActionBar(setupToolbar);
        getSupportActionBar().setTitle("Account Setup");

        mStorageRef = FirebaseStorage.getInstance().getReference();

        mAuthUser = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    //Condition for checking permission in Android M(6)
                    if (ContextCompat.checkSelfPermission(SetupActivity.this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(SetupActivity.this, "permisssion need to read storage", Toast.LENGTH_LONG).show();
                        ActivityCompat.requestPermissions(SetupActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, permission.ACCESS_NETWORK_STATE
                                , permission.WRITE_EXTERNAL_STORAGE, permission.INTERNET}, 1);
                    } else {

                        // start picker to get image for cropping and then use the image in cropping activity
                        imagePickerMethod();
                    }
                } else {//for lower than Android M

                    // start picker to get image for cropping and then use the image in cropping activity
                    imagePickerMethod();
                }
            }
        });

        saveSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String uname = userName.getText().toString();

                if (!TextUtils.isEmpty(uname) && mainImageUri != null) {

                    final String userId = mAuthUser.getCurrentUser().getUid();

                    StorageReference image_path = mStorageRef.child("profile_image").child(userId + ".jpg");

                    image_path.putFile(mainImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                            if (task.isSuccessful()) {

                                Uri download_image = task.getResult().getUploadSessionUri();

                                Map<String,String> userMap = new HashMap<>();
                                userMap.put("name",uname);
                                userMap.put("profile_image",download_image.toString());

                                firebaseFirestore.collection("Users")
                                        .document(userId).set(userMap)
                                        .addOnCompleteListener
                                                (new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()){
                                            Toast.makeText(SetupActivity.this,"Settings updated", Toast.LENGTH_LONG).show();
                                            Intent gotoMain = new Intent(SetupActivity.this,MainActivity.class);
                                            startActivity(gotoMain);
                                            finish();

                                        }else {
                                            String errorM = task.getException().getMessage();
                                            Toast.makeText(SetupActivity.this,"error"+errorM, Toast.LENGTH_LONG).show();
                                        }


                                    }
                                });

                            } else {

                                String errorMessage = task.getException().getMessage();
                                Toast.makeText(SetupActivity.this, "error" + errorMessage, Toast.LENGTH_LONG).show();

                            }

                        }
                    });
                }
            }
        });

        deleteUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser firebaseUser = mAuthUser.getCurrentUser();

                firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SetupActivity.this, "user deleted", Toast.LENGTH_LONG).show();
                            sendToMain();
                        }
                    }
                });
            }
        });


    }

    private void imagePickerMethod() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .start(SetupActivity.this);
    }

    private void sendToMain() {
        Intent mainIntent = new Intent(SetupActivity.this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }

    private void buttonMethod() {
        userImage = findViewById(R.id.userImage);
        userName = findViewById(R.id.nameEditText);
        saveSetting = findViewById(R.id.saveSettings);
        deleteUserProfile = findViewById(R.id.deletUser);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Request
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                mainImageUri = result.getUri();
                userImage.setImageURI(mainImageUri);


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }


}
