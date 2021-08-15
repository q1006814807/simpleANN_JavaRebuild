package com.lxl.layer;

import com.lxl.activation.Activation;
import com.lxl.activation.impl.DLinearActivation;
import com.lxl.activation.impl.LinearActivation;
import com.lxl.costFunction.CostFunction;
import com.lxl.paramsContainer.BatchGradients;
import com.lxl.utils.Matrix;

import java.io.*;
import java.util.ArrayList;

public class LayersBox implements Serializable {

    private Layer[] layers;
    transient boolean isInit = false;
    private int costFunctionID = 0;

    public LayersBox(Layer... layers) {
        this.layers = layers;
    }

    public int getCostFunctionID() {
        return costFunctionID;
    }

    public void setCostFunctionID(int costFunctionID) {
        this.costFunctionID = costFunctionID;
    }

    public int getDepth() {
        return layers.length;
    }

    public void initialize() {
        if (isInit) return;
        if (layers.length < 2) throw new RuntimeException("LayersBox初始化错误！Layers层数错误！");
        isInit = true;

        ((InputLayer)layers[0]).initialize();
        for (int i = 1; i < layers.length; ++i) {
            layers[i].initialize(layers[i-1]);
        }
    }

    public Layer getLayer(int depth) {
        return layers[depth];
    }

    public Activation getActivation(int depth) {
        return layers[depth].getActivation();
    }

    public Activation getDActivation(int depth) {
        return layers[depth].getDActivation();
    }

    public Matrix getTheta(int depth) {
        return layers[depth].getTheta();
    }

    public Layer[] getLayers() {
        return layers;
    }

    public int getNumberOfNodes(int depth) {
        return layers[depth].getNumberOfNodes();
    }

    public void updateLayers(BatchGradients batchGradients, float learningRate) {
        ArrayList<Matrix> dThetas = batchGradients.getDThetas();
        for (int i = 0; i < getDepth(); ++i) {
            Matrix theta = layers[i].getTheta();
            Matrix dTheta = dThetas.get(i);
            dTheta.divideIn(batchGradients.getCount()).multiplyIn(learningRate);
            theta.subIn(dTheta);
        }
    }

    public void writeObject(ObjectOutputStream out) throws IOException {
        for (int i = 0; i < layers.length; ++i) {
            layers[i].writeObject(out);
        }

        out.writeInt(costFunctionID);
    }

    public void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        for (int i = 0; i < layers.length; ++i) {
            layers[i].readObject(in);
        }
        costFunctionID = in.readInt();
    }

    public void save(String name) {
        File file = new File("src\\data\\" + name + ".model");
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = new FileOutputStream(file);
            oos = new ObjectOutputStream(fos);
            this.writeObject(oos);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public void load(String name, int depth) {
        File file = new File("src\\data\\" + name + ".model");
        FileInputStream fis = null;
        ObjectInputStream ois = null;

        layers = new Layer[depth];
        for (int i = 0; i < depth; ++i) {
            layers[i] = new Layer(1, new LinearActivation(), new DLinearActivation());
        }

        try {
            fis = new FileInputStream(file);
            ois = new ObjectInputStream(fis);
            readObject(ois);

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        isInit = true;
    }

    //public static LayersBox loadOf(String name) {
    //    LayersBox layersBox = new LayersBox();
    //    layersBox.
    //}
}
