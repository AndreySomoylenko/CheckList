package ru.samsung.case2022;

import android.content.Context;
import android.graphics.Bitmap;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;

import ru.samsung.case2022.ml.Model;


public class TFLiteInterpreter {

    Model model;

    public TFLiteInterpreter(Context context) throws IOException {
        try {
            model = Model.newInstance(context);

        } catch (IOException e) {
            // TODO Handle the exception
        }
    }

    public float[] runInference(Bitmap bitmap) {
        try {

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 400,400, 3}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * 400 * 400 * 3);
            int [] intValues = new int[400 * 400];
            bitmap.getPixels(intValues, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
            inputFeature0.loadBuffer(byteBuffer);
            int pixel = 0;
            for (int i = 0; i < 400; i++) {
                for (int j = 0; j < 400; j++) {
                     int val =  intValues[pixel++];
                     byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 1));
                     byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f / 1));
                     byteBuffer.putFloat((val & 0xFF) * (1.f / 1));
                }
            }

            // Runs model inference and gets result.
            Model.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
            // Releases model resources if no longer used.
            model.close();
            return outputFeature0.getFloatArray();
        } catch (Exception e) {
            // TODO Handle the exception
        }
        return null;
    }

    public int getResult(float[] outputs) {
        int index = -1;
        float max = -1;
        for (int i = 0; i < outputs.length; i++) {
            if (outputs[i] > max) {
                max = outputs[i];
                index = i;
            }
        }
        return index;
    }
}