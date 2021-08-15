package com.lxl.paramsContainer;

import com.lxl.layer.LayersBox;
import com.lxl.utils.Matrix;
import com.lxl.utils.UniUtils;

import java.util.ArrayList;

public class BatchGradients {

    private ArrayList<Matrix> dThetas;
    private int count = 0;

    private float accumCost = 0;
    private int accumRights = 0;

    public BatchGradients(LayersBox layersBox) {
        dThetas = new ArrayList<Matrix>(layersBox.getDepth());

        UniUtils.resizeList(dThetas, layersBox.getDepth());

        for (int i = 0; i < dThetas.size(); ++i) {
            dThetas.set(i,
                    new Matrix(
                            layersBox.getTheta(i).getRow(),
                            layersBox.getTheta(i).getCol()
                    )
            );
        }
    }

    public ArrayList<Matrix> getDThetas() {
        return dThetas;
    }

    public int getCount() {
        return count;
    }

    public float getAccumCost() {
        return accumCost;
    }

    public float getStandardCost() {
        return (float)(accumCost * ((double)10000 / count));
    }

    public float getRightRate() {
        return (float)accumRights / count;
    }


    public int getAccumRights() {
        return accumRights;
    }




    // 若要实现Batch多线程，需要在如下增加syn标记
    public void addGradients(Gradients g) {
        this.count++;
        this.accumCost += g.getCost();
        this.accumRights += g.isRight() ? 1 : 0;

        for (int i = 0; i < dThetas.size(); ++i) {
            dThetas.get(i).addIn(g.getDTheta(i));
        }

    }

}
