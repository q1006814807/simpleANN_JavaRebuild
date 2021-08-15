package com.lxl.test;

import com.lxl.activation.impl.DTanhActivation;
import com.lxl.activation.impl.TanhActivation;
import com.lxl.dataLoader.DataLoader;
import com.lxl.layer.InputLayer;
import com.lxl.layer.Layer;
import com.lxl.layer.LayersBox;
import com.lxl.layer.OutputLayer;
import com.lxl.modelController.ModelController;

public class Test1_model1 {

    public static void main(String[] args) throws InterruptedException {
        Layer layer1 = new InputLayer(784, new TanhActivation(), new DTanhActivation());
        Layer layer2 = new InputLayer(100, new TanhActivation(), new DTanhActivation());
        Layer layer3 = new OutputLayer(10);
        LayersBox layersBox = new LayersBox(layer1, layer2, layer3);

        layersBox.load("model_entropy_1_3L", 3);

        DataLoader dataLoader = new DataLoader("dataset.properties");
        ModelController modelController = new ModelController(dataLoader, layersBox);

        modelController.setDataRange(0, 255);
        // 初始化工作结束===========================

        modelController.initializeTest();
        modelController.checkTest();

    }
}
