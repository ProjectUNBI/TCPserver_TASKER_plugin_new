package unbi.com.tcpserverfinal.objects;

public class StringPair {
    private String Raw,decrypted;
    private boolean isvalid;

    public StringPair(String s, String decrypted, boolean valid) {
        Raw=s;
        this.decrypted=decrypted;
        isvalid=valid;
    }

    public boolean isIsvalid() {
        return isvalid;
    }

    public void setIsvalid(boolean isvalid) {
        this.isvalid = isvalid;
    }


    public String getRaw() {
        return Raw;
    }

    public void setRaw(String raw) {
        Raw = raw;
    }

    public String getDecrypted() {
        return decrypted;
    }

    public void setDecrypted(String decrypted) {
        this.decrypted = decrypted;
    }
}
