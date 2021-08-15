package com.lxl.activation.impl;

import com.lxl.activation.Activation;
import com.lxl.utils.Matrix;

public class LinearActivation implements Activation {
    @Override
    public Matrix executeIn(Matrix m) {
        return m;
    }

    @Override
    public Matrix executeNew(Matrix m) {
        if (m == null) return null;
        return new Matrix(m);
    }

    @Override
    public int getSerialID() {
        return 1;
    }
}
