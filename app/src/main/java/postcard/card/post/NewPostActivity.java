package postcard.card.post;

import android.content.Intent;
import android.graphics.Bitmap;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import id.zelory.compressor.Compressor;

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
   private Bitmap compressedImageFile;

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

                   //generating random through uuid
                   final String randomeName = UUID.randomUUID().toString();

                  StorageReference filepath = storageReference.child("post_image").child(randomeName+".jpg");

                  filepath.putFile(postImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                      @Override
                      public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                          if (task.isSuccessful()){

                              // original image URL from firebase server
                               final String downloadURL = task.getResult().getDownloadUrl().toString();

                              //parsing uri into file path
                              File newImageFile = new File(postImageUri.getPath());

                              try {

                                  //compressor code for thimbling image file
                                  compressedImageFile = new Compressor(NewPostActivity.this)
                                          .setMaxHeight(200)
                                          .setMaxWidth(200)
                                          .setQuality(5)
                                          .compressToBitmap(newImageFile);

                              } catch (IOException e) {
                                  e.printStackTrace();
                              }
                              //firebase code to pass compressed file as we cannt use putFile method as it only suppor URL.
                              ByteArrayOutputStream baos = new ByteArrayOutputStream();
                              compressedImageFile.compress(Bitmap.CompressFormat.JPEG, 90, baos);
                              byte[] thumbImageData = baos.toByteArray();

                              UploadTask Thumbpath = storageReference
                                      .child("post_image/thumb")
                                      .child(randomeName+".jpg")
                                      .putBytes(thumbImageData);

                              Thumbpath.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                  @Override
                                  public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                      String downloadThumbUri = taskSnapshot.getDownloadUrl().toString();

                                      Map<String, Object> postMap = new HashMap<>();
                                      postMap.put("user_id",user_ID);
                                      postMap.put("time_stamp",FieldValue.serverTimestamp()); //passing Server Time Stamp
                                      postMap.put("post_text",posttext);
                                      postMap.put("image_uri", downloadURL); //passing the original non compressed image
                                      postMap.put("thumb_image", downloadThumbUri); //passing compressed thumb image url

                                      firebaseFirestore.collection("user_post")
                                              .add(postMap)
                                              .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {

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
                              }).addOnFailureListener(new OnFailureListener() {
                                  @Override
                                  public void onFailure(@NonNull Exception e) {




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
