package com.lxl.paramsContainer;

import com.lxl.utils.Matrix;
import com.lxl.utils.UniUtils;

import java.util.ArrayList;

public class ProcessParams {

    private ArrayList<Matrix> A;
    private ArrayList<Matrix> Z;

    public ProcessParams(int numberOfLayers) {
        A = new ArrayList<>(numberOfLayers);
        Z = new ArrayList<>(numberOfLayers);

        UniUtils.resizeList(A, numberOfLayers);
        UniUtils.resizeList(Z, numberOfLayers);
    }

    public Matrix getA(int layer) {
        return A.get(layer);
    }

    public Matrix getZ(int layer) {
        return Z.get(layer);
    }

    public void setA(int layer, Matrix m) {
        A.set(layer, m);
    }

    public void setZ(int layer, Matrix m) {
        Z.set(layer, m);
    }
}
