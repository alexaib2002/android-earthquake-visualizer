package org.dam.earthquakevisualizer.interfaces;

import org.dam.earthquakevisualizer.javabeans.Earthquake;

import java.util.List;

@FunctionalInterface
public interface ExecutableFilter {
    List<Earthquake> run();

    default ExecutableFilter wrapStringOn(String str) {
        return new ExecutableFilter() {
            @Override
            public List<Earthquake> run() {
                return ExecutableFilter.this.run();
            }

            @Override
            public String toString() {
                return str;
            }
        };
    }
}
