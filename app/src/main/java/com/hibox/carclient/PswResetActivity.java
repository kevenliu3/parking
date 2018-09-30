package com.hibox.carclient;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class PswResetActivity extends Activity {

    private EditText newEt, confirmeEt, oldEt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_psw_reset);

        newEt = (EditText) findViewById(R.id.new_psw_et);
        confirmeEt = (EditText) findViewById(R.id.comfire_new_psw_et);
        oldEt = (EditText) findViewById(R.id.oldPsw_et);

        findViewById(R.id.tv_psd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        findViewById(R.id.submmit_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (oldEt.getText().toString().trim().length() == 0) {
                    Toast.makeText(PswResetActivity.this, "旧密码不能为空", Toast.LENGTH_LONG).show();
                    return;
                }

                if (oldEt.getText().toString().trim().length() > 0 && !oldEt.getText().toString().trim().equals("123456")) {
                    Toast.makeText(PswResetActivity.this, "旧密码不正确", Toast.LENGTH_LONG).show();
                    return;
                }

                if (newEt.getText().toString().trim().length() > 0 && newEt.getText().toString().trim().equals(confirmeEt.getText().toString().trim())) {
                    Toast.makeText(PswResetActivity.this, "密码修改成功", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(PswResetActivity.this, "密码修改失败，请确认新密码一致且不为空。", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
