-- Tables for the "events" SQLite database 

CREATE TABLE IF NOT EXISTS event(
    -- alias for auto-incrementing ROWID (see https://www.sqlite.org/autoinc.html)
    event_id INTEGER PRIMARY KEY, 

    -- SQLite does not directly support dates without a year component,
    -- so we'll need to use something like 9999 or 0000 as the year.
    event_date DATE NOT NULL,

    event_description TEXT NOT NULL,

    category_id INTEGER NOT NULL,

    -- See https://www.sqlite.org/foreignkeys.html
    FOREIGN KEY (category_id) REFERENCES category(category_id)
);

CREATE TABLE IF NOT EXISTS category(
    category_id INTEGER PRIMARY KEY,

    primary_name TEXT NOT NULL,
    secondary_name TEXT   -- is allowed to be NULL!
);
