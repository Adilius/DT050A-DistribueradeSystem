package se.miun.distsys.listeners;

import se.miun.distsys.messages.LeaveMessage;

public interface LeaveMessageListener {
    void onIncomingLeaveMessage(LeaveMessage leaveMessage);
}
