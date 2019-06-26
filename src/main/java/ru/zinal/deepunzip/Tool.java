package ru.zinal.deepunzip;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import net.lingala.zip4j.io.inputstream.ZipInputStream;
import net.lingala.zip4j.model.LocalFileHeader;

/**
 *
 * @author zinal
 */
public class Tool {
    
    private final String zipfile;
    private final String dirname;
    private final String password;

    public Tool(String zipfile, String dirname, String password) {
        this.zipfile = zipfile;
        this.dirname = dirname;
        this.password = password;
    }
    
    public static void main(String[] args) {
        if (args.length != 2 && args.length != 3) {
            System.out.println("USAGE: Tool zipfile.zip dirname [password]");
            System.exit(1);
        }
        try {
            new Tool(args[0], args[1], (args.length > 2) ? args[2] : null)
                    .run();
        } catch(Exception ex) {
            ex.printStackTrace(System.out);
            System.exit(2);
        }
    }

    private void run() throws Exception {
        File dir = new File(dirname);
        dir.mkdirs();
        try (FileInputStream fis = new FileInputStream(zipfile)) {
            unpack(fis, dir, password.toCharArray());
        }
    }

    private void unpack(InputStream is, File dir, char[] password) throws Exception {
        LocalFileHeader header;
        try (ZipInputStream zis = new ZipInputStream(is, password)) {
            while ((header = zis.getNextEntry()) != null) {
                if ( header.isDirectory() ) {
                    File subdir = new File(dir, header.getFileName());
                    System.out.println("MKDIR " + subdir);
                    subdir.mkdirs();
                } else if (header.getFileName().toLowerCase().endsWith(".zip")) {
                    File f = new File(dir, header.getFileName() + ".orig."
                        + System.currentTimeMillis());
                    System.out.println("TEMP " + f);
                    try {
                        saveToFile(zis, f);
                        File subdir = new File(dir, header.getFileName());
                        subdir.mkdirs();
                        try (FileInputStream fis = new FileInputStream(f)) {
                            unpack(fis, subdir, null);
                        }
                    } finally {
                        f.delete();
                    }
                } else {
                    File f = new File(dir, header.getFileName());
                    System.out.println("WRITE " + f);
                    f.getParentFile().mkdirs();
                    saveToFile(zis, f);
                }
            }
        }
    }
    
    private void saveToFile(InputStream is, File f) throws Exception {
        final byte[] buf = new byte[4096];
        try (FileOutputStream fos = new FileOutputStream(f)) {
            int readLen;
            while ((readLen = is.read(buf)) != -1) {
                fos.write(buf, 0, readLen);
            }
        }
    }

}
