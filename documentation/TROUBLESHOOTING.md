## Troubleshooting TM4E

This document list some typical options and workflows to actively analyze and fix issues happening in TM4E. Target users of those troubleshooting hints are TM4E users and contributors who want to add some more technical information about issues they face or investigate when reporting or discussing a specific bug on issue tickets. 

#### Get more debug traces from TM4E in Eclipse IDE

Add a line `org.eclipse.tm4e.ui/trace=true` into a properties file (usually named `debug.options`). Then edit you `eclipse.ini` file so it adds the followng paramter `-debug /path/to/debug.options`). The file will be inspected and the option will trigger additional debug information in the Eclipse IDE log.

#### Enable recording of text events to more easily reproduce bugs and write tests

Add a line `org.eclipse.tm4e.ui/debug/log/GenerateTest=true` into a properties file (usually named `debug.options`). Then edit you `eclipse.ini` file so it adds the followng paramter `-debug /path/to/debug.options`). The file will be inspected and the option will trigger generation of a Java test skeleton on the error output stream when closing an editor which uses TM4E for syntax highlighting.