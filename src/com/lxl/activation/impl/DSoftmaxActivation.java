package com.lxl.activation.impl;

import com.lxl.activation.Activation;
import com.lxl.utils.Matrix;

public class DSoftmaxActivation implements Activation {
    @Override
    public Matrix executeIn(Matrix m) {
        if (m == null) return null;
        float maxVal = m.getMax();
        // 第一步：求expSum
        double expSum = 0;
        for (int r = 0; r < m.getRow(); ++r) {
            for (int c = 0; c < m.getCol(); c++) {
                double exp = Math.exp(m.get(r, c) - maxVal);
                expSum += exp;
            }
        }
        // 第二步：
        for (int r = 0; r < m.getRow(); ++r) {
            for (int c = 0; c < m.getCol(); c++) {
                double exp = Math.exp(m.get(r, c) - maxVal);
                m.set(r, c, (float)(exp * (expSum - exp) / (expSum * expSum)));
            }
        }
        return m;
    }

    @Override
    public Matrix executeNew(Matrix m) {
        if (m == null) return null;
        Matrix matrix = new Matrix(m);
        float maxVal = m.getMax();
        // 第一步：求expSum
        double expSum = 0;
        for (int r = 0; r < m.getRow(); ++r) {
            for (int c = 0; c < m.getCol(); c++) {
                double exp = Math.exp(m.get(r, c) - maxVal);
                expSum += exp;
            }
        }
        // 第二步：
        for (int r = 0; r < m.getRow(); ++r) {
            for (int c = 0; c < m.getCol(); c++) {
                double exp = Math.exp(m.get(r, c) - maxVal);
                matrix.set(r, c, (float)(exp * (expSum - exp) / (expSum * expSum)));
            }
        }
        return matrix;
    }

    @Override
    public int getSerialID() {
        return -3;
    }

}
