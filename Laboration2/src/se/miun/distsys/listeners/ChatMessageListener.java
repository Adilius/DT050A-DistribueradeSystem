package se.miun.distsys.listeners;

import se.miun.distsys.messages.ChatMessage;

public interface ChatMessageListener {
    void onIncomingChatMessage(ChatMessage chatMessage);
}
