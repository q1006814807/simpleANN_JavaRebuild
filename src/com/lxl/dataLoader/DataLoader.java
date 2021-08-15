package com.lxl.dataLoader;

import com.lxl.utils.Matrix;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

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

    public DataLoader(String configFile) {
        File file = new File("src\\data\\" + configFile);
        Properties properties = new Properties();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            properties.load(fis);
            String trainImage = "src\\data\\" + properties.getProperty("trainImage");
            String trainLabel = "src\\data\\" + properties.getProperty("trainLabel");
            String testImage  = "src\\data\\" + properties.getProperty("testImage");
            String testLabel  = "src\\data\\" + properties.getProperty("testLabel");

            TRAIN_LEN  = Integer.parseInt(properties.getProperty("TRAIN_LEN"));
            TEST_LEN   = Integer.parseInt(properties.getProperty("TEST_LEN"));
            IMAGE_ROW  = Integer.parseInt(properties.getProperty("IMAGE_ROW"));
            IMAGE_COL  = Integer.parseInt(properties.getProperty("IMAGE_COL"));
            IMAGE_SKIP = Integer.parseInt(properties.getProperty("IMAGE_SKIP"));
            LABEL_SKIP = Integer.parseInt(properties.getProperty("LABEL_SKIP"));

            this.fileTrainImage = new File(trainImage);
            this.fileTrainLabel = new File(trainLabel);
            this.fileTestImage = new File(testImage);
            this.fileTestLabel = new File(testLabel);


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public ArrayList<Matrix> loadTrainImage() {
        ArrayList<Matrix> arr = new ArrayList<>(TRAIN_LEN);
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(fileTrainImage);
            fis.skip(IMAGE_SKIP);

            while (fis.available() > 0) {
                arr.add(readImage(fis, IMAGE_ROW, IMAGE_COL));
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return arr;
    }
    public ArrayList<Integer> loadTrainLabel() {
        ArrayList<Integer> arr = new ArrayList<>(TRAIN_LEN);
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(fileTrainLabel);
            fis.skip(LABEL_SKIP);

            byte[] bytes = fis.readNBytes(TRAIN_LEN);
            for (byte b : bytes) {
                arr.add(parseToInt(b));
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return arr;
    }
    public ArrayList<Matrix> loadTestImage() {
        ArrayList<Matrix> arr = new ArrayList<>(TEST_LEN);
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(fileTestImage);
            fis.skip(IMAGE_SKIP);

            while (fis.available() > 0) {
                arr.add(readImage(fis, IMAGE_ROW, IMAGE_COL));
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return arr;
    }
    public ArrayList<Integer> loadTestLabel() {
        ArrayList<Integer> arr = new ArrayList<>(TEST_LEN);
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(fileTestLabel);
            fis.skip(LABEL_SKIP);

            byte[] bytes = fis.readNBytes(TEST_LEN);
            for (byte b : bytes) {
                arr.add(parseToInt(b));
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return arr;
    }

    private static int parseToInt(byte[] bytes) {
        int res = 0;
        for (byte b : bytes) {
            res *= 256;
            res += b < 0 ? 256 + b : b;
        }
        return res;
    }
    private static int parseToInt(byte b) {
        return b < 0 ? 256 + b : b;
    }

    private static Matrix readImage(InputStream fis, int row, int col) throws IOException {
        Matrix matrix = new Matrix(row, col);
        byte[] bytes = fis.readNBytes(row * col);

        for (int r = 0; r < row; r++) {
            for (int c = 0; c < col; c++) {
                matrix.set(r, c, parseToInt(bytes[r * col + c]));
            }
        }
        return matrix;
    }

    public int getTrainLen() {
        return TRAIN_LEN;
    }

    public int getTestLen() {
        return TEST_LEN;
    }

    public int getImageRow() {
        return IMAGE_ROW;
    }

    public int getImageCol() {
        return IMAGE_COL;
    }
}
