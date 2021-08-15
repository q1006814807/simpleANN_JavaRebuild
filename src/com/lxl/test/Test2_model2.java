package com.lxl.test;

import com.lxl.activation.impl.DTanhActivation;
import com.lxl.activation.impl.TanhActivation;
import com.lxl.dataLoader.DataLoader;
import com.lxl.layer.InputLayer;
import com.lxl.layer.LayersBox;
import com.lxl.layer.OutputLayer;
import com.lxl.modelController.ModelController;
import com.lxl.layer.Layer;

public class Test2_model2 {
    public static void main(String[] args) {
        Layer layer1 = new InputLayer(784, new TanhActivation(), new DTanhActivation());
        Layer layer2 = new Layer(128, new TanhActivation(), new DTanhActivation());
        Layer layer3 = new Layer(64, new TanhActivation(), new DTanhActivation());
        Layer layer4 = new OutputLayer(10);


        LayersBox layersBox = new LayersBox(layer1, layer2, layer3, layer4);
        layersBox.setCostFunctionID(1);
        layersBox.load("model_entropy_2_4L", 4);

        DataLoader dataLoader = new DataLoader("dataset.properties");
        ModelController modelController = new ModelController(dataLoader, layersBox);

        modelController.setDataRange(0, 255);


        //annModel.initializeTraining(50000);
        //
        //
        //
        //int times = 1;
        //long t = System.currentTimeMillis();
        //
        //for (int i = 0; i < times; i++) {
        //    annModel.trainForEpoch(0.01f, 100);
        //}
        //
        //t = (System.currentTimeMillis() - t) / (1000 * times);
        //System.out.println("平均每次：" + t + "秒");
        //
        //annModel.checkValidation();
        //annModel.checkTraining();
        //
        //Scanner scanner = new Scanner(System.in);
        //System.out.print("是否保存该模型？(1 or 0):");
        //int ans = scanner.nextInt();
        //if (ans == 0) return;
        //layersBox.save("model_entropy_2_4L");
        //

        modelController.initializeTest();
        modelController.checkTest();

    }
}
