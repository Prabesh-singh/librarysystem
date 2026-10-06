package com.library.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoDBConnection {

    private static final String CONNECTION_STRING = "mongodb+srv://prabeshsingh189_db_user:nxh9IKipBhu5dE5e@cluster0.lwnpztd.mongodb.net/?appName=Cluster0";

    private static MongoClient mongoClient;

    public static MongoDatabase getDatabase() {

        if (mongoClient == null) {
            mongoClient = MongoClients.create(CONNECTION_STRING);
        }

        return mongoClient.getDatabase("library");
    }
}