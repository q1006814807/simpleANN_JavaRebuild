package com.lxl.costFunction;

import com.lxl.paramsContainer.ProcessParams;
import com.lxl.utils.Matrix;

public interface CostFunction {
    float getCost();
    Matrix getEndDZ();
    boolean isRight();

    int getSerialID();
}
