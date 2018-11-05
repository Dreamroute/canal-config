#!/bin/bash

function usage() {
    echo "Usage:"
    echo "  run.sh [CONFIG]"
    echo "example:"
    echo "  run.sh -e canal.instance.master.address=127.0.0.1:3306 \\"
    echo "         -e canal.instance.dbUsername=canal \\"
    echo "         -e canal.instance.dbPassword=canal \\"
    echo "         -e canal.instance.connectionCharset=UTF-8 \\"
    echo "         -e canal.instance.tsdb.enable=true \\"
    echo "         -e canal.instance.gtidon=false \\"
    echo "         -e canal.instance.filter.regex=.*\\\\\\..* "
    exit
}

function check_port() {
    local port=$1
    local TL=$(which telnet)
    if [ -f $TL ]; then
        data=`echo quit | telnet 127.0.0.1 $port| grep -ic connected`
        echo $data
        return
    fi

    local NC=$(which nc)
    if [ -f $NC ]; then
        data=`nc -z -w 1 127.0.0.1 $port | grep -ic succeeded`
        echo $data
        return
    fi
    echo "0"
    return
}

function getMyIp() {
    case "`uname`" in
        Darwin)
         myip=`echo "show State:/Network/Global/IPv4" | scutil | grep PrimaryInterface | awk '{print $3}' | xargs ifconfig | grep inet | grep -v inet6 | awk '{print $2}'`
         ;;
        *)
         myip=`ip route get 1 | awk '{print $NF;exit}'`
         ;;
  esac
}

NET_MODE=""
case "`uname`" in
    Darwin)
        bin_abs_path=`cd $(dirname $0); pwd`
        ;;
    Linux)
        bin_abs_path=$(readlink -f $(dirname $0))
        NET_MODE="--net=host"
        ;;
    *)
        NET_MODE="--net=host"
        bin_abs_path=`cd $(dirname $0); pwd`
        ;;
esac
BASE=${bin_abs_path}

if [ $# -eq 0 ]; then
    usage
elif [ "$1" == "-h" ] ; then
    usage
elif [ "$1" == "help" ] ; then
    usage
fi

DATA="$BASE/data"
mkdir -p $DATA
CONFIG=${@:1}
VOLUMNS="-v $DATA:/home/admin/canal-server/logs"
PORTLIST="8000 2222 11111 11112"
PORTS=""
for PORT in $PORTLIST ; do
    #exist=`check_port $PORT`
    exist="0"
    if [ "$exist" == "0" ]; then
        PORTS="$PORTS -p $PORT:$PORT"
    else
        echo "port $PORT is used , pls check"
        exit 1
    fi
done

MEMORY="-m 4096m"
LOCALHOST=`getMyIp`
#cmd="docker run -it -h $LOCALHOST $CONFIG --name=canal-server $VOLUMNS $NET_MODE $PORTS $MEMORY canal/canal-server"
cmd="docker run -it -h 10.82.12.63 $CONFIG --name=canal-server $VOLUMNS $NET_MODE $PORTS $MEMORY dreamroute/canal:latest"
echo $cmd
eval $cmd