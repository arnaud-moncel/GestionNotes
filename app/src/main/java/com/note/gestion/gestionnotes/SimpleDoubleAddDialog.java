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
import android.widget.TextView;

/**
 * Created by Arnaud Moncel on 22/02/2018.
 */

public class SimpleDoubleAddDialog extends DialogFragment {

    private NoticeDialogListener m_Listener;
    private AlertDialog m_dialog;

    private Boolean m_edtdNotEmpty = false;
    private Boolean m_edtqNotEmpty = false;

    private static final String TITLE = "title";
    private static final String MSG = "message";
    private static final String MSG2 = "message2";

    public interface NoticeDialogListener {
        void onDialogPositiveClick(DialogFragment dialog);
    }

    public static SimpleDoubleAddDialog newInstance(int title, int message, int message2 ) {
        SimpleDoubleAddDialog dialog = new SimpleDoubleAddDialog();
        Bundle args = new Bundle();
        args.putInt( TITLE, title);
        args.putInt( MSG, message );
        args.putInt( MSG2, message2 );
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
        builder.setTitle( getArguments().getInt( TITLE ) );

        View dialogView = getActivity().getLayoutInflater().inflate( R.layout.content_simple_double_dialog, null );

        builder.setView( dialogView );

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                m_Listener.onDialogPositiveClick( SimpleDoubleAddDialog.this );
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
                final EditText edt = ((AlertDialog) dialog).findViewById( R.id.edit_price );
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

        ((TextView) dialogView.findViewById( R.id.text_price )).setText( getArguments().getInt( MSG ) );
        ((TextView) dialogView.findViewById( R.id.text_qty )).setText( getArguments().getInt( MSG2 ) );

        EditText nEdtd = dialogView.findViewById(R.id.edit_price);
        nEdtd.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if (TextUtils.isEmpty( s )) {
                    m_edtdNotEmpty = false;
                    m_dialog.getButton( AlertDialog.BUTTON_POSITIVE ).setEnabled(false);
                } else {
                    m_edtdNotEmpty = true;
                    if( m_edtqNotEmpty ) {
                        m_dialog.getButton( AlertDialog.BUTTON_POSITIVE ).setEnabled(true);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        EditText nEdtq = dialogView.findViewById(R.id.edit_qty);
        nEdtq.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty( s )) {
                    m_edtqNotEmpty = false;
                    m_dialog.getButton( AlertDialog.BUTTON_POSITIVE ).setEnabled(false);
                } else {
                    m_edtqNotEmpty = true;
                    if( m_edtdNotEmpty ) {
                        m_dialog.getButton( AlertDialog.BUTTON_POSITIVE ).setEnabled(true);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return m_dialog;
    }
}
