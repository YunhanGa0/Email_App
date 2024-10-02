package com.example.mc_ass1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import java.util.ArrayList;
import java.util.List;

import android.content.SharedPreferences;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

/**
 * MainActivity.java
 *
 * This is the main activity class for the email application. It is responsible for
 * displaying the email list, handling user login status, implementing search
 * functionality, and managing the navigation drawer.
 *
 * Features:
 * - Display email list
 * - User login status check
 * - Email search functionality
 * - Navigation drawer (includes settings, sent items, starred emails, trash, and logout options)
 * - Compose new email
 *
 * @author Yunhan Gao
 * @version 1.0
 * @since 2024-10-02
 */

public class MainActivity extends AppCompatActivity {

    private SearchView searchViewEmails;
    private ImageButton fabComposeEmail;
    private RecyclerView recyclerView;
    private EmailAdapter emailAdapter;
    private List<Email> emailList;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if user is login
        SharedPreferences prefs = getSharedPreferences("EmailPrefs", MODE_PRIVATE);
        String userEmail = prefs.getString("user_email", null);

        if (userEmail == null) {
            // If user is not login, go to login page
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

        // Initialize DrawerLayout
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        // Set user email
        View headerView = navigationView.getHeaderView(0);
        TextView textViewUserEmail = headerView.findViewById(R.id.textViewUserEmail);
        textViewUserEmail.setText(userEmail);

        // Check if correct
        Log.d("MainActivity", "Setting user email: " + userEmail);

        // Set menu button
        ImageButton btnMenu = findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        // set menu listener
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_settings) {
                Toast.makeText(this, "设置", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.nav_sent) {
                Toast.makeText(this, "已发送", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.nav_starred) {
                Toast.makeText(this, "星标邮件", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.nav_trash) {
                Toast.makeText(this, "垃圾箱", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.nav_logout) {
                // logout
                SharedPreferences.Editor editor = prefs.edit();
                editor.remove("user_email");
                editor.apply();

                // Go to email page
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                return true;
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        // Set search window
        searchViewEmails = findViewById(R.id.searchViewEmails);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerViewEmails);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Generate fake email data
        emailList = new ArrayList<>();
        emailList.add(new Email(R.drawable.avatar, "Bob", "Project Update", "Yesterday"));
        emailList.add(new Email(R.drawable.avatar, "Paul", "Budget Analysis for Q3", "4 days ago"));
        emailList.add(new Email(R.drawable.avatar, "Quincy", "New Policy Updates", "Last month"));
        emailList.add(new Email(R.drawable.avatar, "Frank", "Quarterly Report - Please Review", "10:15 AM"));
        emailList.add(new Email(R.drawable.avatar, "Alice", "Meeting tomorrow?", "09:30 AM"));
        emailList.add(new Email(R.drawable.avatar, "Henry", "Important: Server Downtime Notice", "3 days ago"));
        emailList.add(new Email(R.drawable.avatar, "Tina", "Reminder: Subscription Renewal", "Last Thursday"));
        emailList.add(new Email(R.drawable.avatar, "Karen", "New Marketing Strategy Proposal", "09:00 AM"));
        emailList.add(new Email(R.drawable.avatar, "Liam", "Follow-up: Client Meeting", "Monday"));
        emailList.add(new Email(R.drawable.avatar, "Rachel", "Happy Birthday!", "Today"));
        emailList.add(new Email(R.drawable.avatar, "Steve", "Meeting Agenda for Tomorrow", "Friday"));
        emailList.add(new Email(R.drawable.avatar, "Carol", "Invoice attached", "2 days ago"));
        emailList.add(new Email(R.drawable.avatar, "David", "Trip plans", "Last week"));
        emailList.add(new Email(R.drawable.avatar, "Eva", "Job offer", "Last month"));
        emailList.add(new Email(R.drawable.avatar, "Mia", "Conference Call Recap", "Saturday"));
        emailList.add(new Email(R.drawable.avatar, "Ivy", "Invitation to Webinar", "Last week"));
        emailList.add(new Email(R.drawable.avatar, "Jack", "Vacation Request Approval", "Last month"));
        emailList.add(new Email(R.drawable.avatar, "Noah", "Updated Design Mockups", "2 weeks ago"));
        emailList.add(new Email(R.drawable.avatar, "Grace", "Lunch Plans for Tomorrow?", "Yesterday"));
        emailList.add(new Email(R.drawable.avatar, "Olivia", "Your Order has Shipped", "Yesterday"));

        // Set the adapter
        emailAdapter = new EmailAdapter(emailList);
        recyclerView.setAdapter(emailAdapter);

        // Write email button
        fabComposeEmail = findViewById(R.id.fabComposeEmail);
        fabComposeEmail.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(MainActivity.this, EmailCompositionActivity.class);
                startActivity(intent);
            } catch (Exception e) {
                Log.e("MainActivity", "Error starting EmailCompositionActivity", e);
                Toast.makeText(this, "Unable to compose email. Please try again.", Toast.LENGTH_LONG).show();
            }
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

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}

