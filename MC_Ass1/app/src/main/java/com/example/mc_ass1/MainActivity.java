package com.example.mc_ass1;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;

import java.util.ArrayList;
import java.util.List;

import android.content.SharedPreferences;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

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

        // 检查用户是否已登录
        SharedPreferences prefs = getSharedPreferences("EmailPrefs", MODE_PRIVATE);
        String userEmail = prefs.getString("user_email", null);

        if (userEmail == null) {
            // 用户未登录，跳转到登录页面
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

        // 初始化 DrawerLayout
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        // 设置用户邮箱
        View headerView = navigationView.getHeaderView(0);
        TextView textViewUserEmail = headerView.findViewById(R.id.textViewUserEmail);
        textViewUserEmail.setText(userEmail);

        // 打印日志，检查邮箱是否被正确设置
        Log.d("MainActivity", "Setting user email: " + userEmail);

        // 设置菜单按钮
        ImageButton btnMenu = findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        // 设置导航抽屉项目点击监听器
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
                // 退出登录
                SharedPreferences.Editor editor = prefs.edit();
                editor.remove("user_email");
                editor.apply();

                // 跳转到登录页面
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                return true;
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

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

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}

