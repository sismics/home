#!/bin/sh

#################################################################
#
# Sismics Home standalone startup script.
#
# Author: Jean-Marc Tremeaux <jm.tremeaux@gmail.com>
#
#################################################################

HOME_HOME=/var/home
#HOME_HOST=0.0.0.0
#HOME_PORT=4001
#HOME_CONTEXT_PATH=/
HOME_MAX_MEMORY=150
HOME_PIDFILE=

quiet=0

usage() {
    echo "Usage: home.sh [options]"
    echo "  --help               This small usage guide."
    echo "  --home=DIR           The directory where home will create its files (database, index...)"
    echo "                       Make sure it is writable. Default: /var/home"
    echo "  --host=HOST          The host name or IP address on which to bind Home."
    echo "                       Only relevant if you have multiple network interfaces and want"
    echo "                       to make Home available on only one of them. The default value 0.0.0.0"
    echo "                       will bind Home to all available network interfaces."
    echo "  --port=PORT          The port on which Home will listen for incoming HTTP traffic"
    echo "                       incoming HTTP traffic. Default: 4001."
    echo "  --context-path=PATH  The context path (i.e., the last part of the Home URL)."
    echo "                       Typically '/' or '/home'. Default: '/'."
    echo "  --max-memory=MB      The memory limit (max Java heap size) in megabytes."
    echo "                       Default: 150"
    echo "  --pidfile=PIDFILE    Write PID to this file. Default: not created."
    echo "  --quiet              Don't print anything to standard out. Default: false."
    exit 1
}

# Parse arguments.
while [ $# -ge 1 ]; do
    case $1 in
        --help)
            usage
            ;;
        --home=?*)
            HOME_HOME=${1#--home=}
            ;;
        --host=?*)
            HOME_HOST=${1#--host=}
            ;;
        --port=?*)
            HOME_PORT=${1#--port=}
            ;;
        --context-path=?*)
            HOME_CONTEXT_PATH=${1#--context-path=}
            ;;
        --max-memory=?*)
            HOME_MAX_MEMORY=${1#--max-memory=}
            ;;
        --pidfile=?*)
            HOME_PIDFILE=${1#--pidfile=}
            ;;
        --quiet)
            quiet=1
            ;;
        *)
            usage
            ;;
    esac
    shift
done

# Use JAVA_HOME if set, otherwise assume java is in the path.
JAVA=java
if [ -e "${JAVA_HOME}" ]
    then
    JAVA=${JAVA_HOME}/bin/java
fi

# Create Home home directory.
mkdir -p ${HOME_HOME}
if [ $? -ne 0 ] ; then
    echo Error creating home base directory ${HOME_HOME}. Make sure the directory is writable.
    exit 1
fi
LOG=${HOME_HOME}/home_startup.log
rm -f ${LOG}

cd $(dirname $0)
if [ -L $0 ] && ([ -e /bin/readlink ] || [ -e /usr/bin/readlink ]); then
    cd $(dirname $(readlink $0))
fi

${JAVA} -Xmx${HOME_MAX_MEMORY}m \
  -Dhome.home=${HOME_HOME} \
  -Dhome.host=${HOME_HOST} \
  -Dhome.port=${HOME_PORT} \
  -Dhome.contextPath=${HOME_CONTEXT_PATH} \
  -Djava.awt.headless=true \
  -jar home-standalone.jar > ${LOG} 2>&1 &

# Write pid to pidfile if it is defined.
if [ $HOME_PIDFILE ]; then
    echo $! > ${HOME_PIDFILE}
fi

if [ $quiet = 0 ]; then
    echo Started Home [PID $!, ${LOG}]
fi

