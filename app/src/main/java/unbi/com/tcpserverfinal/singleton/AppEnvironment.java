package unbi.com.tcpserverfinal.singleton;

public class AppEnvironment {
    private static final AppEnvironment ourInstance = new AppEnvironment();

    public static AppEnvironment getInstance() {
        return ourInstance;
    }

    private AppEnvironment() {
    }
    boolean testing;

    public boolean isTesting() {
        return testing;
    }

    public void setTesting(boolean testing) {
        this.testing = testing;
    }
}
