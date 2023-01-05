package com.unifiedtnc.belview_foods;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

public class UpdateProductActivity extends AppCompatActivity {

    EditText name,price,description;
    Spinner spinner;
    ImageView imageView;
    Uri filePath;
    Bitmap bitmap;
    TextView  btnUpdate;
    ProgressDialog progressDialog;
    String med_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        imageView=findViewById(R.id.updteImageID);
        name=findViewById(R.id.updtenameID);
        price=findViewById(R.id.updtepriceID);
        spinner=findViewById(R.id.spinnerId);
        description=findViewById(R.id.updtedescriptionID);
        btnUpdate= findViewById(R.id.updatebtnID);
        progressDialog=new ProgressDialog(this);
        fetchCategory();
        ProductModel productModel =  (ProductModel) getIntent().getExtras().getSerializable("PRODUCT");
        med_id=productModel.product_key;
        // imageView.setImageBitmap(productModel.imageUrl);
        Glide.with(this).load(productModel.imageUrl).into(imageView);
        name.setText(productModel.name);
        price.setText(productModel.price);
        //   date.setText(productModel.current_date);
        description.setText(productModel.description);




                     imageView.setOnClickListener(new View.OnClickListener() {
                         @Override
                         public void onClick(View v) {
                             Dexter.withActivity(UpdateProductActivity.this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                                     .withListener(new PermissionListener() {
                                         @Override
                                         public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                             Intent intent=new Intent(Intent.ACTION_PICK);
                                             intent.setType("image/*");
                                             startActivityForResult(Intent.createChooser(intent,"Select Image"),1);
                                         }

                                         @Override
                                         public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                                         }

                                         @Override
                                         public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                                     permissionToken.continuePermissionRequest();
                                         }
                                     }).check();
                         }
                     });

    btnUpdate.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            updateProduct();
        }
    });
    }

    private void fetchCategory() {

        final ArrayList<String> list_category = new ArrayList<>();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        dbRef.child("category").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list_category.clear();
                list_category.add("Select Category");
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    String str_catgery = dataSnapshot.child("category").getValue(String.class);

                    list_category.add(str_catgery);

                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, list_category);
                adapter.setDropDownViewResource(R.layout.spinner_item);
                spinner.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateProductActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void updateProduct() {




        progressDialog.setTitle("File Uploader");
        progressDialog.show();

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        final StorageReference storageReference = firebaseStorage.getReference("Image1" + new Random().nextInt(50));
        storageReference.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                        DatabaseReference dbRef = firebaseDatabase.getReference();
                        ProductModel productModel = new ProductModel(med_id,name.getText().toString(),
                                price.getText().toString(),
                                description.getText().toString(),
                                spinner.getSelectedItem().toString(),
                                uri.toString()
                        );
                        addProduct(productModel);

                    }
                });


            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                float percent = (100 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                progressDialog.setMessage("Uploaded\t" + (int) percent + "%");
            }
        });


    }

    private void addProduct(ProductModel productModel) {



        FirebaseDatabase.getInstance().getReference("Product").child(productModel.product_key).setValue(productModel, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    Log.e("TAG", "Data Not saved");

                } else {
                    progressDialog.dismiss();
                    Toast.makeText(UpdateProductActivity.this, "Product updated successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode==1 && resultCode==RESULT_OK){
               filePath=data.getData();
            try {
                InputStream inputStream=getContentResolver().openInputStream(filePath);
                bitmap=BitmapFactory.decodeStream(inputStream);
                imageView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}