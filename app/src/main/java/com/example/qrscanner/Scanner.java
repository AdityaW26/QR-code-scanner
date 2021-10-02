package com.example.qrscanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

@SuppressWarnings("deprecation")
public class Scanner extends AppCompatActivity {

    CodeScanner codeScanner;
    CodeScannerView scannerView;
    BottomSheetBehavior bottomSheetBehavior ;
    TextView result;
    ImageButton webSearch, copy;
    String QRresult;
    CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        intiValues();

        codeScanner.setDecodeCallback(result -> runOnUiThread(() -> {
            QRresult = result.getText();
            displayResult(QRresult);
        }));

        scannerView.setOnClickListener(v -> codeScanner.startPreview());

        webSearch.setOnClickListener(v -> {
            if (QRresult!=null && !TextUtils.isEmpty(QRresult)) {
                Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(QRresult));
                startActivity(webIntent);
            }
        });

        copy.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText(null, QRresult);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(Scanner.this, "Copied to clipboard", Toast.LENGTH_SHORT).show();
        });

    }

    private void intiValues() {
        scannerView = findViewById(R.id.scanner_view);
        codeScanner = new CodeScanner(this,scannerView);

        View bottomSheet = findViewById(R.id.bottomSheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        result = findViewById(R.id.result);
        webSearch = findViewById(R.id.webSearch);
        copy = findViewById(R.id.copy);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        coordinatorLayout.setVisibility(View.GONE);
    }

    private void displayResult(String qRresult) {

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        coordinatorLayout.setVisibility(View.VISIBLE);
        result.setText(qRresult);

        if (qRresult.contains("https")){
            webSearch.setVisibility(View.VISIBLE);
        }else {
            webSearch.setVisibility(View.GONE);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        requestForCamera();
    }

    private void requestForCamera() {
        Dexter.withActivity(this).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                codeScanner.startPreview();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                Toast.makeText(Scanner.this,"Camera Permission is required.",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
    }
}