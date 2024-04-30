package com.example.myapplication;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;

public class MainActivity extends AppCompatActivity {
    static String TAG = "báo lỗi";
    static int sizeScale = 0;
    ImageView imageViewInput, imageViewOutput;
    Button buttonChooseImage, buttonChangeTone, buttonFlip, buttonGaussian, buttonZoomIn, buttonZoomOut;
    PermissionManager permissionManager;
    ImageProcessor imageProcessor;
    Mat mat;

    private static final int PERMISSION_REQUEST_CODE = 123;

    private final ActivityResultLauncher<String> getImageLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
        if (uri != null) {
            imageViewInput.setImageURI(uri);
            mat = ImageProcessor.uriToMat(this, uri);
        }
    });

    static {
        if (!OpenCVLoader.initLocal()) {
            Log.e(TAG, "Không thể khởi tạo OpenCV");
        } else {
            Log.d(TAG, "OpenCV đã được khởi tạo thành công");
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        permissionManager = new PermissionManager(this);

        imageProcessor = new ImageProcessor();

        imageViewInput = findViewById(R.id.imageViewInput);
        imageViewOutput = findViewById(R.id.imageViewOutput);

        buttonChooseImage = findViewById(R.id.buttonChooseImage);
        buttonChooseImage.setOnClickListener(v -> {
            permissionManager.checkAndRequestPermission();
            if (permissionManager.status == 1) {
                openFileChooser();
            }
        });

        buttonChangeTone = findViewById(R.id.buttonChangeColor);
        buttonChangeTone.setOnClickListener(v -> {
            if (mat != null) {
                imageProcessor.changeTone(mat, 160, imageViewOutput);
            }
        });

        buttonFlip = findViewById(R.id.buttonFlip);
        buttonFlip.setOnClickListener(v -> {
            if (mat != null) {
                imageProcessor.flipImage(mat, imageViewOutput);
            }
        });

        buttonGaussian = findViewById(R.id.buttonGaussian);
        buttonGaussian.setOnClickListener(v -> {
            if (mat != null) {
                imageProcessor.GaussianBlur(mat, 7, imageViewOutput);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Xử lý kết quả của yêu cầu quyền.
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openFileChooser();
            } else {
                // Quyền không được cấp. Hiển thị thông báo cho người dùng biết rằng ứng dụng không thể thực hiện hành động mong muốn.
                permissionManager.showPermissionDeniedMessage();
            }
        }
    }

    private void openFileChooser() {
        // Sử dụng ActivityResultLauncher để mở hộp thoại truy cập bộ nhớ
        getImageLauncher.launch("image/*");
    }
}