#!/usr/bin/env bash
stable_release=$(grep defproject project.clj | grep -o "\".*"\" | sed "s/\"//g" | awk "/^[0-9]+\.[0-9]+\.[0-9]+$/")
if [[ -z "$stable_release" ]]; then
  echo 'No stable release to confirm'
  exit 0
else
  echo "Confirming stable release of $stable_release"
  git config user.email "$GH_EMAIL" > /dev/null 2>&1
  git config user.name "$GH_USER" > /dev/null 2>&1
  git remote set-url origin "https://$GH_USER:$GITHUB_TOKEN@github.com/$CIRCLE_PROJECT_USERNAME/$CIRCLE_PROJECT_REPONAME.git"

  lein with-profile -user,-dev,+ci,+ncrw run -m nedap.ci.release-workflow.api confirm-stable $(grep defproject project.clj | grep -o "\".*"\" | sed "s/\"//g")
fi
