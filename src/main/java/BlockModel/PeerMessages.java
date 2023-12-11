package BlockModel;


import com.google.gson.GsonBuilder;
import java.io.Serializable;
import ConnectionAPI.ReverseCoinBlockMessagesInterface;

public class PeerMessages implements Serializable, ReverseCoinBlockMessagesInterface {

    private static final long serialVersionUID = 1145141919810L;

    private CommandCode CommandCode;
    private String BlockMessage;

    public PeerMessages() {
    }

    public PeerMessages(CommandCode code) {
        this.CommandCode = code;
    }

    public PeerMessages(CommandCode code, String message) {
        this.CommandCode = code;
        this.BlockMessage = message;
    }

    @Override
    public CommandCode getCommandCode() {
        return CommandCode;
    }

    @Override
    public void setCommandCode(CommandCode CommandCode) {
        this.CommandCode = CommandCode;
    }

    @Override
    public String getMessage() {
        return BlockMessage;
    }

    @Override
    public void setMessage(String message) {
        this.BlockMessage = message;
    }

    @Override
    public String toJsonString(String data) {
        return new GsonBuilder().create().toJson(data);
    }

}
