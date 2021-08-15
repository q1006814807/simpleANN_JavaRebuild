package com.lxl.layer;

import com.lxl.activation.Activation;
import com.lxl.activation.ActivationFactory;
import com.lxl.utils.Matrix;
import com.lxl.utils.UniUtils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class Layer implements Serializable {
    private Matrix theta;
    private int numberOfNodes;
    transient private Activation activation;
    transient private Activation dActivation;


    public Layer(int numberOfNodes, Activation activation, Activation dActivation) {
        this.numberOfNodes = numberOfNodes;
        this.activation = activation;
        this.dActivation = dActivation;
    }

    protected void initialize(Layer preLayer) {
        float res = (float)Math.sqrt(6.f / (preLayer.getNumberOfNodes() + this.numberOfNodes));

        int preNumberOfNodes = preLayer.getNumberOfNodes();
        theta = new Matrix(numberOfNodes+1, preNumberOfNodes+1, -res, +res);
        // 将第一行哨兵theta都设为0;
        for (int i = 0; i < theta.getCol(); ++i) theta.set(0, i, 0);
        // 将所有偏置设为0;
        for (int i = 1; i < theta.getRow(); ++i) theta.set(i, 0, 0);
    }

    public int getNumberOfNodes() {
        return numberOfNodes;
    }

    public Activation getActivation() {
        return activation;
    }

    public Activation getDActivation() {
        return dActivation;
    }

    public Matrix getTheta() {
        return theta;
    }

    public void setTheta(Matrix theta) {
        this.theta = theta;
    }

    public void writeObject(ObjectOutputStream out) throws IOException {
        //out.writeObject(theta);
        theta.writeObject(out);
        out.writeInt(activation.getSerialID());
        out.writeInt(dActivation.getSerialID());
        out.writeInt(numberOfNodes);
    }

    public void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        //theta = (Matrix)in.readObject();
        if (theta == null) theta = new Matrix(1, 1);
        theta.readObject(in);
        activation = ActivationFactory.getActivation(in.readInt());
        dActivation = ActivationFactory.getActivation(in.readInt());
        numberOfNodes = in.readInt();
    }


}
