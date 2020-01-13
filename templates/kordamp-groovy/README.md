# <%= name %>

[![Build Status](https://github.com/<%= org %>/<%= projectId %>/workflows/Check/badge.svg)](https://github.com/<%= org %>/<%= projectId %>/actions)
[![Download](https://api.bintray.com/packages/<%= bintrayOrg %>/<%= bintrayRepo %>/<%= projectId %>/images/download.svg)](https://bintray.com/<%= bintrayOrg %>/<%= bintrayRepo %>/<%= projectId %>/_latestVersion)
[![Coverage Status](https://coveralls.io/repos/github/<%= org %>/<%= projectId %>/badge.svg?branch=master)](https://coveralls.io/github/<%= org %>/<%= projectId %>?branch=master)

<%= desc %>

See [Full Documentation][4]


## Next Steps (To Be Deleted)

This project uses GitHub Actions which to run check for every build and to publish to BinTray and GitHub Pages on each release. Follow the steps to complete the setup.

### Create a new repository on GitHub 
[Create a new repository on GitHub][1] `<%= projectId %>` for organization `<%= org %>`

### Add the repository to Coveralls.io

[Add the repository to Coveralls.io][2]

### Create Secrets
[Create the following secrets on GitHub][3]:

`BINTRAY_USER` - Username of a BinTray user with write priviledges to https://bintray.com/<%= bintrayOrg %>/<%= bintrayRepo %>/

`BINTRAY_KEY` - Access key with write priveledges to https://bintray.com/<%= bintrayOrg %>/<%= bintrayRepo %>/

`COVERALLS_REPO_TOKEN` - Coveralls token which is available at https://coverals.io/github/<%= org %>/<%= projectId %>

`GITHUB_PERSONAL_TOKEN` - GitHub repository token with `repo` priviledges for https://bintray.com/<%= bintrayOrg %>/<%= bintrayRepo %>/
 
3. Init Git repository in the root of this project, commit everything and push to GitHub:
    ```
    git init
    git add -A
    git commit -m "Initial commit"
    git remote add origin git@github.com:<%= org %>/<%= projectId %>.git
    git push -u origin master
    ```

### Cleanup

Delete the setup section of the README file once it is no longer needed. Keep the links!

[1]: https://github.com/new
[2]: https://coveralls.io/repos/new
[3]: https://github.com/agorapulse/<%= org %>/<%= projectId %>/settings/secrets
[4]: https://<%= org %>.github.io/<%= projectId %>
