CREATE TABLE "devices" (
  "id"              BIGSERIAL PRIMARY KEY,
  "name"            VARCHAR NOT NULL,
  "identifier"      VARCHAR NOT NULL,
  "created"         TIMESTAMP NOT NULL
);