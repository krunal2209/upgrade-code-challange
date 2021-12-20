CREATE TABLE IF NOT EXISTS reservations (
    uuid                    VARCHAR(36) NOT NULL,
    camp_site_uuid          VARCHAR(36) NOT NULL,
    email_address           VARCHAR(255) NOT NULL,
    full_name               VARCHAR(255) NOT NULL,
    arrival_date            DATE NOT NULL,
    departure_date          DATE NOT NULL,
    cancellation_date       timestamp(6),
    version                 BIGINT NOT NULL,
    created_on timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_on timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    PRIMARY KEY (uuid)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE INDEX reservations_idx0 ON reservations (uuid);
