package se.miun.distsys.messages;

public class LeaveMessage extends Message {

    public String username = "";

    public LeaveMessage(String username){ this.username = username;}
}
