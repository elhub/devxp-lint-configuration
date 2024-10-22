.PHONY: all clean test

all: clean install lint test

build:
	echo 'true'

clean:
	rm -rf .checkmake.ini .golangci.yml .jsonlintrc .markdownlint.json .prettierrc.json .yamllint.yml revive.toml

install: clean
	npm_config_registry=https://jfrog.elhub.cloud/artifactory/api/npm/elhub-npm/; npx mega-linter-runner --install

check:
	# npx mega-linter-runner -r beta
	@echo "Check is not implemented yet"

lint:
	golangci-lint run --config=.golangci.yml ./...

teamcityCheck:
	cd .teamcity && mvn teamcity-configs:generate
