package com.example.demo.api.model;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.List;

import org.dmg.pmml.FieldName;
import org.jpmml.evaluator.*;
import org.xml.sax.SAXException;
import javax.xml.bind.JAXBException;
import java.io.IOException;

public class AlgorithmPMML extends Algorithm {

    private Evaluator evaluator;
    private String modelFolder;
    private Path modelPath;

    public AlgorithmPMML(int id, String name, String filename, int type){
        super(id, name, filename, type);
    }

    public void loadAlgorithm()  throws JAXBException, IOException, SAXException {

        this.modelFolder = AlgorithmPMML.class.getClassLoader().getResource("models").getPath();

        this.modelPath = Paths.get(modelFolder, this.getFilename());
        this.evaluator = new LoadingModelEvaluatorBuilder()
                .load(modelPath.toFile())
                .build();

        this.evaluator.verify();
    }

    public float predict(float[] values){

        FieldName targetName = this.evaluator.getTargetFields().get(0).getName();
        List<InputField> inputFields = this.evaluator.getInputFields();

        Map<String, Float> features = new HashMap<>();
        features.put("step", values[0]);
        features.put("amount", values[1]);
        features.put("oldbalanceOrg", values[2]);
        features.put("newbalanceOrig", values[3]);
        features.put("oldbalanceDest", values[4]);
        features.put("newbalanceDest", values[5]);
        features.put("CASH_IN", values[6]);
        features.put("CASH_OUT", values[7]);
        features.put("DEBIT", values[8]);
        features.put("PAYMENT", values[9]);
        features.put("TRANSFER", values[10]);
        features.put("isFlaggedFraud", values[11]);

        Map<FieldName, FieldValue> arguments = new LinkedHashMap<>();
        for (InputField inputField : inputFields) {
            FieldName inputName = inputField.getName();
            Float value = features.get(inputName.toString());
            FieldValue inputValue = inputField.prepare(value);
            arguments.put(inputName, inputValue);
        }

        Map<FieldName, ?> results = this.evaluator.evaluate(arguments);// Extracting prediction
        Map<String, ?> resultRecord = EvaluatorUtil.decodeAll(results);

        float y = ((Integer)resultRecord.get(targetName.toString())).floatValue();

        System.out.printf("Prediction is %f\n", y);
        System.out.printf("PMML output: %s\n", resultRecord);

        return y;
    }

}
