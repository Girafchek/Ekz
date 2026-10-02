package interfaces;

import java.io.Serializable;

public interface Dispatchable extends Serializable {
    void dispatch();
    void recall();
    boolean isInTransit();
}