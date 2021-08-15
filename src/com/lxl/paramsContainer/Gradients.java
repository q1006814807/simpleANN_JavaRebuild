package com.lxl.paramsContainer;

import com.lxl.utils.Matrix;
import com.lxl.utils.UniUtils;

import java.util.ArrayList;

public class Gradients {

    private ArrayList<Matrix> dThetas;
    private float cost = 0;
    private boolean isRight = false;

    public Gradients(int numberOfLayers) {
        dThetas = new ArrayList<>(numberOfLayers);
        UniUtils.resizeList(dThetas, numberOfLayers);
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public void setDTheta(int idx, Matrix dTheta) {
        this.dThetas.set(idx, dTheta);
    }

    public Matrix getDTheta(int idx) {
        return this.dThetas.get(idx);
    }

    public ArrayList<Matrix> getDThetas() {
        return dThetas;
    }

    public boolean isRight() {
        return isRight;
    }

    public void setRight(boolean right) {
        isRight = right;
    }
}
