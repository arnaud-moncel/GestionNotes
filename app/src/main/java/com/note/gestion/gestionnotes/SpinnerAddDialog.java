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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Arnaud Moncel on 09/03/18.
 */

public class SpinnerAddDialog extends DialogFragment {

    private NoticeDialogListener m_Listener;
    private AlertDialog m_dialog;

    private static final String TITLE = "title";
    private static final String MSG1 = "message1";
    private static final String MSG2 = "message2";
    private static final String STRINGS = "strings";

    public interface NoticeDialogListener {
        void onDialogPositiveClick(DialogFragment dialog);
    }

    public static SpinnerAddDialog newInstance(int title, int message1, int message2, ArrayList<String> strings) {
        SpinnerAddDialog dialog = new SpinnerAddDialog();
        Bundle args = new Bundle();
        args.putInt( TITLE, title);
        args.putInt( MSG1, message1 );
        args.putInt( MSG2, message2 );
        args.putStringArrayList( STRINGS, strings );
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onAttach( Activity activity ) {
        super.onAttach( activity );
        m_Listener = (NoticeDialogListener) activity;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder( getActivity() );
        builder.setTitle( getArguments().getInt( TITLE ) );

        View dialogView = getActivity().getLayoutInflater().inflate( R.layout.content_spinner_dialog, null );

        builder.setView( dialogView );

        builder.setPositiveButton( android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick( DialogInterface dialog, int id ) {
                m_Listener.onDialogPositiveClick( SpinnerAddDialog.this );
            }
        });

        builder.setNegativeButton( android.R.string.no, new DialogInterface.OnClickListener() {
            public void onClick( DialogInterface dialog, int which ) {}
        });

        m_dialog = builder.create();
        m_dialog.setOnShowListener( new DialogInterface.OnShowListener() {

            @Override
            public void onShow( final DialogInterface dialog ) {
                ( ( AlertDialog ) dialog ).getButton( AlertDialog.BUTTON_POSITIVE ).setEnabled( false );
                final EditText edt = ( ( AlertDialog ) dialog ).findViewById( R.id.edit_text );
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

        ((TextView) dialogView.findViewById( R.id.text_view ) ).setText( getArguments().getInt( MSG1 ) );
        ((TextView) dialogView.findViewById( R.id.spinner_view ) ).setText( getArguments().getInt( MSG2 ) );

        EditText nEdt = dialogView.findViewById(R.id.edit_text);
        nEdt.addTextChangedListener(new TextWatcher() {

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

        Spinner nSpinner = dialogView.findViewById( R.id.vat_spinner );
        ArrayAdapter<String> adapter = new ArrayAdapter<>( m_dialog.getContext(), android.R.layout.simple_spinner_item, getArguments().getStringArrayList( STRINGS ) );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        nSpinner.setAdapter(adapter);

        return m_dialog;
    }
}
