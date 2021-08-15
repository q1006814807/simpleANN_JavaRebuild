package com.lxl.costFunction.impl;

import com.lxl.activation.Activation;
import com.lxl.costFunction.BaseCost;
import com.lxl.costFunction.CostFunction;
import com.lxl.layer.LayersBox;
import com.lxl.paramsContainer.ProcessParams;
import com.lxl.utils.Matrix;
import com.lxl.utils.UniUtils;

public class CrossEntropyCost extends BaseCost implements CostFunction {
    public CrossEntropyCost(ProcessParams params, Matrix yi, LayersBox layersBox) {
        super(params, yi, layersBox);
    }

    @Override
    public float getCost() {
        Matrix A = params.getA(layersBox.getDepth()-1);
        return UniUtils.getCrossEntropy(A, yi);
    }

    @Override
    public Matrix getEndDZ() {
        int curDepth = layersBox.getDepth() - 1;
        Matrix A = params.getA(curDepth);
        return A.sub(yi);
    }

    @Override
    public int getSerialID() {
        return 1;
    }


}
