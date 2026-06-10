package restclient.resource;

import org.springframework.core.io.InputStreamResource;

import java.io.IOException;
import java.io.InputStream;

public class MultipartFileInputStreamResource extends InputStreamResource {

    private final String filename;
    private final long contentLength;

    public MultipartFileInputStreamResource(InputStream inputStream, String filename, Long contentLength) {
        super(inputStream);
        this.filename = filename;
        this.contentLength = contentLength;
    }

    public MultipartFileInputStreamResource(InputStream inputStream, String filename) {
        this(inputStream, filename, -1L);
    }

    @Override
    public String getFilename() {
        return this.filename;
    }

    @Override
    public long contentLength() throws IOException {
        return this.contentLength;
    }
}
