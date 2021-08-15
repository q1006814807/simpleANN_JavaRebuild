package com.lxl.activation;

import com.lxl.activation.impl.*;

public class ActivationFactory {
    private ActivationFactory() {}

    public static Activation getActivation(int activationSerialID) {
        switch (activationSerialID) {
            case 1  : return new LinearActivation();
            case -1 : return new DLinearActivation();

            case 2  : return new TanhActivation();
            case -2 : return new DTanhActivation();

            case 3  : return new SoftmaxActivation();
            case -3 : return new DSoftmaxActivation();

            case 4  : return new ReluActivation();
            case -4 : return new DReluActivation();
        }
        return null;
    }
}
