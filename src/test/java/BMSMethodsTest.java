import com.fazecast.jSerialComm.SerialPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BMSMethodsTest
{
    BMSMethods dummyBMS;
    Room[] dummyRoomList;

    @BeforeEach
    void setUp() throws InterruptedException
    {
        dummyBMS = new BMSMethods();

        dummyRoomList = new Room[3];
        dummyRoomList[0] = new Room("test1", 'n', 10, null, 0);
        dummyRoomList[1] = new Room("test2", 'n', 11, null, 1);
        dummyRoomList[2] = new Room("test3", 'n', 12, null, 2);


    }

    @Test
    void setRelayBoard()
    {
        SerialPort forTesting = SerialPort.getCommPort("COM2");
        dummyBMS.setRelayBoard(forTesting);
        assertEquals(dummyBMS.relayBoard, forTesting);
    }


    @Test
    void getPrimary()
    {
        assertEquals(dummyBMS.getPrimary()[0].getRoomName(),"CR 1" );
        assertEquals(dummyBMS.getPrimary()[1].getRoomName(),"CR 2" );
        assertEquals(dummyBMS.getPrimary()[8].getRoomName(),"Edit" );
        assertEquals(dummyBMS.getPrimary().length,9 );
    }

    @Test
    void getSecondary()
    {
        assertEquals(dummyBMS.getSecondary()[0].getRoomName(),"Kitchen" );
        assertEquals(dummyBMS.getSecondary().length,3 );
    }

    @Test
    void logInfo() {
    }

    @Test
    void relayWrite() {
    }

    @Test
    void relayRead() {
    }

    @Test
    void formatOutput() {
    }

    @Test
    void launchAll() {
    }

    @Test
    void shutdownAll() {
    }

    @Test
    void launchStudio1() {
    }

    @Test
    void shutdownStudio1() {
    }

    @Test
    void launchStudio2() {
    }

    @Test
    void shutdownStudio2() {
    }

    @Test
    void launchStudio3() {
    }

    @Test
    void shutdownStudio3() {
    }

    @Test
    void openDamper() {
    }

    @Test
    void closeDamper() {
    }

    @Test
    void readSensor() {
    }

    @Test
    void massSetPreviousState() {
    }

    @Test
    void removeFromListPrevious() {
    }

    @Test
    void refreshAllRooms() {
    }

    @Test
    void roomsRequestingX() {
    }

    @Test
    void removeMRs() {
    }

    @Test
    void findMRs() {
    }

    @Test
    void printRoomNames() {
    }

    @Test
    void printCurrentTemps() {
    }

    @Test
    void printTargetTemps() {
    }

    @Test
    void printPreviousStates() {
    }

    @Test
    void printCurrentRequest() {
    }

    @Test
    void tempDifference() {
    }

    @Test
    void closeRoomForHVAC() {
    }

    @Test
    void openRoomsForHVAC() {
    }

    @Test
    void addRoomLists() {
    }

    @Test
    void logBuildingStatus() {
    }

    @Test
    void printInfo()
    {


    }

    @Test
    void findTotalAirflowRequested()
    {
        int out = dummyBMS.findTotalAirflowRequested(dummyRoomList);
        assertEquals(out, 33);//total of all rooms pAir (percentage air)
    }

    @Test
    void findRoom()
    {
        assertEquals(dummyBMS.findRoom("CR 1").getRoomName(),"CR 1");
        assertEquals(dummyBMS.findRoom("CR 2").getRoomName(),"CR 2");
        assertEquals(dummyBMS.findRoom("Edit").getRoomName(),"Edit");//secondary
        assertNull(dummyBMS.findRoom("CR 4"));
    }

    @Test
    void extremeTempCheck_whenNormal()
    {
        dummyBMS.getPrimary()[0].setCurrentTemp(76);
        dummyBMS.getPrimary()[0].setRequestState('n');
        dummyBMS.extremeTempCheck();
        assertEquals(dummyBMS.getPrimary()[0].getRequestState(),'n');
    }

    @Test
    void extremeTempCheck_whenHot()
    {
        dummyBMS.getPrimary()[0].setCurrentTemp(100);
        dummyBMS.extremeTempCheck();
        assertEquals(dummyBMS.getPrimary()[0].getRequestState(),'c');
    }

    @Test
    void extremeTempCheck_whenCold()
    {
        dummyBMS.getPrimary()[0].setCurrentTemp(20);
        dummyBMS.extremeTempCheck();
        assertEquals(dummyBMS.getPrimary()[0].getRequestState(),'h');
    }

}