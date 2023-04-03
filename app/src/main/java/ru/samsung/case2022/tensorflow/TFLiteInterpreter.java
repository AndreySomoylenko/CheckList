package ru.samsung.case2022.tensorflow;

import android.content.Context;
import android.graphics.Bitmap;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import ru.samsung.case2022.ml.Model;

/**
 * The TFLiteInterpreter class
 * @author Ismail Velidzhanov
 * @version 1.0
 * This class is used to manage tflite model
 */
public class TFLiteInterpreter {

    /**
     * Variable model which contains model
     */
    Model model;

    /**
     * List of products for model
     */

    String[] products = {"Печенье сладкое с маком", "Капуста брокколи", "Сыр полутвердый", "Кофе растворимый с добавлением молотого", "Творог мягкий 2%", "Тесто замороженное дрожжевое", "Молоко 3,2% пастеризованное", "Блинчики с мясом", "Сметана из топленых сливок 15%", "Чай черный листовой", "Печенье", "Брокколи", "Сыр", "Кофе", "Творог", "Тесто", "Молоко", "Блинчики", "Сметана", "Чай"};

    /**
     * Constructor of class
     * Is used to initialize model
     * @param context aplication context
     * @throws IOException if model not exists
     */
    public TFLiteInterpreter(Context context) throws IOException{
        model = Model.newInstance(context);
    }

    /**
     * This method is used to run model
     * @param bitmap which contains user photo
     * @return array of weights
     */
    public float[] runInference(Bitmap bitmap) {
        try {

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 400,400, 3}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * 400 * 400 * 3);
            byteBuffer.order(ByteOrder.nativeOrder());
            int [] intValues = new int[400 * 400];
            bitmap.getPixels(intValues, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
            int pixel = 0;
            for (int i = 0; i < 400; i++) {
                for (int j = 0; j < 400; j++) {
                     int val =  intValues[pixel++];
                     byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 1));
                     byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f / 1));
                     byteBuffer.putFloat((val & 0xFF) * (1.f / 1));
                }
            }
            inputFeature0.loadBuffer(byteBuffer);

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

    /**
     * This method returns product which has the most weight in the array
     * @param outputs weights
     * @return name of product which model recognized images
     */
    public String getResult(float[] outputs) {
        int index = -1;
        float max = -1;
        for (int i = 0; i < outputs.length; i++) {
            if (outputs[i] > max) {
                max = outputs[i];
                index = i;
            }
        }
        return products[index];
    }
    public String getResult2(float[] outputs) {
        int index = -1;
        float max = -1;
        for (int i = 0; i < outputs.length; i++) {
            if (outputs[i] > max) {
                max = outputs[i];
                index = i;
            }
        }
        return products[index + 10];
    }
}