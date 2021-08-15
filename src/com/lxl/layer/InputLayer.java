package com.lxl.layer;

import com.lxl.activation.Activation;
import com.lxl.activation.impl.DLinearActivation;
import com.lxl.activation.impl.LinearActivation;
import com.lxl.utils.Matrix;

import java.util.ArrayList;

public class InputLayer extends Layer{
    public InputLayer(int numberOfNodes, Activation activation, Activation dActivation) {
        super(numberOfNodes, activation, dActivation);
    }

    public InputLayer(int numberOfNodes) {
        super(numberOfNodes, new LinearActivation(), new DLinearActivation());
    }

    protected void initialize() {
        setTheta(new Matrix(getNumberOfNodes()+1, 1));
    }
}