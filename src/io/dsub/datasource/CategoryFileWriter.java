package io.dsub.datasource;

import io.dsub.model.Category;
import io.dsub.util.FileHandler;
import io.dsub.util.LocalDataType;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Logger;

public class CategoryFileWriter implements ModelWriter<Category> {

    private static final Logger logger = Logger.getLogger(CategoryFileWriter.class.getName());

    private final Path source;

    public CategoryFileWriter() {
        this(FileHandler.getPath(LocalDataType.CATEGORY));
    }

    public CategoryFileWriter(Path source) {
        this.source = source;
    }

    @Override
    public void write(Category item) {
        if (item != null) {
            this.writeTo(item);
        }
    }

    @Override
    public void writeAll(Category[] items) {
        if (items == null) return;
        this.writeTo(items);
    }

    @Override
    public void reset() {
        boolean ans = this.source.toFile().delete();
        if (!ans) {
            logger.warning("file delete failed");
            return;
        }
        try {
            ans = this.source.toFile().createNewFile();
            if (!ans) {
                logger.warning("file creation failed");
            }
        } catch (IOException e) {
            logger.warning(e.getMessage());
        }
    }

    private void writeTo(Category... items) {
        if (items == null) return;

        try (FileWriter writer = new FileWriter(this.source.toFile(), true)) {
            for (Category item : items) {
                writer.append(item.toString());
            }
            writer.flush();
        } catch (IOException e) {
            logger.warning(e.getMessage());
        }
    }
}
