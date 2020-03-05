package test;

import com.bdqn.order.OrderApplication;
import com.bdqn.order.util.common.SocketServer;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes={OrderApplication.class})// 指定启动类
public class ApplicationTest {

    @Autowired
    private SocketServer socketServer;

    @Test
    public void add() throws Exception {
        socketServer.start();
        log.info("test::::::>>>>" + socketServer.getPort());
    }

}
