#!/bin/bash

remotes_up_to_date=1

usage="\


Usage:
  release.sh major|minor|patch|release|prerel [<prerel>]|build <build>)

  [ from semver.sh ]

  -- The version must match X.Y.Z[-PRERELEASE][+BUILD]
     where X, Y and Z are non-negative integers.
  -- PRERELEASE is a dot separated sequence of non-negative integers and/or
     identifiers composed of alphanumeric characters and hyphens (with
     at least one non-digit). Numeric identifiers must not have leading
     zeros. A hyphen ('-') introduces this optional part.
  -- BUILD is a dot separated sequence of identifiers composed of alphanumeric
     characters and hyphens. A plus ('+') introduces this optional part.

"

function log_() {
	echo "[$(basename "$0")] $*"
}

function error() {
	log_ "[error]: $*"
	exit 1
}

function usage() {
	error "$usage"
}

function check_remote_up_to_date() {
	local branch upstream count
	case $# in
	1) branch=$(git symbolic-ref HEAD 2>/dev/null) || return 1 ;;
	2) branch="$2" ;;
	*)
		echo "usage: check_remote_up_to_date <upstream> [<branch>]" 1>&2
		return 1
		;;
	esac
	upstream="$1"/"$branch"
	count=$(git rev-list --count --left-right "$upstream...$branch") || return 1
	set -- $count
	case $1,$2 in
	0,0) log_ "$upstream: up to date" ;;
	0,*)
		log_ "$upstream: $2 commit(s) behind working directory"
		remotes_up_to_date=0
		;;
	*,0)
		log_ "$upstream: $2 commit(s) ahead of working directory"
		remotes_up_to_date=0
		;;
	*)
		log_ "$upstream has diverged from your branch, and have $2 and $1 different commits each, respectively."
		remotes_up_to_date=0
		;;
	esac
}

function process_semver() {
	log_ "current git branch: $(git branch | grep '*' | awk '{print $2}')"
	git branch | grep '\* main' >/dev/null || error "not on main branch"

	if output=$(git status --porcelain) && [ "$output" ]; then
		#log_ "working directory isn't clean, commit any changes"
		error "working directory isn't clean, commit any changes"
	fi
	log_ "working directory is clean"

	for o in $(git remote -v | grep fetch | cut -f 1 -); do
		check_remote_up_to_date "${o}" main
	done
	[ $remotes_up_to_date -eq 1 ] || error "some remotes aren't up to date with the local branch"

	git_describe_version="$(git describe --tags)"
	./semver.sh bump "${@}" "${git_describe_version}" &>/dev/null || error "failed execution: './semver.sh bump ${@} ${git_describe_version}'"
	semver_version="v$(./semver.sh bump ${@} ${git_describe_version})"
	log_ "./semver.sh bump ${@} ${git_describe_version}: ${semver_version}"

	read -p "create git tag (${semver_version})? " -n 1 -r
	echo
	if [[ $REPLY =~ ^[Yy]$ ]]; then
		git tag -a "${semver_version}" -m "release ${semver_version}" || error "problem creating tag"
	fi

	read -p "push tag to remote repositories? " -n 1 -r
	echo
	if [[ $REPLY =~ ^[Yy]$ ]]; then
		for r in $(git remote -v | grep fetch | cut -f 1 -); do
			git push "${r}" --tags || error "problem pushing tag"
		done
	fi

	read -p "create release object in remote repositories? " -n 1 -r
	echo
	if [[ $REPLY =~ ^[Yy]$ ]]; then
		for r in $(git remote -v | grep fetch | cut -f 1 -); do
			if [[ $r != "all" ]]; then
				goreleaser release --config=.goreleaser."${r}".yml --rm-dist &> /dev/null || error "problem with goreleaser"
			fi
		done
	fi
}

case $# in
0) usage ;;
esac

case $1 in
major | minor | patch | prerel | release) process_semver "${@}" ;;
*) usage ;;
esac
