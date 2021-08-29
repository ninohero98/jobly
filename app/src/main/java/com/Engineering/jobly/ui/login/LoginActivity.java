package com.Engineering.jobly.ui.login;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.constraintlayout.motion.utils.ViewState;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.Engineering.jobly.MainFrame;
import com.Engineering.jobly.R;
import com.Engineering.jobly.ui.login.LoginViewModel;
import com.Engineering.jobly.ui.login.LoginViewModelFactory;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.Random;

import static com.Engineering.jobly.BuildConfig.APPLICATION_ID;
import static com.Engineering.jobly.BuildConfig.VERSION_NAME;


public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    FirebaseAuth mAuth;

    DatabaseReference mdatabase;
    FirebaseUser mUser;
    EditText Email;
    EditText Password;
    EditText Name;
    EditText Location;
    EditText Phone;
    EditText PasswordVrify;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mdatabase = FirebaseDatabase.getInstance().getReference().child("Users");


        setContentView(R.layout.activity_login);
        loginViewModel = ViewModelProviders.of(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        Email = findViewById(R.id.username);
        Password = findViewById(R.id.password);
        Location = findViewById(R.id.LocationIN);
        Phone = findViewById(R.id.PhoneIN);
        PasswordVrify = findViewById(R.id.passwordverify);
        final Button loginButton = findViewById(R.id.login);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);
        Name =  findViewById(R.id.Name);
        final RadioGroup mRadioGroup = findViewById(R.id.RadioGroup);
        LoadData();
        loginButton.setText("Login In");
        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                if (loginFormState.getUsernameError() != null) {
                    Email.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    Password.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                }
                setResult(Activity.RESULT_OK);

                //Complete and destroy login activity once successful
                finish();
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(Email.getText().toString(),
                        Password.getText().toString());
            }
        };
        Email.addTextChangedListener(afterTextChangedListener);
        Password.addTextChangedListener(afterTextChangedListener);
        Password.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(Email.getText().toString(),
                            Password.getText().toString());
                }
                return false;
            }
        });

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.SignInRadio) {
                    //some code
                    Name.setVisibility(View.GONE);
                    Phone.setVisibility(View.GONE);
                    Location.setVisibility(View.GONE);
                    PasswordVrify.setVisibility(View.GONE);
                    loginButton.setText("Login In");
                } else if (checkedId == R.id.SignUpRadio) {
                    Name.setVisibility(View.VISIBLE);
                    Phone.setVisibility(View.VISIBLE);
                    Location.setVisibility(View.VISIBLE);
                    PasswordVrify.setVisibility(View.VISIBLE);
                    loginButton.setText("Sign Up");
                }

            }

        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // loadingProgressBar.setVisibility(View.VISIBLE);

                if(loginButton.getText() =="Login In") {
                    Email.setEnabled(false);
                    Password.setEnabled(false);
                    Name.setEnabled(false);
                    Phone.setEnabled(false);
                    Location.setEnabled(false);
                    PasswordVrify.setEnabled(false);
                    userSign();
                }
                else if(loginButton.getText() =="Sign Up"){
                    Email.setEnabled(false);
                    Password.setEnabled(false);
                    Name.setEnabled(false);
                    Phone.setEnabled(false);
                    Location.setEnabled(false);
                    PasswordVrify.setEnabled(false);

                    if(PasswordVrify.getText().toString().equals(Password.getText().toString())) {
                        UserRegister();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Password Doesn't Match", Toast.LENGTH_SHORT).show();
                        Email.setEnabled(true);
                        Password.setEnabled(true);
                        Name.setEnabled(true);
                        Phone.setEnabled(true);
                        Location.setEnabled(true);
                        PasswordVrify.setEnabled(true);
                    }
                }

                //Login To Firebase


            }
        });
    }

    private void LoadData() {
        String fileNameString = "SaveData.save";
        SharedPreferences sharedPreferences;
        sharedPreferences = getSharedPreferences(fileNameString, MODE_PRIVATE);
        String E = sharedPreferences.getString("Email","N/A");
        String P = sharedPreferences.getString("Password","N/A");
        if(!(E.equals(P)) && E !="N/A" && E !=""){
            Email.setText(E);
            Password.setText(P);
            userSign();
        }

    }

    private void SaveData() {
        SharedPreferences sharedPreferences = getSharedPreferences("SaveData.save", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Email", Email.getText().toString());
        editor.putString("Password", Password.getText().toString());
        editor.commit();
    }

    @Override
    public void onBackPressed() {
        super.finish();
    }

    private void userSign() {
        ProgressDialog dialog = new ProgressDialog(this);
        String email = Email.getText().toString().trim();
        String password = Password.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Enter the correct Email", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Enter the correct password", Toast.LENGTH_SHORT).show();
            return;
        }
        dialog.setMessage("Loging in, Please Wait...");
        dialog.setIndeterminate(true);
        dialog.show();
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    dialog.dismiss();

                    (Toast.makeText(LoginActivity.this, "Login not Successfull", Toast.LENGTH_SHORT)).show();
                    Password.setEnabled(true);
                    Email.setEnabled(true);
                    Name.setEnabled(true);
                } else {
                    dialog.dismiss();

                    checkIfEmailVerified();

                }
            }
        });

    }
    //This function helps in verifying whether the email is verified or not.
    private void checkIfEmailVerified(){
        FirebaseUser users=FirebaseAuth.getInstance().getCurrentUser();
        boolean emailVerified=users.isEmailVerified();
        if(!emailVerified){
            Toast.makeText(this,"Verify the Email Id",Toast.LENGTH_SHORT).show();
            mAuth.signOut();
            Email.setEnabled(true);
            Password.setEnabled(true);
            Name.setEnabled(true);
            Phone.setEnabled(true);
            Location.setEnabled(true);
        }
        else {
            //Login Into Account...
            SaveData();
            startActivity(new Intent(this, MainFrame.class));
            finish();
        }
    }



    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }



    private void UserRegister() {
        ProgressDialog mDialog = new ProgressDialog(this);
        String name = Name.getText().toString().trim();
        String email = Email.getText().toString().trim();
        String password = Password.getText().toString().trim();

        if (TextUtils.isEmpty(name)){
            Toast.makeText(this, "Enter Name", Toast.LENGTH_SHORT).show();
            return;
        }else if (TextUtils.isEmpty(email)){
            Toast.makeText(this, "Enter Email", Toast.LENGTH_SHORT).show();
            return;
        }else if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show();
            return;
        }else if (Password.length()<6){
            Toast.makeText(this,"Password must be greater then 6 digit",Toast.LENGTH_SHORT).show();
            return;
        }
        mDialog.setMessage("Creating User, Please Wait...");
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    sendEmailVerification();
                    mDialog.dismiss();
                    OnAuth(task.getResult().getUser());
                    Email.setEnabled(true);
                    Password.setEnabled(true);
                    Name.setEnabled(true);
                    Phone.setEnabled(true);
                    Location.setEnabled(true);

                }else{
                    Toast.makeText(LoginActivity.this,"Error on creating user",Toast.LENGTH_SHORT).show();
                    Email.setEnabled(true);
                    Password.setEnabled(true);
                    Name.setEnabled(true);
                    Phone.setEnabled(true);
                    Location.setEnabled(true);
                    mDialog.dismiss();
                }
            }
        });
    }
    //Email verification code using FirebaseUser object and using isSucccessful()function.
    private void sendEmailVerification() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user!=null){
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(LoginActivity.this,"Check your Email for verification",Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                    }
                }
            });
        }
    }

    private void OnAuth(FirebaseUser user) {
        createAnewUser(user.getUid());
    }

    private void createAnewUser(String uid) {
        User user = BuildNewuser(uid);
        mdatabase.child(uid).setValue(user);
    }


    private User BuildNewuser(String uid){
        return new User(
                Name.getText().toString().trim(),
                Email.getText().toString().trim(), Phone.getText().toString().trim(),Location.getText().toString().trim(), "@" + Name.getText().toString().split(" ")[0]+ ((new Random()).nextInt(99999999))
        ,uid);
    }
}


