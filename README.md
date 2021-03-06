Dashbozu
=====================================

Caution!
----------------
This product is no longer maintained. Visit to [Dashbozu2](https://github.com/codefirst/dashbozu2) project page.

[![Build Status](https://secure.travis-ci.org/codefirst/dashbozu.png?branch=master)](http://travis-ci.org/codefirst/dashbozu)

Overview
----------------
Dashbozu is an integration service of all dashboards of the world!

Supported services
-----------------------

 * Jenkins
 * git
 * Redmine
 * Heroku
 * Travis CI
 * GitHub
 * NewRelic (alerts)

Requirements
-----------------------

 * Play 2.0 or later
 * PostgreSQL 8 or later

### How to install

#### Play 2.0 or later

See [Documentation: Installing - Playframework](http://www.playframework.org/documentation/2.0/Installing).

#### PostgreSQL

Install PostgreSQL by your package system(e.g. homebrew). And type following command:

    $ cd /path/to/db
    $ initdb .
    $ postgres -D .

And type at other terminal:

    $ createdb dashbozu

Install
-----------------------

Type below commands.

    $ play stage
    $ target/start -Dconfig.resource=prod.conf

And access [http://localhost:9000](http://localhost:9000)

### (Optional) Realtime update

Signup(or login) http://pusher.com/ and add new App. And update ``prod.conf``:

    pusher.enable=true
    pusher.id=<pusher_id>
    pusher.key=<key>
    pusher.secret=<secret>

### (Optional) Push notification to iPhone

Signup(or login) http://boxcar.com and add new service. And update ``prod.conf``:

    boxcar.enable=true
    boxcar.key=<key>
    boxcar.secret=<secret>

And install boxcar App to your iPhone/iPad.

Install hooks
-----------------------

### Jenkins

Add following command to your job:

    curl "http://dashbozu.example.com/hook/jenkins?url=[jenkins url]&project=[project name]"

### Git

Copy to script to your git repository.

    cp $DASHBOZU_HOME/script/gitbozuhook.rb /path/to/gitdir/hooks

And add ``post-receive``:

    #! /bin/sh
    read oldrev newrev refname
    ruby /path/to/gitdir/hooks/gitbozuhook.rb $oldrev $newrev $refname [dashbozu url]/hook/git

### Redmine

Install redmine dashbozu notifiler plugin:

    $ cd $RAILS_ROOT/plugins
    $ git clone git://github.com/suer/redmine_dashbozu_notifiler.git

Access to plugin settings page: Administration > Plugins > Redmine Dashbozu Notifier plugin's "Configure".
Then set dashbozu root URL.

### Heroku

Install deployhook addon:

    $ heroku addons:add deployhooks:http --url=http://dashbozu.example.com/hook/heroku

### Travis CI

Add webhook setting to .travis.yml

    notifications:
      webhooks: http://dashbozu.example.com/hook/travisci

### GitHub

Set webhook URL to http://dashbozu.example.com/hook/github via API:

    curl -u USER_NAME -i \
      -X POST \
      -H "Accept: application/json" -H "Content-type: application/json" \
      -d '{ "name": "web", "active": true, "events": [ "push", "pull_request", "issues", "issue_comment" ],
        "config": { "url": "http://dashbozu.example.com/hook/github", "content_type": "form" } }' \
      https://api.github.com/repos/USER_NAME/PROJECT_NAME/hooks

### NewRelic

Set webhook URL to http://dashbozu.example.com/hook/newrelic

Other tips
-----------------------
### Deploy to heroku

Update ``conf/heroku.conf`` and see [Documentation: ProductionHeroku - Playframework](http://www.playframework.org/documentation/2.0/ProductionHeroku).

### Testing dashbozu

To testing dashbozu, we recomend use heroku bozu.

    $ curl "http://localhost:9000/hook/heroku?head_long=$(uuid)&app=test&git_log=$(uuid)&url=http://example.com&user=mzp"

Author
-----------------------
 * @banjun
 * @mallowlabs
 * @mzp
 * @shimomura1004
 * @suer

License
-----------------------

The MIT License (MIT) Copyright (c) 2012 codefirst.org

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restricti on, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING
BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NON
INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMA
GES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT
 OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

### Dashbozu depends on below software:

 * [Twitter Bootstrap](http://twitter.github.com/bootstrap/) - Apache License 2.0

