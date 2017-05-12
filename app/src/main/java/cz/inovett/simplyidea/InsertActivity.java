package cz.inovett.simplyidea;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class InsertActivity extends AppCompatActivity {
    private EditText name, category, fullText;
    private Button mSubmitBtn;
    private DatabaseReference databaseReference;
   /* private StorageReference storageReference;*/
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startApplication(view);
            }
        });


        name = (EditText) findViewById(R.id.nameField);
        category = (EditText) findViewById(R.id.editTextCategory);
        fullText = (EditText) findViewById(R.id.editTextFullText);
        mSubmitBtn = (Button) findViewById(R.id.buttonSubmit);

        /*progressDialog = new ProgressDialog(this);*/
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Blog");


        mSubmitBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View view){
                StartPosting();
            }
        });

    }

    private void StartPosting() {
        String strName = name.getText().toString().trim();
        String strCategory = category.getText().toString().trim();
        String strFullText = fullText.getText().toString().trim();

        if (!TextUtils.isEmpty(strName)&&!TextUtils.isEmpty(strCategory)
                &&!TextUtils.isEmpty(strFullText)){

            DatabaseReference newPost = databaseReference.push();
            newPost.child("title").setValue(strName);
            newPost.child("Category").setValue(strCategory);
            newPost.child("Text").setValue(strFullText);
            Intent i = new Intent(this,MainMenuActivity.class);
            Toast.makeText(this,"Successfully added", Toast.LENGTH_SHORT).show();
            startActivity(i);
        }

    }

    public void startApplication(View view) {
        Intent i = new Intent(this,MainMenuActivity.class);
        startActivity(i);

    }

}
