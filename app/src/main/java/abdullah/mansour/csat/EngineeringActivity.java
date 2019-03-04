package abdullah.mansour.csat;

import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import abdullah.mansour.csat.Fragments.PHDFragment;
import abdullah.mansour.csat.Fragments.SignInFragment;
import abdullah.mansour.csat.Fragments.SignUpFragment;
import abdullah.mansour.csat.Fragments.TAFragment;

public class EngineeringActivity extends AppCompatActivity
{
    FragmentPagerAdapter fragmentPagerAdapter;
    ViewPager viewPager;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_engineering);

        viewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tabs);

        fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager())
        {
            private final Fragment[] mFragments = new Fragment[]
                    {
                            new PHDFragment(),
                            new TAFragment()
                    };
            private final String[] mFragmentNames = new String[]
                    {
                            "Ph.D.",
                            "T.A",
                    };

            @Override
            public Fragment getItem(int position)
            {
                return mFragments[position];
            }

            @Override
            public int getCount()
            {
                return mFragments.length;
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position)
            {
                return mFragmentNames[position];
            }
        };

        viewPager.setAdapter(fragmentPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
