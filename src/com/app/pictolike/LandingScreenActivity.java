package com.app.pictolike;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class LandingScreenActivity extends AbstractAppActivity {

    /* **************************************************************** */
    /* ******************* AbstractAppActivity ************************ */
    /* **************************************************************** */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar bar = getActionBar();
        bar.hide();

        setContentView(R.layout.activity_landing);

        findViewById(R.id.btn_come_in).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(LandingScreenActivity.this, SignInActivity.class);
                LandingScreenActivity.this.startActivity(i);
            }
        });

        findViewById(R.id.btn_sign_up).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(LandingScreenActivity.this, SignUpActivity.class);
                LandingScreenActivity.this.startActivity(i);
            }
        });
    }
}
