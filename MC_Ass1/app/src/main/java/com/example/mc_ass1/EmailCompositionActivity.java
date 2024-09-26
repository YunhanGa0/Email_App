package com.example.mc_ass1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class EmailCompositionActivity extends AppCompatActivity {

    private EditText etFrom, etTo, etCc, etBcc, etSubject, etBody;
    private Button btnClear, btnPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_composition);

        // Initialize views
        etFrom = findViewById(R.id.et_from);
        etTo = findViewById(R.id.et_to);
        etCc = findViewById(R.id.et_cc);
        etBcc = findViewById(R.id.et_bcc);
        etSubject = findViewById(R.id.et_subject);
        etBody = findViewById(R.id.et_body);
        btnClear = findViewById(R.id.btn_clear);
        btnPreview = findViewById(R.id.btn_preview);

        // Clear
        btnClear.setOnClickListener(v -> {
            etFrom.setText("");
            etTo.setText("");
            etCc.setText("");
            etBcc.setText("");
            etSubject.setText("");
            etBody.setText("");
        });

        // Preview Button
        btnPreview.setOnClickListener(v -> {
            Intent intent = new Intent(EmailCompositionActivity.this, EmailReadingActivity.class);
            intent.putExtra("from", etFrom.getText().toString());
            intent.putExtra("to", etTo.getText().toString());
            intent.putExtra("cc", etCc.getText().toString());
            intent.putExtra("subject", etSubject.getText().toString());
            intent.putExtra("body", etBody.getText().toString());
            startActivity(intent);
        });

        // Preserve data across configuration changes
        if (savedInstanceState != null) {
            etFrom.setText(savedInstanceState.getString("from"));
            etTo.setText(savedInstanceState.getString("to"));
            etCc.setText(savedInstanceState.getString("cc"));
            etBcc.setText(savedInstanceState.getString("bcc"));
            etSubject.setText(savedInstanceState.getString("subject"));
            etBody.setText(savedInstanceState.getString("body"));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("from", etFrom.getText().toString());
        outState.putString("to", etTo.getText().toString());
        outState.putString("cc", etCc.getText().toString());
        outState.putString("bcc", etBcc.getText().toString());
        outState.putString("subject", etSubject.getText().toString());
        outState.putString("body", etBody.getText().toString());
    }
}
