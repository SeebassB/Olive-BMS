import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


class RoomTest
{
    @Mock
    private Room dummyRoom;

    @BeforeEach
    void setUp()
    {
        dummyRoom = new Room("dummy", 'c', 10, null, 0);
    }

    @Test
    void getRoomName()
    {
        //dummy set up for roomName is dummy
        assertEquals("dummy", dummyRoom.getRoomName());
    }

    @Test
    void setCurrentTemp()
    {
        dummyRoom.setCurrentTemp(80);
        assertEquals(80, dummyRoom.getCurrentTemp());
    }

    @Test
    void getCurrentTemp()
    {
        //default for currentTemp is 72
        assertEquals(72, dummyRoom.getCurrentTemp());
    }


    @Test
    void setTargetTemp()
    {
        dummyRoom.setTargetTemp(80);
        assertEquals(80, dummyRoom.getTargetTemp());
    }

    @Test
    void getTargetTemp()
    {
        //default is 74
        assertEquals(74, dummyRoom.getTargetTemp());
    }

    @Test
    void setCoolHeat()
    {
        dummyRoom.setCoolHeat('h');
        Assertions.assertEquals('h', dummyRoom.getCoolHeat());
    }

    @Test
    void getCoolHeat()
    {
        //default for coolHeat is c
        assertEquals('c', dummyRoom.getCoolHeat());
    }

    @Test
    void getTargetCutoffTemp()
    {
        //default taget is 74, default is set to cool, magic number is 1.5 cutoff aggregate
        assertEquals(75.5, dummyRoom.getTargetCutoffTemp());
    }

    @Test
    void getPercentAirflow()
    {
        //default is
        assertEquals(10, dummyRoom.getPercentAirflow());
    }

    @Test
    void setDamperStateOff()
    {
        dummyRoom.setDamperState("off");
        assertEquals("off", dummyRoom.getDamperState());
    }

    @Test
    void setDamperStateOn()
    {
        dummyRoom.setDamperState("on");
        assertEquals("on", dummyRoom.getDamperState());
    }

    @Test
    void getDamperState()
    {
        dummyRoom.setDamperState("off");
        assertEquals("off", dummyRoom.getDamperState());
    }

    @Test
    void getDamperNumber()
    {
        assertEquals(0, dummyRoom.getDamperNumber());
    }

    @Test
    void setRequestState()
    {
        dummyRoom.setRequestState('H');
        assertEquals('H', dummyRoom.getRequestState());
    }

    @Test
    void getRequestState()
    {
        //default is none
        assertEquals('n', dummyRoom.getRequestState());
    }

    @Test
    void setPreviousState()
    {
        dummyRoom.setPreviousState('h');
        assertEquals('h', dummyRoom.getPreviousState());
    }

    @Test
    void getPreviousState()
    {
        assertEquals('n', dummyRoom.getPreviousState());
    }

    @Test
    void fixTargetCutoffTempC()
    {
        //default target temp is 74
        dummyRoom.setCoolHeat('C');
        dummyRoom.fixTargetCutoffTemp();
        assertEquals(75.5, dummyRoom.getTargetCutoffTemp());
    }

    @Test
    void fixTargetCutoffTempc()
    {
        //default target temp is 74
        dummyRoom.setCoolHeat('c');
        dummyRoom.fixTargetCutoffTemp();
        assertEquals(75.5, dummyRoom.getTargetCutoffTemp());
    }

    @Test
    void fixTargetCutoffTempn()
    {
        //default target temp is 74
        dummyRoom.setCoolHeat('n');
        dummyRoom.fixTargetCutoffTemp();
        assertEquals(75.5, dummyRoom.getTargetCutoffTemp());
    }

    @Test
    void fixTargetCutoffTemph()
    {
        //default target temp is 74
        dummyRoom.setCoolHeat('h');
        dummyRoom.fixTargetCutoffTemp();
        assertEquals(72.5, dummyRoom.getTargetCutoffTemp());
    }

    @Test
    void fixTargetCutoffTempH()
    {
        //default target temp is 74
        dummyRoom.setCoolHeat('H');
        dummyRoom.fixTargetCutoffTemp();
        assertEquals(75.5, dummyRoom.getTargetCutoffTemp());
    }

    @Test
    void updateTemp()
    {
        Room mockRoom = spy(new Room("mock", 'c', 10, "test", 0));

        try (MockedStatic<BMSMethods> mockedBMS = mockStatic(BMSMethods.class))
        {
            mockedBMS.when(() -> BMSMethods.readSensor("test")).thenReturn(80.0);

            mockRoom.updateTemp();

            mockedBMS.verify(() -> BMSMethods.readSensor("test"));
            verify(mockRoom).fixTargetCutoffTemp();

        }

    }

    @Test
    void updateTemp_whenFail()
    {
        Room mockRoom = spy(new Room("mock", 'c', 10, "test", 0));

        try (MockedStatic<BMSMethods> mockedBMS = mockStatic(BMSMethods.class))
        {
            mockedBMS.when(() -> BMSMethods.readSensor("test")).thenThrow(new IOException("Sensor Failure"));

            mockRoom.updateTemp();

            mockedBMS.verify(() -> BMSMethods.readSensor("test"));

            verify(mockRoom).fixTargetCutoffTemp();

        }

    }

    @Test
    void updateRequestState_TempLow_CoolHeat_c()
    {
        //default target temp is 74
        dummyRoom.setCurrentTemp(60);
        dummyRoom.setCoolHeat('c');
        dummyRoom.updateRequestState();

        assertEquals(dummyRoom.getRequestState(), 'n');
    }

    @Test
    void updateRequestState_TempLow_CoolHeat_n()
    {
        //default target temp is 74
        dummyRoom.setCurrentTemp(60);
        dummyRoom.setCoolHeat('n');
        dummyRoom.updateRequestState();

        assertEquals(dummyRoom.getRequestState(), 'n');
    }

    @Test
    void updateRequestState_TempLow_CoolHeat_h()
    {
        //default target temp is 74
        dummyRoom.setCurrentTemp(60);
        dummyRoom.setCoolHeat('h');
        dummyRoom.updateRequestState();

        assertEquals(dummyRoom.getRequestState(), 'H');
    }
    @Test
    void updateRequestState_TempMid_CoolHeat_c()
    {
        //default target temp is 74
        dummyRoom.setCurrentTemp(73);
        dummyRoom.setCoolHeat('c');
        dummyRoom.updateRequestState();

        assertEquals(dummyRoom.getRequestState(), 'n');
    }

    @Test
    void updateRequestState_TempMidLow_CoolHeat_n()
    {
        //default target temp is 74
        dummyRoom.setCurrentTemp(73);
        dummyRoom.setCoolHeat('n');
        dummyRoom.updateRequestState();

        assertEquals(dummyRoom.getRequestState(), 'n');
    }

    @Test
    void updateRequestState_TempMidLow_CoolHeat_h()
    {
        //default target temp is 74
        dummyRoom.setCurrentTemp(73);
        dummyRoom.setCoolHeat('h');
        dummyRoom.updateRequestState();

        assertEquals(dummyRoom.getRequestState(), 'h');
    }

    @Test
    void updateRequestState_TempMidHigh_CoolHeat_c()
    {
        //default target temp is 74
        dummyRoom.setCurrentTemp(75);
        dummyRoom.setCoolHeat('c');
        dummyRoom.updateRequestState();

        assertEquals(dummyRoom.getRequestState(), 'c');
    }

    @Test
    void updateRequestState_TempMidHigh_CoolHeat_n()
    {
        //default target temp is 74
        dummyRoom.setCurrentTemp(75);
        dummyRoom.setCoolHeat('n');
        dummyRoom.updateRequestState();

        assertEquals(dummyRoom.getRequestState(), 'n');
    }

    @Test
    void updateRequestState_TempMidHigh_CoolHeat_h()
    {
        //default target temp is 74
        dummyRoom.setCurrentTemp(75);
        dummyRoom.setCoolHeat('h');
        dummyRoom.updateRequestState();

        assertEquals(dummyRoom.getRequestState(), 'n');
    }

    @Test
    void updateRequestState_TempHigh_CoolHeat_c()
    {
        //default target temp is 74
        dummyRoom.setCurrentTemp(80);
        dummyRoom.setCoolHeat('c');
        dummyRoom.updateRequestState();

        assertEquals(dummyRoom.getRequestState(), 'C');
    }

    @Test
    void updateRequestState_TempHigh_CoolHeat_n()
    {
        //default target temp is 74
        dummyRoom.setCurrentTemp(80);
        dummyRoom.setCoolHeat('n');
        dummyRoom.updateRequestState();

        assertEquals(dummyRoom.getRequestState(), 'n');
    }

    @Test
    void updateRequestState_TempHigh_CoolHeat_h()
    {
        //default target temp is 74
        dummyRoom.setCurrentTemp(80);
        dummyRoom.setCoolHeat('h');
        dummyRoom.updateRequestState();

        assertEquals(dummyRoom.getRequestState(), 'n');
    }
}