package cz.inovett.simplyidea;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class SetupActivity extends AppCompatActivity {

    private ImageButton imageButton;
    private EditText name, email, password, info;
    private Button saveBtutton;
    private static final int GAELLERY_REQUEST = 1;
    private Uri mImageuri = null;
    private DatabaseReference mDatabaseUsers;
    private FirebaseAuth mAuth;
    private StorageReference mStorageImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        mStorageImage = FirebaseStorage.getInstance().getReference().child("Profile_images");
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        name = (EditText) findViewById(R.id.SetupName);
        email = (EditText) findViewById(R.id.SetupEmail);
        password = (EditText) findViewById(R.id.SetupPassword);
        info = (EditText) findViewById(R.id.SetupInformation);



        saveBtutton = (Button) findViewById(R.id.ButtonSave);

        saveBtutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartSetupAccount();
            }
        });

        imageButton = (ImageButton) findViewById(R.id.imageButtonSetup);

        imageButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               Intent galleryIntent = new Intent();
               galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
               galleryIntent.setType("image/*");
               startActivityForResult(galleryIntent, GAELLERY_REQUEST);
           }
       });

    }
    private void StartSetupAccount() {
        final String nameStr = name.getText().toString().trim();
        final String emailStr = email.getText().toString().trim();
        final String passwordStr = password.getText().toString().trim();
        final String infoStr = info.getText().toString().trim();
        final String user_id = mAuth.getCurrentUser().getUid();

        if (!TextUtils.isEmpty(nameStr)||!TextUtils.isEmpty(emailStr)||!TextUtils.isEmpty(passwordStr)
                ||!TextUtils.isEmpty(infoStr)||mImageuri!=null){

            StorageReference filepath = mStorageImage.child(mImageuri.getLastPathSegment());

            filepath.putFile(mImageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    @SuppressWarnings("VisibleForTests") Uri downloadUri = taskSnapshot.getDownloadUrl();
                    mDatabaseUsers.child(user_id).child("name").setValue(nameStr);
                    mDatabaseUsers.child(user_id).child("email").setValue(emailStr);
                    mDatabaseUsers.child(user_id).child("password").setValue(passwordStr);
                    mDatabaseUsers.child(user_id).child("info").setValue(infoStr);
                    mDatabaseUsers.child(user_id).child("image").setValue(downloadUri);
                }
            });

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(requestCode == GAELLERY_REQUEST && resultCode == RESULT_OK){
            // start picker to get image for cropping and then use the image in cropping activity

            Uri imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mImageuri = result.getUri();

                imageButton.setImageURI(mImageuri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}
