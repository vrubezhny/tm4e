# Contributing to Eclipse TM4E

Welcome to the Eclipse TM4E contributor land, and thanks in advance for your help in making Eclipse TM4E better and better!

🏠 Official Eclipse TM4E Git repo is [https://github.com/eclipse/tm4e](https://github.com/eclipse/tm4e) .

## ⚖️ Legal and Eclipse Foundation terms

The project license is available at [LICENSE](LICENSE).

This Eclipse Foundation open project is governed by the Eclipse Foundation
Development Process and operates under the terms of the Eclipse IP Policy.

Before your contribution can be accepted by the project team, 
contributors must have an Eclipse Foundation account and 
must electronically sign the Eclipse Contributor Agreement (ECA).

* [http://www.eclipse.org/legal/ECA.php](http://www.eclipse.org/legal/ECA.php)

For more information, please see the Eclipse Committer Handbook:
[https://www.eclipse.org/projects/handbook/#resources-commit](https://www.eclipse.org/projects/handbook/#resources-commit).

## 💬 Get in touch with the community

Eclipse TM4E use mainly 2 channels for strategical and technical discussions

* 🐞 View and report issues through uses GitHub Issues at https://github.com/eclipse/m2e-tm4e/issues.
* 📧 Join the tm4e-dev@eclipse.org mailing-list to get in touch with other contributors about project organization and planning, and browse archive at 📜 [https://accounts.eclipse.org/mailing-list/tm4e-dev](https://accounts.eclipse.org/mailing-list/tm4e-dev)

## 🆕 Trying latest builds

Latest builds, for testing, can usually be found at `https://download.eclipse.org/tm4e/snapshots/` .

## 🧑‍💻 Developer resources

### ⌨️ Setting up the Development Environment manually


* Use Eclipse IDE with Plugin Development Environment installed.
* Clone this repository <a href="https://mickaelistria.github.io/redirctToEclipseIDECloneCommand/redirect.html"><img src="https://mickaelistria.github.io/redirctToEclipseIDECloneCommand/cloneToEclipseBadge.png" alt="Clone to Eclipse IDE"/></a> for m2e-core.
* _File > Open Projects from Filesystem..._ , select the path to tm4e Git repo and the relevant children projects you want to import

### 🏗️ Build

Prerequisite: Latest Maven release or Eclipse m2e.

then `mvn clean verify` from CLI or Right-click on the tm4e root folder > Run As > Maven build

### ⬆️ Version bump

tm4e tries to use OSGi Semantic Version (to properly expose its API contracts and breakage) and Reproducible Version Qualifiers (to minimize the avoid producing multiple equivalent artifacts for identical source). This requires the developer to manually bump version from time to time. Somes rules are that:

* Versions are bumped on a __per module grain__ (bump version of individual bundles/features one by one when necessary), __DON'T bump version of parent pom, nor of other modules you don't change__
* __Versions are bumped maximum once per release__ (don't bump versions that were already bumped since last release)
* __Don't bump versions of what you don't change__
* __Bump version of the bundles you're modifying only if it's their 1st change since last release__
* Version bump may need to be cascaded to features that *include* the artifact you just changed, and then to features that *include* such features and so on (unless the version of those features were already bumped since last release).

The delta for version bumps are:

* `+0.0.1` (next micro) for a bugfix, or an internal change that doesn't surface to APIs
* `+0.1.0` (next minor) for an API addition
* `+1.0.0` (next major) for an API breakage (needs to be discussed on the mailing-list first)
* If some "smaller" bump already took place, you can replace it with your "bigger one". Eg, if last release has org.eclipse.tm4e 0.4.1; and someone already bumped version to 0.4.2 (for an internal change) and you're adding a new API, then you need to change version to 0.5.0.

### ➕ Submit changes

TM4E only accepts contributions via GitHub Pull Requests against [https://github.com/eclipse/tm4e](https://github.com/eclipse/tm4e) repository.
