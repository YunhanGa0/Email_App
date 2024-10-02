package com.example.mc_ass1;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import android.content.SharedPreferences;

public class EmailReadingActivity extends AppCompatActivity {

    private TextView textViewFrom, textViewTo, textViewCc, textViewSubject, textViewBody;
    private ImageButton btnEdit, btnSend;
    private LinearLayout layoutAttachment, layoutImage, layoutSchedule;
    private TextView tvAttachmentName, tvImageName, tvScheduleTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_reading);

        // 初始化视图
        initViews();

        // 获取 intent 数据
        Intent intent = getIntent();
        setEmailContent(intent);

        // 设置附加内容预览
        setAttachmentPreview(intent);
        setImagePreview(intent);
        setSchedulePreview(intent);

        // 设置按钮点击监听器
        setButtonListeners();
    }

    private void initViews() {
        textViewFrom = findViewById(R.id.tv_from);
        textViewTo = findViewById(R.id.tv_to);
        textViewCc = findViewById(R.id.tv_cc);
        textViewSubject = findViewById(R.id.tv_subject);
        textViewBody = findViewById(R.id.tv_body);

        btnEdit = findViewById(R.id.btn_edit);
        btnSend = findViewById(R.id.btn_send);

        layoutAttachment = findViewById(R.id.layout_attachment);
        layoutImage = findViewById(R.id.layout_image);
        layoutSchedule = findViewById(R.id.layout_schedule);

        tvAttachmentName = findViewById(R.id.tv_attachment_name);
        tvImageName = findViewById(R.id.tv_image_name);
        tvScheduleTime = findViewById(R.id.tv_schedule_time);
    }

    private void setEmailContent(Intent intent) {
        textViewFrom.setText("From: " + intent.getStringExtra("from"));
        textViewTo.setText("To: " + intent.getStringExtra("to"));
        textViewCc.setText("CC: " + intent.getStringExtra("cc"));
        textViewSubject.setText("Subject: " + intent.getStringExtra("subject"));
        textViewBody.setText("Body: " + intent.getStringExtra("body"));
    }

    private void setAttachmentPreview(Intent intent) {
        String attachmentName = intent.getStringExtra("attachment_name");
        if (attachmentName != null && !attachmentName.isEmpty()) {
            tvAttachmentName.setText(attachmentName);
            layoutAttachment.setVisibility(View.VISIBLE);
        } else {
            layoutAttachment.setVisibility(View.GONE);
        }
    }

    private void setImagePreview(Intent intent) {
        String imageName = intent.getStringExtra("image_name");
        if (imageName != null && !imageName.isEmpty()) {
            tvImageName.setText(imageName);
            layoutImage.setVisibility(View.VISIBLE);
        } else {
            layoutImage.setVisibility(View.GONE);
        }
    }

    private void setSchedulePreview(Intent intent) {
        String scheduleTime = intent.getStringExtra("schedule_time");
        if (scheduleTime != null && !scheduleTime.isEmpty()) {
            tvScheduleTime.setText(scheduleTime);
            layoutSchedule.setVisibility(View.VISIBLE);
        } else {
            layoutSchedule.setVisibility(View.GONE);
        }
    }

    private void setButtonListeners() {
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editEmail();
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();
            }
        });
    }

    private void editEmail() {
        Intent intent = new Intent(EmailReadingActivity.this, EmailCompositionActivity.class);
        intent.putExtra("from", textViewFrom.getText().toString().replace("From: ", ""));
        intent.putExtra("to", textViewTo.getText().toString().replace("To: ", ""));
        intent.putExtra("cc", textViewCc.getText().toString().replace("CC: ", ""));
        intent.putExtra("subject", textViewSubject.getText().toString().replace("Subject: ", ""));
        intent.putExtra("body", textViewBody.getText().toString().replace("Body: ", ""));
        intent.putExtra("isEditing", true);  // 添加一个标志表示这是编辑模式
        startActivityForResult(intent, REQUEST_CODE_EDIT_EMAIL);  // 使用 startActivityForResult
    }

    private static final int REQUEST_CODE_EDIT_EMAIL = 1001;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_EDIT_EMAIL && resultCode == RESULT_OK) {
            // 更新邮件内容
            if (data != null) {
                textViewFrom.setText("From: " + data.getStringExtra("from"));
                textViewTo.setText("To: " + data.getStringExtra("to"));
                textViewCc.setText("CC: " + data.getStringExtra("cc"));
                textViewSubject.setText("Subject: " + data.getStringExtra("subject"));
                textViewBody.setText("Body: " + data.getStringExtra("body"));
            }
        }
    }

    private void sendEmail() {
        SharedPreferences prefs = getSharedPreferences("EmailPrefs", MODE_PRIVATE);
        final String username = prefs.getString("user_email", ""); // get the email saved in login
        final String password = "afig iyuj dfkx vwjd"; // google app password

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username)); // use login email as sender
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(textViewTo.getText().toString().replace("To: ", "")));
            message.setSubject(textViewSubject.getText().toString().replace("Subject: ", ""));
            message.setText(textViewBody.getText().toString().replace("Body: ", ""));

            new AsyncTask<Void, Void, String>() {
                @Override
                protected String doInBackground(Void... params) {
                    try {
                        Transport.send(message);
                        return "Success";
                    } catch (MessagingException e) {
                        e.printStackTrace();
                        return "Error: " + e.getMessage();
                    }
                }

                @Override
                protected void onPostExecute(String result) {
                    if (result.equals("Success")) {
                        Toast.makeText(EmailReadingActivity.this, "Email has send successfully", Toast.LENGTH_SHORT).show();
                        // success alert
                        showSuccessDialog();
                    } else {
                        Toast.makeText(EmailReadingActivity.this, result, Toast.LENGTH_LONG).show();
                    }
                }
            }.execute();

        } catch (MessagingException e) {
            Toast.makeText(EmailReadingActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void showSuccessDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Success")
                .setMessage("Your email has send successfully！")
                .setPositiveButton("Check", null)
                .show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("from", textViewFrom.getText().toString());
        outState.putString("to", textViewTo.getText().toString());
        outState.putString("cc", textViewCc.getText().toString());
        outState.putString("subject", textViewSubject.getText().toString());
        outState.putString("body", textViewBody.getText().toString());
        outState.putString("attachmentName", tvAttachmentName.getText().toString());
        outState.putString("imageName", tvImageName.getText().toString());
        outState.putString("scheduleTime", tvScheduleTime.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            textViewFrom.setText(savedInstanceState.getString("from"));
            textViewTo.setText(savedInstanceState.getString("to"));
            textViewCc.setText(savedInstanceState.getString("cc"));
            textViewSubject.setText(savedInstanceState.getString("subject"));
            textViewBody.setText(savedInstanceState.getString("body"));
            tvAttachmentName.setText(savedInstanceState.getString("attachmentName"));
            tvImageName.setText(savedInstanceState.getString("imageName"));
            tvScheduleTime.setText(savedInstanceState.getString("scheduleTime"));
        }
    }
}

