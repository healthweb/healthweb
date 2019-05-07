# Shared data types

DTOs Written in kotlin, these data types are transpiled into TypeScript during the test step. These are then used in the front end.

We need java code to run in order for the transpilation dependency to do its job, i found no earlier step with access to the classpath than the test step.
Other options I've evaluated:
- Creating a gradle plugin [This guy tried.](https://github.com/alexvas/kotlin2typescript)
Yet the plugin he build seems unable to grab the classes from my project classpath.
- Kotlin APT (Annotation Processing)
Turned out to be a lot more difficult than it sounded at first. Lombok does it, Micronaut does it, but I find no common way to analyze the sources with ease like in kotlin-reflection.

*TODO*: When [this](https://youtrack.jetbrains.com/issue/KT-16604) kotlin feature is implemented, support for generating Typescript definitions will be added as a feature in a kotlin delivered gradle plugin(?). 
