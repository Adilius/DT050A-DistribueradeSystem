package se.miun.distsys.messages;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class MessageSerializer {

    public byte[] serializeMessage(Message message) {
        ByteArrayOutputStream bos;
        ObjectOutputStream oos;
        GZIPOutputStream gos;
        byte[] byteArray = null;
        try {
            bos = new ByteArrayOutputStream();
            gos = new GZIPOutputStream(bos);
            oos = new ObjectOutputStream(gos);
            oos.writeUnshared(message);            
            oos.flush();            
			oos.close();
			gos.close();
			bos.close();
            byteArray = bos.toByteArray();

        } catch (Exception e) {
        	e.printStackTrace();
        }
        oos = null;
        bos = null;
        gos = null;
        return byteArray;
    }

    public Message deserializeMessage(byte[] byteRepresentation) {
        if (byteRepresentation == null) {
            return null;
        }        
        ByteArrayInputStream bis;
        ObjectInputStream ois;
        GZIPInputStream gis;
        Message message;
        try {

            bis = new ByteArrayInputStream(byteRepresentation);
            gis = new GZIPInputStream(bis);            
            ois = new ObjectInputStream(gis);            
            message = (Message) ois.readUnshared();
            ois.close();
            gis.close();
            bis.close();
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return message;
    }
}
