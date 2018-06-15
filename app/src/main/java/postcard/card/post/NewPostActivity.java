package postcard.card.post;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class NewPostActivity extends AppCompatActivity {

    Button postBT;
    EditText postText;
    ImageView postImage;
    Toolbar postToolBar;
    FirebaseAuth mAuth;
    FirebaseFirestore firebaseFirestore;
    StorageReference storageReference;
    String user_ID;
    private Uri postImageUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        buttonMethod();
        toolbar();

        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        user_ID = mAuth.getCurrentUser().getUid();


        postImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setMinCropResultSize(512,512)
                        .setAspectRatio(1,1)
                        .start(NewPostActivity.this);
            }
        });

        postBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String posttext;
               posttext = postText.getText().toString();

               if (!TextUtils.isEmpty(posttext)){

                   final String randomeName = FieldValue.serverTimestamp().toString();

                  StorageReference filepath = storageReference.child("post_image").child(randomeName+".jpg");

                  filepath.putFile(postImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                      @Override
                      public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                          if (task.isSuccessful()){

                              String downloadURL = task.getResult().getDownloadUrl().toString();


                              Map<String, Object> postMap = new HashMap<>();
                              postMap.put("user_id",user_ID);
                              postMap.put("time_stamp",randomeName);
                              postMap.put("post_text",posttext);
                              postMap.put("image_uri", downloadURL);

                              firebaseFirestore.collection("user_post").add(postMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                  @Override
                                  public void onComplete(@NonNull Task<DocumentReference> task) {
                                      if (task.isSuccessful()){

                                          Toast.makeText(NewPostActivity.this, "sucess",Toast.LENGTH_LONG).show();
                                          Intent newI = new Intent(NewPostActivity.this, MainActivity.class);
                                          startActivity(newI);
                                          finish();



                                      }
                                  }
                              });





                          }

                      }
                  });
               }

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK){
                postImageUri = result.getUri();

                postImage.setImageURI(postImageUri);

            }
        }

    }

    private void buttonMethod() {
        postBT = findViewById(R.id.postBT);
        postImage = findViewById(R.id.postImageView);
        postText = findViewById(R.id.postText);
    }

    private void toolbar() {

        postToolBar = findViewById(R.id.postToolbar);
        setSupportActionBar(postToolBar);
        getSupportActionBar().setTitle("Add New Post");
        //for home back button && manifest file
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
