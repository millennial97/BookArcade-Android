package in.bookarcade.app;

import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.Objects;

import in.bookarcade.app.adapter.BottomNavigationViewPagerAdapter;
import in.bookarcade.app.auth.LoginActivity;
import in.bookarcade.app.main.CategoriesFragment;
import in.bookarcade.app.main.HomeFragment;
import in.bookarcade.app.main.ProfileFragment;
import in.bookarcade.app.utils.UniversalImageLoader;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //Java built-in types
    private boolean doubleBackToExitPressedOnce = false;

    //Android widgets
    private ViewPager viewPager;
    private Fragment homeFragment;
    private Fragment dashboardFragment;
    private Fragment profileFragment;
    private MenuItem prevMenuItem;
    private NavigationView navigationView;
    private View headerView;
    private TextView tv_name, tv_email;
    private ImageView img_user;

    //External types
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser mUser;
    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        preInit();
        initViews();
        mainInit();
    }

    private void preInit() {
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        UniversalImageLoader universalImageLoader = new UniversalImageLoader(this);
        ImageLoader.getInstance().init(universalImageLoader.getDefaultConfig());
    }

    private void initViews() {
        viewPager = findViewById(R.id.viewpager);
        bottomNavigation = findViewById(R.id.navigation);

        headerView = navigationView.getHeaderView(0);
        tv_name = headerView.findViewById(R.id.tv_name);
        tv_email = headerView.findViewById(R.id.tv_email);
        img_user = headerView.findViewById(R.id.imageView);

        //Listeners
        bottomNavigation.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.navigation_home:
                                viewPager.setCurrentItem(0);
//                                Objects.requireNonNull(getSupportActionBar()).setTitle("Store");
                                break;
                            case R.id.navigation_dashboard:
                                viewPager.setCurrentItem(1);
//                                Objects.requireNonNull(getSupportActionBar()).setTitle("Dashboard");
                                break;
                            case R.id.navigation_profile:
                                viewPager.setCurrentItem(2);
                                break;
                        }
                        return false;
                    }
                });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                switch (position) {
                    case 0:
                        Objects.requireNonNull(getSupportActionBar()).setTitle("Store");
                        break;
                    case 1:
                        Objects.requireNonNull(getSupportActionBar()).setTitle("Dashboard");
                        break;
                    case 2:
                        Objects.requireNonNull(getSupportActionBar()).setTitle("Profile");
                        break;
                    default:

                }
            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                } else {
                    bottomNavigation.getMenu().getItem(0).setChecked(false);
                }
                bottomNavigation.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottomNavigation.getMenu().getItem(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void mainInit() {
        setupViewPager(viewPager);

        tv_name.setText(Objects.requireNonNull(mAuth.getCurrentUser()).getDisplayName());
        tv_email.setText(mUser.getEmail());

        if (Objects.requireNonNull(mUser.getProviders()).contains("facebook.com")) {
            UniversalImageLoader.setImage(String.valueOf(mUser.getPhotoUrl()) + "?height=200", img_user, null);
        } else {
            TextDrawable textDrawable = TextDrawable.builder()
                    .beginConfig().
                            textColor(getResources().getColor(R.color.colorPrimary)).
                            withBorder(4)
                    .endConfig().buildRound(String.valueOf(Objects.requireNonNull(mUser.getDisplayName()).charAt(0)),
                            getResources().getColor(R.color.colorLight));
            img_user.setBackground(textDrawable);
        }

    }

    private void setupViewPager(ViewPager viewPager) {
        BottomNavigationViewPagerAdapter adapter = new BottomNavigationViewPagerAdapter(getSupportFragmentManager());
        homeFragment = HomeFragment.newInstance();
        dashboardFragment = CategoriesFragment.newInstance();
        profileFragment = ProfileFragment.newInstance();
        adapter.addFragment(homeFragment);
        adapter.addFragment(dashboardFragment);
        adapter.addFragment(profileFragment);
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);


        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(Objects.requireNonNull(searchManager).getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == R.id.action_search) {

        } else if (id == R.id.action_cart) {
            startActivity(new Intent(HomeActivity.this, CartActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_student) {
            startActivity(new Intent(HomeActivity.this, StudentSchemeActivity.class));

        } else if (id == R.id.nav_suggest) {

        } else if (id == R.id.nav_donate) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_logout) {
            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this)
                    .setMessage("Confirm Log Out?")
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            FirebaseAuth.getInstance().signOut();
                            LoginManager.getInstance().logOut();
                            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                            finish();
                        }
                    });
            builder.show();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press again to exit BookArcade", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}
