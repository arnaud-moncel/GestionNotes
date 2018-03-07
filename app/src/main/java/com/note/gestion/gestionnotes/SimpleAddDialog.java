package com.note.gestion.gestionnotes;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by Arnaud Moncel on 22/02/2018.
 */

public class SimpleAddDialog extends DialogFragment {

    private NoticeDialogListener m_Listener;
    private AlertDialog m_dialog;

    private static final String TITLE = "title";
    private static final String MSG = "message";

    public interface NoticeDialogListener {
        void onDialogPositiveClick(DialogFragment dialog);

        void OnStop();
    }

    public static SimpleAddDialog newInstance(int title, int message ) {
        SimpleAddDialog dialog = new SimpleAddDialog();
        Bundle args = new Bundle();
        args.putInt( TITLE, title);
        args.putInt( MSG, message );
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        m_Listener = (NoticeDialogListener) activity;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder( getActivity() );
        builder.setMessage( getArguments().getInt( MSG ) ).setTitle( getArguments().getInt( TITLE ) );

        View dialogView = getActivity().getLayoutInflater().inflate( R.layout.content_simple_dialog, null );

        builder.setView( dialogView );

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                m_Listener.onDialogPositiveClick( SimpleAddDialog.this );
            }
        });

        builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {}
        });

        m_dialog = builder.create();
        m_dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(final DialogInterface dialog) {
                ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                final EditText edt = ((AlertDialog) dialog).findViewById( R.id.edit_text );
                edt.post(new Runnable() {
                    @Override
                    public void run() {
                        InputMethodManager imm = (InputMethodManager) edt.getContext().getSystemService( ((AlertDialog) dialog).getContext().INPUT_METHOD_SERVICE );
                        imm.showSoftInput( edt, InputMethodManager.SHOW_IMPLICIT );
                        edt.requestFocus();
                    }
                });
            }
        });

        EditText newEdt = dialogView.findViewById(R.id.edit_text);
        newEdt.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (TextUtils.isEmpty( charSequence )) {
                    m_dialog.getButton( AlertDialog.BUTTON_POSITIVE ).setEnabled(false);
                } else {
                    m_dialog.getButton( AlertDialog.BUTTON_POSITIVE ).setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        return m_dialog;
    }
}
