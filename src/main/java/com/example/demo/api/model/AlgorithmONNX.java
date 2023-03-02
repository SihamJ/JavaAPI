package com.example.demo.api.model;

import java.nio.FloatBuffer;
import java.util.Collections;
import ai.onnxruntime.*;

public class AlgorithmONNX extends Algorithm {

    private OrtEnvironment env;
    private OrtSession.SessionOptions opts;
    private OrtSession session;

    public AlgorithmONNX(int id, String name, String path, int type){
        super(id, name, path, type);
    }

    public void loadAlgorithm() throws OrtException {
        this.env = OrtEnvironment.getEnvironment();
        this.opts = new OrtSession.SessionOptions();
        this.session = env.createSession("/home/sihartist/Desktop/JavaAPI/src/main/resources/models/" + this.getFilename(), opts);
    }

    public float predict(float[] vals) throws OrtException{

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
        this.session.close();
        this.env.close();

        return outputValues[0][0];
    }

}
