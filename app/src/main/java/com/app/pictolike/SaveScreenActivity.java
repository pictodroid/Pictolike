package com.app.pictolike;

import java.util.ArrayList;
import java.util.List;

import org.askerov.dynamicgrid.BaseDynamicGridAdapter;
import org.askerov.dynamicgrid.DynamicGridView;

import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.pictolike.data.Constant;
import com.app.pictolike.data.PictoFile;
import com.app.pictolike.data.ProfileItem;
import com.app.pictolike.helpers.ImageLoaderHelper;
import com.app.pictolike.mysql.MySQLConnect;
import com.app.pictolike.sqlite.SqliteHandler;

public class SaveScreenActivity extends Fragment {

	private DynamicGridView mGridView;
	private ArrayList<ProfileItem> mData;
	private int current_page = 1;
	private boolean loadingMore;
	private GridDynamicAdapter mAdapter;
	private boolean stopLoadingData;
	private LinearLayout progressLayout;
	private ArrayList<PictoFile> list;
	private int itemCount = 24;
	
	
	private boolean dragEnabled = false;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSqliteHandler = new SqliteHandler(getActivity());
    }

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.activity_savescreen,
				container, false);
		// static data
		final int profileCount = 126;

		setupViews(rootView);
		// runProfileCounter(profileCount, rootView);
		final RelativeLayout parentProfileHeaderView = (RelativeLayout) rootView
				.findViewById(R.id.profile_header_container);
		parentProfileHeaderView
				.setBackgroundResource(R.drawable.ic_total_likes);
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
    private SqliteHandler mSqliteHandler;
	private void setupViews(final View rootView) {
		// setContentView(R.layout.activity_profile);
		/** Code to change Action Bar Color */
		// ActionBar bar = getActionBar();
		// ColorDrawable cd = new ColorDrawable(0xFFFBAC00);
		// bar.setBackgroundDrawable(cd);

		List<PictoFile> list = mSqliteHandler.loadSavedPictos();


		mAdapter = new GridDynamicAdapter(getActivity(), list, 2);
		mGridView = (DynamicGridView) rootView
				.findViewById(R.id.profile_grid_view);
		mGridView.setAdapter(mAdapter);

		mGridView.setOnScrollListener(gridviewScrollListener);

		progressLayout = (LinearLayout) rootView
				.findViewById(R.id.linlaProgressBar);
		progressLayout.setVisibility(View.GONE);

		mGridView.setOnDragListener(new DynamicGridView.OnDragListener() {
			@Override
			public void onDragStarted(int position) {
				Log.d("TEST", "drag started at position " + position);
			}

			@Override
			public void onDragPositionsChanged(int oldPosition, int newPosition) {
				Log.d("TEST", String.format(
						"drag item position changed from %d to %d",
						oldPosition, newPosition));
			}
		});

		mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Toast.makeText(getActivity(),
						parent.getAdapter().getItem(position).toString(),
						Toast.LENGTH_SHORT).show();
			}
		});
		
		
		rootView.findViewById(R.id.profile_header_container).setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				if(!dragEnabled) {
					rootView.findViewById(R.id.profile_header_container).setBackgroundResource(R.drawable.ic_blank_star);
					dragEnabled = true;
					mGridView.startEditMode();
				} else {
					rootView.findViewById(R.id.profile_header_container).setBackgroundResource(R.drawable.ic_blank_star_2);
					dragEnabled = false;
					mGridView.stopEditMode();
				}
				return false;
			}
		});
	}

	private void runProfileCounter(final int likeCount, View rootView) {
		final TextView profileCountView = (TextView) rootView
				.findViewById(R.id.profile_likes_counter_view);

		// Integer colorFrom = Color.rgb(255, 206, 84);
		// Integer colorTo = Color.rgb(93, 156, 236);
		// ValueAnimator colorAnimation = ValueAnimator.ofObject(
		// new ArgbEvaluator(), colorFrom, colorTo);
		// colorAnimation.setDuration(7000);
		// colorAnimation.addUpdateListener(new AnimatorUpdateListener() {
		//
		// @Override
		// public void onAnimationUpdate(final ValueAnimator animator) {
		// if (getActivity() != null) {
		// getActivity().runOnUiThread(new Runnable() {
		// public void run() {
		// final ShapeDrawable footerBackground = new ShapeDrawable(
		// new OvalShape());
		// footerBackground.setIntrinsicHeight(60);
		// footerBackground.setIntrinsicWidth(60);
		// footerBackground.setBounds(new Rect(0, 0, 60, 60));
		// footerBackground.getPaint().setColor(
		// (Integer) animator.getAnimatedValue());
		// parentProfileHeaderView
		// .setBackground(footerBackground);
		// }
		// });
		// }
		// }
		// });

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
		// colorAnimation.start();
	}

	// TODO implement endless adapter here!
	class ProfileGridAdapter extends BaseDynamicGridAdapter {

		protected ProfileGridAdapter(Context context, int columnCount) {
			super(context, columnCount);
			// TODO Auto-generated constructor stub
		}

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
		public View getView(int arg0, View view, ViewGroup arg2) {
			View convertView = view;

			if (convertView == null) {
				LayoutInflater inflater = getActivity().getLayoutInflater();
				convertView = inflater.inflate(R.layout.save_item_view, arg2,
						false);
			}
			return convertView;
		}

		@Override
		public void reorderItems(int originalPosition, int newPosition) {
			// TODO Auto-generated method stub

		}

		@Override
		public int getColumnCount() {
			// TODO Auto-generated method stub
			return 0;
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
				//	new LoadMorePhotos().execute();
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
			mAdapter = new GridDynamicAdapter(getActivity(), list, 2);
			mGridView.setAdapter(mAdapter);

			// Setting new scroll position
			mGridView.setSelection(currentPosition + 1);

			loadingMore = false;
			progressLayout.setVisibility(View.GONE);
		}

		// @Override
		// protected void onCLickImageView(Void result){
		// if clicked on an icon open the picture full screen (fill the screen)
		// using other views on your activity j
		// lets make imageview fill parent on Click event.
		// }

	}

	public class GridDynamicAdapter extends BaseDynamicGridAdapter {
		public GridDynamicAdapter(Context context, List<PictoFile> items,
				int columnCount) {
			super(context, items, columnCount);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			GridViewHolder holder;
			if (convertView == null) {
				convertView = LayoutInflater.from(getContext()).inflate(
						R.layout.item_grid, null);
				holder = new GridViewHolder(convertView);
				convertView.setTag(holder);
			} else {
				holder = (GridViewHolder) convertView.getTag();
			}
			holder.build((PictoFile) getItem(position));
			return convertView;
		}

		private class GridViewHolder {
			private TextView titleText;
			private ImageView image;

			private GridViewHolder(View view) {
				titleText = (TextView) view.findViewById(R.id.item_title);
				image = (ImageView) view.findViewById(R.id.item_img);
			}

			void build(String title) {
				titleText.setText(title);
				image.setImageResource(R.drawable.like_pic);
			}

            public void build(final PictoFile pItem) {
                titleText.setText(pItem.filename);
                new ImageLoaderHelper(getActivity()).displayImage(MySQLConnect.formatImageUrl(pItem.filename), image);
//                imageLoader.displayImage(MySQLConnect.formatImageUrl(pItem.filename), iv, options);
//                image.setImageResource(R.drawable.like_pic);

            }
        }
	}
}
