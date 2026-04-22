import com.fazecast.jSerialComm.SerialPort;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BMSMainControllerTest
{



    @Test
    void main()
    {
    }

    @Test
    void portCheck_whenNoPorts() throws InterruptedException {

        BMSMethods mockedBMS = mock(BMSMethods.class);
        SerialPort mockedSerialPort = mock(SerialPort.class);


        when(mockedSerialPort.isOpen()).thenReturn(false, true);

        when(mockedBMS.relayBoard.getCommPorts()).thenReturn(new SerialPort[0]);

        try(MockedStatic<SerialPort> serialPortMock = mockStatic(SerialPort.class))
        {
            serialPortMock.when(SerialPort::getCommPorts).thenReturn(new SerialPort[0]);

            mockedSerialPort.getCommPorts();

            serialPortMock.verify(SerialPort::getCommPorts, atLeastOnce());
            verify(mockedSerialPort, atLeast(2)).isOpen();
            verify(mockedSerialPort, never()).openPort();

        }

    }

    @Test
    void portCheck_when1Port()
    {

    }

    @Test
    void portCheck_whenManyPorts()
    {

    }


}