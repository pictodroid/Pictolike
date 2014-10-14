package com.app.pictolike;

import java.util.ArrayList;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.app.pictolike.data.ProfileItem;

public class ProfileScreenActivity extends Fragment {

	private GridView mGridView;
	private ArrayList<ProfileItem> mData;
	private int current_page = 1;
	private boolean loadingMore;
	private ProfileGridAdapter mAdapter;
	private boolean stopLoadingData;
	private LinearLayout progressLayout;

	private int itemCount = 24;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.activity_profile, container,
				false);
		// static data
		final int profileCount = 126;

		setupViews(rootView);
		runProfileCounter(profileCount, rootView);

		return rootView;
	}

	// @Override
	// protected void onCreate(Bundle savedInstanceState) {
	// super.onCreate(savedInstanceState);
	// setupViews();
	//
	// // static data
	// final int profileCount = 126;
	// runProfileCounter(profileCount);
	// }

	private void setupViews(View rootView) {
		// setContentView(R.layout.activity_profile);
		/** Code to change Action Bar Color */
		// ActionBar bar = getActionBar();
		// ColorDrawable cd = new ColorDrawable(0xFFFBAC00);
		// bar.setBackgroundDrawable(cd);
		mAdapter = new ProfileGridAdapter();
		mGridView = (GridView) rootView.findViewById(R.id.profile_grid_view);
		mGridView.setAdapter(mAdapter);

		mGridView.setOnScrollListener(gridviewScrollListener);

		progressLayout = (LinearLayout) rootView
				.findViewById(R.id.linlaProgressBar);
		progressLayout.setVisibility(View.GONE);
	}

	private void runProfileCounter(final int likeCount, View rootView) {
		final TextView profileCountView = (TextView) rootView
				.findViewById(R.id.profile_likes_counter_view);

		final RelativeLayout parentProfileHeaderView = (RelativeLayout) rootView
				.findViewById(R.id.profile_header_container);
		parentProfileHeaderView.setBackgroundResource(R.drawable.ic_total_likes);

//		Integer colorFrom = Color.rgb(255, 206, 84);
//		Integer colorTo = Color.rgb(93, 156, 236);
//		ValueAnimator colorAnimation = ValueAnimator.ofObject(
//				new ArgbEvaluator(), colorFrom, colorTo);
//		colorAnimation.setDuration(7000);
//		colorAnimation.addUpdateListener(new AnimatorUpdateListener() {
//
//			@Override
//			public void onAnimationUpdate(final ValueAnimator animator) {
//				if (getActivity() != null) {
//					getActivity().runOnUiThread(new Runnable() {
//						public void run() {
//							final ShapeDrawable footerBackground = new ShapeDrawable(
//									new OvalShape());
//							footerBackground.setIntrinsicHeight(60);
//							footerBackground.setIntrinsicWidth(60);
//							footerBackground.setBounds(new Rect(0, 0, 60, 60));
//							footerBackground.getPaint().setColor(
//									(Integer) animator.getAnimatedValue());
//							parentProfileHeaderView
//									.setBackground(footerBackground);
//						}
//					});
//				}
//			}
//		});

		new Thread(new Runnable() {
			@Override
			public void run() {
				final double percentageCounter = .005;
				double counter = 0;
				double addedValue = likeCount * percentageCounter;
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				while (counter <= likeCount + addedValue) {
					final int fCounter = (int) (counter <= likeCount ? counter
							: likeCount);
					if (getActivity() != null) {
						getActivity().runOnUiThread(new Runnable() {
							public void run() {
								profileCountView.setText("" + fCounter);
							}
						});
					}

					try {
						Thread.sleep(20);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					counter += addedValue;
				}
			}
		}).start();
//		colorAnimation.start();
	}

	// TODO implement endless adapter here!
	class ProfileGridAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return itemCount;
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int arg0, View view, ViewGroup arg2) {
			View convertView = view;

			if (convertView == null) {
				LayoutInflater inflater = getActivity().getLayoutInflater();
				convertView = inflater.inflate(R.layout.profile_item_view,
						arg2, false);
			}

			return convertView;
		}
	}

	OnScrollListener gridviewScrollListener = new OnScrollListener() {

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {

		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			int lastInScreen = firstVisibleItem + visibleItemCount;
			if ((lastInScreen == totalItemCount) && !(loadingMore)) {
				if (stopLoadingData == false) {
					// FETCH THE NEXT BATCH OF FEEDS
					new LoadMorePhotos().execute();
				}
			}
		}
	};

	private class LoadMorePhotos extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... arg0) {

			// SET LOADING MORE "TRUE"
			loadingMore = true;

			// INCREMENT CURRENT PAGE
			current_page += 1;

			// Next page request
			itemCount += 12;
			return null;
		}

		@Override
		protected void onPreExecute() {
			progressLayout.setVisibility(View.VISIBLE);
		}

		@Override
		protected void onPostExecute(Void result) {
			int currentPosition = mGridView.getFirstVisiblePosition();

			// pass ArrayList data here...
			mAdapter = new ProfileGridAdapter();
			mGridView.setAdapter(mAdapter);

			// Setting new scroll position
			mGridView.setSelection(currentPosition + 1);

			loadingMore = false;
			progressLayout.setVisibility(View.GONE);
		}
		
		//@Override
		//protected void onCLickImageView(Void result){
			//if clicked on an icon open the picture full screen (fill the screen)
			//using other views on your activity j
			//lets make imageview fill parent on Click event.
		//}

	}
}
