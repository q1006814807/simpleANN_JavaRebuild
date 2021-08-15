package com.lxl.modelController;

import com.lxl.activation.Activation;
import com.lxl.costFunction.CostFunction;
import com.lxl.costFunction.CostFunctionFactory;
import com.lxl.dataLoader.DataLoader;
import com.lxl.layer.Layer;
import com.lxl.layer.LayersBox;
import com.lxl.paramsContainer.BatchGradients;
import com.lxl.paramsContainer.Gradients;
import com.lxl.paramsContainer.ProcessParams;
import com.lxl.utils.Matrix;

import java.util.ArrayList;
import java.util.Collections;

public class ModelController {
    private final DataLoader dataLoader;

    private LayersBox layersBox;

    private ArrayList<Matrix> trainImages;
    private ArrayList<Integer> trainLabels;
    private int validationStart;
    private float[] dataRange = new float[2];

    private ArrayList<Matrix> testImages;
    private ArrayList<Integer> testLabels;

    public ModelController(DataLoader dataLoader, LayersBox layersBox) {
        this.layersBox = layersBox;

        this.dataLoader = dataLoader;
    }

    public ModelController(DataLoader dataLoader, Layer... layers) {
        this.layersBox = new LayersBox(layers);

        this.dataLoader = dataLoader;
    }

    public void initializeTraining(int validationStart) {
        System.out.println("【程序】开始加载训练数据...");
        this.validationStart = validationStart;
        layersBox.initialize();
        trainImages = dataLoader.loadTrainImage();
        trainLabels = dataLoader.loadTrainLabel();

        System.out.println("【程序】训练数据加载完毕...");
    }

    public void initializeTest() {
        System.out.println("【程序】开始加载测试数据...");
        layersBox.initialize();
        testImages = dataLoader.loadTestImage();
        testLabels = dataLoader.loadTestLabel();
        System.out.println("【程序】测试数据加载完毕...");
    }

    private ProcessParams forwardPropagate(Matrix xi) {
        ProcessParams params = new ProcessParams(layersBox.getDepth());

        // Z[0] = Xi + Bias[0]
        Matrix Z0 = xi.add(layersBox.getTheta(0));

        // A[0] = act_0(Z[0])
        Matrix A0 = layersBox.getActivation(0).executeNew(Z0);

        A0.set(0, 0, 1);

        params.setZ(0, Z0);
        params.setA(0, A0);

        for (int i = 1; i < layersBox.getDepth(); ++i) {
            Matrix theta = layersBox.getTheta(i);
            // Z[i] = theta[i] * A[i-1]
            Matrix Zi = theta.multiply(params.getA(i - 1));
            // A[i] = act_i(Z[i])
            Matrix Ai = layersBox.getActivation(i).executeNew(Zi);

            Ai.set(0, 0, 1);

            params.setZ(i,Zi);
            params.setA(i,Ai);
        }

        params.getA(layersBox.getDepth()-1).set(0, 0, 0);

        return params;
    }

    private Gradients backPropagate(Matrix yi, ProcessParams params, CostFunction costFunc) {
        int curDepth = layersBox.getDepth() - 1;

        Gradients gradients = new Gradients(layersBox.getDepth());
        // 保存数据 是否预测正确 计算Cost
        gradients.setCost(costFunc.getCost());
        gradients.setRight(costFunc.isRight());

        Matrix dZ = costFunc.getEndDZ();
        Activation dAct = null;

        // 构造矩阵dθ（与Theta行列对应）
        Matrix dTheta = new Matrix(
                layersBox.getNumberOfNodes(curDepth) + 1,
                layersBox.getNumberOfNodes(curDepth-1) + 1
        );

        for (int r = 1; r < dTheta.getRow(); ++r) {
            for (int c = 0; c < dTheta.getCol(); ++c) {
                dTheta.set(r, c, params.getA(curDepth-1).get(c, 0));
            }
        }

        dTheta.colMultiplyIn(dZ);
        gradients.setDTheta(curDepth, dTheta);

        // dZ(l, i) = act(l)'(Z(l, i))
        //            * Σ(k=1, node(l)){
        //              dZ(l+1, k) * θ(l+1, k, i)
        //            }
        // dθ(l, i, j) = A(l-1, j) * dZ(l, i);
        for (--curDepth; curDepth >= 1; --curDepth) {

            // 此处dZ还属于后一层
            Matrix dBack = dZ.transpose().multiply(layersBox.getTheta(curDepth+1)).transpose();
            dAct = layersBox.getDActivation(curDepth);

            // 计算此层的dZ
            dZ = dAct.executeNew(params.getZ(curDepth)).colMultiplyIn(dBack);
            dZ.set(0, 0, 0);

            // 构造dTheta
            dTheta = new Matrix(
                    layersBox.getNumberOfNodes(curDepth) + 1,
                    layersBox.getNumberOfNodes(curDepth-1) + 1
            );
            for (int r = 1; r < dTheta.getRow(); ++r) {
                for (int c = 0; c < dTheta.getCol(); ++c) {
                    dTheta.set(r, c, params.getA(curDepth-1).get(c, 0));
                }
            }

            dTheta.colMultiplyIn(dZ);
            gradients.setDTheta(curDepth, dTheta);
        }


        // 计算输入层theta（只包含bias）
        // 此处dZ还属于后一层
        Matrix dBack = dZ.transpose().multiply(layersBox.getTheta(curDepth+1)).transpose();
        dAct = layersBox.getDActivation(curDepth);
        // 计算此层的dZ(dZ即bias)
        dZ = dAct.executeNew(params.getZ(curDepth)).colMultiplyIn(dBack);
        dZ.set(0, 0, 0);
        gradients.setDTheta(curDepth, dZ);

        return gradients;
    }

    public void trainForEpoch(float learningRate, int batchSize) {
        System.out.println("【程序】该Epoch开始训练...");
        // 打乱trainingSet
        ArrayList<Integer> iMap = new ArrayList<>(validationStart);
        for (int i = 0; i < validationStart; ++i) iMap.add(i);
        Collections.shuffle(iMap);

        // 一轮batchSize 总共validationStart/batchSize轮

        for (int i = 0; i < validationStart/batchSize; ++i) {
            BatchGradients batchGradients = new BatchGradients(layersBox);

            for (int k = i * batchSize; k < (i+1)*batchSize; ++k) {
                Matrix xi = normalizeImage(trainImages.get(iMap.get(k)));
                Matrix yi = normalizeLabel(trainLabels.get(iMap.get(k)));

                ProcessParams params = forwardPropagate(xi);

                CostFunction costFunc = CostFunctionFactory.
                        getCostFunction(layersBox.getCostFunctionID(), params, yi, layersBox);

                Gradients gradients = backPropagate(yi, params, costFunc);
                batchGradients.addGradients(gradients);
            }

            layersBox.updateLayers(batchGradients, learningRate);
            System.out.println("----------第" + i + "批样本训练----------");
            System.out.println("Cost      = " + batchGradients.getStandardCost());
            System.out.println("RightRate = " + batchGradients.getRightRate());
        }

        System.out.println("【程序】该Epoch训练完毕...");
    }

    public void trainForEpochWithAllTrainingData(float learningRate, int batchSize) {
        System.out.println("【程序】该Epoch开始训练...");
        // 打乱trainingSet
        ArrayList<Integer> iMap = new ArrayList<>(trainImages.size());
        for (int i = 0; i < trainImages.size(); ++i) iMap.add(i);
        Collections.shuffle(iMap);

        // 一轮batchSize 总共validationStart/batchSize轮

        for (int i = 0; i < trainImages.size()/batchSize; ++i) {
            BatchGradients batchGradients = new BatchGradients(layersBox);

            for (int k = i * batchSize; k < (i+1)*batchSize; ++k) {
                Matrix xi = normalizeImage(trainImages.get(iMap.get(k)));
                Matrix yi = normalizeLabel(trainLabels.get(iMap.get(k)));

                ProcessParams params = forwardPropagate(xi);
                CostFunction costFunc = CostFunctionFactory.
                        getCostFunction(layersBox.getCostFunctionID(), params, yi, layersBox);

                Gradients gradients = backPropagate(yi, params, costFunc);

                batchGradients.addGradients(gradients);
            }

            layersBox.updateLayers(batchGradients, learningRate);
            System.out.println("----------第" + i + "批样本训练----------");
            System.out.println("Cost      = " + batchGradients.getStandardCost());
            System.out.println("RightRate = " + batchGradients.getRightRate());
        }

        System.out.println("【程序】该Epoch训练完毕...");
    }

    public void checkValidation() {

        System.out.println("【程序】开始验证集校验...");
        double cost = 0;
        int rights = 0;

        for (int i = validationStart; i < trainImages.size() ; ++i) {
            Matrix xi = normalizeImage(trainImages.get(i));
            Matrix yi = normalizeLabel(trainLabels.get(i));

            ProcessParams params = forwardPropagate(xi);
            CostFunction costFunc = CostFunctionFactory.
                    getCostFunction(layersBox.getCostFunctionID(), params, yi, layersBox);

            cost += costFunc.getCost();
            rights += costFunc.isRight() ? 1 : 0;
        }

        cost *= (10000 / (float)(trainImages.size() - validationStart));
        float rightRate = (rights / (float)(trainImages.size() - validationStart));

        System.out.println("【程序】校验结束！校验信息为：");
        System.out.println("Cost      = " + cost);
        System.out.println("RightRate = " + rightRate);
    }

    public void checkTest() {

        System.out.println("【程序】开始验测试校验...");
        double cost = 0;
        int rights = 0;

        for (int i = 0; i < testImages.size() ; ++i) {
            Matrix xi = normalizeImage(testImages.get(i));
            Matrix yi = normalizeLabel(testLabels.get(i));

            ProcessParams params = forwardPropagate(xi);
            CostFunction costFunc = CostFunctionFactory.
                    getCostFunction(layersBox.getCostFunctionID(), params, yi, layersBox);

            cost += costFunc.getCost();
            rights += costFunc.isRight() ? 1 : 0;
        }

        cost *= (10000 / (float)testImages.size());
        float rightRate = (rights / (float)testImages.size());

        System.out.println("【程序】校验结束！校验信息为：");
        System.out.println("Cost      = " + cost);
        System.out.println("RightRate = " + rightRate);
    }

    public void checkTraining() {
        System.out.println("【程序】开始训练集校验...");
        double cost = 0;
        int rights = 0;

        for (int i = 0; i < validationStart; ++i) {
            Matrix xi = normalizeImage(trainImages.get(i));
            Matrix yi = normalizeLabel(trainLabels.get(i));

            ProcessParams params = forwardPropagate(xi);
            CostFunction costFunc = CostFunctionFactory.
                    getCostFunction(layersBox.getCostFunctionID(), params, yi, layersBox);

            cost += costFunc.getCost();
            rights += costFunc.isRight() ? 1 : 0;
        }

        cost *= (10000f / validationStart);
        float rightRate = (rights / (float)validationStart);

        System.out.println("【程序】校验结束！校验信息为：");
        System.out.println("Cost      = " + cost);
        System.out.println("RightRate = " + rightRate);
    }


    private Matrix normalizeImage(Matrix image) {
        Matrix xi = new Matrix(image.getRow() + 1, 1);
        xi.set(0, 0, 1);
        float normalized;
        for (int j = 1; j < xi.getRow(); ++j) {
            if (dataRange[0] < dataRange[1])
                normalized = 2 * (image.get(j-1, 0) - dataRange[0]) /
                        (dataRange[1] - dataRange[0]) - 1;
            else normalized = image.get(j-1, 0);

            xi.set(j, 0, normalized);
        }
        return xi;
    }

    private Matrix normalizeLabel(int label) {
        Matrix yi = new Matrix(10 + 1, 1);
        yi.set(1 + label, 0, 1);
        return yi;
    }

    public void setDataRange(float left, float right) {
        dataRange[0] = left;
        dataRange[1] = right;
        if (left > right) {
            right = dataRange[0];
            dataRange[0] = dataRange[1];
            dataRange[1] = right;
        }
    }
}
