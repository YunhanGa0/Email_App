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
import androidx.appcompat.app.AppCompatActivity;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

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
        startActivity(intent);
        finish(); // 结束当前的 EmailReadingActivity
    }

    private void sendEmail() {
        String from = textViewFrom.getText().toString().replace("From: ", "");
        String to = textViewTo.getText().toString().replace("To: ", "");
        String cc = textViewCc.getText().toString().replace("CC: ", "");
        String subject = textViewSubject.getText().toString().replace("Subject: ", "");
        String body = textViewBody.getText().toString().replace("Body: ", "");

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // 只使用能够发送邮件的应用
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{to}); // 收件人
        intent.putExtra(Intent.EXTRA_CC, new String[]{cc}); // 抄送
        intent.putExtra(Intent.EXTRA_SUBJECT, subject); // 主题
        intent.putExtra(Intent.EXTRA_TEXT, body); // 正文

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(Intent.createChooser(intent, "选择邮件客户端:"));
        } else {
            Toast.makeText(this, "没有安装邮件客户端。", Toast.LENGTH_SHORT).show();
        }
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

