package org.example;

import org.dmg.pmml.FieldName;
import org.jpmml.evaluator.*;
import org.xml.sax.SAXException;
import java.io.File;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class LoadPMML {
    public static void main(String[] args) throws JAXBException, IOException, SAXException {
       String modelFolder = LoadPMML.class.getClassLoader().getResource("models").getPath();
       String modelName = "svm.xml";

       System.out.println("Hello World");
        Path modelPath = Paths.get(modelFolder, modelName);
        Evaluator evaluator = new LoadingModelEvaluatorBuilder()
                .load(modelPath.toFile())
                .build();

        evaluator.verify();

        // Prediction

        FieldName targetName = evaluator.getTargetFields().get(0).getName();
        List<InputField> inputFields = evaluator.getInputFields();

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
        features.put("isFraud", 0.0);

        Map<FieldName, FieldValue> arguments = new LinkedHashMap<>();
        for (InputField inputField : inputFields) {
            FieldName inputName = inputField.getName();
            Double value = features.get(inputName.toString());
            FieldValue inputValue = inputField.prepare(value);
            arguments.put(inputName, inputValue);
        }

        Map<FieldName, ?> results = evaluator.evaluate(arguments);// Extracting prediction
        Map<String, ?> resultRecord = EvaluatorUtil.decodeAll(results);
        Integer yPred = (Integer) resultRecord.get(targetName.toString());
        System.out.printf("Prediction is %d\n", yPred);
        System.out.printf("PMML output %s\n", resultRecord);
    }
}