public class Main {

    private static final String HOST = "127.0.0.1";
    private static final int PORT = 6379;

    public static void main(String[] args) throws InterruptedException {
        MeetingSite site = new MeetingSite(HOST, PORT);
        site.printUsers();
    }
}
