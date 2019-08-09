package com.adtv.raite;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

public class Register extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener
{
    private TextView errorMsg;
    private CheckBox showPass;
    private EditText idInput, passInput;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        errorMsg = (TextView) findViewById(R.id.tv_register_err_msg);
        showPass = (CheckBox) findViewById(R.id.cb_regi_remember);
        idInput = (EditText) findViewById(R.id.et_re_id);
        passInput = (EditText) findViewById(R.id.et_login_pass);

        showPass.setOnCheckedChangeListener(this);

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
}
