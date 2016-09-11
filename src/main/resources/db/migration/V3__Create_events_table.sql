CREATE TABLE "events" (
  "id"          BIGSERIAL PRIMARY KEY,
  "device"      VARCHAR NOT NULL,
  "value"       VARCHAR NOT NULL
);