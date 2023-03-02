package com.example.demo.api.controller;

import com.example.demo.api.model.Algorithm;
import com.example.demo.service.AlgorithmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.utils.Utils;
import java.util.*;

@RestController
public class AlgorithmController {

    @Autowired
    private AlgorithmService userService;

    public AlgorithmController(AlgorithmService userService){
        this.userService = userService;
    }

    @GetMapping("/hello")
    public String sayHello(){
        return "Hello World";
    }

    @GetMapping(path = "/algorithm")
    public Object getAlgo(@RequestParam Integer id, @RequestParam float[] values){
        if(values.length != Utils.NB_ATTRIBUTES){
            System.out.println("Wrong number of attributes");
            return null;
        }
        Optional algo = userService.getAlgorithm(id);
        if(algo.isPresent()){
            Algorithm algorithm = (Algorithm) algo.get();
            try {
                algorithm.loadAlgorithm();
            }
            catch(Exception e){
                System.out.println("Failed to load algorithm");
            }
            float y = -1.0f;
            try {
                y = algorithm.predict(values);
            }
            catch(Exception e){
                System.out.println("Failed to predict class");
            }
            Map prediction = Collections.singletonMap("isFraud", y);
            return new Object[]{ algo.get(), prediction};
        }
        return null;
    }
}
