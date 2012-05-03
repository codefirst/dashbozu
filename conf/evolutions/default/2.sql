# --- Add Author, Status field

# --- !Ups

alter table "ACTIVITY" add column "AUTHOR" text;
alter table "ACTIVITY" add column "STATUS" varchar(32);

# --- !Downs

alter table "ACTIVITY" drop column "AUTHOR";
alter table "ACTIVITY" drop column "STATUS";
