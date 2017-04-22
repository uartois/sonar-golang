# SonarQube Plugin for the Go language


It integrates [GoMetaLinter](https://github.com/alecthomas/gometalinter) reports 
within SonarQube dashboard.

The user must generate a GoMetaLinter report for his code using the checkstyle
format. The report is thus integrated to SonarQube using 
[sonar-scanner](https://docs.sonarqube.org/display/SCAN/Analyzing+with+SonarQube+Scanner).

Release 1.0 only provides golint support. Upcoming releases will bring support 
for additional linters.


# Author

+ Thibault Falque
+ Daniel Le Berre


# Installation

* Download the latest version of the artifact
* Stop sonarqube server
* Put the jar file in `$SONAR_PATH/extensions/plugins`
* Start sonarqube server

# Enabling all latest rules

If you have already installed the plugin and you want enable the new rules of
the new version of the plugin, follow those steps after the installation:

* Go on the Quality Profiles page
* Click on the arrow near the "Create" button
* Click on "Restore Built-In Profiles"
* Choose the language (Go)
* Click on "Restore"


# Using the plugin

* First step: create a `sonar-project.properties` file.
```
sonar.projectKey=yourprojectid
sonar.projectName=name of project 
sonar.projectVersion=1.0
sonar.golint.reportPath=report.xml //default
sonar.sources=./
```

* Second step: install [gometalinter](https://github.com/alecthomas/gometalinter)
```shell
go get -u gopkg.in/alecthomas/gometalinter.v1
gometalinter.v1 --install
```

* Generate a gometalinter report using the checkstyle format:
```shell
gometalinter.v1 ./... --checkstyle > report.xml
```

* Start the analysis
```shell
sonar-scanner
```

It is assumed that you have the sonar scanner executable on your path and
to run it at the root of your go project.
