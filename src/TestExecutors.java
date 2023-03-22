public class TestExecutors extends Thread{
    private String word;
    private int delay;

    public TestExecutors(String word, int delay) {
        super("PingPong with " + word);  // Thread(String name)
        this.word = word;
        this.delay = delay;
    }
    public void run() {
        for (;;) {
            System.out.println(word);
            try {
                sleep(delay);
            } catch (InterruptedException e) {
                return;
            }
        }
    }

}
