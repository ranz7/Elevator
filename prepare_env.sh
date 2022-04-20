#!/usr/bin/bash

SCRIPT_DIR=$(dirname "$0")
GIT_PRE_PUSH=${SCRIPT_DIR}"/.git/hooks/pre-push"
echo ${SCRIPT_DIR}
if [ ! -f "${GIT_PRE_PUSH}" ]; then
  echo "Create git-hub hooks"
  cat "${SCRIPT_DIR}""/hooks/pre-push" >${GIT_PRE_PUSH}
fi
