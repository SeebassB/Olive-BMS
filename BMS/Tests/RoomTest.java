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
        assertEquals(72, dummyRoom.getTargetTemp());
    }

    @Test
    void setCoolHeat()
    {
        dummyRoom.setCoolHeat('h');
        assertEquals('h', dummyRoom.getCoolHeat());
    }

    @Test
    void getCoolHeat()
    {
        //default for coolHeat is c
        assertEquals('c', dummyRoom.getCoolHeat());
    }

    @org.junit.jupiter.api.Test
    void getTargetCutoffTemp() {
    }

    @org.junit.jupiter.api.Test
    void getPercentAirflow() {
    }

    @org.junit.jupiter.api.Test
    void setDamperState() {
    }

    @org.junit.jupiter.api.Test
    void getDamperState() {
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