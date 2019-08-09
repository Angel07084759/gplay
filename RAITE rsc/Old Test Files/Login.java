package com.adtv.raite;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener
{
    private CheckBox showPass;
    private TextView errorMsg;
    private EditText idInput, passInput;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        showPass = (CheckBox) findViewById(R.id.cb_show_pass);
        errorMsg = (TextView) findViewById(R.id.tv_err_msg);
        idInput = (EditText) findViewById(R.id.et_id);
        passInput = (EditText) findViewById(R.id.et_pass);

        showPass.setOnCheckedChangeListener(this);
    }



    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
    {
        if (buttonView.getId() == showPass.getId())
        {
            if (isChecked)
            {
                ((EditText) findViewById(R.id.et_pass)).setInputType(InputType.TYPE_CLASS_TEXT);
            }
            else
            {
                ((EditText) findViewById(R.id.et_pass)).setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
            ((EditText) findViewById(R.id.et_pass)).setSelection(((EditText) findViewById(R.id.et_pass)).getText().length());
        }
    }
}
