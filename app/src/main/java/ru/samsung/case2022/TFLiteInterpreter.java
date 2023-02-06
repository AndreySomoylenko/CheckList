package ru.samsung.case2022;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class TFLiteInterpreter {

    private Interpreter interpreter;

    public TFLiteInterpreter(FileInputStream modelPath) throws IOException {
        FileChannel fileChannel = modelPath.getChannel();
        long fileSize = fileChannel.size();
        MappedByteBuffer buffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileSize);
        this.interpreter = new Interpreter(buffer);
    }

    public float[] runInference(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float[][] input = new float[1][width * height];
        bitmap = scaleBitmap(bitmap, width, height);
        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int i = 0; i < pixels.length; i++) {
            input[0][i] = (float)((pixels[i] >> 16) & 0xFF) / 255.0f;
        }
        float[][] output = new float[1][10];
        this.interpreter.run(input, output);
        return output[0];
    }

    public Bitmap scaleBitmap(Bitmap bitmap, int newWidth, int newHeight) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
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

    public void close() {
        this.interpreter.close();
    }
}