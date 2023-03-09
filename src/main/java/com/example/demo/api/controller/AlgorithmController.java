package com.example.demo.api.controller;

import com.example.demo.api.model.Algorithm;
import com.example.demo.service.AlgorithmService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
public class AlgorithmController {

    @Autowired
    private AlgorithmService algorithmService;

    public AlgorithmController(AlgorithmService algorithmService){
        this.algorithmService = algorithmService;
    }

    @GetMapping("/hello")
    public String sayHello(){
        return "Hello World";
    }

    @RequestMapping(value = "/predict", method = RequestMethod.POST)
    public HashMap predict(@RequestBody String payload) throws Exception {

        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(payload);
        }catch (JSONException err){
            return new HashMap<String, Object>(){
                {
                    put("message", "Failed to load JSON request: " + err.toString());
                    put("isFraud", -1);
                }
            };
        }
        String algoName = jsonObject.getString("algo");

        Optional algo = algorithmService.getAlgorithm(algoName);

        if(algo.isPresent()){
            Algorithm algorithm = (Algorithm) algo.get();
            String algoDescription = algorithm.getDescription();


            if( ! algorithm.isLoaded()){
                return new HashMap<String, Object>(){
                    {
                        put("message", "Failed to load " + algoDescription);
                        put("isFraud", -1);
                    }
                };
            }

            float y = -1.0f;
            try {
                y = algorithm.predict((JSONArray) jsonObject.get("transaction"));
            }
            catch(Exception e) {
                return new HashMap<String, Object>() {
                    {
                        put("message", "Failed to predict with " + algoDescription + ": " + e.toString());
                        put("isFraud", -1);
                    }
                };
            }
            float finalY = y;
            return new HashMap<String, Object>(){
                    {
                        put("message", "Predicted with " + algoDescription);
                        put("isFraud", finalY);
                    }
            };
        }


        return new HashMap<String, Object>(){
            {
                put("message", "Can't find algorithm in Database");
                put("isFraud", -1);
            }
        };
    }
}
