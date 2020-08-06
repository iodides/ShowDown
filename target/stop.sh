pid=`ps -ef | grep showdown.jar | grep -v grep | awk '{print $2}'`
kill -9 $pid
