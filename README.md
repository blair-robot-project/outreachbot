# outreachbot
bot used for outreach since 2022


## Workflows

There are currently two workflows, `run-tests.yml` to run tests and `gen-docs.yml` to generate documentation. Here is how `gen-docs.yml` works:

- It's triggered whenever you push to `main`
- It runs `./gradlew dokkaHtml` to generate HTML from all our doc comments
- The generated HTML is uploaded and deployed to GitHub Pages
- The documentation is then accessible at https://blair-robot-project.github.io/outreachbot/.
