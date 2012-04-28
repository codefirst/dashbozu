# --- First database schema

# --- !Ups

create table activity (
  id         text primary key,
  title      text not null,
  body       text not null,
  created_at  timestamp not null,
  source     varchar(255) not null,
  project    text not null,
  url        text,
  icon_url    text
);

create index ix_activity_created_at on activity (created_at);

# --- !Downs

drop table if exists activity;

