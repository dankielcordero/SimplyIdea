package cz.inovett.simplyidea;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    private EditText EmailField, NameField, Pass1Field, Pass2Field;
    private String nameStr, emailStr, pass1Str, pass2Str;
    private FirebaseAuth auth;
    private ProgressDialog progressDialog;
    private DatabaseReference databaseRefence;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();
        databaseRefence = FirebaseDatabase.getInstance().getReference().child("Users");
        progressDialog = new ProgressDialog(this);


        EmailField = (EditText) findViewById(R.id.emailField);
        NameField = (EditText) findViewById(R.id.nameField);
        Pass1Field = (EditText) findViewById(R.id.passwordField);
        Pass2Field = (EditText) findViewById(R.id.passwordField2);

    }


    public void startApplication(View view) {
        nameStr = NameField.getText().toString().trim();
        emailStr = EmailField.getText().toString().trim();
        pass1Str = Pass1Field.getText().toString().trim();
        pass2Str = Pass2Field.getText().toString().trim();


        if (TextUtils.isEmpty(nameStr)) {
            Toast.makeText(getApplicationContext(), "Enter name!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(emailStr)) {
            Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(pass1Str)) {
            Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (pass1Str.length() < 6) {
            Toast.makeText(getApplicationContext(), "Password1 too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (pass2Str.length() < 6) {
            Toast.makeText(getApplicationContext(), "Password2 too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
            return;
        }


        //create user



        if (!TextUtils.isEmpty(nameStr)&&!TextUtils.isEmpty(emailStr)&&!TextUtils.isEmpty(pass1Str)&&!TextUtils.isEmpty(pass2Str)){
            if (pass1Str.equals(pass2Str)){

                progressDialog.setMessage("Singning Up ....");
                progressDialog.show();

                auth.createUserWithEmailAndPassword(emailStr, pass1Str)
                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(RegisterActivity.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();

                                } else {
                                    String user_id = auth.getCurrentUser().getUid();

                                    DatabaseReference current_user_db = databaseRefence.child(user_id);
                                    current_user_db.child("name").setValue(nameStr);
                                    current_user_db.child("image").setValue("deafult");
                                    progressDialog.dismiss();

                                    Intent i = new Intent(RegisterActivity.this,MainMenuActivity.class);
                                    /*i.putExtra("userName",NameField.getText().toString());*/
                                    startActivity(i);
                                    finish();
                                }
                            }
                        });

            }else{
                Toast.makeText(this,"Password are not equals",Toast.LENGTH_SHORT).show();
            }



        }


        /*Intent i = new Intent(RegisterActivity.this,MainPage.class);
        startActivity(i);*/
    }


    public void startLogin(View view) {
        Intent i = new Intent(this,LoginActivity.class);
        startActivity(i);
    }
}
