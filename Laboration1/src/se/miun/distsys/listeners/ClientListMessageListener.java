package se.miun.distsys.listeners;

import se.miun.distsys.messages.ClientListMessage;

public interface ClientListMessageListener {
    void onIncomingClientListMessage(ClientListMessage clientListMessage);
}
