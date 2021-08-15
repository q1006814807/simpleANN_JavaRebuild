package com.lxl.costFunction.impl;

import com.lxl.activation.Activation;
import com.lxl.costFunction.BaseCost;
import com.lxl.costFunction.CostFunction;
import com.lxl.layer.LayersBox;
import com.lxl.paramsContainer.ProcessParams;
import com.lxl.utils.Matrix;
import com.lxl.utils.UniUtils;

public class LeastSquaresCost extends BaseCost implements CostFunction {

    public LeastSquaresCost(ProcessParams params, Matrix yi, LayersBox layersBox) {
        super(params, yi, layersBox);
    }

    @Override
    public float getCost() {
        Matrix A = params.getA(layersBox.getDepth()-1);
        return 0.5f * UniUtils.getSquareDist(A, yi);
    }

    @Override
    public Matrix getEndDZ() {
        int curDepth = layersBox.getDepth() - 1;
        Matrix A = params.getA(curDepth);

        // 构造列向量DZ = act'(Z)
        // DZ = (A - Yi) * act'(Z)
        Activation dAct = layersBox.getDActivation(curDepth);

        Matrix dZ = A.sub(yi).dotElemIn(dAct.executeNew(params.getZ(curDepth)));

        return dZ;
    }

    @Override
    public int getSerialID() {
        return 0;
    }
}
