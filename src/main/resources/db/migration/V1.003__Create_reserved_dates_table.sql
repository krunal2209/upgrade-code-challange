CREATE TABLE IF NOT EXISTS reserved_dates (
    id                      BIGINT NOT NULL AUTO_INCREMENT,
    reservation_id          VARCHAR(36),
    camp_site_uuid          VARCHAR(36),
    reserved_date           DATE,
    version                 BIGINT NOT NULL,
    created_on timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_on timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id),
    UNIQUE KEY camp_site_reserved_date_UNIQUE (camp_site_uuid, reserved_date)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE INDEX reserved_dates_idx0 ON reserved_dates (reserved_date);
