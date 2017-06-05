package com.itt.service;

import com.mongodb.MongoClient;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DbManagerService {

    private Morphia morphia;
    private Datastore dataStore;

    public DbManagerService(
            @Value("${spring.data.mongodb.database}")
            String databaseName,
            @Value("${spring.data.mongodb.host}")
            String databaseServer,
            @Value("${spring.data.mongodb.port}")
            int databasePort
    ) {
        MongoClient mongoClient = new MongoClient(databaseServer+":"+databasePort);
        this.morphia = new Morphia();
        this.dataStore = morphia.createDatastore(mongoClient, databaseName);
    }

    public Morphia getMorphia() {
        return morphia;
    }

    public DbManagerService setMorphia(Morphia morphia) {
        this.morphia = morphia;
        return this;
    }

    public Datastore getDataStore() {
        return dataStore;
    }

    public DbManagerService setDataStore(Datastore dataStore) {
        this.dataStore = dataStore;
        return this;
    }
}

