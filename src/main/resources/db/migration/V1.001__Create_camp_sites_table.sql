CREATE TABLE IF NOT EXISTS camp_sites (
    uuid                    VARCHAR(36) NOT NULL,
    name                    VARCHAR(36) NOT NULL,
    version                 BIGINT NOT NULL,
    created_on timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_on timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    PRIMARY KEY (uuid)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE INDEX camp_sites_idx0 ON camp_sites (uuid);
