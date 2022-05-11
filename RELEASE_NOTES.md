# Eclipse TM4E: Release notes

This page describes the noteworthy improvements provided by each release of Eclipse TM4E.

### Next release...

## 0.4.6

* 📅 Release Date (tentative): ?
* All changes: https://github.com/eclipse/tm4e/compare/0.4.5...master

### Latest release

## 0.4.5

* 📅 Release Date: May 11th 2022
* All changes: https://github.com/eclipse/tm4e/compare/0.4.3...0.4.5
* 0.4.4 release was skipped to align public and internal version numbers


* Many many many... code improvements
* Dependency updates
* Bugfixes in file detection and compare editor

## 0.4.3

* 📅 Release Date: 19th November 2021
* All changes: https://github.com/eclipse/tm4e/compare/0.4.2...0.4.3

#### Support locations outside a local file system

This allows to have syntax highlighting for buffers that are not backed by a filesystem locations.

#### Compatible with viewers that are not IProjectionViewer

Avoid an exception making TM4E incompatible with most simple ITextViewer in some case.

#### Fix content-type detection

Parent content-type are taken into account and allow to enable syntax highlighting for the children of content-types which are bound to a grammar/scope as well, without extra configuration.

#### Choose default theme according to background color of the widget

By default, TM4E now decides of the best theme to use according to the background color of the text widget. This allows to get a better text theme when mixing dark global them with light background or editor, or light global theme with dark background in editor.


## 0.4.2

* 📅 Release Date (tentative): 6th September 2021
* All changes: https://github.com/eclipse/tm4e/compare/0.4.1...0.4.2

No new feature, numerous bugfixes and code improvements.

## 0.4.1

* 📅 Release Date: 27th August 2020
* All changes: https://github.com/eclipse/tm4e/compare/0.4.0...0.4.1

#### Users can add TextMate theme

It's not possible for users to add extra TextMate theme to use in their IDE via the Textmate > Theme preference page.

## 0.4.0

* 📅 Release Date: 21st November 2019
* All changes: https://github.com/eclipse/tm4e/compare/0.3.4...0.4.0

#### Improve logging mechanism

Most TM4E API entry-points can now be configured with a specific logger. That allows embedders (like the Eclipse UI plugin) to pass a specific logger so TM4E can log at the same location as other parts of the application that includes it, or enable some totally different logging is it fits better.

In Eclipse IDE, the logger takes the "trace" settings into account and logs in the usual Eclipse logs.

## Previous releases

No release notes were maintained before that.
