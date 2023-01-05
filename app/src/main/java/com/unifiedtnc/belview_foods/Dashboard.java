package com.unifiedtnc.belview_foods;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Dashboard extends AppCompatActivity {
    RecyclerView recyclerView, rv_recentItem;
    ShowCategoryAdapter adapter;
    ProgressBar progressBar;
    DrawerLayout Dlayout;
    ActionBarDrawerToggle abDtoggle;
    Toolbar toolbar;
    ImageView imageSlider;
    SearchView searchView;
    TextView txtViewAll;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        init();
        imageSlider = findViewById(R.id.slider);
        ArrayList<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel(R.drawable.image2, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.image3, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.image1, ScaleTypes.FIT));
        //  imageSlider.setImageList(slideModels,ScaleTypes.FIT);
        recyclerView = findViewById(R.id.categoryrecycleview);
        rv_recentItem = findViewById(R.id.rv_recent_item);
        txtViewAll = findViewById(R.id.textViewAll);
        searchView = findViewById(R.id.searchView);
        progressBar = findViewById(R.id.progressBar);
        fetchCategory();
        fetchProducts();
        txtViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Dashboard.this, ShowCategoryActivity.class));
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                assert adapter !=null;
                adapter.getFilter().filter(newText);
                return true;
            }
        });
    }

    private void fetchProducts() {

        progressBar.setVisibility(View.VISIBLE);
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        final ArrayList<ProductModel> productModelArrayList = new ArrayList<>();
        dbRef.child("Product").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productModelArrayList.clear();
                progressBar.setVisibility(View.GONE);
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    productModelArrayList.add(
                            new ProductModel(
                                    dataSnapshot.getKey(),
                                    dataSnapshot.child("name").getValue(String.class),
                                    dataSnapshot.child("price").getValue(String.class),
                                    dataSnapshot.child("description").getValue(String.class),
                                    dataSnapshot.child("category").getValue(String.class),
                                    dataSnapshot.child("imageUrl").getValue(String.class)
                            )
                    );
                }

                LinearLayoutManager manager = new LinearLayoutManager(Dashboard.this, RecyclerView.HORIZONTAL, false);
                rv_recentItem.setLayoutManager(manager);
                ShowProductAdapter adapter = new ShowProductAdapter(Dashboard.this, productModelArrayList);
                rv_recentItem.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Dashboard.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }

    public void OnClickBtn(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.lythome: {
                break;
            }
            case R.id.lytcategory: {
                startActivity(new Intent(Dashboard.this, ShowCategoryActivity.class).putExtra("status", "user"));
                break;
            }

            case R.id.lytcart: {
                startActivity(new Intent(Dashboard.this, AddCart.class));
                Toast.makeText(this, "Cart", Toast.LENGTH_SHORT).show();
                break;
            }
            default:
                Toast.makeText(this, "no ", Toast.LENGTH_SHORT).show();


        }

    }

    private void fetchCategory() {
        final ArrayList<CategoryModel> list_category = new ArrayList<>();
        progressBar.setVisibility(View.VISIBLE);
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        dbRef.child("category").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list_category.clear();
                progressBar.setVisibility(View.GONE);
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String str_catgery = dataSnapshot.child("category").getValue(String.class);
                    String category_img = dataSnapshot.child("category_Image").getValue(String.class);
                    CategoryModel categoryModel = new CategoryModel(dataSnapshot.getKey(), str_catgery, category_img);
                    list_category.add(categoryModel);
                }
                LinearLayoutManager manager = new GridLayoutManager(Dashboard.this, 3);
                recyclerView.setLayoutManager(manager);
                adapter = new ShowCategoryAdapter(Dashboard.this, list_category);
                recyclerView.setAdapter(adapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Dashboard.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void init() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Dlayout = findViewById(R.id.drawer_layout);
        abDtoggle = new ActionBarDrawerToggle(this, Dlayout, R.string.open, R.string.close);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setItemTextColor(ColorStateList.valueOf(Color.BLACK));
        navigationView.setItemIconTintList(ColorStateList.valueOf(Color.BLACK));
        setupDrawerContent(navigationView);
        Dlayout.addDrawerListener(abDtoggle);
        abDtoggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        /*--------------------------------------*/
//        requestNowbtn = (Button) findViewById(R.id.btn_request);
//        back_tool_image = (ImageView) findViewById(R.id.img);
//        back_toolbar_title=findViewById(R.id.back_toolbar_title);
        final View headerView = navigationView.getHeaderView(0);
        final TextView tvName = headerView.findViewById(R.id.header_name);
        final TextView tvEmail = headerView.findViewById(R.id.tvMobile);
        tvName.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        tvEmail.setText(AppData.name);
//        ImageView imag = headerView.findViewById(R.id.pic_profile);

//        tvEmail.setText("AppData.email");

//        back_tool_image.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openDrawer();
//
//            }
//        });

//        back_tool_image.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_black_24dp));

    }


    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menu_home:
                        startActivity(new Intent(Dashboard.this, Dashboard.class));
                        finish();
                        break;
                    case R.id.profile:
                        startActivity(new Intent(Dashboard.this, MyProfileActivity.class));
                        break;
                    case R.id.cart:
                        startActivity(new Intent(Dashboard.this, AddCart.class));
                        break;

                    case R.id.menu_tracker:
                        startActivity(new Intent(Dashboard.this, OrderDetailActivity.class).putExtra("status", "user"));
//                fragmentClass = MessagesFragment.class;
                        break;
                    case R.id.changePass:
                        startActivity(new Intent(Dashboard.this, UpdatePassword.class));

                        break;
                    case R.id.about:
                        startActivity(new Intent(Dashboard.this, AboutActivity.class));
                        break;
                    case R.id.menu_logout:
                        FirebaseAuth.getInstance().signOut();
//                       FirebaseAuth.getInstance().getCurrentUser().()
                        startActivity(new Intent(Dashboard.this, MainActivity.class));
                        finish();
                        break;
                    default:
//                startActivity(new Intent(HomeScreenActivity.this,HomeActivity.class));
//                fragmentClass = HomeFragment.class;
                }

                return true;
            }

        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public void onBackPressed() {
        IsFinish("Want to close app?");
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (abDtoggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void IsFinish(String alertmessage) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        android.os.Process.killProcess(android.os.Process.myPid());
                        // This above line close correctly
                        //finish();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(Dashboard.this);
        builder.setMessage(alertmessage)
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }
}