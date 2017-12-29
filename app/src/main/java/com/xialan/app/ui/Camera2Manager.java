package com.xialan.app.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureFailure;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;

import com.xialan.app.utils.UIUtils;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by ${苗} on 2017/11/1.
 */

public class Camera2Manager {
    private final TextureView mTextureView;
    private final int mCamera_Id;
    private final Activity mActivity;
    private CaptureRequest.Builder previewRequestBuilder;
    private CaptureRequest.Builder captureRequestBuilder;
    private ImageReader imageReader;
    private OnCaptureCompletedListener mOnCaptureCompletedListener;
    private int mPreview;
    private Surface outputTarget;
    private Size mPreviewSize;
    private SurfaceTexture surfaceTexture;

    public Camera2Manager(TextureView textureView, int cameraid, Activity activity) {
        this.mTextureView = textureView;
        this.mCamera_Id = cameraid;
        this.mActivity = activity;

        initTextureview();
    }

    private void initTextureview() {
        mTextureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
                //textureview可用(活跃状态)
                openCamera();
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
                stopCamera();
                return true;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

            }
        });
    }

    /**
     * 开启相机
     */
    private void openCamera() {
        //设定输出目标
        surfaceTexture = mTextureView.getSurfaceTexture();
        //获取camera2manager
        CameraManager camera2Manager = (CameraManager) mActivity.getSystemService(Context.CAMERA_SERVICE);
        try {
            if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            CameraCharacteristics characteristics = camera2Manager.getCameraCharacteristics(mCamera_Id+"");
            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            mPreviewSize = getPreferredPreviewSize(map.getOutputSizes(SurfaceTexture.class), mTextureView.getWidth(), mTextureView.getHeight());
            surfaceTexture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());
            camera2Manager.openCamera(mCamera_Id + "", cameraOpenCallBack, null);
        } catch (Exception e) {
            UIUtils.showToast(mActivity,"摄像头连接失败!需要重新开启");
            e.printStackTrace();
        }
    }
    private Size getPreferredPreviewSize(Size[] sizes, int width, int height) {
        List<Size> collectorSizes = new ArrayList<>();
        for (Size option : sizes) {
            if (width > height) {
                if (option.getWidth() > width && option.getHeight() > height) {
                    collectorSizes.add(option);
                }
            } else {
                if (option.getHeight() > width && option.getWidth() > height) {
                    collectorSizes.add(option);
                }
            }
        }
        for (int i=0;i<collectorSizes.size();i++){
            Log.e("分辨率",collectorSizes.get(i)+"");
        }

        if (collectorSizes.size() > 0) {
            return Collections.min(collectorSizes, new Comparator<Size>() {
                @Override
                public int compare(Size s1, Size s2) {
                    return Long.signum(s1.getWidth() * s1.getHeight() - s2.getWidth() * s2.getHeight());
                }
            });
        }

        return sizes[0];
    }

    private CameraDevice mCameraDevice;
    //打开相机时候的监听器，通过他可以得到相机实例，这个实例可以创建请求建造者
    private CameraDevice.StateCallback cameraOpenCallBack = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(CameraDevice cameraDevice) {
            mCameraDevice = cameraDevice;
            startPreview();
        }

        @Override
        public void onDisconnected(CameraDevice cameraDevice) {
            Log.d(TAG, "相机连接断开");
            if (mCameraDevice != null) {
                mCameraDevice = null;
            }
        }

        @Override
        public void onError(CameraDevice cameraDevice, int i) {
            stopCamera();
            Log.d(TAG, "相机打开失败");
        }
    };

    /**
     * 开始预览
     */
    private void startPreview() {
        mTextureView.setScaleX(-1);
        mTextureView.setScaleY(1);
        //构建imagereader 用于读写相机流数据
        imageReader = ImageReader.newInstance(mPreviewSize.getWidth(), mPreviewSize.getHeight(), ImageFormat.JPEG, 1);
        imageReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
            @Override
            public void onImageAvailable(ImageReader imageReader) {
                Image image = imageReader.acquireNextImage();
                ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                byte[] data = new byte[buffer.remaining()];
                buffer.get(data);
                //显示图片
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 1;
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);
                Bitmap  newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(newBitmap);
                Matrix matrix = new Matrix();
                matrix.setScale(-1, 1);
                Bitmap new2 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
                canvas.drawBitmap(new2, new Rect(0, 0, new2.getWidth(), new2.getHeight()), new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()), null);
                mOnCaptureCompletedListener.captureSuccess(newBitmap);
                buffer.clear();
                image.close();
            }
        }, null);

        outputTarget = new Surface(surfaceTexture);
        resetCamera();
    }

    /**
     * 重新开始预览 读取
     */
    private void resetCamera() {
        //预览请求构建
        try {
            previewRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            previewRequestBuilder.addTarget(outputTarget);
            // 重设自动对焦模式
            previewRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER,
                    CameraMetadata.CONTROL_AF_TRIGGER_CANCEL);
            // 设置自动曝光模式
            previewRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE,
                    CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
            //创建输出预览目标
            mCameraDevice.createCaptureSession(Arrays.asList(imageReader.getSurface(), outputTarget), stateCallback, null);
        } catch (Exception e) {
            UIUtils.showToast(mActivity,"摄像头连接失败!需要重新开启");
            e.printStackTrace();
        }
    }

    //预览对象
    private CameraCaptureSession mPreviewSession;
    //预览页面创建监听
    private CameraCaptureSession.StateCallback stateCallback = new CameraCaptureSession.StateCallback() {

        @Override
        public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
            CaptureRequest mCaptureRequest = previewRequestBuilder.build();
            mPreviewSession = cameraCaptureSession;
            try {
                //反复读取 实现不间断预览
                mPreviewSession.setRepeatingRequest(mCaptureRequest, null, null);
            } catch (Exception e) {
                UIUtils.showToast(mActivity,"摄像头连接失败!需要重新开启");
                e.printStackTrace();
            }
        }

        @Override
        public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
            //创建预览失败
            UIUtils.showToast(mActivity,"摄像头开启失败!需要重新开启");
        }
    };

    public void takePicture(OnCaptureCompletedListener onCaptureCompletedListener) {
        if (mOnCaptureCompletedListener != null) {
            mOnCaptureCompletedListener = null;
        }
        mOnCaptureCompletedListener = onCaptureCompletedListener;
        try {
            captureRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureRequestBuilder.addTarget(imageReader.getSurface());
            mPreviewSession.stopRepeating();
            captureRequestBuilder.addTarget(imageReader.getSurface());
            // 重设自动对焦模式
            captureRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER,
                    CameraMetadata.CONTROL_AF_TRIGGER_CANCEL);
            // 设置自动曝光模式
            captureRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE,
                    CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
            mPreviewSession.capture(captureRequestBuilder.build(),captureCallback,null);
        } catch (Exception e) {
            UIUtils.showToast(mActivity,"摄像头连接失败!需要重新开启");
            e.printStackTrace();
        }
    }
    public interface OnCaptureCompletedListener {
        void captureSuccess(Bitmap bitmap);
        void captureFailed();
    }

    /**
     * 监听拍照结果
     */
    private CameraCaptureSession.CaptureCallback captureCallback = new CameraCaptureSession.CaptureCallback() {
        // 拍照成功
        @Override
        public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
            resetCamera();
        }

        @Override
        public void onCaptureFailed(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull CaptureFailure failure) {
            super.onCaptureFailed(session, request, failure);

            mOnCaptureCompletedListener.captureFailed();
            stopCamera();
        }
    };
    /**
     * 停止拍照释放资源
     */
    public void stopCamera() {
        try{
            if (mPreviewSession != null) {
                mPreviewSession.close();
            }
            if (imageReader != null) {
                imageReader.close();
            }
            if (mCameraDevice != null) {
                mCameraDevice.close();
                mCameraDevice = null;
            }
        }catch (Exception e){

        }

        System.gc();
    }

}
