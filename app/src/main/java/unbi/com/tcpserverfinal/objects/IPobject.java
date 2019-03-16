package unbi.com.tcpserverfinal.objects;

public class IPobject {
    private String ip;
    private int port;

    public IPobject() {
        this.ip="192.168.1.1";
        this.port=8080;
    }

    public IPobject(String adress) {
        if(adress==null||!adress.contains(":")){
            return;
        }
        String[] part = adress.split(":");
        this.ip = part[0];
        this.port = Integer.valueOf(part[1]);
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
