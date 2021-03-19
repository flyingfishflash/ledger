#!/bin/sh

git tag -a $1

git push origin
git push origin --tags
git push gitlab
git push gitlab --tags
git push gitea
git push gitea --tags

goreleaser release --config=.goreleaser.gitea.yml --rm-dist
goreleaser release --config=.goreleaser.github.yml --rm-dist
goreleaser release --config=.goreleaser.gitlab.yml --rm-dist
