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

        Map<String, Double> features = new HashMap<>();
        features.put("step", 4.9600000e+02);
        features.put("amount", 7.6615145e+05);
        features.put("oldbalanceOrg", 7.6615145e+05);
        features.put("newbalanceOrig", 0.0);
        features.put("oldbalanceDest", 0.0);
        features.put("newbalanceDest", 0.0);
        features.put("CASH_IN", 0.0);
        features.put("CASH_OUT", 0.0);
        features.put("DEBIT", 0.0);
        features.put("PAYMENT", 0.0);
        features.put("TRANSFER", 0.0);
        features.put("isFlaggedFraud", 1.0);

        Map<FieldName, FieldValue> arguments = new LinkedHashMap<>();
        for (InputField inputField : inputFields) {
            FieldName inputName = inputField.getName();
            Double value = features.get(inputName.toString());
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
