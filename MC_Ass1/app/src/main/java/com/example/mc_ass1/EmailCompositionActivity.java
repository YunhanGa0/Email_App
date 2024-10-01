package com.example.mc_ass1;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class EmailCompositionActivity extends AppCompatActivity {

    private EditText etFrom, etTo, etCc, etBcc, etSubject, etBody;
    private Button btnClear, btnPreview;
    private ImageButton btnInsertImage, btnAttachFile, btnSchedule;
    private ImageButton btnExpand;
    private LinearLayout layoutCcBcc;
    private boolean isCcBccVisible = false;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PICK_FILE_REQUEST = 2;
    private Uri selectedImageUri;
    private Uri selectedFileUri;
    private long scheduledTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_composition);

        initializeViews();
        autoFillFromField();
        setButtonListeners();
        restoreSavedInstanceState(savedInstanceState);
    }

    private void initializeViews() {
        etFrom = findViewById(R.id.et_from);
        etTo = findViewById(R.id.et_to);
        etCc = findViewById(R.id.et_cc);
        etBcc = findViewById(R.id.et_bcc);
        etSubject = findViewById(R.id.et_subject);
        etBody = findViewById(R.id.et_body);
        btnClear = findViewById(R.id.btn_clear);
        btnPreview = findViewById(R.id.btn_preview);
        btnExpand = findViewById(R.id.btn_expand);
        layoutCcBcc = findViewById(R.id.layout_cc_bcc);
        btnInsertImage = findViewById(R.id.btn_insert_image);
        btnAttachFile = findViewById(R.id.btn_attach_file);
        btnSchedule = findViewById(R.id.btn_schedule);
    }

    private void autoFillFromField() {
        SharedPreferences prefs = getSharedPreferences("EmailPrefs", MODE_PRIVATE);
        String userEmail = prefs.getString("user_email", "");
        etFrom.setText(userEmail);
    }

    private void setButtonListeners() {
        btnClear.setOnClickListener(v -> clearFields());
        btnPreview.setOnClickListener(v -> previewEmail());
        btnExpand.setOnClickListener(v -> toggleCcBccVisibility());
        btnInsertImage.setOnClickListener(v -> openImagePicker());
        btnAttachFile.setOnClickListener(v -> openFilePicker());
        btnSchedule.setOnClickListener(v -> showDateTimePicker());
    }

    private void clearFields() {
        etFrom.setText("");
        etTo.setText("");
        etCc.setText("");
        etBcc.setText("");
        etSubject.setText("");
        etBody.setText("");
        selectedImageUri = null;
        selectedFileUri = null;
        scheduledTime = 0;
    }

    private void previewEmail() {
        Intent intent = new Intent(EmailCompositionActivity.this, EmailReadingActivity.class);
        intent.putExtra("from", etFrom.getText().toString());
        intent.putExtra("to", etTo.getText().toString());
        intent.putExtra("cc", etCc.getText().toString());
        intent.putExtra("subject", etSubject.getText().toString());
        intent.putExtra("body", etBody.getText().toString());
        if (selectedImageUri != null) {
            intent.putExtra("image", selectedImageUri.toString());
        }
        if (selectedFileUri != null) {
            intent.putExtra("attachment", selectedFileUri.toString());
        }
        startActivity(intent);
    }

    private void toggleCcBccVisibility() {
        if (isCcBccVisible) {
            layoutCcBcc.setVisibility(View.GONE);
            btnExpand.setImageResource(android.R.drawable.arrow_down_float);
        } else {
            layoutCcBcc.setVisibility(View.VISIBLE);
            btnExpand.setImageResource(android.R.drawable.arrow_up_float);
        }
        isCcBccVisible = !isCcBccVisible;
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, PICK_FILE_REQUEST);
    }

    private void showDateTimePicker() {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    showTimePicker(calendar);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void showTimePicker(final Calendar calendar) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (view, hourOfDay, minute) -> {
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendar.set(Calendar.MINUTE, minute);
                    scheduledTime = calendar.getTimeInMillis();
                    Toast.makeText(this, "Email will be sent at " + calendar.getTime(), Toast.LENGTH_LONG).show();
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
        );
        timePickerDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE_REQUEST && data != null) {
                selectedImageUri = data.getData();
                Toast.makeText(this, "Image selected", Toast.LENGTH_SHORT).show();
            } else if (requestCode == PICK_FILE_REQUEST && data != null) {
                selectedFileUri = data.getData();
                Toast.makeText(this, "File attached", Toast.LENGTH_SHORT).show();
            }
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
        if (selectedImageUri != null) {
            outState.putString("image", selectedImageUri.toString());
        }
        if (selectedFileUri != null) {
            outState.putString("attachment", selectedFileUri.toString());
        }
        outState.putLong("scheduledTime", scheduledTime);
    }

    private void restoreSavedInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            etFrom.setText(savedInstanceState.getString("from"));
            etTo.setText(savedInstanceState.getString("to"));
            etCc.setText(savedInstanceState.getString("cc"));
            etBcc.setText(savedInstanceState.getString("bcc"));
            etSubject.setText(savedInstanceState.getString("subject"));
            etBody.setText(savedInstanceState.getString("body"));
            String imageUriString = savedInstanceState.getString("image");
            if (imageUriString != null) {
                selectedImageUri = Uri.parse(imageUriString);
            }
            String attachmentUriString = savedInstanceState.getString("attachment");
            if (attachmentUriString != null) {
                selectedFileUri = Uri.parse(attachmentUriString);
            }
            scheduledTime = savedInstanceState.getLong("scheduledTime");
        }
    }
}