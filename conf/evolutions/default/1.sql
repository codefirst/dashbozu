# --- First database schema

# --- !Ups

create table "ACTIVITY" (
  "ID"         varchar(255) primary key,
  "TITLE"      text not null,
  "BODY"       text not null,
  "CREATED_AT" timestamp not null,
  "SOURCE"     varchar(255) not null,
  "PROJECT"    text not null,
  "URL"        text,
  "ICON_URL"   text
);

create index ix_activity_created_at on "ACTIVITY" ("CREATED_AT");

# --- !Downs

drop table if exists "ACTIVITY";

