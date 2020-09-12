package se.miun.distsys.messages;

import java.util.ArrayList;
import java.util.List;

public class ClientListMessage extends Message {

    public List<String> clients = new ArrayList<>();

    public ClientListMessage(List<String> clients){ this.clients = clients; }
}
