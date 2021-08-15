package com.lxl.layer;

import com.lxl.activation.Activation;
import com.lxl.activation.impl.DSoftmaxActivation;
import com.lxl.activation.impl.SoftmaxActivation;

public class OutputLayer extends Layer {
    public OutputLayer(int numberOfNodes) {
        super(numberOfNodes, new SoftmaxActivation(), new DSoftmaxActivation());
    }

    public OutputLayer(int numberOfNodes, Activation activation, Activation dActivation) {
        super(numberOfNodes, activation, dActivation);
    }



}
