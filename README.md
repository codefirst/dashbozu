Dashbozu
=====================================

Dashbozu is an integration service of all dashboards of the world!

Supported services
-----------------------

 * Jenkins
 * git
 * Redmine

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

Install psot script plugin:

    $ cd $RAILS_ROOT/vender/plugins
    $ git clone git://github.com/suer/redmine_post_script.git

Update ``$RAILS_ROOT/vender/plugins/redmine_post_script/bin/post_script.rb``:

    require 'open-uri'
    open("http://dashbozu.herokuapp.com/hook/redmine?url=https://codefirst.org/redmine/activity.atom?key=[API key]") {|_|}

Other tpis
-----------------------
### Deploy to heroku

Update ``conf/heroku.conf`` and see [Documentation: ProductionHeroku - Playframework](http://www.playframework.org/documentation/2.0/ProductionHeroku).

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