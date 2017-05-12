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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private EditText email, pass;
    private String strEmail, strPass;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseUsers;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = (EditText) findViewById(R.id.loginEmailField);
        pass = (EditText) findViewById(R.id.loginPasswordField);
        mAuth = FirebaseAuth.getInstance();
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabaseUsers.keepSynced(true);
        mProgress = new ProgressDialog(this);
    }

    public void startRegister(View view) {
        Intent i = new Intent(this,RegisterActivity.class);
        startActivity(i);
    }
    public void startApplication(View view) {
        checkLogin();

        /*Intent i = new Intent(this,MainMenuActivity.class);
        startActivity(i);*/

    }

    private void checkLogin() {
        strEmail = email.getText().toString().trim();
        strPass = pass.getText().toString().trim();

        if(!TextUtils.isEmpty(strEmail)&&!TextUtils.isEmpty(strPass)){
            mProgress.setMessage("Checking Login ....");
            mProgress.show();
            mAuth.signInWithEmailAndPassword(strEmail,strPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        mProgress.dismiss();
                        checkUserExist();
                    }else{
                        mProgress.dismiss();
                        Toast.makeText(LoginActivity.this,"Error Login",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else{
            Toast.makeText(LoginActivity.this,"Fill in all the boxes!",Toast.LENGTH_SHORT).show();
        }
    }

    private void checkUserExist() {

        final String name = mAuth.getCurrentUser().getUid();
        mDatabaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(name)){
                    Intent i = new Intent(LoginActivity.this,MainMenuActivity.class);
                    startActivity(i);
                }else{
                    Toast.makeText(LoginActivity.this,"Wrong email or password",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(LoginActivity.this,SetupActivity.class); /*musí se vyřešit problémy*/
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
