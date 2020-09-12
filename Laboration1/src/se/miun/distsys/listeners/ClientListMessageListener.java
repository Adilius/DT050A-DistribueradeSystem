package se.miun.distsys.listeners;

import se.miun.distsys.messages.ClientListMessage;

public interface ClientListMessageListener {
    void onIncomingLeaveMessage(ClientListMessage clientListMessage);
}
