package postcard.card.post;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {
    Toolbar mainToolBar;

    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar();
        firebaseAuth = FirebaseAuth.getInstance();

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
        }

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
        finish();

    }
}
