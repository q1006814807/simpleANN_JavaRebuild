package com.lxl.utils;
import java.io.*;
import java.util.*;

public class Matrix implements Serializable {
    private ArrayList<ArrayList<Float>> arr;

    /**
     * 通过指定行列数，以及初始值构建矩阵
     * @param row
     * @param col
     * @param initVal
     */
    public Matrix(int row, int col, float initVal) {
        if (row <= 0 || col <= 0) throw new RuntimeException("row or col is illegal!");
        //arr = new float[row][col];
        arr = new ArrayList<>(row);

        for (int r = 0; r < row; ++r) {
            arr.add(new ArrayList<>(col));
            List<Float> cur = arr.get(r);
            for (int c = 0; c < col; ++c) {
                cur.add(initVal);
            }
        }
    }
    /**
     * 通过指定行列数，初始值为0构建矩阵
     * @param row
     * @param col
     */
    public Matrix(int row, int col) {
        this(row, col, 0f);
    }

    /**
     * 通过指定行列数，初始值为[initLower, initUpper]之间的数构建矩阵
     * @param row
     * @param col
     * @param initLower
     * @param initUpper
     */
    public Matrix(int row, int col, float initLower, float initUpper) {
        if (row <= 0 || col <= 0) throw new RuntimeException("row or col is illegal!");
        arr = new ArrayList<>(row);

        for (int r = 0; r < row; ++r) {
            arr.add(new ArrayList<>(col));
            List<Float> cur = arr.get(r);
            for (int c = 0; c < col; ++c) {
                float initVal = UniUtils.randomFloat(initLower, initUpper);
                //arr[r][c] = initVal;
                cur.add(initVal);
            }
        }
    }

    /**
     * 深度拷贝构建矩阵
     * @param arr
     */
    public Matrix(float[][] arr) {
        if (arr == null || arr.length == 0 || arr[0].length == 0)
            throw new RuntimeException("length of arr is illegal!");
        int row = arr.length;
        int col = arr[0].length;

        this.arr = new ArrayList<>(row);
        for (int r = 0; r < row; ++r) {
            this.arr.add(new ArrayList<>(col));
            List<Float> cur = this.arr.get(r);
            for (int c = 0; c < col; ++c) {
                cur.add(arr[r][c]);
            }
        }

    }

    /**
     * 深度拷贝构建矩阵
     * @param another
     */
    public Matrix(Matrix another) {
        int row = another.getRow();
        int col = another.getCol();
        //arr = new float[row][col];
        arr = new ArrayList<>(row);

        for (int r = 0; r < row; ++r) {
            arr.add(new ArrayList<>(col));
            List<Float> cur = arr.get(r);
            for (int c = 0; c < col; ++c) {
                cur.add(another.get(r, c));
            }
        }
    }

    /**
     * 拷贝引用构建矩阵
     * @param list
     */
    public Matrix(ArrayList<ArrayList<Float>> list) {
        if (list == null || list.size() == 0 || list.get(0).size() == 0)
            throw new RuntimeException("length of arr is illegal!");
        int row = list.size();
        int col = list.get(0).size();
        this.arr = list;
    }

    /********************************************************
     * 以下为矩阵运算
     *******************************************************/

    /**
     * 矩阵相加，结果保存在自身，返回自身引用
     * @param another
     * @return 自身引用
     */
    public Matrix addIn(Matrix another) {
        int row = Math.min(getRow(), another.getRow());
        int col = Math.min(getCol(), another.getCol());
        for (int r = 0; r < row; ++r) {
            for (int c = 0; c < col; ++c) {
                this.set(r, c, this.get(r, c) + another.get(r, c));
            }
        }
        return this;
    }
    /**
     * 矩阵所有元素增加val，结果保存在自身，返回自身引用
     * @param val
     * @return 自身引用
     */
    public Matrix addIn(float val) {
        for (int r = 0; r < getRow(); ++r) {
            for (int c = 0; c < getCol(); ++c) {
                this.set(r, c, this.get(r, c) + val);
            }
        }
        return this;
    }
    /**
     * 矩阵相加，结果保存在新矩阵，返回新矩阵引用
     * @param another
     * @return 新引用
     */
    public Matrix add(Matrix another) {
        int row = Math.min(getRow(), another.getRow());
        int col = Math.min(getCol(), another.getCol());
        Matrix matrix = new Matrix(row, col);
        for (int r = 0; r < row; ++r) {
            for (int c = 0; c < col; ++c) {
                matrix.set(r, c, this.get(r, c) + another.get(r, c));
            }
        }

        return matrix;
    }
    /**
     * 矩阵所有元素增加val，结果保存在新矩阵，返回自身引用
     * @param val
     * @return 新引用
     */
    public Matrix add(float val) {
        int row = getRow();
        int col = getCol();

        Matrix matrix = new Matrix(row, col);
        for (int r = 0; r < row; ++r) {
            for (int c = 0; c < col; ++c) {
                matrix.set(r, c, this.get(r, c) + val);
            }
        }

        return matrix;
    }

    public Matrix subIn(Matrix another) {
        int row = Math.min(getRow(), another.getRow());
        int col = Math.min(getCol(), another.getCol());
        for (int r = 0; r < row; ++r) {
            for (int c = 0; c < col; ++c) {
                this.set(r, c, this.get(r, c) - another.get(r, c));
            }
        }
        return this;
    }
    public Matrix subIn(float val) {
        return this.addIn(-val);
    }

    public Matrix sub(Matrix another) {
        int row = Math.min(getRow(), another.getRow());
        int col = Math.min(getCol(), another.getCol());
        Matrix matrix = new Matrix(row, col);
        for (int r = 0; r < row; ++r) {
            for (int c = 0; c < col; ++c) {
                matrix.set(r, c, this.get(r, c) - another.get(r, c));
            }
        }
        return matrix;
    }
    public Matrix sub(float val) {
        return this.add(-val);
    }


    /**
     * 矩阵所有元素乘以val，结果保存在自身，返回自身引用
     * @param val
     * @return 自身引用
     */
    public Matrix multiplyIn(float val) {
        for (int r = 0; r < getRow(); ++r) {
            for (int c = 0; c < getCol(); ++c) {
                this.set(r, c, this.get(r, c) * val);
            }
        }
        return this;
    }
    /**
     * 矩阵所有元素乘以val，结果保存在新矩阵，返回新矩阵引用
     * @param val
     * @return 新矩阵引用
     */
    public Matrix multiply(float val) {
        Matrix matrix = new Matrix(getRow(), getCol());

        for (int r = 0; r < getRow(); ++r) {
            for (int c = 0; c < getCol(); ++c) {
                matrix.set(r, c, this.get(r, c) * val);
            }
        }

        return matrix;
    }

    /**
     * 矩阵乘法，结果保存在矩阵，返回新矩阵引用
     * @param another
     * @return 返回新矩阵引用
     */
    public Matrix multiply(Matrix another) {
        // 矩阵乘法

        int R1 = this.getRow();
        int C1 = this.getCol();
        int R2 = another.getRow();
        int C2 = another.getCol();

        if (C1 != R2) return this;
        Matrix matrix = new Matrix(R1, C2);

        for (int r = 0; r < R1; ++r) {
            for (int c = 0; c < C2; ++c) {
                double sum = 0.;
                for (int i = 0; i < C1; ++i) {
                    sum += this.get(r, i) * another.get(i, c);
                }

                matrix.set(r, c, (float)sum);
            }
        }
        return matrix;
    }
    @Deprecated

    /**
     * 矩阵乘法，结果保存在自身，返回自身引用
     * @param another
     * @return 返回自身引用
     */
    public Matrix multiplyIn(Matrix another) {
        // 矩阵乘法
        this.setReference(this.multiply(another));
        return this;
    }

    public Matrix divide(float val) {
        Matrix matrix = new Matrix(getRow(), getCol());

        for (int r = 0; r < getRow(); ++r) {
            for (int c = 0; c < getCol(); ++c) {
                matrix.set(r, c, this.get(r, c) / val);
            }
        }

        return matrix;
    }
    public Matrix divideIn(float val) {
        for (int r = 0; r < getRow(); ++r) {
            for (int c = 0; c < getCol(); ++c) {
                this.set(r, c, this.get(r, c) / val);
            }
        }
        return this;
    }

    /**
     * 矩阵相应元素相乘（建议为同型矩阵）再相加
     * @param another
     * @return
     */
    public float dotAllAndSumAll(Matrix another) {
        int row = Math.min(getRow(), another.getRow());
        int col = Math.min(getCol(), another.getCol());

        double res = 0;

        for (int r = 0; r < row; ++r) {
            for (int c = 0; c < col; ++c) {
                res += this.get(r, c) * another.get(r, c);
            }
        }

        return (float)res;
    }

    /**
     * 返回矩阵所有元素的和
     * @return
     */
    public float sumAll() {
        double res = 0;
        for (int r = 0; r < getRow(); ++r) {
            for (int c = 0; c < getCol(); ++c) {
                res += this.get(r, c);
            }
        }
        return (float)res;
    }

    /**
     * 矩阵相应元素相乘（建议为同型矩阵），结果保存在自身，返回自身引用
     * @param another
     * @return 返回自身引用
     */
    public Matrix dotElemIn(Matrix another) {
        int row = Math.min(getRow(), another.getRow());
        int col = Math.min(getCol(), another.getCol());
        for (int r = 0; r < row; ++r) {
            for (int c = 0; c < col; ++c) {
                this.set(r, c, this.get(r, c) * another.get(r, c));
            }
        }

        return this;
    }
    /**
     * 矩阵相应元素相乘（建议为同型矩阵），结果保存在新矩阵，返回新矩阵引用
     * @param another
     * @return 返回新矩阵引用
     */
    public Matrix dotElem(Matrix another) {
        int row = Math.min(getRow(), another.getRow());
        int col = Math.min(getCol(), another.getCol());

        Matrix matrix = new Matrix(row, col);
        for (int r = 0; r < row; ++r) {
            for (int c = 0; c < col; ++c) {
                matrix.set(r, c, this.get(r, c) * another.get(r, c));
            }
        }

        return matrix;
    }

    /**
     * 矩阵相应行各元素相乘（建议为同型矩阵）再相加，结果保存新矩阵，返回新矩阵引用
     * @param another
     * @return 新矩阵引用
     */
    public Matrix dotRowAndSum(Matrix another) {
        int row = Math.min(getRow(), another.getRow());
        int col = Math.min(getCol(), another.getCol());
        Matrix matrix = new Matrix(row, 1);
        for (int r = 0; r < row; ++r) {
            double sum = 0;
            for (int c = 0; c < col; ++c) {
                sum += this.get(r, c) * another.get(r, c);
            }
            matrix.set(r, 0, (float)sum);
        }
        return matrix;
    }

    /**
     * 矩阵相应行各元素相乘（建议为同型矩阵）再相加，结果保存自身，返回自身引用
     * @param another
     * @return 自身引用
     */
    @Deprecated
    public Matrix dotRowAndSumIn(Matrix another) {
        this.setReference(dotRowAndSum(another));
        return this;
    }

    /**
     * 转置，结果保存在新矩阵，返回新矩阵引用
     * @return
     */
    public Matrix transpose() {
        Matrix matrix = new Matrix(getCol(), getRow());

        for (int r = 0; r < getRow(); ++r) {
            for (int c = 0; c < getCol(); ++c) {
                matrix.set(c, r, this.get(r, c));
            }
        }

        return matrix;
    }

    /**
     * 转置，结果保存在自身，返回自身引用
     * @return
     */
    @Deprecated
    public Matrix transposeIn() {
        this.setReference(transpose());
        return this;
    }

    public float sumRow(int r) {
        if (!isValidRowCol(r, 0)) {
            throw new RuntimeException("sumRow 参数r越界！");
        }

        double sum = 0;
        for (int c = 0; c < getCol(); ++c) {
            sum += get(r, c);
        }

        return (float)sum;
    }
    public float sumCol(int c) {
        if (!isValidRowCol(0, c)) {
            throw new RuntimeException("sumCol 参数c越界！");
        }

        double sum = 0;
        for (int r = 0; r < getRow(); ++r) {
            sum += get(r, c);
        }

        return (float)sum;
    }

    /**
     * 将自身每一列都与列向量做向量乘
     * |a x|             |d|   |ad xd|
     * |b y| colMultiply |e| = |be ye|
     * |c z|             |f|   |cf zf|
     * @param another 列向量
     * @return
     */
    public Matrix colMultiplyIn(Matrix another) {
        for (int r = 0; r < getRow(); ++r) {
            for (int c = 0; c < getCol(); ++c) {
                set(r, c, get(r, c) * another.get(r, 0));
            }
        }
        return this;
    }


    /********************************************************/


    //public float[] toFlatArray() {
    //    float[] flatArray = new float[getRow() * getCol()];
    //
    //    for (int r = 0; r < getRow(); ++r) {
    //        for (int c = 0; c < getCol(); ++c) {
    //            flatArray[r * getCol() + c] = this.get(r, c);
    //        }
    //    }
    //
    //    return flatArray;
    //}
    //public float[][] getMetaArray() {
    //    return this.arr;
    //}

    /**
     * 获取矩阵中最大值二维索引
     * @return int[2]
     */
    public int[] getMaxRC() {
        int maxR = 0;
        int maxC = 0;
        for (int r = 0; r < getRow(); r++) {
            for (int c = 0; c < getCol(); c++) {
                if (this.get(r, c) > this.get(maxR, maxC)) {
                    maxR = r;
                    maxC = c;
                }
            }
        }
        return new int[]{maxR, maxC};
    }
    /**
     * 获取矩阵中最小值二维索引
     * @return int[2]
     */
    public int[] getMinRC() {
        int minR = 0;
        int minC = 0;
        for (int r = 0; r < getRow(); r++) {
            for (int c = 0; c < getCol(); c++) {
                if (this.get(r, c) < this.get(minR, minC)) {
                    minR = r;
                    minC = c;
                }
            }
        }
        return new int[]{minR, minC};
    }
    /**
     * 获取矩阵中最大值
     * @return
     */
    public float getMax() {
        int[] maxRC = this.getMaxRC();
        return this.get(maxRC[0], maxRC[1]);
    }

    /**
     * 获取矩阵中最小值
     * @return
     */
    public float getMin() {
        int[] minRC = this.getMinRC();
        return this.get(minRC[0], minRC[1]);
    }

    /**
     * 获取矩阵对应行列的值
     * @param r
     * @param c
     * @return
     */
    public float get(int r, int c) {
        if (r < 0 || c < 0 || r >= getRow() || c >= getCol()) return 0f;
        return arr.get(r).get(c);
    }

    /**
     * 设置矩阵对应行列的值
     * @param r
     * @param c
     * @param val
     */
    public void set(int r, int c, float val) {
        if (!isValidRowCol(r, c)) return;
        arr.get(r).set(c, val);
    }


    public Matrix getRowProjMatrix(int r) {
        Matrix matrix = new Matrix(1, getCol());

        for (int c = 0; c < getCol(); ++c) {
            matrix.set(0, c, get(r, c));
        }

        return matrix;
    }
    public Matrix getColProjMatrix(int c) {
        Matrix matrix = new Matrix(getRow(), 1);

        for (int r = 0; r < getRow(); ++r) {
            matrix.set(r, 0, get(r, c));
        }

        return matrix;
    }

    /**
     * 从另外一个矩阵中将其值赋值到自身，不改变本身结构（行列数不变），当复制对象的行或列超过自身时将忽略超过的部分
     * @param another
     */
    public void copyValuesFrom(Matrix another) {
        int row = Math.min(getRow(), another.getRow());
        int col = Math.min(getCol(), another.getCol());
        for (int r = 0; r < row; ++r) {
            for (int c = 0; c < col; ++c) {
                set(r, c, another.get(r, c));
            }
        }
    }

    /**
     * 设置arr属性的引用
     * @param another
     */
    private void setReference(Matrix another) {
        this.arr = another.arr;
    }

    /**
     * 返回矩阵的行
     * @return
     */
    public int getRow() {
        return arr.size();
    }

    /**
     * 返回矩阵的列
     * @return
     */
    public int getCol() {
        return arr.get(0).size();
    }

    /**
     * 判断行列是否合法
     * @param r
     * @param c
     * @return
     */
    public boolean isValidRowCol(int r, int c) {
        return r >= 0 && c >= 0 && r < getRow() && c < getCol();
    }


    @Override
    public String toString() {
        int row = getRow();
        int col = getCol();

        StringBuilder str = new StringBuilder();
        str.append("Matrix(row = ").append(row).append(", col = ").append(col).append("){\n");
        for (int r = 0; r < row; ++r) {
            str.append(arr.get(r).toString());
            str.append('\n');
        }
        str.append("}\n");

        return str.toString();
    }


    public void addRow() {

    }

    public void reshape(int newR, int newC, float defaultVal) {

        if (newR <= getRow()) {
            UniUtils.resizeList(arr, newR);
        } else {
            int t = newR - getRow();
            while (t-- > 0) {
                arr.add(new ArrayList<>(newC));
            }
        }

        for (int r = 0; r < newR; ++r) {
            UniUtils.resizeList(arr.get(r), newC, defaultVal);
        }
    }



    public void writeObject(ObjectOutputStream out) throws IOException {
        out.writeObject(arr);
    }

    public void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        arr = (ArrayList<ArrayList<Float>>) in.readObject();
    }

}
