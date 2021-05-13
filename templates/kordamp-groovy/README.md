# <%= name %>

[![Build Status](https://github.com/<%= org %>/<%= projectId %>/workflows/Check/badge.svg)](https://github.com/<%= org %>/<%= projectId %>/actions)
[![Maven Central](https://img.shields.io/maven-central/v/<%= group %>/<%= projectId %>.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22<%= group %>%22%20AND%20a:%22<%= projectId %>%22)
[![Coverage Status](https://coveralls.io/repos/github/<%= org %>/<%= projectId %>/badge.svg?branch=master)](https://coveralls.io/github/<%= org %>/<%= projectId %>?branch=master)

<%= desc %>

See [Full Documentation][DOCS]

[DOCS]: https://<%= org %>.github.io/<%= projectId %>


## Next Steps (To Be Deleted)

This project uses GitHub Actions which to run check for every build and to publish to Maven Central and GitHub Pages on each release. Follow the steps to complete the setup.

### Create a new repository on GitHub 
[Create a new repository on GitHub][1] `<%= projectId %>` for organization `<%= org %>`

### Add the repository to Coveralls.io

[Add the repository to Coveralls.io][2]

### Create Secrets
[Create the following secrets on GitHub][3]:

`SONATYPE_USERNAME` - Sonatype username for publishing to the Maven Central

`SONATYPE_PASSWORD` - Sonatype password for publishing to the Maven Central

`SIGNING_KEY_ID` - Signing key ID

`SIGNING_PASSWORD` - Signing password

`SIGNING_SECRET_KEY_BASE64` - Base64 encoded private signing key

`COVERALLS_REPO_TOKEN` - Coveralls token which is available at https://coveralls.io/github/<%= org %>/<%= projectId %>

`PERSONAL_TOKEN` - GitHub repository token with `repo` priviledges for https://github.com/<%= org %>/<%= projectId %>/

This [guide][4] can help you setup your Maven Central credentials, and the signing keys.

### Initialize Guide and License Headers

```
./gradlew initGuide licenseFormat
```
 
### Init Git repository in the root of this project, commit everything and push to GitHub:
  
```
git init
git add -A
git commit -m "Initial commit"
git remote add origin git@github.com:<%= org %>/<%= projectId %>.git
git push -u origin master
```

### Cleanup

Following steps will commit the cleaned up README file. You can copy them and keep in the clipboard. 
Then paste the snippet into the terminal once the clean up is finished. 
```
git add README.md
git commit -m "Cleaned up README.md"
git push origin master
```

Delete the **Next Steps** section of this README file once it is no longer required and push again 

[1]: https://github.com/new
[2]: https://coveralls.io/repos/new
[3]: https://github.com/<%= org %>/<%= projectId %>/settings/secrets
[4]: https://dev.to/madhead/no-bullshit-guide-on-publishing-your-gradle-projects-to-maven-central-3ok4
