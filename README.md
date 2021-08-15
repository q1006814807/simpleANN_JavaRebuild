# simpleANN_JavaRebuild

# 《SimpleANN 》

## ——基于Java的简单全连接神经网络框架

作者：笑融君				时间：2021/08/13



## 一、简介 

​	这是一款非常简略的全连接神经网络框架，得益于Java面向对象的特点，该框架所有模块功能明确、层次清晰，并且实现了一个简单的矩阵库，因此非常适合初学者进行参考学习；激活函数、代价（损失）函数、输入层、隐藏层、输出层皆对象模块化，可自由组合搭配；框架的优化算法为miniBatch梯度下降；最后，此次介绍基于手写数字MNIST进行演示。

- MNIST 下载连接：http://yann.lecun.com/exdb/mnist/

- 预计代码：2000行



## 二、框架结构介绍

​		如图所示，主要框架结构为：

![structure](D:\Workspace\JavaLearningWorkspace\SimpleANN\arts\readme.assets\structure.jpg)

- 激活函数模块 Activation
- 代价（损失）函数模块 CostFunction
- 数据加载模块 DataLoader
- 神经网络层与实体模块 Layer
- 神经网络操作模块 ModelController
- 参数容器模块 ParamsContainer
- 测试模块 Test
- 工具模块 Utils
- 以及数据目录 Data

![image-20210815160512811](D:\Workspace\JavaLearningWorkspace\SimpleANN\arts\readme.assets\image-20210815160512811.png)

## 三、快速开始 Quickstart



### 1）配置data目录

​	将**MNIST**数据导入到**src\\data**目录，并且创建**dataset.properties**配置文件，并配置以下值（不要加空格）

```text
#训练数据文件
trainImage=train-images.idx3-ubyte
#训练标签文件
trainLabel=train-labels.idx1-ubyte
#测试数据文件
testImage=t10k-images.idx3-ubyte
#测试标签文件
testLabel=t10k-labels.idx1-ubyte
#训练数据集长度
TRAIN_LEN=60000
#测数据集长度
TEST_LEN=10000
#数据长宽
IMAGE_ROW=784
IMAGE_COL=1
#数据开始位置(单位：字节)
IMAGE_SKIP=16
LABEL_SKIP=8
```

![image-20210815160532300](D:\Workspace\JavaLearningWorkspace\SimpleANN\arts\readme.assets\image-20210815160532300.png)



### 2）测试代码

​	在test模块目录中创建**QuickStart.java**，并复制运行以下代码

```java
package com.lxl.test;

import com.lxl.activation.impl.DTanhActivation;
import com.lxl.activation.impl.TanhActivation;
import com.lxl.dataLoader.DataLoader;
import com.lxl.layer.InputLayer;
import com.lxl.layer.Layer;
import com.lxl.layer.LayersBox;
import com.lxl.layer.OutputLayer;
import com.lxl.modelController.ModelController;

public class QuickStart {
    public static void main(String[] args) {
        // 创建输入层、隐藏层以及输出层
        Layer layer1 = new InputLayer(784, new TanhActivation(), new DTanhActivation());
        Layer layer2 = new Layer(100, new TanhActivation(), new DTanhActivation());
        Layer layer3 = new OutputLayer(10);
        // 组装Layer到LayersBox中，形成神经网络实体
        LayersBox layersBox = new LayersBox(layer1, layer2, layer3);
        
	    // 指定数据集配置文件
        DataLoader dataLoader = new DataLoader("dataset.properties");
        
        // 创建模型
        ModelController modelController = new ModelController(dataLoader, layersBox);
        // 设置数据值范围，以便数据标准化
        modelController.setDataRange(0, 255);
        // 初始化训练模式（初始化层参数、加载数据集至内存等），并指定在训练集中的开始位置来分割出一部份做为验证集（后半部分，以下0-49999为训练集、50000-59999为验证集）
        modelController.initializeTraining(50000);

        // 开始训练，以下示例将训练2个周期，每一次将遍历一次训练集，其中参数0.2f为学习率，100为miniBatch的Size大小
        modelController.trainForEpoch(0.2f, 100);
        modelController.trainForEpoch(0.2f, 100);
        
        // 查看验证集结果
        modelController.checkValidation();
    }
}

```

测试结果示例：

![image-20210813221951994](D:\Workspace\JavaLearningWorkspace\SimpleANN\arts\readme.assets\image-20210813221951994.png)



### 3）保存和加载模型文件

- 训练结束后，保存文件：

```java
layerBox.save("m1"); // 参数为[模型名称]
```

![image-20210813221922091](D:\Workspace\JavaLearningWorkspace\SimpleANN\arts\readme.assets\image-20210813221922091.png)

- 读取模型

```java
layerBox.load("m1", 3); // 参数为[模型名称]，[网络层数]
```



## 四、工具模块 Utils

![image-20210813221907658](D:\Workspace\JavaLearningWorkspace\SimpleANN\arts\readme.assets\image-20210813221907658.png)



​	工具模块由**Matrix**简单矩阵库、**UniUtils**工具类组成。

- Matrix

  **Matrix**矩阵库由二维数组实现，提供了一系列必要的线性代数运算方法。

- UniUtils

  **UniUtils**工具类提供了一些简单的方法，如随机数、数组resize等等。

​	工具模块比较简单，不多赘述。



## 五、激活函数模块与自定义激活函数 Activation

![image-20210813221852120](D:\Workspace\JavaLearningWorkspace\SimpleANN\arts\readme.assets\image-20210813221852120.png)

​	如图所示，激活函数模块由**激活函数Activation接口**以及**其实impl现类**组成（其中**ActivationFactory**是作为序列化的工具，并不重要，可以忽略），因此你可以自由地实现Activation接口从而拓展增加新的激活函数！

- 如何定义呢？请看如下例子：

```java
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

```

​	实现Activation接口，就需要你重写**executeIn()、executeNew()、getSerialID()** 三个方法，executeIn()与**executeNew**()的区别在与：答案是否保存在自身，或者说是否创建新的Matrix来保存答案。**要记得定义相应的导函数哦！**





## 六、代价（损失）函数模块 CostFunction

![image-20210815154408855](D:\Workspace\JavaLearningWorkspace\SimpleANN\arts\readme.assets\image-20210815154408855.png)

​	代价（损失）函数模块由**CostFunction**接口、BaseCost基类以及他们的继承与实现类**impl**组成（其中**CostFunctionFactory**也是作为序列化工具，并不重要，可以忽略）

- CostFunction

```java
public interface CostFunction {
    float getCost();
    
    Matrix getEndDZ();
    
    boolean isRight();
    
    int getSerialID();
}
```

​	定义要求了需要实现的4个方法，分别是实现计算Cost代价（损失）值、实现计算输出层Z梯度的向量矩阵、实现判断预测是否正确以及实现获取序列化ID。

- BaseCost

```java
public class BaseCost {
    protected ProcessParams params;
    protected Matrix yi;
    protected LayersBox layersBox;

    public BaseCost(ProcessParams params, Matrix yi, LayersBox layersBox) {
        this.params = params;
        this.yi = yi;
        this.layersBox = layersBox;
    }

    public boolean isRight() {
        Matrix A = params.getA(layersBox.getDepth()-1);
        // 是否预测正确
        return A.getMaxRC()[0] == yi.getMaxRC()[0];
    }

}
```

​	BaseCost是CostFunction实现类impl需要继承的基类，提供了构造方法、需要的成员参数、以及实现了预测是否正确的功能，简化了CostFunction实现类的编写难度。

- 继承与实现类impl

  以CrossEntropyCost交叉熵代价（损失）函数为例

```java
public class CrossEntropyCost extends BaseCost implements CostFunction {
    public CrossEntropyCost(ProcessParams params, Matrix yi, LayersBox layersBox) {
        super(params, yi, layersBox);
    }

    @Override
    public float getCost() {
        Matrix A = params.getA(layersBox.getDepth()-1);
        return UniUtils.getCrossEntropy(A, yi);
    }

    @Override
    public Matrix getEndDZ() {
        int curDepth = layersBox.getDepth() - 1;
        Matrix A = params.getA(curDepth);
        return A.sub(yi);
    }

    @Override
    public int getSerialID() {
        return 1;
    }


}
```

目前的代价（损失）函数已经实现了最小二乘法以及交叉熵，通过一行代码的设置即可轻松更换与使用。



- 如何设置神经网络模型的代价（损失）函数呢？

```java
// 首先定义层layer
Layer layer1 = new InputLayer(784, new TanhActivation(), new DTanhActivation());
Layer layer2 = new Layer(128, new TanhActivation(), new DTanhActivation());
Layer layer3 = new Layer(64, new TanhActivation(), new DTanhActivation());
Layer layer4 = new OutputLayer(10);
// 组装成一个LayersBox神经网络实体
LayersBox layersBox = new LayersBox(layer1, layer2, layer3, layer4);
// 通过LayersBox的setCostFunctionID()方法设置代价（损失）函数，0：最小二乘法，1：交叉熵
layersBox.setCostFunctionID(1); // 交叉熵
layersBox.setCostFunctionID(0); // 最小二乘法
```









## 七、数据加载模块 DataLoader

![image-20210813221833388](D:\Workspace\JavaLearningWorkspace\SimpleANN\arts\readme.assets\image-20210813221833388.png)

缩略代码：

```java
package com.lxl.dataLoader;
public class DataLoader {
    private File fileTrainImage = null;
    private File fileTrainLabel = null;
    private File fileTestImage = null;
    private File fileTestLabel = null;
    private int TRAIN_LEN;
    private int TEST_LEN;

    private int IMAGE_ROW;
    private int IMAGE_COL;
    private int IMAGE_SKIP;
    private int LABEL_SKIP;

    private String configFile;

    public DataLoader(String configFile);

    public ArrayList<Matrix> loadTrainImage();
    public ArrayList<Integer> loadTrainLabel();
    public ArrayList<Matrix> loadTestImage();
    public ArrayList<Integer> loadTestLabel();

    private static int parseToInt(byte[] bytes);

    private static Matrix readImage(InputStream fis, int row, int col);
}

```

使用其中4个方法即可获取训练集数据以及标签、测试集数据以及标签的**Matrix**数组：

```java
DataLoader dataLoader = new DataLoader("dataset.properties");
dataLoader.loadTrainImage(); // 获取训练数据数组
dataLoader.loadTrainLabel(); // 获取训练标签数组
dataLoader.loadTestImage(); // 获取测试数据数组
dataLoader.loadTestLabel(); // 获取测试标签数组
```



## 八、神经网络层与实体模块 Layer

![image-20210813221647657](D:\Workspace\JavaLearningWorkspace\SimpleANN\arts\readme.assets\image-20210813221647657.png)

- Layer

![image-20210815160954201](D:\Workspace\JavaLearningWorkspace\SimpleANN\arts\readme.assets\image-20210815160954201.png)

​	其中**InputLayer**、**OutputLayer**继承于**Layer**，对Layer实现了简单的参数封装，其本质没有任何差别，你也可以只使用**Layer**创建网络层结构。**Layer**对象负责该层的结点参数theta、激活函数activation的保存。

- LayersBox

![image-20210815161006948](D:\Workspace\JavaLearningWorkspace\SimpleANN\arts\readme.assets\image-20210815161006948.png)

​	**LayersBox**集装箱是作为**Layer**的组织管理容器，是一个**神经网络实体**，对外界提供方便的层参数**set、get**方法，并且负责每一层的参数的初始化与梯度下降，相应地负责管理使用哪种代价函数，也负责读取、保存层模型的功能。



## 九、参数容器模块 ParamsContainer

![image-20210813223433686](D:\Workspace\JavaLearningWorkspace\SimpleANN\arts\readme.assets\image-20210813223433686.png)

- ProcessParams

  fowardPropagate()前向传播的过程参数，主要保存了每一层的A、Z参数

![image-20210813223621045](D:\Workspace\JavaLearningWorkspace\SimpleANN\arts\readme.assets\image-20210813223621045.png)

- Gradients

  backPropagate()反向传播的过程参数，主要保存了每一层的梯度dθ、代价值cost、是否正确isRight

  ![image-20210813223835563](D:\Workspace\JavaLearningWorkspace\SimpleANN\arts\readme.assets\image-20210813223835563.png)

- BatchGradients

  miniBatch训练中用于累积保存每一次训练的Gradients参数，提供cost统计、正确率rightRate统计等方法

![image-20210813224307283](D:\Workspace\JavaLearningWorkspace\SimpleANN\arts\readme.assets\image-20210813224307283.png)

## 十、神经网络操作模块 ModelController

![image-20210815172150411](D:\Workspace\JavaLearningWorkspace\SimpleANN\arts\readme.assets\image-20210815172150411.png)

​	神经网络操作模块主要对LayersBox组织管理的神经网络实体进行训练，如果你把以上所有模块都看完了得话，那么神经网络操作模块是一点都不难理解的，因为它只是对以上模块的组合使用而已，每个模块各司其职，分工明确。要理解主模块，需要从一次miniBatch训练周期的过程开始着手：

```java
public void trainForEpoch(float learningRate, int batchSize) {
    System.out.println("【程序】该Epoch开始训练...");
    // 打乱trainingSet
    ArrayList<Integer> iMap = new ArrayList<>(validationStart);
    for (int i = 0; i < validationStart; ++i) iMap.add(i);
    Collections.shuffle(iMap);

    // 一轮batchSize 总共validationStart/batchSize轮

    for (int i = 0; i < validationStart/batchSize; ++i) {
        BatchGradients batchGradients = new BatchGradients(layersBox);

        for (int k = i * batchSize; k < (i+1)*batchSize; ++k) {
            Matrix xi = normalizeImage(trainImages.get(iMap.get(k)));
            Matrix yi = normalizeLabel(trainLabels.get(iMap.get(k)));

            ProcessParams params = forwardPropagate(xi);

            CostFunction costFunc = CostFunctionFactory.
                getCostFunction(layersBox.getCostFunctionID(), params, yi, layersBox);

            Gradients gradients = backPropagate(yi, params, costFunc);
            batchGradients.addGradients(gradients);
        }

        layersBox.updateLayers(batchGradients, learningRate);
        System.out.println("----------第" + i + "批样本训练----------");
        System.out.println("Cost      = " + batchGradients.getStandardCost());
        System.out.println("RightRate = " + batchGradients.getRightRate());
    }

    System.out.println("【程序】该Epoch训练完毕...");
}
```

​	一次训练包括：

1）根据一个数据进行神经网络前向传播，得到保存过程参数A、Z的ProcessParams；

2）根据前向传播的过程参数ProcessParams以及正确标签进行反向传播，得到保存每一层梯度参数的Gradients；

3）将Gradients直接参与学习或者积累起来加入BatchGradients，最后将梯度参数传入LayersBox.updateLayers()方法进行更新学习。



- 操作模块常用方法

```java
// 初始化训练集
void initializeTraining(int validationStart);

// 初始化测试及
void initializeTest();

// 查看验证集cost / rightRate
void checkValidation();

// 查看测试集cost / rightRate
void checkTest();

// 查看训练集cost / rightRate
void checkTraining();

// 设置数据范围，便于数据标准化
void setDataRange(float left, float right);

// 一个周期训练（训练集）
void trainForEpoch(float learningRate, int batchSize);

// 一个周期训练（训练集+验证集）
void trainForEpochWithAllTrainingData(float learningRate, int batchSize)

```



## 十一、公式推导补充

![img](D:\Workspace\JavaLearningWorkspace\SimpleANN\arts\readme.assets\c19757b0f18c0a94bcdfee2c6c57be57.png)

![img](D:\Workspace\JavaLearningWorkspace\SimpleANN\arts\readme.assets\e356e9fceba8b6f7f7b13e0eae43ab07.png)

![img](D:\Workspace\JavaLearningWorkspace\SimpleANN\arts\readme.assets\img_1430(20210814-001424).jpg)

![image-20210815171043583](D:\Workspace\JavaLearningWorkspace\SimpleANN\arts\readme.assets\image-20210815171043583.png)

