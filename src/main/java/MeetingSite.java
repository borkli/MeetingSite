import redis.clients.jedis.Jedis;

import java.util.Date;
import java.util.List;
import java.util.Set;

public class MeetingSite {

    private final Jedis base = new Jedis();
    private Set<String> resultUsers;
    private final int COUNT_USERS = 20;
    private final double NUM_RANDOM = 0.1;
    private final String NAME_BASE = "Users";

    public MeetingSite(String host, int port) {
        base.slaveof(host, port);
    }

    private void addUser() {
        base.del(NAME_BASE);
        for (int i = 1; i <= COUNT_USERS; i++) {
            base.zadd(NAME_BASE, new Date().getTime(), "Пользователь " + i);
        }
        resultUsers = base.zrange(NAME_BASE, 0, new Date().getTime());
    }

    private String getRandomUser() {
        int numUser = (int) (resultUsers.size() * Math.random());
        List<String> users = resultUsers.stream().toList();
        return users.get(numUser);
    }

    void printUsers() throws InterruptedException {
        addUser();
        while (resultUsers.size() != 0) {
            printUserPay();
            String user = base.zrange(NAME_BASE, 0, new Date().getTime())
                    .stream().findFirst().orElseThrow();
            System.out.println(user); //вывод
            base.zadd(NAME_BASE, new Date().getTime(), user);
            resultUsers.remove(user);
        }
        Thread.sleep(1000);
        printUsers();
    }

    private void printUserPay() {
        if (Math.random() < NUM_RANDOM) {
            String userRandom = getRandomUser();
            System.out.println(userRandom + " оплатил платную услугу");
            base.zadd(NAME_BASE, 0, userRandom);
        }
    }
}
