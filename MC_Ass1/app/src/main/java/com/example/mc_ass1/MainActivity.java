package com.example.mc_ass1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SearchView searchViewEmails;
    private ImageButton fabComposeEmail;
    private RecyclerView recyclerView;
    private EmailAdapter emailAdapter;
    private List<Email> emailList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 设置搜索栏
        searchViewEmails = findViewById(R.id.searchViewEmails);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerViewEmails);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Generate fake email data
        emailList = new ArrayList<>();
        emailList.add(new Email(R.drawable.img_1, "Bob", "Project Update", "Yesterday"));
        emailList.add(new Email(R.drawable.img_1, "Paul", "Budget Analysis for Q3", "4 days ago"));
        emailList.add(new Email(R.drawable.img_1, "Quincy", "New Policy Updates", "Last month"));
        emailList.add(new Email(R.drawable.img_1, "Frank", "Quarterly Report - Please Review", "10:15 AM"));
        emailList.add(new Email(R.drawable.img_1, "Alice", "Meeting tomorrow?", "09:30 AM"));
        emailList.add(new Email(R.drawable.img_1, "Henry", "Important: Server Downtime Notice", "3 days ago"));
        emailList.add(new Email(R.drawable.img_1, "Tina", "Reminder: Subscription Renewal", "Last Thursday"));
        emailList.add(new Email(R.drawable.img_1, "Karen", "New Marketing Strategy Proposal", "09:00 AM"));
        emailList.add(new Email(R.drawable.img_1, "Liam", "Follow-up: Client Meeting", "Monday"));
        emailList.add(new Email(R.drawable.img_1, "Rachel", "Happy Birthday!", "Today"));
        emailList.add(new Email(R.drawable.img_1, "Steve", "Meeting Agenda for Tomorrow", "Friday"));
        emailList.add(new Email(R.drawable.img_1, "Carol", "Invoice attached", "2 days ago"));
        emailList.add(new Email(R.drawable.img_1, "David", "Trip plans", "Last week"));
        emailList.add(new Email(R.drawable.img_1, "Eva", "Job offer", "Last month"));
        emailList.add(new Email(R.drawable.img_1, "Mia", "Conference Call Recap", "Saturday"));
        emailList.add(new Email(R.drawable.img_1, "Ivy", "Invitation to Webinar", "Last week"));
        emailList.add(new Email(R.drawable.img_1, "Jack", "Vacation Request Approval", "Last month"));
        emailList.add(new Email(R.drawable.img_1, "Noah", "Updated Design Mockups", "2 weeks ago"));
        emailList.add(new Email(R.drawable.img_1, "Grace", "Lunch Plans for Tomorrow?", "Yesterday"));
        emailList.add(new Email(R.drawable.img_1, "Olivia", "Your Order has Shipped", "Yesterday"));



        // Set the adapter
        emailAdapter = new EmailAdapter(emailList);
        recyclerView.setAdapter(emailAdapter);

        // 设置写邮件按钮
        fabComposeEmail = findViewById(R.id.fabComposeEmail);
        fabComposeEmail.setOnClickListener(v -> {
            // 跳转到写邮件界面
            Intent intent = new Intent(MainActivity.this, EmailCompositionActivity.class);
            startActivity(intent);
        });

        // Set up SearchView listener for filtering emails by sender
        searchViewEmails.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                emailAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                emailAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }
}

