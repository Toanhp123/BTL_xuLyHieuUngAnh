package com.example.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.photo.Photo;

import java.io.InputStream;

public class ImageProcessor {
    @NonNull
    public static Mat uriToMat(Context context, Uri uri) {
        Mat mat = new Mat();
        try {
            // Đọc ảnh từ URI sử dụng BitmapFactory
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            // Chuyển đổi Bitmap thành Mat sử dụng Utils của OpenCV
            Utils.bitmapToMat(bitmap, mat);

            // Giải phóng tài nguyên
            assert inputStream != null : "inputStream is null";
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mat;
    }

    public void filterNLM(Mat inputMat, @NonNull ImageView imageView) {
        Mat resultMat = new Mat();
        Photo.fastNlMeansDenoisingColored(inputMat, resultMat, 20, 10, 7, 21);

        // Chuyển đổi kết quả thành ảnh bitmap
        Bitmap resultBitmap = Bitmap.createBitmap(resultMat.cols(), resultMat.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(resultMat, resultBitmap);

        // Hiển thị kết quả trên ImageView
        imageView.setImageBitmap(resultBitmap);
    }

    public void filterBilateral(Mat input, @NonNull ImageView imageView) {
        //chuyển RGBA sang RGB
        Imgproc.cvtColor(input, input, Imgproc.COLOR_BGRA2BGR);

        Mat resultMat = new Mat();
        Imgproc.bilateralFilter(input, resultMat, 10, 250, 50);

        //chuyển RGB sang RGBA
        Imgproc.cvtColor(resultMat, resultMat, Imgproc.COLOR_RGB2RGBA);

        // Chuyển đổi kết quả thành ảnh bitmap
        Bitmap resultBitmap = Bitmap.createBitmap(resultMat.cols(), resultMat.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(resultMat, resultBitmap);

        // Hiển thị kết quả trên ImageView
        imageView.setImageBitmap(resultBitmap);
    }

    public void changeTone(Mat input, int hueShift, @NonNull ImageView imageView) {
        // Chuyển đổi không gian màu RGB sang HSV
        Mat hsvImage = new Mat();
        Imgproc.cvtColor(input, hsvImage, Imgproc.COLOR_BGR2HSV);

        // Điều chỉnh giá trị hue
        Core.add(hsvImage, new Scalar(hueShift, 0, 0), hsvImage);

        // Chuyển đổi lại sang không gian màu BGR
        Mat resultMat = new Mat();
        Imgproc.cvtColor(hsvImage, resultMat, Imgproc.COLOR_HSV2BGR);

        // Chuyển đổi kết quả thành ảnh bitmap
        Bitmap resultBitmap = Bitmap.createBitmap(resultMat.cols(), resultMat.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(resultMat, resultBitmap);

        // Hiển thị kết quả trên ImageView
        imageView.setImageBitmap(resultBitmap);
    }

    public void flipImage(Mat input, @NonNull ImageView imageView) {
        //lật ảnh
        Mat resultMat = new Mat();
        Core.flip(input, resultMat, 1);

        // Chuyển đổi kết quả thành ảnh bitmap
        Bitmap resultBitmap = Bitmap.createBitmap(resultMat.cols(), resultMat.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(resultMat, resultBitmap);

        // Hiển thị kết quả trên ImageView
        imageView.setImageBitmap(resultBitmap);
    }

    public void GaussianBlur(Mat input, int kernelSize, @NonNull ImageView imageView) {
        //lọc gaussian
        Mat resultMat = new Mat();
        Imgproc.GaussianBlur(input, resultMat, new Size(kernelSize, kernelSize), 0);

        // Chuyển đổi kết quả thành ảnh bitmap
        Bitmap resultBitmap = Bitmap.createBitmap(resultMat.cols(), resultMat.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(resultMat, resultBitmap);

        // Hiển thị kết quả trên ImageView
        imageView.setImageBitmap(resultBitmap);
    }

    public void resizeImage(Mat input, double scale, @NonNull ImageView imageView) {
        //zoom in, zoom out theo scale
        Mat resultMat = new Mat();
        Imgproc.resize(input, resultMat, new Size(input.cols() * scale, input.rows() * scale));

        // Chuyển đổi kết quả thành ảnh bitmap
        Bitmap resultBitmap = Bitmap.createBitmap(resultMat.cols(), resultMat.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(resultMat, resultBitmap);

        // Hiển thị kết quả trên ImageView
        imageView.setImageBitmap(resultBitmap);
    }
}
