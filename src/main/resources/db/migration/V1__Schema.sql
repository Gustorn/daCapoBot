CREATE TABLE IF NOT EXISTS chat_log (
    id          INTEGER,
	  timestamp   INTEGER     NOT NULL,
	  user        TEXT        COLLATE NOCASE,
	  message     TEXT        COLLATE NOCASE,

    CONSTRAINT pk__chat_log PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS tracks (
	  id          INTEGER,
	  title       TEXT        COLLATE NOCASE,
	  path        TEXT        NOT NULL,
	  artist      TEXT        COLLATE NOCASE,
	  album       TEXT        COLLATE NOCASE,

    CONSTRAINT pk__tracks PRIMARY KEY(id),
    CONSTRAINT uq__tracks_path UNIQUE(path)
);

CREATE TABLE IF NOT EXISTS vetoes (
    id          INTEGER,
    timestamp   INTEGER     NOT NULL,
    user        TEXT        COLLATE NOCASE,
    track_id    INTEGER     NOT NULL,

    CONSTRAINT pk__vetoes PRIMARY KEY(id),
    CONSTRAINT fk__vetoes__track_id__tracks_id FOREIGN KEY(track_id) REFERENCES tracks(id)
);

CREATE TABLE IF NOT EXISTS requests (
    id          INTEGER,
    timestamp   INTEGER     NOT NULL,
    user        TEXT        COLLATE NOCASE,
    track_id    INTEGER     NOT NULL,

    CONSTRAINT pk__requests PRIMARY KEY(id),
    CONSTRAINT fk__requests_track_id__tracks__id FOREIGN KEY(track_id) REFERENCES tracks(id)
);

