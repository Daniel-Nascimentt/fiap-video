package br.com.fiapvideo.integration.config;


import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.MountableFile;

import java.time.Duration;

public abstract class MongoDBContainerConfig {

    private static final String PATH_CARGA_INICIAL = "./carga-mongodb-it.js";

    @Container
    @ServiceConnection
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo")
            .withEnv("MONGO_INITDB_REPLICA_SET_MODE", "false")
            .withCopyFileToContainer(MountableFile.forClasspathResource(
                            PATH_CARGA_INICIAL),
                    "/docker-entrypoint-initdb.d/init-script.js");
}
