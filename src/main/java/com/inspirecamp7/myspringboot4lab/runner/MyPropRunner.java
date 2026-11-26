package com.inspirecamp7.myspringboot4lab.runner;

import com.inspirecamp7.myspringboot4lab.config.MyEnvironment;
import com.inspirecamp7.myspringboot4lab.properties.MyProProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class MyPropRunner implements CommandLineRunner {

    private static final Logger logger =
            LoggerFactory.getLogger(MyPropRunner.class);

    private final MyProProperties myProProperties;
    private final MyEnvironment myEnvironment;

    public MyPropRunner(MyProProperties myProProperties,
                        MyEnvironment myEnvironment) {
        this.myProProperties = myProProperties;
        this.myEnvironment = myEnvironment;
    }

    @Override
    public void run(String... args) {
        logger.info("username = {}", myProProperties.getUsername());
        logger.info("port = {}", myProProperties.getPort());
        logger.info("mode = {}", myEnvironment.getMode());

        logger.debug("환경 객체 = {}", myEnvironment);
    }
}