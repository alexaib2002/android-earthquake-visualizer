package org.dam.earthquakevisualizer.interfaces;

import org.dam.earthquakevisualizer.javabeans.Earthquake;

import java.util.ArrayList;
import java.util.List;

@FunctionalInterface
public interface ExecutableFilter {
    List<Earthquake> run();
}
