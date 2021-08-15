package com.lxl.costFunction;

import com.lxl.layer.LayersBox;
import com.lxl.paramsContainer.ProcessParams;
import com.lxl.utils.Matrix;

public class BaseCost {
    protected ProcessParams params;
    protected Matrix yi;
    protected LayersBox layersBox;

    public BaseCost(ProcessParams params, Matrix yi, LayersBox layersBox) {
        this.params = params;
        this.yi = yi;
        this.layersBox = layersBox;
    }

    public boolean isRight() {
        Matrix A = params.getA(layersBox.getDepth()-1);
        // 是否预测正确
        return A.getMaxRC()[0] == yi.getMaxRC()[0];
    }

}
