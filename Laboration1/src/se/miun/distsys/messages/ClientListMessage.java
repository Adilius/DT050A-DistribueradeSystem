package se.miun.distsys.messages;

import java.util.ArrayList;
import java.util.List;

public class ClientListMessage extends Message {

    public List<String> clientList = new ArrayList<>();

    public ClientListMessage(List<String> clientList){ this.clientList = clientList; }
}
