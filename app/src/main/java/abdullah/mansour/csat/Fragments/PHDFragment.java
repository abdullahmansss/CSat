package abdullah.mansour.csat.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.victor.loading.rotate.RotateLoading;

import abdullah.mansour.csat.DetailsActivity;
import abdullah.mansour.csat.Models.PHDModel;
import abdullah.mansour.csat.Models.ReviewModel;
import abdullah.mansour.csat.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class PHDFragment extends Fragment
{
    View view;

    private NestedScrollView nested_scroll_view;
    private LinearLayout linearLayout;
    ImageView imageView;
    private LinearLayout lyt_expand_text;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<PHDModel, PHDViewHolder> firebaseRecyclerAdapter;

    FirebaseRecyclerAdapter<PHDModel, PHDViewHolder> firebaseRecyclerAdapterSpecialty;

    EditText nfc_id;
    Button filter_search_btn,clear_filter_btn;
    TextView filter_txt;
    String nfc_txt;

    RotateLoading rotateLoading;

    public final static String EXTRA_PHD_KEY = "phd_key";
    public final static String EXTRA_PHD_ID = "phd_id";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.phd_fragment, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        recyclerView = view.findViewById(R.id.doctors_recyclerview);
        rotateLoading = view.findViewById(R.id.rotateloading);

        rotateLoading.start();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        databaseReference.keepSynced(true);

        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        DisplayallDoctors();

        initComponent();

        nfc_id = view.findViewById(R.id.nfc_id_field);
    }

    private void DisplayallDoctors()
    {
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("phds")
                .limitToLast(50);

        FirebaseRecyclerOptions<PHDModel> options =
                new FirebaseRecyclerOptions.Builder<PHDModel>()
                        .setQuery(query, PHDModel.class)
                        .build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<PHDModel, PHDViewHolder>(options)
        {
            @Override
            protected void onBindViewHolder(@NonNull PHDViewHolder holder, int position, @NonNull final PHDModel model)
            {
                rotateLoading.stop();

                final String key = getRef(position).getKey();

                holder.BindPlaces(model);

                holder.setRating(key);

                holder.doctor_details.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Intent intent = new Intent(getContext(), DetailsActivity.class);
                        intent.putExtra(EXTRA_PHD_KEY, key);
                        startActivity(intent);
                    }
                });

                holder.view_profile_btn.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Intent intent = new Intent(getContext(), DetailsActivity.class);
                        intent.putExtra(EXTRA_PHD_KEY, key);
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public PHDViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
                View view = LayoutInflater.from(getContext()).inflate(R.layout.phd_item, parent, false);
                return new PHDViewHolder(view);
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
        rotateLoading.stop();
    }

    private void DisplayallDoctorsbySpecialty(String nfc_id)
    {
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("sectionedphds")
                .child(nfc_id)
                .limitToLast(50);

        FirebaseRecyclerOptions<PHDModel> options =
                new FirebaseRecyclerOptions.Builder<PHDModel>()
                        .setQuery(query, PHDModel.class)
                        .build();

        firebaseRecyclerAdapterSpecialty = new FirebaseRecyclerAdapter<PHDModel, PHDViewHolder>(options)
        {
            @Override
            protected void onBindViewHolder(@NonNull PHDViewHolder holder, int position, @NonNull final PHDModel model)
            {
                rotateLoading.stop();

                final String key = getRef(position).getKey();

                holder.BindPlaces(model);

                holder.setRating(key);

                holder.doctor_details.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Intent intent = new Intent(getContext(), DetailsActivity.class);
                        intent.putExtra(EXTRA_PHD_ID, key);
                        startActivity(intent);
                    }
                });

                holder.view_profile_btn.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Intent intent = new Intent(getContext(), DetailsActivity.class);
                        intent.putExtra(EXTRA_PHD_ID, key);
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public PHDViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
                View view = LayoutInflater.from(getContext()).inflate(R.layout.phd_item, parent, false);
                return new PHDViewHolder(view);
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapterSpecialty);
    }

    private void initComponent()
    {
        imageView = view.findViewById(R.id.toggle_image);
        linearLayout = view.findViewById(R.id.toggle_lin);
        lyt_expand_text = view.findViewById(R.id.lyt_expand_text);
        filter_search_btn = view.findViewById(R.id.filter_search_btn);
        clear_filter_btn = view.findViewById(R.id.clear_filter_btn);
        filter_txt = view.findViewById(R.id.filter_txt);
        lyt_expand_text.setVisibility(View.GONE);

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleSectionText(imageView);
            }
        });

        clear_filter_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (firebaseRecyclerAdapterSpecialty != null)
                {
                    toggleSectionText(imageView);

                    firebaseRecyclerAdapterSpecialty.stopListening();

                    DisplayallDoctors();

                    firebaseRecyclerAdapter.startListening();

                    filter_txt.setText("Filter");
                    nfc_id.setText("");

                    firebaseRecyclerAdapterSpecialty = null;

                    hideSoftKeyboard(nfc_id);
                } else
                {
                    Toast.makeText(getContext(), "there's no filter to clear", Toast.LENGTH_SHORT).show();
                    hideSoftKeyboard(nfc_id);
                }
            }
        });

        filter_search_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                nfc_txt = nfc_id.getText().toString();

                if (firebaseRecyclerAdapterSpecialty != null || nfc_txt.length() != 0)
                {
                    if (nfc_txt.length() != 0)
                    {
                        toggleSectionText(imageView);

                        firebaseRecyclerAdapter.stopListening();

                        DisplayallDoctorsbySpecialty(nfc_txt);

                        firebaseRecyclerAdapterSpecialty.startListening();

                        filter_txt.setText("Filter by ID (" + nfc_txt + ")" );

                        hideSoftKeyboard(nfc_id);
                    } else
                    {
                        Toast.makeText(getContext(), "please select filter to search", Toast.LENGTH_SHORT).show();
                        hideSoftKeyboard(nfc_id);
                    }
                } else
                {
                    Toast.makeText(getContext(), "please select filter to search", Toast.LENGTH_SHORT).show();
                    hideSoftKeyboard(nfc_id);
                }
            }
        });

        // nested scrollview
        nested_scroll_view = view.findViewById(R.id.nested_scroll_view);
    }

    private void toggleSectionText(View view)
    {
        boolean show = toggleArrow(view);
        if (show)
        {
            expand(lyt_expand_text, new AnimListener()
            {
                @Override
                public void onFinish()
                {
                    nestedScrollTo(nested_scroll_view, lyt_expand_text);
                }
            });
        } else
        {
            collapse(lyt_expand_text);
        }
    }

    public boolean toggleArrow(View view)
    {
        if (view.getRotation() == 0) {
            view.animate().setDuration(200).rotation(180);
            return true;
        } else {
            view.animate().setDuration(200).rotation(0);
            return false;
        }
    }

    public static void nestedScrollTo(final NestedScrollView nested, final View targetView)
    {
        nested.post(new Runnable() {
            @Override
            public void run() {
                nested.scrollTo(500, targetView.getBottom());
            }
        });
    }

    public static void expand(final View v, final AnimListener animListener)
    {
        Animation a = expandAction(v);
        a.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                animListener.onFinish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        v.startAnimation(a);
    }

    public static void collapse(final View v)
    {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public interface AnimListener
    {
        void onFinish();
    }

    private static Animation expandAction(final View v)
    {
        v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final int targtetHeight = v.getMeasuredHeight();

        v.getLayoutParams().height = 0;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? ViewGroup.LayoutParams.WRAP_CONTENT
                        : (int) (targtetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        a.setDuration((int) (targtetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
        return a;
    }

    public static class PHDViewHolder extends RecyclerView.ViewHolder
    {
        TextView doctor_name;
        MaterialRippleLayout doctor_details;
        Button view_profile_btn;
        RatingBar ratingBar;

        float rate = 0.0f;

        PHDViewHolder(View itemView)
        {
            super(itemView);

            doctor_name = itemView.findViewById(R.id.doctor_fullname);
            doctor_details = itemView.findViewById(R.id.details_btn);
            view_profile_btn = itemView.findViewById(R.id.view_profile_btn);
            ratingBar = itemView.findViewById(R.id.ratingbar);
        }

        void BindPlaces(final PHDModel phdModel)
        {
            doctor_name.setText(phdModel.getFullname());
        }

        void setRating(String key)
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

    protected void hideSoftKeyboard(EditText input)
    {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
    }
}
