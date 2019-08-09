package com.adtv.raite;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener
{
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;

    private TextView errorMsg;
    private CheckBox remember, showPass;
    private EditText idInput, passInput;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        errorMsg = (TextView) findViewById(R.id.tv_login_err_msg);
        showPass = (CheckBox) findViewById(R.id.cb_login_show_pass);
        remember = (CheckBox) findViewById(R.id.cb_login_remember);
        idInput = (EditText) findViewById(R.id.et_login_id);
        passInput = (EditText) findViewById(R.id.et_login_pass);

        showPass.setOnCheckedChangeListener(this);

        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();

        if (loginPreferences.getBoolean("remember", false)) {
            idInput.setText(loginPreferences.getString("id", ""));
            passInput.setText(loginPreferences.getString("pass", ""));
            idInput.setSelection(idInput.getText().length());
            remember.setChecked(true);
        }
    }



    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
    {
        if (buttonView.getId() == showPass.getId())
        {
            if (isChecked)
            {
                passInput.setInputType(InputType.TYPE_CLASS_TEXT);
            }
            else
            {
                passInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
            passInput.setSelection(passInput.getText().length());
        }
    }
    //REGEX
    //EMAIL:^[A-Za-z0-9+_.-]+@(.+)$
    //

    public void logIn(View view)//if (id.matches(".*^[a-zA-Z].{2,14}") && !id.matches(".*[\\W_].+") || id.matches("[0-9]{10}"))
    {//((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(idInput.getWindowToken(), 0);

        errorMsg.setText("");
        String id = idInput.getText().toString().trim();
        String pass = passInput.getText().toString().trim();

        if (id.length() == 0 || pass.length() == 0)
        {
            errorMsg.setText("Cannot enter empty fields!");
            return;
        }
        else if (id.matches("[0-9]{10}"))//10 digits number (phone number)
        {
            errorMsg.setText("Invalid phone number input input!");
            return;
        }

        if (remember.isChecked())
        {
            loginPrefsEditor.putBoolean("remember", true);
            loginPrefsEditor.putString("id", id);
            loginPrefsEditor.putString("pass", pass);
            loginPrefsEditor.commit();
        }
        else
        {
            loginPrefsEditor.clear();
            loginPrefsEditor.commit();
        }
        //startActivity(new Intent(this, Main.class));
        finish();//LAUCH NEXT AXTIVITY HERE
    }

    public void register(View view)
    {
        startActivity(new Intent(this, Register.class));
        finish();//
    }
}

/*
//User ID must contain 3 to 15 characters, must start with a letter, and can contain numeric values.
if (id.matches(".*^[a-zA-Z].{2,14}") && !id.matches(".*[\\W_].+") || id.matches("[0-9]{10}"))
 */