package com.app.pictolike.helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.app.pictolike.R;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

/**
 * Created by Alexandr on 10/17/2014.
 */
public class ImageLoaderHelper {
    private final ImageLoader imageLoader;
    private final DisplayImageOptions options;

    public ImageLoaderHelper(Context pContext){
        imageLoader = ImageLoader.getInstance();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                pContext).threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheSize(50 * 1024 * 1024) // 50 Mb
                .tasksProcessingOrder(QueueProcessingType.LIFO).writeDebugLogs() // Remove
                        // for
                        // release
                        // app
                .build();

        imageLoader.init(config);

        options = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.ic_empty)
                .resetViewBeforeLoading(true).cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true).displayer(new FadeInBitmapDisplayer(300)).build();
    }

    public void displayImage(final String pUrl, final ImageView pIv) {
        imageLoader.displayImage(pUrl,pIv,options);
    }
}
