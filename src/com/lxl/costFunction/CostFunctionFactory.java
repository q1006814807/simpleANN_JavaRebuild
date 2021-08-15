package com.lxl.costFunction;

import com.lxl.costFunction.impl.CrossEntropyCost;
import com.lxl.costFunction.impl.LeastSquaresCost;
import com.lxl.layer.LayersBox;
import com.lxl.paramsContainer.ProcessParams;
import com.lxl.utils.Matrix;

public class CostFunctionFactory {
    private CostFunctionFactory(){}

    public static CostFunction getCostFunction(int costFunctionID, ProcessParams params, Matrix yi, LayersBox layersBox) {
        switch (costFunctionID) {
            case 0 : return new LeastSquaresCost(params, yi, layersBox);
            case 1 : return new CrossEntropyCost(params, yi, layersBox);
        }

        throw new RuntimeException("CostFunctionID 不存在!");
    }

}
