package com.example.demo.service;

import com.example.demo.api.model.*;
import org.springframework.stereotype.Component;
import com.example.demo.utils.Utils;

import java.util.Optional;
import java.util.*;

@Component
public class UserService {
    private List<Algorithm> algorithmList;

    public UserService(){

        algorithmList = new ArrayList<>();

        Algorithm SVM = new AlgorithmPMML(Utils.SVM, "Support Vector Machine", "svm.pmml", Utils.PMML);
        Algorithm RF = new AlgorithmPMML(Utils.RANDOM_FOREST, "Random Forest", "rf.pmml", Utils.PMML);
        Algorithm CNN = new AlgorithmONNX(Utils.CNN, "CNN", "cnn.onnx", Utils.ONNX);
        Algorithm DT = new AlgorithmPMML(Utils.DECISION_TREE, "Decision Tree", "dtree.pmml", Utils.PMML);

        algorithmList.addAll(Arrays.asList(CNN, SVM, RF, DT));
    }


    public Optional<Algorithm> getAlgorithm(Integer id){
        Optional optional = Optional.empty();
        for(Algorithm algo: algorithmList){
            if(id == algo.getId()){
                optional = Optional.of(algo);
                return optional;
            }
        }
        return optional;
    }
}
