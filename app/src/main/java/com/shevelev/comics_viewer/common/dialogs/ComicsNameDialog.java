package com.shevelev.comics_viewer.common.dialogs;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.shevelev.comics_viewer.R;
import com.shevelev.comics_viewer.common.func_interfaces.IActionOneArgs;
import com.shevelev.comics_viewer.common.func_interfaces.IActionZeroArgs;

/**
 * Dialog for name of comics
 */
public class ComicsNameDialog extends CommonDialogBase<ComicsNameDialog.Model, ComicsNameDialog.Model>
{
    private EditText textControl;
    private CheckBox isPrivateComicsControl;

    /**
     * Model for dialog
     */
    public static final class Model
    {
        public String title;
        public boolean isPrivate;

        public Model(String title, boolean isPrivate)
        {
            this.title = title;
            this.isPrivate = isPrivate;
        }
    }

    public ComicsNameDialog(
            Activity parentActivity,
            IActionOneArgs<Model> okAction,
            IActionZeroArgs cancelAction,
            int titleResourceId,
            int dialogLayoutId,
            Model initModel)
    {
        super(parentActivity, okAction, cancelAction, titleResourceId, dialogLayoutId, initModel);
    }

    @Override
    protected void initControls(View view, Model initModel)
    {
        textControl=((EditText)view.findViewById(R.id.et_comics_name));
        textControl.setText(initModel.title);
        textControl.setSelection(initModel.title.length());           // Move cursor to end

        isPrivateComicsControl=((CheckBox)view.findViewById(R.id.isPrivateComics));
        isPrivateComicsControl.setChecked(initModel.isPrivate);

        textControl.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void afterTextChanged(Editable s) { }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after){ }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                okButton.setEnabled(s.length()>0);
            }
        });
    }

    @Override
    protected Model getOutputModel()
    {
        return new Model(textControl.getText().toString(), isPrivateComicsControl.isChecked());
    }
}