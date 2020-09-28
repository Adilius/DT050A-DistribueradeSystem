package se.miun.distsys.messages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientListMessage extends Message {

    public Map<String, Integer> clientList = new HashMap<String, Integer>();

    public ClientListMessage(Map<String, Integer> clientList){ this.clientList = clientList; }
}
