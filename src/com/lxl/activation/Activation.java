package com.lxl.activation;

import com.lxl.utils.Matrix;

public interface Activation {
    /**
     * 对传入的矩阵自身进行修改，并返回自身
     * @param m 矩阵
     * @return 返回自身引用
     */
    Matrix executeIn(Matrix m);

    /**
     * 对传入的矩阵自身不做修改，并返回新创建的矩阵
     * @param m 矩阵
     * @return 返回新矩阵
     */
    Matrix executeNew(Matrix m);

    int getSerialID();
}
