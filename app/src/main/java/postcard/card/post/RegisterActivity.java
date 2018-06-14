package postcard.card.post;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    EditText emailID,passd1,passd2;
    Button registerBT, loginBT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        backgroundAnim();
        buttonmethod();

        final FirebaseAuth mAuth = FirebaseAuth.getInstance();

        loginBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendLoginActivity();
            }
        });
        registerBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailID.getText().toString();
                String pass1 = passd1.getText().toString();
                String pass2 = passd2.getText().toString();

                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass1) && !TextUtils.isEmpty(pass2)){
                    if (pass1.equals(pass2)){
                        mAuth.createUserWithEmailAndPassword(email,pass1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    setupActivity();
                                }else {
                                    String errorMessage = task.getException().getMessage();
                                    Toast.makeText(RegisterActivity.this,"error"+errorMessage,Toast.LENGTH_LONG).show();
                                }


                            }
                        });
                    }else{
                        String error = "both password should be same";
                        Toast.makeText(RegisterActivity.this,error,Toast.LENGTH_LONG).show();
                    }
                }else {
                    String errorMessage = "please enter all the fields";
                    Toast.makeText(RegisterActivity.this,errorMessage, Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    private void sendLoginActivity() {
        Intent sendLoginActivity = new Intent(RegisterActivity.this,LoginActivity.class);
        startActivity(sendLoginActivity);
        finish();
    }

    private void setupActivity() {
        Intent setupActivity = new Intent(RegisterActivity.this, SetupActivity.class);
        startActivity(setupActivity);
        finish();
    }

    private void sendToManinActivity() {
        Intent mainActivity = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(mainActivity);
        finish();
    }

    private void buttonmethod() {

        emailID = findViewById(R.id.registerPEmailET);
        passd1 = findViewById(R.id.passdRegisterP1);
        passd2 = findViewById(R.id.passdRegisterP2);
        registerBT = findViewById(R.id.registerBTMain2);
        loginBT = findViewById(R.id.regLoginBT);
    }

    public void backgroundAnim() {
        ConstraintLayout cl = findViewById(R.id.registerCLayout);
        AnimationDrawable anmd = (AnimationDrawable) cl.getBackground();
        anmd.setExitFadeDuration(500);
        anmd.setEnterFadeDuration(500);
        anmd.start();

    }
}
