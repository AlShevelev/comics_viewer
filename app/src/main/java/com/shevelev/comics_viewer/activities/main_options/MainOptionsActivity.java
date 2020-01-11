package com.shevelev.comics_viewer.activities.main_options;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;

import com.shevelev.comics_viewer.R;
import com.shevelev.comics_viewer.activities.ActivityCodes;
import com.shevelev.comics_viewer.activities.ActivityResultCodes;
import com.shevelev.comics_viewer.common.dialogs.CreatePasswordDialog;
import com.shevelev.comics_viewer.common.dialogs.EnterPasswordDialog;
import com.shevelev.comics_viewer.common.helpers.ToastsHelper;
import com.shevelev.comics_viewer.dal.dto.Option;
import com.shevelev.comics_viewer.options.OptionsFacade;
import com.shevelev.comics_viewer.options.OptionsKeys;
import com.shevelev.comics_viewer.options.OptionsValues;

public class MainOptionsActivity extends Activity
{
    private LinearLayout createPasswordControl;
    private LinearLayout enterPasswordControl;
    private LinearLayout changePasswordControl;

    private boolean passwordEnteredOnStart;

    /**
     * Start activity
     */
    public static void start(Activity parentActivity)
    {
        Intent intent = new Intent(parentActivity, MainOptionsActivity.class);
        parentActivity.startActivityForResult(intent, ActivityCodes.MAIN_OPTIONS);
    }

    /**
     * Parse result of activity
     */
    public static boolean parseResult(Intent data)
    {
        return data.getBooleanExtra(ActivityResultCodes.IS_PASSWORD_ENTERED, false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_options);

        createPasswordControl = (LinearLayout)findViewById(R.id.createPassword);
        enterPasswordControl = (LinearLayout)findViewById(R.id.enterPassword);
        changePasswordControl = (LinearLayout)findViewById(R.id.changePassword);

        createPasswordControl.setOnClickListener(v -> { onCreatePasswordClick(); });
        enterPasswordControl.setOnClickListener(v -> {
            onEnterPasswordClick();
        });
        changePasswordControl.setOnClickListener(v -> {
            onChangePasswordClick();
        });

        setPasswordControlsVisibility();

        passwordEnteredOnStart = OptionsFacade.ShortLivings.get(OptionsKeys.PasswordEntered)!=null;
    }

    private void setPasswordControlsVisibility()
    {
        createPasswordControl.setVisibility(View.GONE);
        enterPasswordControl.setVisibility(View.GONE);
        changePasswordControl.setVisibility(View.GONE);

        if(OptionsFacade.LongLivings.get(OptionsKeys.Password)!=null)
        {
            if(OptionsFacade.ShortLivings.get(OptionsKeys.PasswordEntered)!=null)
                changePasswordControl.setVisibility(View.VISIBLE);
            else
                enterPasswordControl.setVisibility(View.VISIBLE);
        }
        else
            createPasswordControl.setVisibility(View.VISIBLE);
    }

    private void onCreatePasswordClick()
    {
        CreatePasswordDialog dialog=new CreatePasswordDialog(
            this,
            (result)->
            {
                OptionsFacade.LongLivings.addOrUpdate(new Option[]{new Option(OptionsKeys.Password, result.password), new Option(OptionsKeys.PasswordsHint, result.hint)});
                OptionsFacade.ShortLivings.addOrUpdate(new Option[]{new Option(OptionsKeys.PasswordEntered, OptionsValues.True)});

                setPasswordControlsVisibility();
            },
            ()-> { });

        dialog.show();
    }

    private void onEnterPasswordClick()
    {
        String password = OptionsFacade.LongLivings.get(OptionsKeys.Password);
        String hint = OptionsFacade.LongLivings.get(OptionsKeys.PasswordsHint);

        EnterPasswordDialog dialog=new EnterPasswordDialog(
            this,
            (result)->
            {
                if(result.equals(password))
                {
                    OptionsFacade.ShortLivings.addOrUpdate(new Option[]{new Option(OptionsKeys.PasswordEntered, OptionsValues.True)});
                    setPasswordControlsVisibility();
                }
                else
                    ToastsHelper.Show(R.string.message_invalid_password, ToastsHelper.Position.Center);
            },
            ()->
            { }, hint);

        dialog.show();
    }

    private void onChangePasswordClick()
    {
        String password = OptionsFacade.LongLivings.get(OptionsKeys.Password);

        CreatePasswordDialog dialog=new CreatePasswordDialog(
            this,
            (result)->
            {
                OptionsFacade.LongLivings.addOrUpdate(new Option[]{new Option(OptionsKeys.Password, result.password), new Option(OptionsKeys.PasswordsHint, result.hint)});
            },
            ()-> { }, R.string.dialog_change_password_title, R.layout.dialog_create_password, password);

        dialog.show();
    }

    @Override
    public void onBackPressed()
    {
        boolean passwordEnteredOnFinish = OptionsFacade.ShortLivings.get(OptionsKeys.PasswordEntered)!=null;

        Intent intent = new Intent();
        intent.putExtra(ActivityResultCodes.IS_PASSWORD_ENTERED, !passwordEnteredOnStart && passwordEnteredOnFinish);         // User enters or creates password
        setResult(RESULT_OK, intent);

        super.onBackPressed();              // call finish() and close acivity
    }
}