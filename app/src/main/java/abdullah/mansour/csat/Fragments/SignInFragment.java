package abdullah.mansour.csat.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.victor.loading.rotate.RotateLoading;

import abdullah.mansour.csat.PHDMainActivity;
import abdullah.mansour.csat.R;

public class SignInFragment extends Fragment
{
    View view;

    EditText email,password;
    Button sign_in,guest,admin;

    String email_txt,password_txt;

    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    // Firebase User
    FirebaseUser user;

    RotateLoading rotateLoading;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.sign_in_fragment, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        email = view.findViewById(R.id.email_field);
        password = view.findViewById(R.id.password_field);
        sign_in = view.findViewById(R.id.sign_in_btn);
        rotateLoading = view.findViewById(R.id.signinrotateloading);

        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email_txt = email.getText().toString();
                password_txt = password.getText().toString();

                if (TextUtils.isEmpty(email_txt))
                {
                    Toast.makeText(getContext(), "please enter your email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password_txt))
                {
                    Toast.makeText(getContext(), "please enter your password", Toast.LENGTH_SHORT).show();
                    return;
                }

                rotateLoading.start();

                UserLogin(email_txt,password_txt);
            }
        });
    }

    private void UserLogin(String email, String password)
    {
        auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            category();
                        } else
                        {
                            rotateLoading.stop();
                            String taskmessage = task.getException().getMessage();
                            Toast.makeText(getContext(), taskmessage, Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void category()
    {
        final String id = getUID();
        databaseReference.child("AllUsers").child("PHD").addListenerForSingleValueEvent(
                new ValueEventListener()
                {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        if (dataSnapshot.hasChild(id))
                        {
                            rotateLoading.stop();
                            //Toast.makeText(getContext(), "doctor : " + id, Toast.LENGTH_SHORT).show();
                            updateDoctorUI();
                        } else
                        {
                            rotateLoading.stop();
                            Toast.makeText(getContext(), "Teacher Assistant", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError)
                    {
                        rotateLoading.stop();
                        Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void updateDoctorUI()
    {
        Intent intent = new Intent(getContext(), PHDMainActivity.class);
        startActivity(intent);
    }

    private String getUID()
    {
        user = auth.getCurrentUser();
        String UserID = user.getUid();

        return UserID;
    }
}
