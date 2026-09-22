# Lint Configuration

## Adding new lint configurations

To add a new lint configurations, add the file to the resources folder. This will then be picked up by MegaLinter
when run in various contexts.

## Configuration Details

### Maximum Line Length

Default line length in most environments should be 160 characters. This is a compromise; originally, line lengths
were limited by monitors/software, those reasons no longer apply, but there is significant research that shows that
long lines also hurt readability. Setting the line length to 160 characters is a compromise; it is long enough that
you should never have to worry about it, without just letting there be no limit. If you regularly find yourself
falling afoul of this limit, you should probably reconsider how you write code.

### MarkDownLint

* The [markdown syntax](https://daringfireball.net/projects/markdown/syntax#list) clearly states that a list must be
  indented by 4 spaces. This rule is enforced by Python-Markdown, which is the rendering engine for MkDocs.
* Maintaining a line length of 160 is generally not an issue in Markdown; the exception is when doing tables.

### JavaScript and TypeScript

MegaLinter uses Biome for JS, TS, JSX, and TSX, replacing the shared ESLint, Standard, and Prettier checks for these languages.
Pipeline checks enforce `resources/biome.json`, ignoring project-local Biome and EditorConfig settings.
Projects with their own `.mega-linter.yml` must extend the shared configuration to receive these checks.

Shared rules: recommended lint rules, two-space indentation, LF endings, and a 160-column formatting target, consistent with shared EditorConfig.
Use single quotes (double in JSX), optional semicolons, and trailing commas. Unused imports/variables, unnecessary `let`.
Import sorting and automatic fixes are disabled.

For editor integration, use a local copy of the shared config. Keep framework-specific linting and TypeScript type checking as separate checks where needed.
