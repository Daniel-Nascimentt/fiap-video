package br.com.fiapvideo.it.config;


import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.MountableFile;

import java.time.Duration;

public abstract class MongoDBContainerConfig {

    private static final String PATH_CARGA_INICIAL = "./usuarios-it.js";

    @Container
    @ServiceConnection
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest")
            .withStartupTimeout(Duration.ofSeconds(120L))
            .withCopyFileToContainer(MountableFile.forClasspathResource(
                            PATH_CARGA_INICIAL),
                    "/docker-entrypoint-initdb.d/init-script.js");

    @BeforeAll
    public static void setup() {
        mongoDBContainer.start();
    }


    @AfterAll
    static void containerDown(){
        mongoDBContainer.stop();
    }

}
