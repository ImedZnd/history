CREATE DATABASE IF NOT EXISTS events
CREATE TABLE event (
                       action VARCHAR primary key ,
                       object_id VARCHAR NOT NULL ,
                       event_time DATE NOT NULL,
);
