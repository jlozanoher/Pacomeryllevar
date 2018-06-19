package com.bonaro.restaurant.DetailedOffers;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bonaro.restaurant.R;


/**
 * Created by lozano on 05/03/17.
 */
public class DetailedOfferPager extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detailed_pager, container, false);
        ViewPager mViewPager = (ViewPager) view.findViewById(R.id.pager_view);
        int card_index = getArguments().getInt("CARD_INDEX");
        int offer_index = getArguments().getInt("OFFER_INDEX");

        DetailedOfferPagerAdapter detailedOfferPagerAdapter = new DetailedOfferPagerAdapter(getFragmentManager(), card_index);
        mViewPager.setAdapter(detailedOfferPagerAdapter);
        mViewPager.setCurrentItem(offer_index);

        return view;
    }


}
