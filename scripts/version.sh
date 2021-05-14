#!/bin/sh

ci_platform="Non-CI Build"
ci_pipeline_id="0"
scm_commit_short_sha=""
version=""

usage="\


Usage:
  version.sh version|ci-platform|ci-pipeline-id|commit|all

"


if [ "$CI" = "true" ]; then

  # Drone CI build
  if [ "$DRONE" = "true" ]; then

    ci_platform="drone"
    ci_pipeline_id="$DRONE_BUILD_NUMBER"
    scm_commit_short_sha=$(echo $DRONE_COMMIT_SHA | cut -c1-8)

    [ -n "$DRONE_COMMIT_BRANCH" ] && version="$DRONE_COMMIT_BRANCH" || version="$DRONE_SEMVER"

  fi

  # Gitlab CI build
  if [ "$GITLAB_CI" = "true" ]; then

    ci_platform="gitlab"
    ci_pipeline_id="$CI_PIPELINE_ID"
    scm_commit_short_sha="$CI_COMMIT_SHORT_SHA"

    [ -n "$CI_COMMIT_BRANCH" ] && version="$CI_COMMIT_BRANCH" || version="$CI_COMMIT_REF_NAME"

  fi

else

  # Non-CI build
  scm_commit_short_sha=$(git log --pretty=format:%h -1)
  version=$(git describe --exact-match 2> /dev/null || echo -n "`git symbolic-ref HEAD 2> /dev/null | cut -b 12-`")

fi

usage() {
  echo "$usage"
  exit 1
}

printVersion() {
  echo -n "$version"
  exit 0
}

printScmCommitShortSha() {
  echo -n "$scm_commit_short_sha"
  exit 0
}

printCiPlatform() {
  echo -n "$ci_platform"
  exit 0
}

printCiPipelineId() {
  echo -n "$ci_pipeline_id"
  exit 0
}

printAll() {
  echo "ci platform: $ci_platform"
  echo "ci pipeline id: $ci_pipeline_id"
  echo "version: $version"
  echo "commit:" "$scm_commit_short_sha"
  exit 0
}

case $# in
0) usage;;
esac

case $1 in
version) printVersion ;;
ci-platform) printCiPlatform ;;
ci-pipeline-id) printCiPipelineId ;;
commit) printScmCommitShortSha ;;
all) printAll ;;
*) usage ;;
esac
