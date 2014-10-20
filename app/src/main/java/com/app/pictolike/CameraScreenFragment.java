package com.app.pictolike;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.pictolike.Utils.AppConfig;
import com.app.pictolike.Utils.ConfirmDlg;
import com.app.pictolike.Utils.LocationMgr;
import com.app.pictolike.Utils.UploadWaitDlg;
import com.app.pictolike.mysql.MySQLCommand;
import com.app.pictolike.mysql.MySQLConnect;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressLint("InlinedApi")
public class CameraScreenFragment extends Fragment implements SurfaceHolder.Callback {

    private static final String TAG = CameraScreenFragment.class.getSimpleName();

    private Camera camera = null;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private ImageView captureButton, flipButton, flashButton;
    private ImageView locview;

    boolean check = false;
    boolean isFlashOn = false;
    int camFlashIndex = 0;

    private UploadWaitDlg waitingDlg;

    /** CallBack event we want to execute when user takes an image */
    private PictureCallback pictureBack = new PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, final Camera camera) {
            final PhotoFile photoFile = getOutputMediaFile();

            if (photoFile.photoFile == null) {
                return;
            }

            String title = "Upload?";
            String msg = "";

            ConfirmDlg confirmlDlg = new ConfirmDlg(CameraScreenFragment.this.getActivity(), title,
                    null);
            confirmlDlg.setButtonText("Yes", "No");
            confirmlDlg.setConfirmListener(new ConfirmDlg.ConfirmListener() {

                @Override
                public void onOkClick() {
                    uploadPicture(photoFile);
                    reconnectCamera(camera);
                }

                @Override
                public void onCancelClick() {
                    reconnectCamera(camera);
                }
            });

            confirmlDlg.show();

            try {
                FileOutputStream fos = new FileOutputStream(photoFile.photoFile);
                fos.write(data);
                fos.close();
                Toast.makeText(getActivity(),
                        "Image saved to phone" , Toast.LENGTH_LONG)
                        .show();
            } catch (FileNotFoundException e) {
                if (AppConfig.DEBUG) {
                    Log.d(TAG, "onPictureTaken :: failed to load image", e);
                }
            } catch (IOException e) {
                if (AppConfig.DEBUG) {
                    Log.d(TAG, "onPictureTaken :: failed to load image", e);
                }
            }

        }
    };

    /* **************************************************************** */
    /* ************************** Fragment **************************** */
    /* **************************************************************** */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_camerascreen, container, false);
        setupViews(rootView);
        return rootView;
    }

    @Override
    public void onDestroy() {
        disableCamera();
        super.onDestroy();
    }
    
    /* **************************************************************** */
    /* ******************** SurfaceHolder.Callback ******************* */
    /* **************************************************************** */

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            if (!initCameraIfNeeded()) {
                return;
            }
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        } catch (IOException e) {
            if (AppConfig.DEBUG) {
                Log.d(TAG, "surfaceCreated :: setPreviewDisplay error", e);
            }
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (camera != null) {
            camera.stopPreview();
            camera.setPreviewCallback(null);
            camera.release();
            camera = null;
        }
    }

    /* **************************************************************** */
    /* ************************ Utility API *************************** */
    /* **************************************************************** */

    private boolean initCameraIfNeeded() {
        try {
            if (null == camera) {
                camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
                camera.setDisplayOrientation(90);
            }
            return true;
        } catch (Exception e) {
            if (AppConfig.DEBUG) {
                Log.e(TAG, "setupViews :: camera open", e);
            }
            return false;
        }
    }
    
    private void setupViews(View rootView) {
        locview = (ImageView) rootView.findViewById(R.id.locview);
        locview.setVisibility(View.INVISIBLE);
        /** Mapping the capture and flip button from the xml */
        captureButton = (ImageView) rootView.findViewById(R.id.captureButton);
        flipButton = (ImageView) rootView.findViewById(R.id.btn_selfie);
        flashButton = (ImageView) rootView.findViewById(R.id.btn_flash);
        /** Making the Surface View Holder for the camera Preview */
        surfaceView = (SurfaceView) rootView.findViewById(R.id.surfaceview);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        if (!initCameraIfNeeded()) {
            return;
        }

        final boolean haveFlash = getActivity().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA_FLASH);
        final Parameters p = camera.getParameters();

        if (!haveFlash) {
            flashButton.setImageResource(R.drawable.ic_no_flash);
        }

        flashButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (haveFlash && !check && camera != null) {
                    if (isFlashOn) {
                        p.setFlashMode(Parameters.FLASH_MODE_OFF);
                        flashButton.setImageResource(R.drawable.ic_flash_off);
                        camera.setParameters(p);
                        camera.startPreview();
                        isFlashOn = false;
                    } else {
                        p.setFlashMode(Parameters.FLASH_MODE_ON);
                        flashButton.setImageResource(R.drawable.ic_flash_on);
                        camera.setParameters(p);
                        camera.startPreview();
                        isFlashOn = true;
                    }
                }
            }
        });

        /**
         * Functionality of flip Button onClickListener where we want to flip
         * the camera
         */
        flipButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!check) {
                    if (camera != null) {
                        camera.stopPreview();
                        camera.setPreviewCallback(null);
                        camera.release();
                        camera = null;
                        surfaceHolder.removeCallback(CameraScreenFragment.this);
                        surfaceHolder = null;
                    }

                    surfaceHolder = surfaceView.getHolder();
                    surfaceHolder.addCallback(CameraScreenFragment.this);
                    surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
                    camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
                    camera.setDisplayOrientation(90);
                    try {
                        camera.setPreviewDisplay(surfaceHolder);
                        flipButton.setImageResource(R.drawable.ic_selfie_on);
                        flashButton.setImageResource(R.drawable.ic_no_flash);
                        isFlashOn = false;
                    } catch (IOException e) {
                        if (AppConfig.DEBUG) {
                            Log.e(TAG, "setupViews :: camera.setPreviewDisplay error", e);
                        }
                    }
                    camera.startPreview();
                    check = true;
                    return;
                } else if (check) {
                    if (camera != null) {
                        camera.stopPreview();
                        camera.setPreviewCallback(null);
                        camera.release();
                        camera = null;
                        surfaceHolder.removeCallback(CameraScreenFragment.this);
                        surfaceHolder = null;
                    }
                    surfaceHolder = surfaceView.getHolder();
                    surfaceHolder.addCallback(CameraScreenFragment.this);
                    surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
                    camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
                    camera.setDisplayOrientation(90);

                    try {
                        camera.setPreviewDisplay(surfaceHolder);
                        flipButton.setImageResource(R.drawable.ic_selfie_on);
                        flashButton.setImageResource(R.drawable.ic_flash_off);
                    } catch (IOException e) {
                        if (AppConfig.DEBUG) {
                            Log.e(TAG, "setupViews :: camera.setPreviewDisplay error", e);
                        }
                    }
                    camera.startPreview();
                    check = false;
                    return;
                }
            }
        });

        /** Functionality of Capture Button where we want to save the image */
        captureButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                captureImage();
            }
        });

    }

    private void disableCamera() {
        locview.setVisibility(View.VISIBLE);

        if (camera != null) {
            camera.stopPreview();
            camera.setPreviewCallback(null);
            camera.release();
            camera = null;
        }
    }

    private void uploadPicture(PhotoFile photoFile) {
        // show animation dlg.
        waitingDlg = new UploadWaitDlg(this.getActivity());
        waitingDlg.show();

        String username = SignInActivity.userNameString;
        String filename = photoFile.fileName;
        String datecreated = photoFile.timeStatmp;
        String locationcreated = LocationMgr.getInstance(getActivity()).getLocation();
        float latitude = LocationMgr.getInstance(getActivity()).getLatitude();
        float longitude = LocationMgr.getInstance(getActivity()).getLongitude();
        String deviceID = "";
        String userage = "";
        String gender = "";

        MySQLConnect.savefile(username, filename, datecreated, locationcreated, deviceID, userage,
                gender,photoFile.file.getAbsolutePath(),longitude,latitude, new MySQLCommand.OnCompleteListener() {

                    @Override
                    public void OnComplete(Object result) {
                        waitingDlg.showMessage("Photo upload complete!");
                    }
                });

    }

    /**
     * Calls when user pushes the shutter button,this method includes the
     * callback to the camera api
     */
    private void captureImage() {
       // locview.setVisibility(View.GONE);
        if (camera == null)
            return;

        camera.takePicture(null, null, pictureBack);
    }

    @Override
    public void onResume() {
       /* if(!SharedpreferenceUtility.getInstance(getActivity()).getBoolean(Constant.CAMERAFIRSTLAUNCH)){
            locview.setBackgroundResource(R.drawable.camera_activation);
            SharedpreferenceUtility.getInstance(getActivity()).putBoolean(Constant.CAMERAFIRSTLAUNCH,true);
        }else{
            surfaceView.setVisibility(View.VISIBLE);
            locview.setVisibility(View.GONE);
            locview.setBackground(null);
        }*/
        super.onResume();
    }

    private void reconnectCamera(final Camera camera) {
        try {
            camera.reconnect();
            camera.startPreview();
        } catch (Exception e) {
            if (AppConfig.DEBUG) {
                Log.e(TAG, "reconnectCamera :: failed", e);
            }
        }
    }
    
    /**
     * getOutputMediaFile returns the file path on the device where we want to
     * save the picture
     */
    private static PhotoFile getOutputMediaFile() {
        String timeStampDir = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        File mediaStorageDir = Environment.getExternalStorageDirectory();
        File dir = new File(mediaStorageDir, timeStampDir);

        if (!dir.isDirectory()) {
            dir.mkdirs();
            if (AppConfig.DEBUG) {
                Log.d(TAG, "getOutputMediaFile :: failed to create directory");
            }
        }

        // Create a media file name
        Date now = new Date();
        String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(now);
        String fileName = "IMG_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(now) + ".png";

        File mediaFile = null;
        try {
            mediaFile = File.createTempFile(File.separator + "IMG_"
                    + new SimpleDateFormat("yyyyMMdd_HHmmss").format(now), ".png", dir);
        } catch (IOException e) {
            if (AppConfig.DEBUG) {
                Log.d(TAG, "getOutputMediaFile :: failed to createTempFile", e);
            }
        }

        PhotoFile photoFile = new PhotoFile();
        photoFile.timeStatmp = timeStamp;
        photoFile.fileName = fileName;
        photoFile.file=mediaFile;
        photoFile.photoFile = mediaFile;

        return photoFile;
    }

    private static class PhotoFile {

        public String timeStatmp;
        public String fileName;
        public File photoFile;
        public File file;
    }

}
