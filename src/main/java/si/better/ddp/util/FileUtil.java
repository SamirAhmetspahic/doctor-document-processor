package si.better.ddp.util;

import org.springframework.messaging.Message;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author ahmetspahics
 */
public class FileUtil
{
    public static Path rename(Path sourcePath, Path targetPath)
    {
        Path path = null;
        try {
            path = Files.move(sourcePath,targetPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path;
    }
}
