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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
   private EditText emailET, passET;
    private Button loginBT,registerBT;
    private FirebaseAuth mAuth;

    ProgressBar pB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        backgroundAnim();

        buttonMethod();
        mAuth = FirebaseAuth.getInstance();

        loginBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailID = emailET.getText().toString();
                String passwd = passET.getText().toString();

                if (!TextUtils.isEmpty(emailID) && !TextUtils.isEmpty(passwd)){
                    pB.setVisibility(View.VISIBLE);
                    mAuth.signInWithEmailAndPassword(emailID,passwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                sendToMainActivity();
                            }else {
                                String errorMessage = task.getException().getMessage();
                                Toast.makeText(LoginActivity.this,"error"+errorMessage, Toast.LENGTH_LONG).show();
                            }
                            pB.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            }
        });

        registerBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerPage = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerPage);
                finish();
            }
        });




    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null){
            sendToMainActivity();
        }
    }

    private void sendToMainActivity(){
        Intent mainActivity = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(mainActivity);
        finish();
    }

    private void buttonMethod() {

        emailET = findViewById(R.id.emailLoginET);
        passET = findViewById(R.id.passLoginET);
        loginBT = findViewById(R.id.loginBT);
        pB = findViewById(R.id.progressBar);
        registerBT = findViewById(R.id.registerLogin);
    }


    public void backgroundAnim() {
        ConstraintLayout cl = findViewById(R.id.loginCLayout);
        AnimationDrawable anmd = (AnimationDrawable) cl.getBackground();
        anmd.setExitFadeDuration(500);
        anmd.setEnterFadeDuration(500);
        anmd.start();

    }

}
