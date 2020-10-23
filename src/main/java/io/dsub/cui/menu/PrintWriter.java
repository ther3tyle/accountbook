package io.dsub.cui.menu;

import io.dsub.ThrowingTask;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;

public class PrintWriter extends BufferedWriter {

    public PrintWriter(Writer out) {
        super(out);
    }

    public PrintWriter(Writer out, int sz) {
        super(out, sz);
    }

    /**
     * Writes a single character.
     *
     * @param c character
     */
    @Override
    public void write(int c) {
        runWithRetries(() -> super.write(c));
    }

    /**
     * Writes a portion of an array of characters.
     *
     * <p> Ordinarily this method stores characters from the given array into
     * this stream's buffer, flushing the buffer to the underlying stream as
     * needed.  If the requested length is at least as large as the buffer,
     * however, then this method will flush the buffer and write the characters
     * directly to the underlying stream.  Thus redundant
     * {@code BufferedWriter}s will not copy data unnecessarily.
     *
     * @param chBuf A character array
     * @param off   Offset from which to start reading characters
     * @param len   Number of characters to write
     * @throws IndexOutOfBoundsException If {@code off} is negative, or {@code len} is negative,
     *                                   or {@code off + len} is negative or greater than the length
     *                                   of the given array
     */
    @Override
    public void write(char[] chBuf, int off, int len) {
        runWithRetries(() -> super.write(chBuf, off, len));
    }

    /**
     * Writes a portion of a String.
     *
     * @param s   String to be written
     * @param off Offset from which to start reading characters
     * @param len Number of characters to be written
     * @throws IndexOutOfBoundsException If {@code off} is negative,
     *                                   or {@code off + len} is greater than the length
     *                                   of the given string
     * @throws IOException               If an I/O error occurs
     * @implSpec While the specification of this method in the
     * {@linkplain Writer#write(String, int, int) superclass}
     * recommends that an {@link IndexOutOfBoundsException} be thrown
     * if {@code len} is negative or {@code off + len} is negative,
     * the implementation in this class does not throw such an exception in
     * these cases but instead simply writes no characters.
     */
    @Override
    public void write(String s, int off, int len) {
        runWithRetries(() -> super.write(s, off, len));
    }

    /**
     * Writes a line separator.  The line separator string is defined by the
     * system property {@code line.separator}, and is not necessarily a single
     * newline ('\n') character.
     */
    @Override
    public void newLine() {
        runWithRetries(super::newLine);
    }

    /**
     * Flushes the stream.
     */
    @Override
    public void flush() {
        runWithRetries(super::flush);
    }

    @Override
    public void close() throws IOException {
        super.close();
    }

    /**
     * Writes an array of characters.
     *
     * @param chBuf Array of characters to be written
     */
    @Override
    public void write(char[] chBuf) {
        runWithRetries(() -> super.write(chBuf));
    }

    /**
     * Writes a string.
     *
     * @param str String to be written
     */
    @Override
    public void write(String str) {
        runWithRetries(() -> super.write(str));
    }

    private void runWithRetries(ThrowingTask task) {
        int count = 0;
        while (true) {
            count++;
            try {
                task.run();
                return;
            } catch (Exception e) {
                if (count >= 3) { // parameterize this if you want to set manually
                    break;
                }
            }
        }
    }
}
