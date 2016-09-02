CREATE TABLE "events" (
  "id"          BIGSERIAL PRIMARY KEY,
  "device_id"   VARCHAR NOT NULL,
  "value"       VARCHAR NOT NULL
);