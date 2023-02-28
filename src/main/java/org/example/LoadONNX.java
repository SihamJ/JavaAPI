package org.example;
import java.util.*;
import ai.onnxruntime.*;


import java.nio.FloatBuffer;
public class LoadONNX {
    public static void main(String[] args) throws OrtException {
        OrtEnvironment env = OrtEnvironment.getEnvironment();
        OrtSession.SessionOptions opts = new OrtSession.SessionOptions();
        OrtSession session = env.createSession("/home/sihartist/Desktop/JavaAPI/src/main/resources/models/cnn.onnx", opts);

        System.out.println(session.getNumInputs());
        System.out.println(session.getInputInfo());
        System.out.println(session.getNumOutputs());
        System.out.println(session.getInputNames());
        System.out.println(session.getOutputNames());

        long[] shape = new long[] {1, 12, 1};
        float[] values = new float[] {4.9600000e+02f, 7.6615145e+05f, 7.6615145e+05f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f};

        FloatBuffer buffer = FloatBuffer.allocate(values.length);
        for (double d : values) {
            buffer.put((float) d);
        }
        buffer.rewind();

        OnnxTensor tensor = OnnxTensor.createTensor(env, buffer, shape);
        System.out.println(tensor.getInfo());


        OrtSession.Result result = session.run(Collections.singletonMap("conv1d_input", tensor), Collections.singleton("dense_1"));

        OnnxTensor resultTensor = (OnnxTensor) result.get(0);
        float[][] outputValues = (float[][]) resultTensor.getValue();

        for(int i=0; i < outputValues.length; i++){
            for(int j = 0; j < outputValues[i].length; j++){
                System.out.println("Prediction [" + i + "][" + j + "] for " + session.getOutputNames() + ": " + outputValues[i][j]);
            }
        }


        result.close();

        session.close();
        env.close();


    }
}
