package abdullah.mansour.csat.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import abdullah.mansour.csat.Models.StudentModel;
import abdullah.mansour.csat.PHDMainActivity;
import abdullah.mansour.csat.Models.PHDModel;
import abdullah.mansour.csat.Models.TAModel;
import abdullah.mansour.csat.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class SignUpFragment extends Fragment
{
    View view;

    EditText name,email,password,id;
    Button sign_up_btn,cancel_btn;

    ProgressDialog progressDialog;

    FirebaseAuth auth;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.sign_up_fragment, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        name = view.findViewById(R.id.fullname_field);
        email = view.findViewById(R.id.email_field);
        password = view.findViewById(R.id.password_field);
        id = view.findViewById(R.id.id_field);

        sign_up_btn = view.findViewById(R.id.sign_up_btn);
        cancel_btn = view.findViewById(R.id.cancel_btn);

        auth = FirebaseAuth.getInstance();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        databaseReference.keepSynced(true);

        sign_up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                progressDialog = new ProgressDialog(getContext());
                progressDialog.setTitle("Student Sign Up");
                progressDialog.setMessage("Please Wait Until Creating Account ...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();
                progressDialog.setCancelable(false);

                String get_name = name.getText().toString();
                String get_email = email.getText().toString();
                String get_password = password.getText().toString();
                String get_id = id.getText().toString();

                if (TextUtils.isEmpty(get_name))
                {
                    Toast.makeText(getContext(), "please enter your name", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(get_email))
                {
                    Toast.makeText(getContext(), "please enter your email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(get_password))
                {
                    Toast.makeText(getContext(), "please enter your password", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(get_id))
                {
                    Toast.makeText(getContext(), "please enter your ID", Toast.LENGTH_SHORT).show();
                    return;
                }

                CreateStudentAccount(get_name,get_email,get_password,get_id);
            }
        });
    }

    private void AddStudenttoDB(String name, String email, String id)
    {
        StudentModel studentModel = new StudentModel(name,email,id);

        databaseReference.child("Students").child(getUID()).setValue(studentModel);
    }

    private void CreateStudentAccount(final String name, final String email, String password, final String id)
    {
        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful())
                        {
                            AddStudenttoDB(name,email,id);
                            progressDialog.dismiss();

                            Intent intent = new Intent(getContext(), PHDMainActivity.class);
                            startActivity(intent);
                        } else
                        {
                            String error_message = task.getException().getMessage();
                            Toast.makeText(getContext(), error_message, Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                });
    }

    private String getUID()
    {
        String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        return id;
    }
}
