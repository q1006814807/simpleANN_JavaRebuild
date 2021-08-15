package com.lxl.activation.impl;

import com.lxl.activation.Activation;
import com.lxl.utils.Matrix;

public class TanhActivation implements Activation {
    @Override
    public Matrix executeIn(Matrix m) {
        if (m == null) return null;
        for (int r = 0; r < m.getRow(); ++r) {
            for (int c = 0; c < m.getCol(); c++) {
                m.set(r, c, (float)Math.tanh(m.get(r, c)));
            }
        }
        return m;
    }

    @Override
    public Matrix executeNew(Matrix m) {
        if (m == null) return null;
        Matrix matrix = new Matrix(m);
        for (int r = 0; r < m.getRow(); ++r) {
            for (int c = 0; c < m.getCol(); c++) {
                matrix.set(r, c, (float)Math.tanh(m.get(r, c)));
            }
        }
        return matrix;
    }

    @Override
    public int getSerialID() {
        return 2;
    }
}
