package unbi.com.tcpserverfinal.custumtasks;

public class BaseTask {
    protected String TaskName;
    private boolean isActive;
    private String TriggerMsg="null";

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getTriggerMsg() {
        return TriggerMsg;
    }

    public void setTriggerMsg(String triggerMsg) {
        TriggerMsg = triggerMsg;
    }

    public String getTaskName() {
        return TaskName;
    }

}
