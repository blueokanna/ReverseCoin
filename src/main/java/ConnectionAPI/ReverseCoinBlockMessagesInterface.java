package ConnectionAPI;

import BlockModel.CommandCode;

public interface ReverseCoinBlockMessagesInterface {

    CommandCode getCommandCode();

    void setCommandCode(CommandCode CommandCode);

    String getMessage();

    void setMessage(String message);

    String toJsonString(String data);
}
