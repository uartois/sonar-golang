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


