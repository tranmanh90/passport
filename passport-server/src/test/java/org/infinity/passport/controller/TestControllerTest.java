package org.infinity.passport.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.StopWatch;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestControllerTest {

    private static final Logger                LOGGER = LoggerFactory.getLogger(TestControllerTest.class);
    @Autowired
    private              WebApplicationContext context;
    private              MockMvc               mockMvc;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
    }

    @Test
    public void testThreadSafe() throws InterruptedException {
        StopWatch watch = new StopWatch();
        watch.start();
        int requestCount = 1000;
        int threadPoolSize = 20;
        ExecutorService threadPool = Executors.newFixedThreadPool(threadPoolSize);

        IntStream.range(0, requestCount).forEach(i -> {
            threadPool.execute(() -> {
                LOGGER.debug("Active thread count: {}", Thread.activeCount());
                String key = UUID.randomUUID().toString().replaceAll("-", "");
                try {
                    this.mockMvc.perform(get("/open-api/test/threadsafe?key=" + key).accept(MediaType.APPLICATION_JSON));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        });

        threadPool.shutdown();
        // Blocks until all tasks have completed execution after a shutdown request
        if (threadPool.awaitTermination(1, TimeUnit.HOURS)) {
            watch.stop();
            LOGGER.debug("Total: {} s", watch.getTotalTimeMillis() / 1000);
            LOGGER.debug("Mean: {} ms", watch.getTotalTimeMillis() / requestCount);
            LOGGER.debug("TPS: {}", requestCount / (watch.getTotalTimeMillis() / 1000));
        }
    }
}
