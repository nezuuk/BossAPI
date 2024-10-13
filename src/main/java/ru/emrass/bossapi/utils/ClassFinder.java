package ru.emrass.bossapi.utils;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;

import java.util.ArrayList;
import java.util.List;


public class ClassFinder {


    public static List<Class<?>> find(String scannedPackage) {
        List<Class<?>> listClass = new ArrayList<>();
        try (ScanResult scanResult = new ClassGraph()
                .whitelistPackages(scannedPackage)
                .scan()) {
            scanResult.getAllClasses().forEach(classInfo -> {
                try {
                    listClass.add(Class.forName(classInfo.getName()));
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        return listClass;
    }
}
