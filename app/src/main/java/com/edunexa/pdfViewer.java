package com.edunexa;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.edunexa.rur.fragments.pdf;
import com.pdfview.PDFView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.util.Arrays;

public class pdfViewer extends AppCompatActivity {
    private PDFView pdfView;
    private String fileurl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pdf_viewer);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        pdfView = (PDFView)findViewById(R.id.viewPdf);
        fileurl = getIntent().getStringExtra("PDf");

        pdfView.computeScroll();

        new fileDownload().execute(fileurl);
    }

    private class fileDownload extends AsyncTask<String, Void, InputStream> {

        @Override
        protected InputStream doInBackground(String... strings) {
            InputStream inputStreame = null;
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                if (urlConnection.getResponseCode() == 200)
                    inputStreame = new BufferedInputStream(urlConnection.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
                return inputStreame;
            }
            return inputStreame;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            if (inputStream != null) {
                String fileName = "example.pdf";
                String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);

                if (fileExtension.equalsIgnoreCase("pdf")) {
                    Toast.makeText(pdfViewer.this, "please wait while the pdf is loading", Toast.LENGTH_SHORT).show();
                    File file = new File("output.pdf");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        try {
                            Files.copy(inputStream, file.toPath());
                        } catch (IOException e) {
                            Toast.makeText(pdfViewer.this, "file not copied", Toast.LENGTH_SHORT).show();
                        }
                    }
                    pdfView.fromFile(file).show();
                }
            } else {
                Toast.makeText(pdfViewer.this, "Failed to load pdf", Toast.LENGTH_SHORT).show();
            }
        }
    }
}