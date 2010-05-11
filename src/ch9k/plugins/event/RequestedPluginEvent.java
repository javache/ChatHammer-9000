package ch9k.plugins.event;

import ch9k.eventpool.DataEvent;
import java.net.InetAddress;

/**
 *
 * @author toon
 */
public class RequestedPluginEvent extends DataEvent {

    private byte[] data;

    private String filename;

    public RequestedPluginEvent(InetAddress target, byte[] data, String filename) {
        super(target);
        this.data = data;
        this.filename = filename;
    }

    public byte[] getData() {
        return data;
    }

    public String getFilename() {
        return filename;
    }

}
