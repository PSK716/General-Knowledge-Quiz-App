package com.example.quizzy.View;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.quizzy.Misc_Section.ImageUtils;
import com.example.quizzy.Model.User;
import com.example.quizzy.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;

public class AddDetails extends AppCompatActivity {

    private Button update;
    private DatabaseReference mDatabase;
    private FirebaseAuth auth;
    private EditText name;
    private Toolbar toolbar;
    private ImageView imageView;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUri;
    private int STORAGE_PERMISSION_CODE=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_details);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        auth= FirebaseAuth.getInstance();
        update=findViewById(R.id.update_button);
        name=findViewById(R.id.Name_details);
        imageView = findViewById(R.id.userImage);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });
        toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                User user=new User(name.getText().toString());
                if(name!=null) {
                    //
                    mDatabase.child("users").child(Objects.requireNonNull(auth.getUid()))
                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(AddDetails.this,"Updated",Toast.LENGTH_SHORT).show();
                            refresh();//Refresh 9
                            finish();
                            progressDialog.dismiss();
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        }
                    });
                }else Toast.makeText(AddDetails.this,"Enter Valid Name",Toast.LENGTH_SHORT).show();
            }
        });
        mDatabase.child("users").child(Objects.requireNonNull(auth.getUid())).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(name.getText().toString().equals("")&&dataSnapshot.exists()){
                            User user=dataSnapshot.getValue(User.class);
                            assert user != null;
                            name.setText(user.name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void openFileChooser() {
        if(ContextCompat.checkSelfPermission(AddDetails.this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)
        {
           // Toast.makeText(AddDetails.this,"You have already granted this Permission!",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        }
        else {
            reuestStoraePermission();

        }

    }

    private void reuestStoraePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(AddDetails.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission Required!")
                   // .setMessage("This permission is needed because of this and that")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(AddDetails.this,
                                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE)  {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void refresh() {

        Intent intent = new Intent(AddDetails.this, com.example.quizzy.View.MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();
            try {

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),mImageUri);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
                String userUid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
                //uploading the image
                UploadTask uploadTask = FirebaseStorage.getInstance()
                        .getReference().child(userUid).putFile(mImageUri);
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
            ImageUtils.displayRoundImageFromUrl(AddDetails.this, mImageUri.toString(), imageView);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
