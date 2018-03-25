package classes;

public class AttachmentWrapper {
    private String name;
    private byte[] data;
    private String mimeType;

    public AttachmentWrapper(String name, byte[] data, String mimeType){
        this.name = name;
        this.data = data;
        this.mimeType = mimeType;
    }

    public String getName() {
        return name;
    }

    public byte[] getData() {
        return data;
    }

    public String getMimeType() {
        return mimeType;
    }
}
