package com.app.pictolike;

import com.app.pictolike.data.Constant;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class HomePageActivity extends Fragment {
	static final int MIN_DISTANCE = 1;
	static final int IMAGE_MARGIN = 50;

	float downXValue;
	float downYValue;
	float alphaValue = 0;
	float imageItemX, imageItemY;
	ImageLoader imageLoader;
	int windowwidth;
	int windowheight;
	DisplayImageOptions options;
	int screenCenter;
	int x_cord, y_cord;
	int Likes = 0;
	float tempX, tempY;
	
	boolean saveSignal=false; //if a picture is swiped to left it's saved.

	boolean rotateRight;

	private RelativeLayout parentView;
	private ImageView likingStatus;
	Activity activity;
	
	public HomePageActivity(Activity activity)
	{
		this.activity=activity;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.activity_homepage, container, false);

		setupViews(rootView);
		return rootView;
	}
	
	
	void setImageOption()
	{
		
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(activity.getApplicationContext())
			.threadPriority(Thread.NORM_PRIORITY - 2)
			.denyCacheImageMultipleSizesInMemory()
			.diskCacheFileNameGenerator(new Md5FileNameGenerator())
			.diskCacheSize(50 * 1024 * 1024) // 50 Mb
			.tasksProcessingOrder(QueueProcessingType.LIFO)			
			.writeDebugLogs() // Remove for release app
			.build();
		
		
		
		imageLoader.init(config);
		
		options = new DisplayImageOptions.Builder()
			.showImageForEmptyUri(R.drawable.ic_empty)
			.resetViewBeforeLoading(true)
			.cacheOnDisk(true)
			.imageScaleType(ImageScaleType.EXACTLY)
			.bitmapConfig(Bitmap.Config.RGB_565)
			.considerExifParams(true)
			.displayer(new FadeInBitmapDisplayer(300))
			.build();
	}

	public void setupViews(View rootView) {

		// super.onCreate(savedInstanceState);

		// setContentView(R.layout.activity_homepage);

		/**
		 * Getting a instance of the action bar so that we can later play around
		 * with it like changing the bacKgroundcolor etc.
		 */
		// ActionBar bar = getActionBar();
		//
		// ColorDrawable cd = new ColorDrawable(0xFFFBAC00);
		// bar.setBackgroundDrawable(cd);
		imageLoader = ImageLoader.getInstance();
		setImageOption();
		
		likingStatus = (ImageView) rootView
				.findViewById(R.id.imageview_liking_status);
		parentView = (RelativeLayout) rootView.findViewById(R.id.layoutview);

		Point windowPoint = new Point();
		getActivity().getWindowManager().getDefaultDisplay()
				.getSize(windowPoint);

		windowwidth = windowPoint.x;
		windowheight = windowPoint.y;
		screenCenter = windowwidth / 2;

		int[] myImageList = new int[] { R.drawable.like_pic,
				R.drawable.like_pic2, R.drawable.like_pic3,
				R.drawable.like_pic4 };

		for (int i = 0; i < Constant.homeScreenImageArray.size(); i++) {
			final RelativeLayout myRelView = new RelativeLayout(getActivity());
			myRelView.setLayoutParams(new LayoutParams(
					(windowwidth - (IMAGE_MARGIN * 2)),
					(int) (windowheight * 0.6)));

			int[] themeColor = { Color.WHITE, Color.rgb(250, 229, 191) };
			ColorBarDrawable rectShapeDrawable = new ColorBarDrawable(
					themeColor);

			myRelView.setX(IMAGE_MARGIN);
			myRelView.setY(IMAGE_MARGIN);
			myRelView.setTag(i);

			ImageView iv = new ImageView(getActivity());
			
			
			imageLoader.displayImage(Constant.homeScreenImageArray.get(i),iv, options);
			
			final int IMAGE_OFFSET = 50;

			iv.setX(IMAGE_OFFSET);
			iv.setY(IMAGE_OFFSET);

			myRelView.addView(iv, new LayoutParams(windowwidth - IMAGE_MARGIN
					* 2 - IMAGE_OFFSET * 2, (int) (windowheight * 0.50)
					- IMAGE_OFFSET));
			//myRelView.setBackground(rectShapeDrawable);
			myRelView.setBackgroundDrawable(rectShapeDrawable);

			final Button imageLike = new Button(getActivity());
			imageLike.setLayoutParams(new LayoutParams(100, 50));
			//imageLike.setBackground(getResources().getDrawable(
					//R.drawable.ic_like_pic));
			imageLike.setX(20);
			imageLike.setY(80);
			imageLike.setAlpha(alphaValue);
			myRelView.addView(imageLike);

			final Button imagePass = new Button(getActivity());
			imagePass.setLayoutParams(new LayoutParams(100, 50));
			//imagePass.setBackground(getResources().getDrawable(
					//R.drawable.ic_skip_pic));
			imagePass.setX((windowwidth - 200));
			imagePass.setY(80);
			imagePass.setRotation(45);
			imagePass.setAlpha(alphaValue);
			myRelView.addView(imagePass);

			final float IMG_DETAIL_Y = (float) (windowheight * 0.55)
					- IMAGE_OFFSET;
			
			TextView textTimeAgo = new TextView(getActivity());
			textTimeAgo.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			textTimeAgo.setText("?? minutes ago");
			textTimeAgo.setX(45);
			textTimeAgo.setY(IMG_DETAIL_Y);
			textTimeAgo.setTypeface(null, Typeface.ITALIC);
			textTimeAgo.setTextColor(Color.rgb(237, 214, 167));
			myRelView.addView(textTimeAgo);

			TextView textMilesAway = new TextView(getActivity());
			textMilesAway.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			textMilesAway.setText("?? miles away");
			textMilesAway.setX(windowwidth - IMAGE_MARGIN
					* 2 - IMAGE_OFFSET * 2 - 220);
			textMilesAway.setY(IMG_DETAIL_Y);
			textMilesAway.setTypeface(null, Typeface.ITALIC);
			textMilesAway.setTextColor(Color.rgb(237, 214, 167));
			myRelView.addView(textMilesAway);

			myRelView.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					int dir = -1;

					switch (event.getAction() & MotionEvent.ACTION_MASK) {
					case MotionEvent.ACTION_POINTER_DOWN:
						Log.d("TEST", "moving ..... ");
						break;
					case MotionEvent.ACTION_DOWN: {
						x_cord = (int) event.getRawX();
						y_cord = (int) event.getRawY();
						dir = y_cord > windowheight / 2 ? -1 : 1;
						break;
					}
					case MotionEvent.ACTION_MOVE: {
						float iX = (x_cord - event.getRawX());
						float iY = (y_cord - event.getRawY());

						x_cord = (int) event.getRawX();
						y_cord = (int) event.getRawY();

						myRelView.setX(myRelView.getX() - iX);
						myRelView.setY(myRelView.getY() - iY);
						boolean isSwipedDown = y_cord > (windowheight * .85);

						if (isSwipedDown) {
							likingStatus.setImageDrawable(getResources()
									.getDrawable(R.drawable.ic_poker_face));
							// imagePass.setAlpha(1);
							// imageLike.setAlpha(0);
							Likes = 0;
						} else if (x_cord > (screenCenter + (screenCenter / 2))) {
							// imageLike.setAlpha(1);
							likingStatus.setImageDrawable(getResources()
									.getDrawable(R.drawable.ic_smiley_face));
							if (x_cord > (windowwidth - (screenCenter / 4))) {
								Likes = 1;
								saveSignal=true; //swipe to left is to save and like
							} else {
								Likes = 0;
							}
						} else if (x_cord < (screenCenter / 2)) {
							// imagePass.setAlpha(1);
							likingStatus.setImageDrawable(getResources()
									.getDrawable(R.drawable.ic_smiley_face)); //HOME task 15
							if (x_cord < screenCenter / 4) {
								Likes = 1;
							} else {
								Likes = 0;
							}
						} else {
							Likes = 0;
							imagePass.setAlpha(0);
							imageLike.setAlpha(0);
							likingStatus.setImageDrawable(getResources()
									.getDrawable(R.drawable.ic_blank_star_2));

						}

						if (!(iX == 0)) {
							myRelView
									.setRotation((float) ((x_cord - screenCenter)
											* (Math.PI / 100) * dir));
						}
						break;
					}

					case MotionEvent.ACTION_UP:
						imageItemX = ((int) myRelView.getX());
						imageItemY = ((int) myRelView.getY());
						tempX = imageItemX;
						myRelView.setRotation(0);
						imagePass.setAlpha(0);
						imageLike.setAlpha(0);

						likingStatus.setImageDrawable(getResources()
								.getDrawable(R.drawable.ic_blank_star_2));

						if (myRelView.getX() == IMAGE_MARGIN
								&& myRelView.getY() == IMAGE_MARGIN) {
							// showImageCaptionDialog();  //HOMEtask13
							break;
						}

						myRelView.setX(IMAGE_MARGIN);
						myRelView.setY(IMAGE_MARGIN);

						if (Likes == 0) {
							Log.e("Event Status", "Skipped"); //nothing changed to Skipped
							myRelView.setX(IMAGE_MARGIN);
							myRelView.setY(IMAGE_MARGIN);
							myRelView.setRotation(0);
						} else if ((Likes == 1)&(saveSignal==false)) {
							Log.e("Event Status", "Liked");
							parentView.removeView(myRelView);
						} else if ((Likes == 1)&(saveSignal==true)) {
							Log.e("Event Status", "Saved");
							parentView.removeView(myRelView);
						}
						break;
					default:
						break;
					}
					return true;
				}
			});

			parentView.addView(myRelView);
		}
	}

/*	private void showImageCaptionDialog() {
		// custom dialog
		final Dialog dialog = new Dialog(getActivity());
		dialog.setContentView(R.layout.image_caption_dialog);
		dialog.setTitle("Title");

		Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
		// if button is clicked, close the custom dialog
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.show();
	}*/ // HOMEtask13 app.asana.com/0/14441527162791/15561088605854
	
	
	
}
