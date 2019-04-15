package abdullah.mansour.csat;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import abdullah.mansour.csat.Fragments.PHDFragment;
import abdullah.mansour.csat.Models.PHDModel;
import abdullah.mansour.csat.Models.ReviewModel;

public class DetailsActivity extends AppCompatActivity
{
    FloatingActionButton floatingActionButton;

    TextView fullname;
    RatingBar ratingBar;

    String key;
    float rate = 0.0f;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<ReviewModel, ReviewViewHolder> firebaseRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        floatingActionButton = findViewById(R.id.add_review);
        fullname = findViewById(R.id.doctor_fullname);
        ratingBar = findViewById(R.id.ratingbar);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        databaseReference.keepSynced(true);

        recyclerView = findViewById(R.id.recyclerview);

        layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        key = getIntent().getStringExtra(PHDFragment.EXTRA_PHD_KEY);

        floatingActionButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showReviewDialog();
            }
        });

        returndata(key);
        setRating(key);
        getReviews(key);
    }

    public void setRating(String key)
    {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.keepSynced(true);

        databaseReference.child("Reviews").child(key).addValueEventListener(
                new ValueEventListener()
                {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        // Get user value
                        rate = 0;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren())
                        {
                            ReviewModel review = snapshot.getValue(ReviewModel.class);
                            rate = rate + review.getRate();
                        }
                        float rate2 = rate / dataSnapshot.getChildrenCount();
                        ratingBar.setRating(rate2);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError)
                    {

                    }
                });
    }

    public void returndata(String key)
    {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.keepSynced(true);

        mDatabase.child("phds").child(key).addListenerForSingleValueEvent(
                new ValueEventListener()
                {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        // Get user value
                        PHDModel phdModel = dataSnapshot.getValue(PHDModel.class);

                        fullname.setText(phdModel.getFullname());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError)
                    {
                        Toast.makeText(getApplicationContext(), "can\'t fetch data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getReviews(String key)
    {
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("Reviews")
                .child(key)
                .limitToLast(50);

        FirebaseRecyclerOptions<ReviewModel> options =
                new FirebaseRecyclerOptions.Builder<ReviewModel>()
                        .setQuery(query, ReviewModel.class)
                        .build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ReviewModel, ReviewViewHolder>(options)
        {
            @Override
            protected void onBindViewHolder(@NonNull ReviewViewHolder holder, int position, @NonNull final ReviewModel model)
            {
                final String key = getRef(position).getKey();

                holder.BindPlaces(model);
            }

            @NonNull
            @Override
            public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
                View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.review_item, parent, false);
                return new ReviewViewHolder(view);
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder
    {
        TextView review;
        RatingBar ratingBar;

        ReviewViewHolder(View itemView)
        {
            super(itemView);

            review = itemView.findViewById(R.id.review_content);
            ratingBar = itemView.findViewById(R.id.ratingbar);
        }

        void BindPlaces(final ReviewModel reviewModel)
        {
            review.setText(reviewModel.getName());
            ratingBar.setRating(reviewModel.getRate());
        }
    }

    private void showReviewDialog()
    {
        final Dialog dialog = new Dialog(DetailsActivity.this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.rate_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes();
        dialog.setCancelable(false);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        Button cancel = dialog.findViewById(R.id.company_rating_cancel_btn);
        Button add_review = dialog.findViewById(R.id.company_rating_review_btn);

        final EditText review_content = dialog.findViewById(R.id.company_review_field);
        final RatingBar ratingBar = dialog.findViewById(R.id.company_ratingbar);

        add_review.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String c = review_content.getText().toString();
                float r = ratingBar.getRating();

                if (TextUtils.isEmpty(c))
                {
                    Toast.makeText(getApplicationContext(), "please write review", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (r == 0)
                {
                    Toast.makeText(getApplicationContext(), "please enter rate", Toast.LENGTH_SHORT).show();
                    return;
                }

                createReview(c,r);
                dialog.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    private void createReview(String c, float r)
    {
        ReviewModel reviewModel = new ReviewModel(c,r);

        databaseReference.child("Reviews").child(key).child(getUid()).setValue(reviewModel);
    }

    @Override
    public void onStart()
    {
        super.onStart();

        if (firebaseRecyclerAdapter != null)
        {
            firebaseRecyclerAdapter.startListening();
        }
    }

    @Override
    public void onStop()
    {
        super.onStop();

        if (firebaseRecyclerAdapter != null)
        {
            firebaseRecyclerAdapter.stopListening();
        }
    }

    public static String getUid()
    {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
}
