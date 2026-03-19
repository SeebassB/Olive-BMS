import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;


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

    @org.junit.jupiter.api.Test
    void getDamperNumber() {
    }

    @org.junit.jupiter.api.Test
    void setRequestState() {
    }

    @Test
    void getRequestState() {
    }

    @Test
    void setPreviousState() {
    }

    @Test
    void getPreviousState() {
    }

    @Test
    void updateTemp() {
    }

    @Test
    void updateRequestState() {
    }
}