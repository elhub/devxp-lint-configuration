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
