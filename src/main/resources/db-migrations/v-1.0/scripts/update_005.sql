CREATE TABLE link_item_categories (
    item_id INTEGER REFERENCES item ON DELETE CASCADE ON UPDATE CASCADE,
    categories_id INTEGER REFERENCES categories ON DELETE CASCADE ON UPDATE CASCADE,
    PRIMARY KEY (item_id, categories_id)
)