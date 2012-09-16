package org.qii.weiciyuan.support.imagetool;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import org.qii.weiciyuan.support.file.FileDownloaderHttpHelper;
import org.qii.weiciyuan.support.file.FileLocationMethod;
import org.qii.weiciyuan.support.file.FileManager;
import org.qii.weiciyuan.support.http.HttpUtility;

import java.io.File;

/**
 * User: Jiang Qi
 * Date: 12-8-3
 */
public class ImageTool {

    private static final int MAX_WIDTH = 480;
    private static final int MAX_HEIGHT = 800 * 2;


    public static Bitmap getPictureThumbnailBitmap(String url) {


        String absoluteFilePath = FileManager.getFileAbsolutePathFromUrl(url, FileLocationMethod.picture_thumbnail);

        Bitmap bitmap = BitmapFactory.decodeFile(absoluteFilePath);

        if (bitmap != null) {
            return ImageEdit.getRoundedCornerBitmap(bitmap);
        } else {
            String path = getBitmapFromNetWork(url, absoluteFilePath, null);
            bitmap = BitmapFactory.decodeFile(path);
            if (bitmap != null)
                return ImageEdit.getRoundedCornerBitmap(bitmap);
        }
        return null;

    }


    public static Bitmap getThumbnailPictureWithRoundedCorner(String url, int reqWidth, int reqHeight, FileDownloaderHttpHelper.DownloadListener downloadListener) {


        String absoluteFilePath = FileManager.getFileAbsolutePathFromUrl(url, FileLocationMethod.picture_thumbnail);

        Bitmap bitmap = decodeBitmapFromSDCard(absoluteFilePath, reqWidth, reqHeight);

        if (bitmap == null) {
            String path = getBitmapFromNetWork(url, absoluteFilePath, downloadListener);
            bitmap = decodeBitmapFromSDCard(path, reqWidth, reqHeight);
        }

        if (bitmap != null) {
            if (bitmap.getHeight() > reqHeight || bitmap.getWidth() > reqWidth) {
                bitmap = cutPic(bitmap, reqWidth, reqHeight);
            }
            bitmap = ImageEdit.getRoundedCornerBitmap(bitmap);
        }

        return bitmap;
    }


    public static Bitmap getBigAvatarWithoutRoundedCorner(String url, int reqWidth, int reqHeight) {


        String absoluteFilePath = FileManager.getFileAbsolutePathFromUrl(url, FileLocationMethod.avatar_large);
        absoluteFilePath = absoluteFilePath + ".jpg";

        Bitmap bitmap = BitmapFactory.decodeFile(absoluteFilePath);

        if (bitmap == null) {
            getBitmapFromNetWork(url, absoluteFilePath, null);
            bitmap = BitmapFactory.decodeFile(absoluteFilePath);
        }

        if (bitmap != null) {
            return Bitmap.createScaledBitmap(bitmap, reqWidth, reqHeight, true);
        }

        return bitmap;
    }

    public static Bitmap getBigAvatarWithRoundedCorner(String url) {


        String absoluteFilePath = FileManager.getFileAbsolutePathFromUrl(url, FileLocationMethod.avatar_large);
        absoluteFilePath = absoluteFilePath + ".jpg";

        Bitmap bitmap = BitmapFactory.decodeFile(absoluteFilePath);

        if (bitmap == null) {
            String path = getBitmapFromNetWork(url, absoluteFilePath, null);
            bitmap = BitmapFactory.decodeFile(absoluteFilePath);
        }

        if (bitmap != null) {
            bitmap = ImageEdit.getRoundedCornerBitmap(bitmap);
        }

        return bitmap;
    }

    public static Bitmap getSmallAvatarWithRoundedCorner(String url) {


        String absoluteFilePath = FileManager.getFileAbsolutePathFromUrl(url, FileLocationMethod.avatar_small);

        absoluteFilePath = absoluteFilePath + ".jpg";

        Bitmap bitmap = BitmapFactory.decodeFile(absoluteFilePath);

        if (bitmap == null) {
            String path = getBitmapFromNetWork(url, absoluteFilePath, null);
            bitmap = BitmapFactory.decodeFile(path);
        }
        if (bitmap != null) {
            bitmap = ImageEdit.getRoundedCornerBitmap(bitmap);
        }
        return bitmap;
    }

    public static Bitmap getMiddlePictureWithRoundedCorner(String url, FileDownloaderHttpHelper.DownloadListener downloadListener) {


        String absoluteFilePath = FileManager.getFileAbsolutePathFromUrl(url, FileLocationMethod.picture_bmiddle);

        Bitmap bitmap = decodeBitmapFromSDCard(absoluteFilePath, MAX_WIDTH, MAX_HEIGHT);

        if (bitmap == null) {
            String path = getBitmapFromNetWork(url, absoluteFilePath, downloadListener);
            bitmap = decodeBitmapFromSDCard(path, MAX_WIDTH, MAX_HEIGHT);
        }
        if (bitmap != null) {
            bitmap = ImageEdit.getRoundedCornerBitmap(bitmap);
        }
        return bitmap;
    }


    public static String getLargePictureWithoutRoundedCorner(String url, FileDownloaderHttpHelper.DownloadListener downloadListener) {


        String absoluteFilePath = FileManager.getFileAbsolutePathFromUrl(url, FileLocationMethod.picture_large);

        File file = new File(absoluteFilePath);

        if (file.exists()) {
            return absoluteFilePath;

        } else {
            String path = getBitmapFromNetWork(url, absoluteFilePath, downloadListener);

            file = new File(path);
            if (file.exists()) {
                return absoluteFilePath;
            } else {
                return "about:blank";
            }


        }

    }


    public static String getMiddlePictureWithoutRoundedCorner(String url, FileDownloaderHttpHelper.DownloadListener downloadListener) {


        String absoluteFilePath = FileManager.getFileAbsolutePathFromUrl(url, FileLocationMethod.picture_bmiddle);

        File file = new File(absoluteFilePath);

        if (file.exists()) {
            return absoluteFilePath;

        } else {
            String path = getBitmapFromNetWork(url, absoluteFilePath, downloadListener);

            file = new File(path);
            if (file.exists()) {
                return absoluteFilePath;
            } else {
                return "about:blank";
            }


        }

    }


    private static Bitmap decodeBitmapFromSDCard(String path,
                                                 int reqWidth, int reqHeight) {


        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, options);

    }


    private static Bitmap cutPic(Bitmap ori, int reqWidth, int reqHeight) {
        Bitmap bitmap = ori;
//        int reqWidth = 396;
//        int reqHeight = 135;

        //resize width to reqWidth
//        if (bitmap.getWidth() < reqWidth) {
//            float width = bitmap.getWidth();
//            float s = reqWidth / width;
//            Matrix matrix = new Matrix();
//            matrix.setScale(s, s);
//            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
//        }
        //then cut middle
        int height = reqHeight < bitmap.getHeight() ? reqHeight : bitmap.getHeight();
        if (height > 0) {
            int needStart = (bitmap.getHeight() - height) / 2;
            Bitmap cropped = Bitmap.createBitmap(bitmap, 0, needStart, bitmap.getWidth(), height);
            return cropped;
        } else {
            return bitmap;
        }
    }


    private static String getBitmapFromNetWork(String url, String path, FileDownloaderHttpHelper.DownloadListener downloadListener) {

        HttpUtility.getInstance().executeDownloadTask(url, path, downloadListener);
        return path;

    }

    private static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {

        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (height > reqHeight && reqHeight != 0) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else if (width > reqWidth && reqWidth != 0) {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }

        }
        return inSampleSize;
    }
}


