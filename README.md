# SONARQUBE in GO
[![Quality Gate](https://forge.univ-artois.fr/sonar/api/badges/gate?key=fr.univartois:sonar-golang-plugin)](https://forge.univ-artois.fr/sonar/dashboard/index/fr.univartois:sonar-golang-plugin)

[![build status](https://forge.univ-artois.fr/terdlb/sonarqubego/badges/master/build.svg)](https://forge.univ-artois.fr/terdlb/sonarqubego/commits/master)

This project is a SonarQube plugin for the langage [Golang](https://golang.org/).

# Author

Thibault Falque

Daniel Le Berre


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


# Use the plugin

* First step create the sonar project properties.
```
sonar.host.url=http://localhost:9000 //change to your url
sonar.projectName=name of project 
sonar.projectVersion=1.0
sonar.login=yourtoken
sonar.projectKey=yourprojectid
sonar.golint.reportPath=report.xml //default
sonar.sources=./
```

* Second step: Install gometalinter beacause the plugin use a report of gometalinter.

`go get -u gopkg.in/alecthomas/gometalinter.v1`

* Generate a gometalinter report:

`gometalinter --checkstyle > report.xml`

* Start the analysis

`./sonar-runner `

Agree you have the sonar runner executable at root of your project.
