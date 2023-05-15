package mobit.elec.mbs.medas;

public class IsemriDosya {
    public byte[] fileByte;
    public String fileName;

    public void setFileByte(byte[] fileByte) {
        this.fileByte = fileByte;
    }

    public byte[] getFileByte() {
        return fileByte;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
