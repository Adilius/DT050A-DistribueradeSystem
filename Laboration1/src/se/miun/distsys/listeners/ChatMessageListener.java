package se.miun.distsys.listeners;

import se.miun.distsys.messages.ChatMessage;

public interface ChatMessageListener {
    public void onIncomingChatMessage(ChatMessage chatMessage);
}
