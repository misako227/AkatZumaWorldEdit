package com.z227.AkatZumaWorldEdit.Core;

public class ItemData {
    private static int testData = 0;

    public ItemData() {
    }

    public static int getTestData() {
        return testData;
    }

    public static void setTestData() {
        testData ++;
    }
    public static void setTestData(int data) {
        testData = data;
    }
}
