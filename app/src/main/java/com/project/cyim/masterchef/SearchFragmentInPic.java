package com.project.cyim.masterchef;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.flurgle.camerakit.CameraListener;
import com.flurgle.camerakit.CameraView;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by Sabrina on 2017/8/13.
 */

public class SearchFragmentInPic extends SearchFragment {

    private static final int INPUT_SIZE = 299;
    private static final int IMAGE_MEAN = 128;
    private static final float IMAGE_STD = 128;
    private static final String INPUT_NAME = "input_1";
    private static final String OUTPUT_NAME = "Softmax";

    private static final String MODEL_FILE = "file:///android_asset/mem_graph.pb";
    private static final String LABEL_FILE =
            "file:///android_asset/labels.txt";

    private Classifier classifier;
    private Executor executor = Executors.newSingleThreadExecutor();
    private TextView textViewResult;
    private Button btnDetectObject, btnSearchObject;
    private ImageView imageViewResult;
    private CameraView cameraView;
    private String best;

    public SearchFragmentInPic() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.searchfragment_pic, container, false);
        cameraView = (CameraView) v.findViewById(R.id.cameraView);
        //imageViewResult = (ImageView) v.findViewById(R.id.imageViewResult);
        textViewResult = (TextView) v.findViewById(R.id.textViewResult);
        textViewResult.setMovementMethod(new ScrollingMovementMethod());

        btnDetectObject = (Button) v.findViewById(R.id.btnDetectObject);
        btnSearchObject = (Button) v.findViewById(R.id.btnSearchObject);

        cameraView.setCameraListener(new CameraListener() {
            @Override
            public void onPictureTaken(byte[] picture) {
                super.onPictureTaken(picture);

                Bitmap bitmap = BitmapFactory.decodeByteArray(picture, 0, picture.length);

                bitmap = Bitmap.createScaledBitmap(bitmap, INPUT_SIZE, INPUT_SIZE, false);

                //imageViewResult.setImageBitmap(bitmap);

                final List<Classifier.Recognition> results = classifier.recognizeImage(bitmap);

                textViewResult.setText(results.toString());

                best = results.get(0).getChineseTitle();
            }
        });

        btnDetectObject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraView.captureImage();
            }
        });

        btnSearchObject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), PicSearchResult.class);
                intent.putExtra("SEARCH_ITEMS", best);
                getContext().startActivity(intent);
            }
        });

        initTensorFlowAndLoadModel();
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        cameraView.start();
    }

    @Override
    public void onPause() {
        cameraView.stop();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                classifier.close();
            }
        });
    }

    private void initTensorFlowAndLoadModel() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    classifier = TensorFlowImageClassifier.create(
                            getActivity().getAssets(),
                            MODEL_FILE,
                            LABEL_FILE,
                            INPUT_SIZE,
                            IMAGE_MEAN,
                            IMAGE_STD,
                            INPUT_NAME,
                            OUTPUT_NAME);
                    makeButtonVisible();
                } catch (final Exception e) {
                    throw new RuntimeException("Error initializing TensorFlow!", e);
                }
            }
        });
    }

    private void makeButtonVisible() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                btnDetectObject.setVisibility(View.VISIBLE);
            }
        });
    }
}