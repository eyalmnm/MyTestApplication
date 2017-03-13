package tests.em_projects.com.mytestapplication.carousel;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import tests.em_projects.com.mytestapplication.R;

public class MyPagerAdapter extends FragmentPagerAdapter implements
		ViewPager.OnPageChangeListener {

	private MyLinearLayout cur = null;
	private MyLinearLayout next = null;
	private SimpleCarouselTest context;
	private FragmentManager fm;
	private float scale;

	public MyPagerAdapter(SimpleCarouselTest context, FragmentManager fm) {
		super(fm);
		this.fm = fm;
		this.context = context;
	}

	@Override
	public Fragment getItem(int position) 
	{
        // make the first pager bigger than others
        if (position == SimpleCarouselTest.FIRST_PAGE)
        	scale = SimpleCarouselTest.BIG_SCALE;
        else
        	scale = SimpleCarouselTest.SMALL_SCALE;
        
        position = position % SimpleCarouselTest.PAGES;
        return MyFragment.newInstance(context, position, scale);
	}

	@Override
	public int getCount()
	{		
		return SimpleCarouselTest.PAGES * SimpleCarouselTest.LOOPS;
	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) 
	{	
		if (positionOffset >= 0f && positionOffset <= 1f)
		{
		        cur = getRootView(position);
        		cur.setScaleBoth(SimpleCarouselTest.BIG_SCALE - SimpleCarouselTest.DIFF_SCALE * positionOffset);

        		if (position < SimpleCarouselTest.PAGES-1) {
        			next = getRootView(position +1);
        			next.setScaleBoth(SimpleCarouselTest.SMALL_SCALE + SimpleCarouselTest.DIFF_SCALE * positionOffset);
        		}
		}
	}

	@Override
	public void onPageSelected(int position) {}
	
	@Override
	public void onPageScrollStateChanged(int state) {}
	
	private MyLinearLayout getRootView(int position)
	{
		return (MyLinearLayout) 
				fm.findFragmentByTag(this.getFragmentTag(position))
				.getView().findViewById(R.id.root);
	}
	
	private String getFragmentTag(int position)
	{
	    return "android:switcher:" + context.pager.getId() + ":" + position;
	}
}
