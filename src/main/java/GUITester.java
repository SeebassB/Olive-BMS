import jssc.SerialPortException;

public class GUITester
{
    public static void main(String[] args) throws SerialPortException, InterruptedException {
        BMSMethods bms = new BMSMethods();
        new GUIController(bms);

    }

}
