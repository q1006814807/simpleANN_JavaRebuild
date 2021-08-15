package com.lxl.activation.impl;

import com.lxl.activation.Activation;
import com.lxl.utils.Matrix;

public class DLinearActivation implements Activation {
    @Override
    public Matrix executeIn(Matrix m) {
        if (m == null) return null;
        for (int r = 0; r < m.getRow(); ++r) {
            for (int c = 0; c < m.getCol(); c++) {
                m.set(r, c, 1.f);
            }
        }
        return m;
    }

    @Override
    public Matrix executeNew(Matrix m) {
        if (m == null) return null;
        return new Matrix(m.getRow(), m.getCol(), 1.f);
    }

    @Override
    public int getSerialID() {
        return -1;
    }
}
