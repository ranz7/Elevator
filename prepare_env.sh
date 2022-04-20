#!/usr/bin/bash

SCRIPT_DIR=$(dirname "$0")
GIT_HOOKS=${SCRIPT_DIR}"/.git/hooks/"
PROJECT_HOOKS="${SCRIPT_DIR}/hooks/*"

for pre_hook_file_path in ${PROJECT_HOOKS}; do
  NAME_OF_HOOK=$(basename ${pre_hook_file_path})
  echo "Add ${NAME_OF_HOOK} hook."
  cat ${pre_hook_file_path} >"${GIT_HOOKS}${NAME_OF_HOOK}"
  chmod +x ${GIT_HOOKS}${NAME_OF_HOOK}
done
