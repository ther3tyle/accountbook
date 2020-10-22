package io.dsub;

@FunctionalInterface
public interface ThrowingTask {
    void run() throws Exception;
}
