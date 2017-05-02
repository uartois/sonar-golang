# Contributing to Golang SonarQube Plugin

The following is a set of guidelines for contributing to Golang Plugin Sonarqube, which are hosted in the University Artois Organization on GitHub. These are just guidelines, not rules. Use your best judgment, and feel free to propose changes to this document in a pull request.


#### Table Of Contents
[What should I know before I get started?](#what-should-i-know-before-i-get-started)

## What should I know before I get started?

### Convention

This project respect the [code convention of Java](http://www.oracle.com/technetwork/java/codeconvtoc-136057.html). So if you want contributing you must respect this convention.

## How Can I Contribute?

### Reporting Bugs

This section guides you through submitting a bug report for Atom. Following these guidelines helps maintainers and the community understand your report :pencil:.

Create an issue on that repository and provide the following information by filling in [the template](ISSUE_TEMPLATE.md).


### Add rule from new Linter

Gometalinter use many linters. So if the plugin don't report the violation of a linter, you can add new violation without write any code.

For a rule you have 3 files that you must modify.

* [the xml file of violations](https://github.com/uartois/sonar-golang/blob/master/src/main/resources/rules/golint-rules.xml). This file define the rules. A rule is a xml tag, for exemple:

```xml
<rule>
  <key>AUniqueKey</key>
  <name>A name for the rule</name>
  <internalKey>AUniqueKey</internalKey>
  <description>A description: foo should be bar
    Additional information can be found at .... (an eventual link with more information)
  </description>
  <severity>MINOR</severity>can be MAJOR, BLOCKER or CRITICAL
  <cardinality>SINGLE</cardinality>
  <status>READY</status>
  <type>CODE_SMELL</type>can be Vulnerability, Bug
  <tag>bad-practice</tag>
  <tag>comments</tag>
  <remediationFunction>CONSTANT_ISSUE</remediationFunction>
  <remediationFunctionBaseEffort>10min</remediationFunctionBaseEffort>
</rule>
More information here https://docs.sonarqube.org/display/DEV/Coding+Rule+Guidelines
```
* [the key properties](https://github.com/uartois/sonar-golang/blob/master/src/main/resources/key.properties) This file define a pattern that can be match with the description and define a link between the issue in the gometalinter report and the violation in the xml.

* [the profile properties](https://github.com/uartois/sonar-golang/blob/master/src/main/resources/profile.properties) This file associate the key of the rule with a boolean. If it's true the rule is activated by default. 
