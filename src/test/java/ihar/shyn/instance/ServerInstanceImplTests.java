package ihar.shyn.instance;

import ihar.shyn.model.BackendInstance;
import ihar.shyn.model.BackendInstanceImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ServerInstanceImplTests {

    @Test
    public void WhenGetServerAddress_ReturnServerAddress() {
        String serverInstanceAddress = "127.0.0.1";
        BackendInstance instance = new BackendInstanceImpl(serverInstanceAddress);
        assertEquals(instance.getAddress(), serverInstanceAddress);
    }

}
