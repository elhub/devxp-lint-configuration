.PHONY: all clean test

run:
	npx mega-linter-runner

clean:
	rm -rf .checkmake.ini .golangci.yml .jsonlintrc .markdownlint.json .prettierrc.json .yamllint.yml revive.toml

install: clean
	npx mega-linter-runner --install

test:
	npx mega-linter-runner -r beta

lint:
	golangci-lint run --config=.golangci.yml ./...

ci_test:
	cd .teamcity && mvn compile && cd ..