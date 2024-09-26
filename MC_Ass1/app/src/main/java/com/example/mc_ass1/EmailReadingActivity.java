package com.example.mc_ass1;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class EmailReadingActivity extends AppCompatActivity {

    private TextView textViewFrom, textViewTo, textViewCc, textViewSubject, textViewBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_reading);

        textViewFrom = findViewById(R.id.tv_from);
        textViewTo = findViewById(R.id.tv_to);
        textViewCc = findViewById(R.id.tv_cc);
        textViewSubject = findViewById(R.id.tv_subject);
        textViewBody = findViewById(R.id.tv_body);

        // Get intent data
        Intent intent = getIntent();
        textViewFrom.setText("From: " + intent.getStringExtra("from"));
        textViewTo.setText("To: " + intent.getStringExtra("to"));
        textViewCc.setText("CC: " + intent.getStringExtra("cc"));
        textViewSubject.setText("Subject: " + intent.getStringExtra("subject"));
        textViewBody.setText("Body: " + intent.getStringExtra("body"));

        Button buttonEdit = findViewById(R.id.btn_edit);
        Button buttonSend = findViewById(R.id.btn_send);

        // Edit Button Click Listener
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editIntent = new Intent(EmailReadingActivity.this, EmailCompositionActivity.class);
                // Pass the data back to the composition activity
                editIntent.putExtra("from", intent.getStringExtra("from"));
                editIntent.putExtra("to", intent.getStringExtra("to"));
                editIntent.putExtra("cc", intent.getStringExtra("cc"));
                editIntent.putExtra("subject", intent.getStringExtra("subject"));
                editIntent.putExtra("body", intent.getStringExtra("body"));
                startActivity(editIntent);
            }
        });

        // Send Button Click Listener (You can implement actual email sending logic here)
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Here, implement the actual email sending logic.
                // This could be using an email sending API or intent.
                // For now, we'll just simulate the action with a Toast.

                // Example of sending email via an intent
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("message/rfc822");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{intent.getStringExtra("to")});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, intent.getStringExtra("subject"));
                emailIntent.putExtra(Intent.EXTRA_TEXT, intent.getStringExtra("body"));

                try {
                    startActivity(Intent.createChooser(emailIntent, "Send email..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    // Handle no email client case
                }
            }
        });
    }
}

