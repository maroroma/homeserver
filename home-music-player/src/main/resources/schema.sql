-- create table ARTIST
-- (
--     ID                UUID         not null PRIMARY KEY,
--     ITEM_NAME         VARCHAR(100) not null,
--     THUMB_PATH        VARCHAR(500),
--     FANART_PATH       VARCHAR(500),
--     LIBRARY_ITEM_PATH VARCHAR(500)
-- );

create table TRACK
(
    ID                UUID         not null PRIMARY KEY,
    ALBUM_ID          UUID         not null references ALBUM (ID),
    ITEM_NAME         VARCHAR(500) not null,
    TRACK_NUMBER      VARCHAR(25),
    DURATION          INTEGER,
    THUMB_PATH        VARCHAR(500),
    FANART_PATH       VARCHAR(500),
    LIBRARY_ITEM_PATH VARCHAR(500)
);

-- create table ALBUM
-- (
--     ID                UUID         not null PRIMARY KEY,
--     ARTIST_ID         UUID         not null references ARTIST (ID),
--     ITEM_NAME         VARCHAR(100) not null,
--     THUMB_PATH        VARCHAR(500),
--     FANART_PATH       VARCHAR(500),
--     LIBRARY_ITEM_PATH VARCHAR(500)
-- );