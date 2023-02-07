package ru.samsung.case2022;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.gpu.CompatibilityList;
import org.tensorflow.lite.gpu.GpuDelegate;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.ops.ResizeOp;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;
import org.tensorflow.lite.support.tensorbuffer.TensorBufferUint8;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class TFLiteInterpreter {

    private Interpreter interpreter;

    public TFLiteInterpreter(FileInputStream modelPath, long start, long end) throws IOException {
        FileChannel fileChannel = modelPath.getChannel();
        MappedByteBuffer buffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, start, end);
        this.interpreter = new Interpreter(buffer);
    }

    public float[] runInference(Bitmap bitmap) {
        TensorImage img = new TensorImage(DataType.UINT8);
        img.load(bitmap);
        ImageProcessor imageProcessor =
                new ImageProcessor.Builder()
                        .add(new ResizeOp(800, 800, ResizeOp.ResizeMethod.BILINEAR))
                        .build();
        img = imageProcessor.process(img);
        TensorBuffer probabilityBuffer =
                TensorBuffer.createFixedSize(new int[]{1, 1001}, DataType.UINT8);
        this.interpreter.run(img.getBuffer(), probabilityBuffer.getBuffer());

        return probabilityBuffer.getFloatArray();
    }

    public int getResult(float[] outputs) {
        int index = -1;
        float max = -1;
        for (int i = 0; i < outputs.length; i++) {
            if (outputs[i] / 250 > max) {
                max = outputs[i] / 250;
                index = i;
            }
        }
        return index;
    }
    public void close() {
        this.interpreter.close();
    }
}