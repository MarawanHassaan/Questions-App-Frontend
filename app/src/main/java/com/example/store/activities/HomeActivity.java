package com.example.store.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.store.R;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

// https://code.tutsplus.com/tutorials/how-to-code-a-navigation-drawer-in-an-android-app--cms-30263

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    protected DrawerLayout mDrawer;
    ActionBarDrawerToggle toggle;
    TextView tv_navBarName;
    ImageView iv_navBarImg;
    FloatingActionButton fb_cartCheckout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar mToolbar = findViewById(R.id.toolbar_home);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);

        mDrawer = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, mDrawer, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        mDrawer.addDrawerListener(toggle);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fb_cartCheckout = findViewById(R.id.cart_floating_button);
        fb_cartCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, PostActivity.class));
            }
        });

        setNavigationHeaderInfo();
    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);

        setNavigationHeaderInfo();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;

        if (item.getItemId() == R.id.nav_item_products) {
            intent = new Intent(this, QuestionsActivity.class);
        } else if (item.getItemId() == R.id.nav_item_orders) {
            intent = new Intent(this, MyquestionsActivity.class);
        } else if (item.getItemId() == R.id.nav_item_cart) {
            intent = new Intent(this, PostActivity.class);
        } else if (item.getItemId() == R.id.nav_item_logout) {
            intent = new Intent(this, MainActivity.class);
            LoginManager.getInstance().logOut();
        } else if (item.getItemId() == R.id.trivia){
            intent = new Intent(this, TriviaActivity.class);
        }
            else {
            intent = new Intent(this, null);
        }

        finish();
        startActivity(intent);

        return true;
    }

    private void setNavigationHeaderInfo() {
        View header = ((NavigationView) findViewById(R.id.nav_view)).getHeaderView(0);
        tv_navBarName = header.findViewById(R.id.tv_navhead_name);
        iv_navBarImg = header.findViewById(R.id.iv_navhead_profile);

        if (Profile.getCurrentProfile() != null) {
            tv_navBarName.setText(Profile.getCurrentProfile().getName());
            Picasso.get().load(Profile.getCurrentProfile().getProfilePictureUri(128, 128)).into(iv_navBarImg);
        }
    }
}
