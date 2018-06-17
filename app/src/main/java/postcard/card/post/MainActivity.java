package postcard.card.post;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    Toolbar mainToolBar;
    FloatingActionButton addPostBT;

    FirebaseAuth mAuth;
    FirebaseFirestore firebaseFirestore;
    String current_user_id;

    BottomNavigationView mainNavBottom;

    HomeFragment homeFragment;
    NotificationFragment notificationFragment;
    AccountFragment accountFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar();
        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();


        //Fragments
        homeFragment = new HomeFragment();
        notificationFragment = new NotificationFragment();
        accountFragment = new AccountFragment();


        ButtonMethod();

        addPostBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent newPostActivity = new Intent(MainActivity.this, NewPostActivity.class);
                startActivity(newPostActivity);

            }
        });

        mainNavBottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.home_bottom:
                        replaceFragment(homeFragment);
                        return true;
                    case R.id.notification_bottom:
                        replaceFragment(notificationFragment);
                        return true;
                    case R.id.account_bottom:
                        replaceFragment(accountFragment);
                        return true;
                        default:
                            return false;
                }

            }
        });





    }





    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null){
            //Send Activity to Login
            Intent loginActivity = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(loginActivity);
            finish();
        }else {

            current_user_id = mAuth.getCurrentUser().getUid();

            firebaseFirestore.collection("Users").document(current_user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    if(task.isSuccessful()){

                        if(!task.getResult().exists()){

                            Intent setupIntent = new Intent(MainActivity.this, SetupActivity.class);
                            startActivity(setupIntent);
                            finish();

                        }

                    } else {

                        String errorMessage = task.getException().getMessage();
                        Toast.makeText(MainActivity.this, "Error : " + errorMessage, Toast.LENGTH_LONG).show();


                    }

                }
            });

        }
    }

    private void ButtonMethod() {
        addPostBT = findViewById(R.id.postFloatingBT);
        mainNavBottom = findViewById(R.id.mainBootmNav);
    }


    private void sendToSetupActivity() {
        Intent setupActivity = new Intent(MainActivity.this, SetupActivity.class);
        startActivity(setupActivity);
    }


        private void toolbar() {
        mainToolBar = (Toolbar) findViewById(R.id.mainToolbar);
        setSupportActionBar(mainToolBar);
        getSupportActionBar().setTitle("Post Card");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_search:
                accountSearch();
                return true;
            case R.id.accountSettings:
                accountMenu();
                return true;
            case R.id.logOutMainActivity:
                accountLogOut();
                return true;

                default:
                    return false;
        }

    }

    private void accountSearch() {

    }

    private void accountLogOut() {
      FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
      firebaseAuth.signOut();
      Intent sendToLogin = new Intent(MainActivity.this,LoginActivity.class);
      startActivity(sendToLogin);
      finish();


    }

    private void accountMenu() {
        Intent accountSetting = new Intent(MainActivity.this, SetupActivity.class);
        startActivity(accountSetting);


    }

    public void replaceFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction =getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_container,fragment);
        fragmentTransaction.commit();
    }




}


