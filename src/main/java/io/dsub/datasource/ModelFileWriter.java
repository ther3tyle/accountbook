package io.dsub.datasource;

import io.dsub.model.Model;
import io.dsub.util.DataType;
import io.dsub.util.FileHelper;
import io.dsub.util.ModelParserUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ModelFileWriter<T extends Model> implements LocalModelWriter<T> {
    private static final Logger logger = Logger.getLogger(ModelFileWriter.class.getName());
    private final Function<String, Model> parser;
    private Path sourcePath;

    public ModelFileWriter(DataType type) {
        this(type, FileHelper.getPath(type));
    }

    public ModelFileWriter(DataType type, File file) {
        this(type, file.toPath());
    }

    public ModelFileWriter(DataType type, Path sourcePath) {
        this.parser = ModelParserUtil.get(type);
        this.sourcePath = sourcePath;
    }

    @Override
    public void write(T item) {
        if (item != null) {
            this.writeTo(item);
        }
    }

    @Override
    public void writeAll(T[] items) {
        if (items == null) return;
        this.writeTo(items);
    }

    @Override
    public void writeAll(Collection<T> items) throws IOException {
        this.writeTo(items);
    }

    @Override
    public void writeTo(File file, T... items) throws IOException {
        writeTo(file, true, Arrays.asList(items));
    }

    @SafeVarargs
    @Override
    public final void overwrite(File file, T... items) throws IOException {
        writeTo(file, false, Arrays.asList(items));
    }

    @Override
    public void overwrite(File file, Collection<T> items) throws IOException {
        writeTo(file, false, items);
    }

    @Override
    public void overwrite(Collection<T> items) throws IOException {
        writeTo(sourcePath.toFile(), false, items);
    }

    /**
     * Base method for writing transaction(s) to given file
     * The given transaction will be appended to the file, but they won't be overwritten.
     *
     * @param file     target file
     * @param isAppend will append or overwrite the file
     * @param items    transaction(s) to be written
     * @throws IOException something went wrong
     */
    private void writeTo(File file, boolean isAppend, Collection<T> items) throws IOException {
        if (file.exists()) {
            file.delete();
            file.createNewFile();
        }
        try(FileWriter fileWriter = new FileWriter(file, isAppend)) {
            try {
                List<String> list = items.stream()
                        .map(Objects::toString)
                        .collect(Collectors.toList());
                for (String s : list) {
                    fileWriter.write(s);
                }
                fileWriter.flush();
            } catch (IOException e) {
                logger.severe(e.getMessage());
            }
        }
    }

    @Override
    public void reset() {
        try {
            if (this.sourcePath.toFile().exists()) {
                this.sourcePath.toFile().delete();
            }
            this.sourcePath.toFile().createNewFile();
        } catch (IOException e) {
            logger.warning(e.getMessage());
        }
    }


    @SafeVarargs
    private void writeTo(T... items) {
        if (items == null || items.length == 0) return;

        try (FileWriter writer = new FileWriter(this.sourcePath.toFile(), true)) {
            for (T item : items) {
                writer.append(item.toString());
            }
            writer.flush();
        } catch (IOException e) {
            logger.warning(e.getMessage());
        }
    }

    private void writeTo(Collection<T> items) {
        if (items == null || items.size() == 0) return;
        try (FileWriter writer = new FileWriter(this.sourcePath.toFile(), true)) {
            for (T item : items) {
                writer.append(item.toString());
            }
            writer.flush();
        } catch (IOException e) {
            logger.warning(e.getMessage());
        }
    }

    @Override
    public Path getSourcePath() {
        return this.sourcePath;
    }

    @Override
    public void setSourcePath(Path sourcePath) {
        this.sourcePath = sourcePath;
    }
}
