#!/usr/bin/env ruby
#
# USAGE: 
#  gitbozuhook.rb oldrev newrev
#

require "open-uri"
require "net/http"

module Net
  class HTTPRequest
    self.class_eval{
      attr_reader :postdata
      def initialize(path, initheader = nil)
        klass = initheader["postdata"] ? HTTP::Post : HTTP::Get if initheader
        @postdata = initheader.delete("postdata")
        super klass::METHOD,
        klass::REQUEST_HAS_BODY,
        klass::RESPONSE_HAS_BODY,
        path, initheader
      end
    }
  end
  class HTTP
    self.class_eval{
      alias :_request :request
      def request(req, body = nil, &block)
        body = req.postdata if req.respond_to?(:postdata)
        _request(req, body, &block)
      end
    }
  end
end

oldrev  = ARGV[0]
newrev  = ARGV[1]
refname = ARGV[2]

gitDir = File.expand_path('../../', __FILE__)
projectName = File::basename(gitDir, ".git")

logs = (`git --git-dir=#{gitDir} log -z --pretty=format:"%H<|>%ci<|>%s" #{oldrev}..#{newrev}`).split("\0").map{|line| line.split "<|>"}

data = "<commits>" + logs.map{|log|
<<END
<commit><id>#{log[0]}</id><body>#{log[2]}</body><date>#{log[1]}</date><project>#{projectName}</project></commit>
END
}.join + "</commits>"

open("http://dashbozu.herokuapp.com/hook/git", {"postdata"=>"commits=#{data}"}){|_|}
