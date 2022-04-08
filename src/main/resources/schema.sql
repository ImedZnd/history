CREATE DATABASE IF NOT EXISTS events;

CREATE TABLE IF NOT EXISTS event (
                       action VARCHAR primary key ,
                       object_id VARCHAR NOT NULL ,
                       event_time DATE NOT NULL
);
