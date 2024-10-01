package com.example.mc_ass1;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

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
        // 返回到 EmailCompositionActivity 进行编辑
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
        // 原有的发送邮件代码
    }
}

