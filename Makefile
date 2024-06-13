.PHONY: all clean test

all: clean install lint test

build:
	npx mega-linter-runner

clean:
	rm -rf .checkmake.ini .golangci.yml .jsonlintrc .markdownlint.json .prettierrc.json .yamllint.yml revive.toml

install: clean
	npx mega-linter-runner --install

check:
	npx mega-linter-runner -r beta

lint:
	golangci-lint run --config=.golangci.yml ./...

teamcityCheck:
	cd .teamcity && mvn teamcity-configs:generate
