#!/usr/bin/env ruby
#
# USAGE: 
#  gitbozuhook.rb oldrev newrev refname posturl
#

oldrev  = ARGV[0]
newrev  = ARGV[1]
refname = ARGV[2].split("/").last rescue ""
posturl = ARGV[3]

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

gitDir = File.expand_path('../../', __FILE__)
projectName = File::basename(gitDir, ".git")

logs = (`git --git-dir=#{gitDir} log -z --pretty=format:"%H<|>%ci<|>%s<|>%an<|>%ae" #{oldrev}..#{newrev}`).split("\0").map{|line| line.split "<|>"}

data = "<commits>" + logs.map{|hash,date,msg,author,email|
<<END
<commit><id>#{hash}</id><refname>#{refname}</refname><author>#{author}</author><email>#{email}</email><body>#{msg}</body><date>#{date}</date><project>#{projectName}</project></commit>
END
}.join + "</commits>"

open(posturl, {"postdata"=>"commits=#{data}"}){|_|}
