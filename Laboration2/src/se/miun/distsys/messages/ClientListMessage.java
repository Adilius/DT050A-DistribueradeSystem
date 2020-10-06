package se.miun.distsys.messages;

import java.util.*;

public class ClientListMessage extends Message {

    public LinkedHashMap<String, Integer> clientList = new LinkedHashMap<String, Integer>();

    public ClientListMessage(LinkedHashMap<String, Integer> clientList){ this.clientList = clientList; }
}
