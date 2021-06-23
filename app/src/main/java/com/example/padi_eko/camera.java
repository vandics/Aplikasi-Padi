package com.example.padi_eko;



import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class camera extends AppCompatActivity {

    private ImageView foto_tampil, imageView8;
    private Button btn_balik_menu, btn_hitung_urea;
    private TextView text_RGB, text_skala, text_rendah, text_sedang,
            text_tinggi, text_sgtTinggi, text_Urea, textView;
    private View view_warna;
    private EditText edit_Nilai_tanah;
    Bitmap bitmap_ambilfoto;
    int pixels, kondisi;
    public String currentPhotoPath;
    File photoFile = null;

    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        foto_tampil = findViewById(R.id.foto_tampil);
        btn_balik_menu = findViewById(R.id.btn_balik_menu);
        btn_hitung_urea = findViewById(R.id.btn_hitung_Urea);
        text_RGB = findViewById(R.id.text_RGB);
        text_tinggi = findViewById(R.id.text_tinggi);
        text_skala = findViewById(R.id.text_skala);
        text_rendah = findViewById(R.id.text_rendah);
        text_sedang = findViewById(R.id.text_sedang);
        text_sgtTinggi = findViewById(R.id.text_sgtTinggi);
        text_Urea = findViewById(R.id.text_Urea);
        view_warna = findViewById(R.id.view_warna);
        edit_Nilai_tanah = findViewById(R.id.edit_Nilai_tanah);
        imageView8 = findViewById(R.id.imageView8);
        textView = findViewById(R.id.textView);

        foto_tampil.setDrawingCacheEnabled(true);
        foto_tampil.buildDrawingCache(true);

        btn_hitung_urea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double luas_area = Double.parseDouble(edit_Nilai_tanah.getText().toString());
                double hasil_urea = (luas_area/10000)*120000;

                text_Urea.setText(Double.toString(hasil_urea));
            }
        });

        kondisi = getIntent().getExtras().getInt("kondisi");

        if(kondisi == 1){
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, 2);
            getIntent().removeExtra("kondisi");


        }
        else{
            getIntent().removeExtra("kondisi");
            Intent aa = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            if(aa.resolveActivity(getPackageManager()) != null){
                try {
                    photoFile = createImageFile();
                }catch (IOException ex){

                }
                if(photoFile != null){
                    Uri photoUri = FileProvider.getUriForFile(camera.this,
                            "com.example.padi_eko.fileprovider",photoFile);
                    aa.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                    startActivityForResult(aa, 200);

                }
            }

        }

        btn_balik_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(camera.this, MainActivity.class));
                finish();
            }
        });


    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 200 && resultCode == RESULT_OK){
            imageView8.setVisibility(View.GONE);
            textView.setVisibility(View.GONE);
            bitmap_ambilfoto = BitmapFactory.decodeFile(currentPhotoPath);
            bitmap_ambilfoto = rotateImage(bitmap_ambilfoto, 90);
            foto_tampil.setImageBitmap(bitmap_ambilfoto);
            foto_tampil.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE)
                    {

                        foto_tampil.setDrawingCacheEnabled(true);

                        bitmap_ambilfoto = foto_tampil.getDrawingCache();
                        pixels = bitmap_ambilfoto.getPixel((int)event.getX(), (int)event.getY());

                        int r = Color.red(pixels);
                        int g = Color.green(pixels);
                        int b = Color.blue(pixels);

                        view_warna.setBackgroundColor(Color.rgb(r,g,b));
                        text_RGB.setText("R("+r+")\t"+"G("+g+")\t"+"B("+b+")");
                        foto_tampil.setDrawingCacheEnabled(false);

                        skalaBWD(r,g,b);
                    }
                    return true;
                }

            });
        }

        else if(requestCode == 2 && resultCode == RESULT_OK && data != null && data.getData() != null){
            Uri uri = data.getData();
            Log.d("debug", "2");
            try {
                final Bitmap[] bitmap_fotogaleri = {MediaStore.Images.Media.getBitmap(getContentResolver(), uri)};
                imageView8.setVisibility(View.GONE);
                textView.setVisibility(View.GONE);
                foto_tampil.setImageBitmap(bitmap_fotogaleri[0]);

                foto_tampil.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if(event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE){
                            foto_tampil.setDrawingCacheEnabled(true);
                            bitmap_fotogaleri[0] = foto_tampil.getDrawingCache();

                            pixels = bitmap_fotogaleri[0].getPixel((int)event.getX(), (int)event.getY());

                            int r = Color.red(pixels);
                            int g = Color.green(pixels);
                            int b = Color.blue(pixels);

                            view_warna.setBackgroundColor(Color.rgb(r,g,b));
                            text_RGB.setText("R("+r+")\t"+"G("+g+")\t"+"B("+b+")");
                            foto_tampil.setDrawingCacheEnabled(false);

                            skalaBWD(r,g,b);

                        }
                        return true;
                    }
                });

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private File createImageFile() throws IOException{
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String ImageFileName = "IMG_" + timestamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(ImageFileName, ".jpg", storageDir);
        currentPhotoPath = null;
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }


    private void skalaBWD(int r, int g, int b){
        if((isBetween(r,78,155) && isBetween(g,129,190) && isBetween(b,65,148)))
        {
            text_skala.setText("skala 2");
            text_rendah.setText("75 kg/Ha");
            text_sedang.setText("100 kg/Ha");
            text_tinggi.setText("125 kg/Ha");
            text_sgtTinggi.setText("150 kg/Ha");

        }
        else if((isBetween(r,51,140) && isBetween(g,83,155) && isBetween(b,40,140)))
        {
            text_skala.setText("skala 3");
            text_rendah.setText("75 kg/Ha");
            text_sedang.setText("100 kg/Ha");
            text_tinggi.setText("125 kg/Ha");
            text_sgtTinggi.setText("150 kg/Ha");
        }
        else if((isBetween(r,28,121) && isBetween(g,53,131) && isBetween(b,29,120)))
        {
            text_skala.setText("skala 4");
            text_rendah.setText("50 kg/Ha");
            text_sedang.setText("75 kg/Ha");
            text_tinggi.setText("100 kg/Ha");
            text_sgtTinggi.setText("125 kg/Ha");
        }
        else if((isBetween(r,16,105) && isBetween(g,35,124) && isBetween(b,17,108)))
        {
            text_skala.setText("skala 5");
            text_rendah.setText("0 kg/Ha");
            text_sedang.setText("0-50 kg/Ha");
            text_tinggi.setText("50 kg/Ha");
            text_sgtTinggi.setText("50 kg/Ha");
        }
        else{
            text_skala.setText("tidak ada skala ");
            text_rendah.setText("tidak ada");
            text_sedang.setText("tidak ada");
            text_tinggi.setText("tidak ada");
            text_sgtTinggi.setText("tidak ada");

        }


    }


    public boolean isBetween(int x, int lower, int upper){

        return lower <= x && x <= upper;
    }


    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }


}