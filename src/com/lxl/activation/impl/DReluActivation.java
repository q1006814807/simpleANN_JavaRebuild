package com.lxl.activation.impl;

import com.lxl.activation.Activation;
import com.lxl.utils.Matrix;

public class DReluActivation implements Activation {
    @Override
    public Matrix executeIn(Matrix m) {
        if (m == null) return null;
        for (int r = 0; r < m.getRow(); ++r) {
            for (int c = 0; c < m.getCol(); c++) {
                if (m.get(r, c) < 0) {
                    m.set(r, c, 0);
                } else {
                    m.set(r, c, 1);
                }
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
                if (m.get(r, c) < 0) {
                    matrix.set(r, c, 0);
                } else {
                    matrix.set(r, c, 1);
                }
            }
        }
        return matrix;
    }

    @Override
    public int getSerialID() {
        return -4;
    }
}
