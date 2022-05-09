# .bashrc

# Source global definitions
if [ -f /etc/bashrc ]; then
  . /etc/bashrc
fi

source ~/.fzf.bash

export http_proxy=http://defra1c-proxy.emea.nsn-net.net:8080
export https_proxy=$http_proxy
export ftp_proxy=$http_proxy
export no_proxy=localhost,127.0.0.0/8,10.0.0.0/8,*.local,nsn-net.net,inside.nokiasiemensnetworks.com,access.nokiasiemensnetworks.com,nsn-intra.net,nsn-rdnet.net,int.net.nokia.com,nesc.nokia.net

gGNB_PATH=/var/fpwork/kozulin/gnb

gBUILD_PATH=$gGNB_PAHT"/uplane/build/l2_ps"
gBUILD_UT_PATH=$gBUILD_PATH"/ut_build"

function ghp {
  echo "--------------- Basics ---------------"

  echo " goFastGrep -word_to_find ->gfg"
  function gfg() {
    if [ "$1" == "" ]; then
      echo provide word to find
      return
    fi
    fgrep --color=auto -riInH "$1" ./
  }

  echo "goGnb -> ggb"
  function ggb() {
    cd $gGNB_PATH
  }

  echo "goUplane -> gue"
  function gue {
    ggb
    cd /uplane/
  }

  echo "goClion -> gcn"
  function gcn() {
    /var/fpwork/kozulin/clion-2021.3/bin/clion.sh >/dev/null 2>&1 &
  }

  echo "go vim - gvm"
  function gvm() {
    vim -c 'set ic' $1
  }

  echo "---------------Tmux---------------"
  echo "go tmux sct-> gtxS "
  function gtxS() {
    ~/local/bin/tmux attach-session -t SCT
  }
  echo "go tmux ut-> gtxY  "
  function gtxU() {
    ~/local/bin/tmux attach-session -t UT
  }

  echo "---------------Build---------------"
  echo "go clean logs -> gcl"
  function gcl() {
    ggb
    rm -rf ./uplane/logs
    rm -rf ./logs
    cd -
  }

  echo "---------------Fuse---------------"
  echo "go build SctFuse 0 -> gbF0"
  function gbF0() {
    ggb
    git clean -xfd
    source ./uplane/L2-PS/shellL2ps.sh
    ./uplane/buildscript/L2-PS/run build_host --extra_cmake_flags "-DCMAKE_BUILD_TYPE=Debug" --icecc
    ./uplane/L2-PS/runL2psSCT.sh --icecc --rebuild_code --fuse
    cd -
  }
  echo "go test SctFuse -d to debbug-> gtF "
  function gtF() {
    ggb
    if [ "$1" == "" ]; then
      echo provide word to find
      return
    fi
    ./uplane/sct/run_on_asik/SRunner.py host --l2ps $1 --test-type fuse $2
    cd -
  }

  echo "-------------------SCT---------------"
  echo "go build Sct0 -> gbS0"
  function gbS0() {
    ggb
    git clean -xfd
    source ./uplane/L2-PS/shellL2ps.sh
    ./uplane/buildscript/L2-PS/run build_host --extra_cmake_flags "-DCMAKE_BUILD_TYPE=Debug" --icecc
    ./uplane/L2-PS/runL2psSCT.sh --icecc --rebuild_code
    cd -
  }

  echo "go test Sct -d to debbug-> gtS "
  function gtS() {
    ggb
    if [ "$1" == "" ]; then
      echo provide word to find
      return
    fi
    ./uplane/sct/run_on_asik/SRunner.py host --l2ps $1 --test-type fuse $2
    cd -
  }

  function gfS() {
    ggb
    if [ $1 == "" ]; then
      echo provide name of the test
      return 0
    fi
    gue
    ninja -C build/tickler/ -t targets | grep --color=auto ^$1
    cd -
  }

  echo "-------------------UT---------------"
  echo "go build Ut0 -> gbU0"
  function gbU0() {
    ggb
    git clean -xfd
    source ./uplane/L2-PS/shellL2ps.sh
    ./uplane/buildscript/L2-PS/run build_host --extra_cmake_flags "-DCMAKE_BUILD_TYPE=Debug" --icecc
    ./uplane/buildscript/L2-PS/run ut_build --extra_cmake_flags "-DCMAKE_BUILD_TYPE=Debug" --icecc
    cd -
  }

  echo "go test Ut -> gbF0"
  function gtT() {
    if [ $1 == "" ]; then
      echo provide name of the test
      return 0
    fi
    cd "$gBUILD_UT_PATH"
    ninja $1
    ctest -VV -R $1
    cd -
  }

  echo "------------- Git ---------------- "

  echo "go pull recursive => gpl"
  function gpl {
    git pull --ff-only
    git submodule sync --recursive
    git submodule update --init --recursive
  }

  echo "go commit head => gch"
  function gch {
    git diff --name-only --cached | egrep --color=auto "(*.cpp|*.hpp|*.h)" | xargs -I % -n 1 sh -c 'clang-format -i %'
    git commit --amend --no-edit --reset-author
    git push origin HEAD:refs/for/master
  }

  echo "go rebase all -> gua"
  function grk() {
    ggb
    git stash
    git checkout master
    git clean -xfd
    gpl
    gpl
    source set_gnb_env.sh
    ./buildscript/universal/run_nb_scripts.sh

    git checkout -b kozulin
    git rebase master
    git stash pop
    cd -
  }
}

ghp

function prepare-5g-env() {
  export CCACHE_DIR=/var/fpwork/ccache # common ccache for all users on server (faster compilation)
  export CCACHE_ENABLE=1
  export CCACHE_PREFIX=icecc
  export CCACHE_PREFIX_CPP=icecc
  export CCACHE_UMASK=002 # allows to share ccache with other users - for CCACHE_DIR=/var/fpwork/ccache only
  export PARALLEL_BUILD_JOBS=$(($(nproc) / 2))
  export CCACHE_MAXSIZE=100G
  export CCACHE_DEPEND=1
  export CCACHE_SLOPPINESS=pch_defines,time_macros
  export ICECC_REMOTE_CPP=1
  export BB_ENV_EXTRAWHITE+=' CCACHE_DEPEND CCACHE_SLOPPINESS ICECC_REMOTE_CPP'
  export GNB_CCACHE_ENABLED=true
  export ICECC_TEST_REMOTEBUILD=1
  source /var/fpwork/$USER/gnb/set_gnb_env.sh
}

TMPDIR=/var/fpwork/$USER/tmp
TMP=$TMPDIR
TEMP=$TMPDIR
export TMPDIR TMP TEMP
if [[ ! -e $TMP ]]; then
  mkdir $TMP
fi
