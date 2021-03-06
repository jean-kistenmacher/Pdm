package com.unisc.pdm;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.hardware.Camera.*;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

//https://stackoverflow.com/questions/48642695/capture-image-using-dynamic-co-ordinates-through-the-camera
public class TrabalhoActivity extends Activity implements SurfaceHolder.Callback {

    Camera camera;
    Camera.Parameters param;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    PictureCallback jpegCallback;
    Button btnCapture, btnSave;
    private DatabaseTrabalho helper;
    List<Imagem> listaInfo = new ArrayList<>();

    int angle = 0, count = 0, MAX = 5, roi = 128;
    private static final int CAMERA_REQUEST_CODE = 100;
    Bitmap bmap, aux;
    int[] RedHistogram = new int[256];
    int[] GreenHistogram = new int[256];
    int[] BlueHistogram = new int[256];
    ImageView photoImage = null, imageRoi = null;
    TextView txtR, txtG, txtB, txtCount, txtDebug, txtLastSample;
    Boolean autoscale = true, reverse = false;
    float X = 0, Y = 0; //X = 165, Y = 203;
    EditText txtSample;
    String[] nome;
    String[][] data;
    String erro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trabalho);

        photoImage = findViewById(R.id.imageView1);
        imageRoi = findViewById(R.id.imageRoi);
        imageRoi.getLayoutParams().height = (int) (Double.parseDouble(String.valueOf(roi)) * 1.16);
        imageRoi.getLayoutParams().width = (int) (Double.parseDouble(String.valueOf(roi)) * 1.16);

        txtR = findViewById(R.id.txtR);
        txtG = findViewById(R.id.txtG);
        txtB = findViewById(R.id.txtB);
        txtSample = findViewById(R.id.txtSample);
        txtCount = findViewById(R.id.txtCount);
        txtDebug = findViewById(R.id.txtDebug);
        txtLastSample = findViewById(R.id.txtLastSample);

        txtR.setText("");
        txtG.setText("");
        txtB.setText("");
        txtLastSample.setText("");

        txtCount.setText((count + 1) + "/" + MAX);

        data = new String[256 * 3][MAX];
        nome = new String[MAX];
        for (int i = 0; i < nome.length; i++) {
            nome[i] = "";
        }


        txtSample.setText("sample" + (count + 1));

        surfaceView = findViewById(R.id.surfaceView1);

        txtSample.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((EditText) v).setText("");
            }
        });

        btnCapture = findViewById(R.id.btnCapture);
        btnCapture.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtSample.getText().toString().equals("") == false) {
                    if (checkName(txtSample.getText().toString()) == false) {
                        camera.takePicture(null, null, jpegCallback);
                    } else
                        MessageBox("Please inform different name!");
                }else
                    MessageBox("Please inform sample name!");
            }
        });

        btnSave = findViewById(R.id.btnSave);
        btnSave.setEnabled(false);


        helper = new DatabaseTrabalho(this);


        //modificar
        imageRoi.setOnTouchListener(new View.OnTouchListener() {
            PointF DownPT = new PointF(); // Record Mouse Position When Pressed Down
            PointF StartPT = new PointF(); // Record Start Position of 'img'

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        imageRoi.setX((int) (StartPT.x + event.getX() - DownPT.x));
                        imageRoi.setY((int) (StartPT.y + event.getY() - DownPT.y));
                        StartPT.set(imageRoi.getX(), imageRoi.getY());

                        int[] values = new int[2];
                        v.getLocationOnScreen(values);
                        X = imageRoi.getX();//values[0];// * WidthRatio;
                        Y = imageRoi.getY() - surfaceView.getY();//values[1] ;//* HeightRatio;
                        txtDebug.setText(X + ", " + Y);


                        break;
                    case MotionEvent.ACTION_DOWN:
                        DownPT.set(event.getX(), event.getY());
                        StartPT.set(imageRoi.getX(), imageRoi.getY());
                        break;
                    case MotionEvent.ACTION_UP:
                        // Nothing have to do
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        init();
    }

    private boolean checkName(String n) {
        boolean flag = false;
        for (int i = 0; i < nome.length; i++) {
            if (nome[i].equals(n))
                flag = true;
        }
        return flag;
    }

    public static void hideSoftKeyboard(Activity activity) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        } catch (Exception ex) {
        }
    }

    AutoFocusCallback myAutoFocusCallback = new AutoFocusCallback() {
        @Override
        public void onAutoFocus(boolean arg0, Camera arg1) {
            camera.cancelAutoFocus();
            if (arg0 == true) {
                Drawable myDrawable = getResources().getDrawable(R.drawable.roi_ok);
                imageRoi.setImageDrawable(myDrawable);
            }
        }
    };


    public Bitmap RotateBitmap(Bitmap source, int angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    void MessageBox(String msg) {
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    //modificar
    public Bitmap CenterCrop(Bitmap source, int newHeight, int newWidth) {
        int sourceWidth = source.getWidth();
        int sourceHeight = source.getHeight();
        float left = (newWidth - sourceWidth) / 2f;
        float top = (newHeight - sourceHeight) / 2f;
        RectF targetRect = new RectF(left, top, left + sourceWidth, top + sourceHeight);
        txtDebug.setText(left + "," + top + "," + (left + sourceWidth) + "," + (top + sourceHeight));
        Bitmap dest = Bitmap.createBitmap(newWidth, newHeight, source.getConfig());
        Canvas canvas = new Canvas(dest);
        canvas.drawBitmap(source, null, targetRect, null);
        return dest;
    }

    private void getRGB(ImageView img) {
        Bitmap bmp = ((BitmapDrawable) img.getDrawable()).getBitmap();

        RedHistogram = new int[256];
        GreenHistogram = new int[256];
        BlueHistogram = new int[256];

        int redTotal = 0;
        int greenTotal = 0;
        int blueTotal = 0;

        for (int i = 0; i < bmp.getWidth(); i++) {
            for (int j = 0; j < bmp.getHeight(); j++) {
                int pixel = bmp.getPixel(i, j);
                int redValue = Color.red(pixel);
                int greenValue = Color.green(pixel);
                int blueValue = Color.blue(pixel);
                RedHistogram[redValue]++;
                GreenHistogram[greenValue]++;
                BlueHistogram[blueValue]++;
                redTotal += redValue;
                greenTotal += greenValue;
                blueTotal += blueValue;
            }
        }

        //salvar banco de dados
//        btnSave.setOnClickListener(new Button.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                salvarBancoDados(v);
//            }
//        });
        txtR.setText(Float.toString(redTotal / (bmp.getHeight() * bmp.getWidth())));
        txtG.setText(Float.toString(greenTotal/ (bmp.getHeight() * bmp.getWidth())));
        txtB.setText(Float.toString(blueTotal / (bmp.getHeight() * bmp.getWidth())));

        Imagem informacao = new Imagem();
        informacao.setNome(txtSample.getText().toString());
        informacao.setR(txtR.getText().toString());
        informacao.setG(txtG.getText().toString());
        informacao.setB(txtB.getText().toString());
        listaInfo.add(informacao);

    }


    private void init() {

        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        btnSave.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvarBancoDados(v);
            }
        });

        jpegCallback = new PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                //decode the data obtained by the camera into a Bitmap
                bmap = BitmapFactory.decodeByteArray(data, 0, data.length);

                //modificar
                aux = RotateBitmap(CenterCrop(bmap, roi, roi), angle);

                photoImage.setImageBitmap(aux);
                getRGB(photoImage);

                nome[count] = String.valueOf(txtSample.getText());
                txtLastSample.setText(nome[count]);

                stop_camera();
                start_camera();
                Drawable myDrawable = getResources().getDrawable(R.drawable.roi);
                imageRoi.setImageDrawable(myDrawable);

                count++;
                if (count == MAX) {
                    btnCapture.setEnabled(false);
                    btnSave.setEnabled(true);
                    txtCount.setText("-/-");
                    txtSample.setText("");
                } else {
                    txtCount.setText((count + 1) + "/" + MAX);
                    txtSample.setText("sample" + (count + 1));
                }
                reverse = true;
            }
        };

        photoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (reverse) {
                    count--;
                    btnCapture.setEnabled(true);
                    txtCount.setText((count + 1) + "/" + MAX);
                    nome[count] = "";
                    txtSample.setText("sample" + (count + 1));

                    reverse = false;
                    photoImage.setImageDrawable(getResources().getDrawable(R.drawable.ready));
                }
            }
        });

        FrameLayout lay = (FrameLayout) findViewById(R.id.frame1);
        lay.setOnClickListener(new FrameLayout.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                camera.autoFocus(myAutoFocusCallback);
                hideSoftKeyboard(TrabalhoActivity.this);
            }
        });
    }

    public void salvarBancoDados(View view) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete("amostra",null,null);
        for (int i = 0; i < listaInfo.size(); i++) {
            ContentValues c = new ContentValues();
            c.put("red",listaInfo.get(i).getR());
            c.put("green",listaInfo.get(i).getG());
            c.put("blue",listaInfo.get(i).getB());
            c.put("name",listaInfo.get(i).getNome());
            long res = db.insert("amostra", null, c);
        }
        Intent intent = new Intent(this, ListaAmostraTrabalho.class);
        startActivity(intent);
    }

    private void start_camera() {
        try {
            camera = Camera.open();
            param = camera.getParameters();
            // Size optimalSize = getOptimalPictureSize();
            List<String> focus = param.getSupportedFocusModes();
            if (focus != null) {
                int mode = 0;
                param.setFocusMode(focus.get(0));
            }
            List<String> whiteMode = param.getSupportedWhiteBalance();
            if (whiteMode != null) {
                param.setWhiteBalance(whiteMode.get(0));
            }

            String[] exposure = {"-6", "-4", "-2", "0", "+2", "+4", "+6"};
            param.setExposureCompensation(Integer.parseInt(exposure[3]));

            boolean flash = getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
            if (flash == true) {
                int mode = 2;
                if (mode == 0)
                    param.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
                else if (mode == 1)
                    param.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                else
                    param.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            }

            //1 Mega-pixel <== NAO PODE ALTERAR
            int w = 640;
            int h = 480;

            param.setPictureSize(w, h);
            param.setPreviewSize(w, h);

            Display display = this.getWindowManager().getDefaultDisplay();
            switch (display.getRotation()) {
                case Surface.ROTATION_0: // This is display orientation
                    angle = 90; // This is camera orientation
                    break;
                case Surface.ROTATION_90:
                    angle = 0;
                    break;
                case Surface.ROTATION_180:
                    angle = 270;
                    break;
                case Surface.ROTATION_270:
                    angle = 180;
                    break;
                default:
                    angle = 90;
                    break;
            }
            camera.setDisplayOrientation(angle);
            camera.setParameters(param);
        } catch (Exception e) {
            MessageBox("init_camera: " + e.getMessage() + ". Go to settings!");
            return;
        }
        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        } catch (Exception e) {
            MessageBox("init_camera: " + e.getMessage() + ". Go to settings!");
            return;
        }
    }

    private void stop_camera() {
        camera.stopPreview();
        camera.release();
    }

    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
        // TODO Auto-generated method stub

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            start_camera();
        } catch (Exception e) {
            MessageBox("init_camera: " + e.getMessage());
        }
    }


    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        camera.stopPreview();
        camera.release();
        camera = null;
    }


    protected void  onDestroy() {
        helper.close();
        super.onDestroy();
    }
    class Imagem {
        String nome;
        String r;
        String g;
        String b;

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public String getR() {
            return r;
        }

        public void setR(String r) {
            this.r = r;
        }

        public String getG() {
            return g;
        }

        public void setG(String g) {
            this.g = g;
        }

        public String getB() {
            return b;
        }

        public void setB(String b) {
            this.b = b;
        }
    }
}
